import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;

/**
 * Created by Dennis on 15-Oct-17.
 **/
public class Sender {

    private Socket senderSocket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader reader;
    private static String brokerHost;


    public static void main(String argv[]) throws Exception {

        new Sender();
    }

    private Sender() {

        //readBrokerHostData();

        try {
            //senderSocket = new Socket(brokerHost, 1234);
            senderSocket = new Socket("localhost", 1234);

            out = new PrintWriter(senderSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));

        } catch (IOException e) {

            System.out.println("\nPlease try later. Currently, the broker is offline!");
            exit();

            System.exit(-1);
        }

        System.out.println("Welcome!\n");

        startClient();
    }

    public void startClient() {

        String toBroker;

        try {
            while(true) {
                toBroker = reader.readLine();
                if (!toBroker.isEmpty()) { //null

                    if(toBroker.equals(".exit")) {
                        System.out.println("You disconnected!");
                        //sendExitMessage();
                        break;
                    }

                    else {

                        String serializedObject = "";
                        Message msg = new Message(InetAddress.getLocalHost().getHostName(),"127.0.0.1", toBroker);

                        try {
                            ByteArrayOutputStream bo = new ByteArrayOutputStream();
                            ObjectOutputStream so = new ObjectOutputStream(bo);
                            so.writeObject(msg);
                            so.flush();
                            serializedObject = new String(Base64.getEncoder().encode(bo.toByteArray()));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println("Sending msg.");
                        sendMessage(serializedObject);
                    }
                }
            }

            out.close();
            in.close();
            senderSocket.close();
        } catch (IOException e) {
            System.err.println("Error");
        }

    }


    private void readBrokerHostData() {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String host = "";

        System.out.print("Enter the broker address: ");

        try {
            host = br.readLine();

        } catch (IOException e) {
            System.out.println("Error with Broker host!");
        }

        brokerHost = host;
    }

    private void sendMessage(String message) {

        System.out.println("Message sent.");
        out.println(message);
    }

    private void exit(){

        try {
            //in.close();
            //out.close();
            senderSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
