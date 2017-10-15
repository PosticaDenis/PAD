import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;

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

    /*private void start() {

        read();

    }*/

    public void startListener() {

        try {

            DatagramSocket serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            while (true) {

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                System.out.println("RECEIVED: " + sentence);

                /*InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                DatagramPacket sendPacket =
                        new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void read() {

    }
}
