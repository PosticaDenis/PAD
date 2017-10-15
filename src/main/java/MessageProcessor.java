import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Dennis on 15-Oct-17.
 **/
public class MessageProcessor extends Thread{

    private Socket processorSocket;

    MessageProcessor() {

    }

    public void run() {

        while (true) {
            if (!Broker.queue.isEmpty()) {

                Message msg = Broker.queue.poll();//removes the head object

                try {
                    /*processorSocket = new Socket(msg.getReceiverAddress(), 1234);

                    PrintWriter out = new PrintWriter(processorSocket.getOutputStream(), true);
                    out.println(msg.getContent());

                    processorSocket.close();*/

                    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

                    DatagramSocket clientSocket = new DatagramSocket();

                    InetAddress IPAddress = InetAddress.getByName(msg.getReceiverAddress());

                    byte[] sendData = new byte[1024];
                    byte[] receiveData = new byte[1024];

                    String sentence = msg.getContent();//inFromUser.readLine();
                    sendData = sentence.getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
                    clientSocket.send(sendPacket);

                    //DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    //clientSocket.receive(receivePacket);
                    //String modifiedSentence = new String(receivePacket.getData());
                    //System.out.println("FROM SERVER:" + modifiedSentence);
                    clientSocket.close();

                } catch (Exception e) {

                    System.out.println("Receiver is offline.");
                    //e.printStackTrace();
                }

                //PrintWriter out = new PrintWriter(processorSocket.getOutputStream(), true);

                //System.out.println("From: " + msg.getSenderAddress());
                //System.out.println("To: " + msg.getReceiverAddress());
                //System.out.println("Msg: " + msg.getContent());


            }
        }
    }

    /*private void connect() {

    }*/
}
