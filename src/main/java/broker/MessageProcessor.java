package broker;

import message.Message;
import org.codehaus.jackson.map.ObjectMapper;
import utils.Scheduler;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dennis on 15-Oct-17.
 **/
    public class MessageProcessor extends Thread{

    //private static int age = 5; // 5 seconds
    private static ObjectMapper mapper;
    private Scheduler scheduler;
    static List<String> responses;

    MessageProcessor() {

        mapper = new ObjectMapper();
        scheduler = new Scheduler();
        responses = new ArrayList<>();
    }

    @Override
    public void run() {

        backupQueue();

        while (true) {
            if (!Broker.queue.isEmpty()) {

                Message msg = Broker.queue.poll();//removes the head object
                backupQueue();

                if (!responses.toString().matches(".*" + msg.getId() + ".*")) {
                    try {
                            DatagramSocket clientSocket = new DatagramSocket();
                            InetAddress IPAddress = InetAddress.getByName(msg.getTo());

                            sendMessage(clientSocket, IPAddress, msg);

                            clientSocket.setSoTimeout(100);
                            readResponse(clientSocket, msg);

                    } catch (Exception e) {

                        System.out.println("Error creating DatagramSocket");
                        //e.printStackTrace();
                    }
                }
            }
        }
    }

    private void sendMessage(DatagramSocket clientSocket, InetAddress IPAddress, Message msg)  {

        try {

            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(msg);
            oo.close();

            byte[] serializedMessage = bStream.toByteArray();

            DatagramPacket sendPacket = new DatagramPacket(serializedMessage, serializedMessage.length, IPAddress, 9876);
            clientSocket.send(sendPacket);
        } catch (IOException e) {

            System.out.println("Errors serializing/sending message");

            clientSocket.close();
            e.printStackTrace();
        }

    }

    private void readResponse(DatagramSocket clientSocket, Message msg) {


        byte[] responseData = new byte[1024];

        try {

            DatagramPacket receivePacket = new DatagramPacket(responseData, responseData.length);
            clientSocket.receive(receivePacket);
            String response = new String(receivePacket.getData());

            //System.out.println("FROM SERVER:" + response);

            responses.add(response);
            clientSocket.close();
        } catch (IOException e) {

            System.out.println("NO RESPONSE");
            saveAsJson(msg);

            clientSocket.close();
        }
    }

    private void saveAsJson(Message msg) {

        if (!msg.getIsSaved()) {

            msg.setIsSaved(true);

            try {

                System.out.println("Saving message " + msg.getId() + " as json.");
                mapper.writeValue(new File(Broker.dsPath + msg.getId() + ".json"), msg);
                //scheduler.scheduleForDeletion(Paths.get(Broker.dsPath + msg.getId() + ".json"), age);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void backupQueue(){

        File f = new File(Broker.backupPath);

        if (f.exists() && !f.isDirectory()) {

            f.delete();
        }

        try {

            mapper.writeValue(new File(Broker.backupPath), Broker.queue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
