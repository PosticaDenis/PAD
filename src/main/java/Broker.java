/**
 * Created by Dennis on 15-Oct-17.
 **/

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Broker {

    static ConcurrentLinkedQueue<Message> queue;

    private static int lifetime = 60;

    static List<SenderListener> senderListeners = new ArrayList<>();
    MessageProcessor messageProcessor;


    public static void main(String argv[]) throws Exception {

        Broker broker = new Broker();

        //broker.startListener(1234);


    }

    private Broker() {

        queue = new ConcurrentLinkedQueue<>();
        messageProcessor = new MessageProcessor();
        startProcessor();
        startListener(1234);

    }

    public void startListener(int port) {

        System.out.println("Listener started.");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {

                SenderListener senderListener = new SenderListener(serverSocket.accept());
                senderListeners.add(senderListener);
                senderListener.start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        }
    }

    private void startProcessor() {

        System.out.println("Processor started.");
        messageProcessor.start();

    }

    private void send() {

    }
}
