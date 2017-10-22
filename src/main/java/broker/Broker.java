package broker;

import message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Dennis on 15-Oct-17.
 **/

public class Broker {

    static ConcurrentLinkedQueue<Message> queue;

    static List<SenderListener> senderListeners = new ArrayList<>();
    private MessageProcessor messageProcessor;
    private StorageMaster storageMaster;
    static String backupPath = "E:\\Denis\\a4s1\\PAD\\src\\main\\java\\storage\\queue.json";
    static String dsPath = "E:\\Denis\\a4s1\\PAD\\src\\main\\java\\storage\\data\\";


    public static void main(String argv[]) throws Exception {

        new Broker();
    }

    private Broker() {

        storageMaster = new StorageMaster();
        startStorageMaster();

        messageProcessor = new MessageProcessor();
        startProcessor();
        startListener(1234);
        startStorageMaster();


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

    private void startStorageMaster() {

        System.out.println("SM started.");

        storageMaster.setUp();
        storageMaster.start();
    }
}