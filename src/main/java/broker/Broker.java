package broker; /**
 * Created by Dennis on 15-Oct-17.
 **/

import message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Broker {

    static ConcurrentLinkedQueue<Message> queue;

    private static int lifetime = 60;

    static List<SenderListener> senderListeners = new ArrayList<>();
    private MessageProcessor messageProcessor;


    public static void main(String argv[]) throws Exception {

        new Broker();
    }

    private Broker() {

        queue = new ConcurrentLinkedQueue<>();
        messageProcessor = new MessageProcessor();
        startProcessor();
        startListener(1234);

    }

    private void startListener(int port) {

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
}