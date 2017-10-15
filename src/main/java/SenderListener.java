import java.io.*;
import java.net.Socket;
import java.util.Base64;

/**
 * Created by Dennis on 15-Oct-17.
 **/
public class SenderListener extends Thread{

    private Socket socket;
    //private PrintWriter out;

    SenderListener(Socket socket) {
        this.socket = socket;

        System.out.println("Hello, i'm " + socket.getRemoteSocketAddress().toString());
    }

    public void run() {

        try {
            //out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String received;
            try {
                while (true) {

                    received = in.readLine();
                    if (received != null) {

                        toQueue(received);
                    }
                }
            } catch (IOException e) {
                removeThread();
            }

            //out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toQueue(String received) {

        try {
            byte b[] = Base64.getDecoder().decode(received.getBytes());
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);

            Object o = si.readObject();

            if(o instanceof Message) {

                Message msg = (Message) o;
                Broker.queue.add(msg);
            }
            else {

                System.out.println("Serialization error");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Message successfully added to the queue.");
    }

    private void removeThread() {

        System.out.println("User unexpectedly disconnected.");

        Broker.senderListeners.remove(this);
    }
}
