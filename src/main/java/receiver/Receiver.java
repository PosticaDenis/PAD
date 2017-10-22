package receiver;

import message.Message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Dennis on 15-Oct-17.
 **/
public class Receiver {

    public static void main(String argv[]) throws Exception {

        new Receiver();
    }

    private Receiver() {

        startListener();

    }

    public void startListener() {

        try {

            DatagramSocket serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];

            while (true) {

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()));
                Message message = (Message) iStream.readObject();
                iStream.close();

                System.out.println("Received from " + message.getFrom() + " : " + message.getContent());

                sendResponse(serverSocket, receivePacket, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(DatagramSocket serverSocket, DatagramPacket receivePacket, Message message) {

        InetAddress IPAddress = receivePacket.getAddress();
        int port = receivePacket.getPort();

        byte[] sendData;

        try {

            sendData = message.getId().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        } catch (IOException e) {

            System.out.println("Error sending response!");
            e.printStackTrace();
        }
    }
}
