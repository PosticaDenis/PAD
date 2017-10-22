package broker;

import message.Message;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import utils.Validator;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by c-denipost on 18-Oct-17.
 **/
public class StorageMaster extends Thread{


    private ObjectMapper mapper;

    StorageMaster() {

        mapper = new ObjectMapper();
    }

    protected void setUp() {

        Broker.queue = new ConcurrentLinkedQueue<>();
        Validator validator = new Validator();

        File f = new File(Broker.backupPath);

        if (f.exists() && !f.isDirectory()) {


            if (validator.isValid(Broker.backupPath)) {

                try {

                    Broker.queue = mapper.readValue(new File(Broker.backupPath), new TypeReference<ConcurrentLinkedQueue<Message>>(){});

                    System.out.println("The restored queue contains " + Broker.queue.size() + " messages.");
                } catch (IOException e) {

                    System.out.println("No saved version found");
                }
            }
            else {
                System.out.println("Backup found, but it is corrupted/empty.");
            }
        }
        else {
            System.out.println("Backup not found.");
        }
    }

    @Override
    public void run() {

        File folder = new File(Broker.dsPath);

        while (true) {

            try {

                sleep(100);
            } catch (InterruptedException ie) {
                System.out.println("I don't want to sleep");
            }

            File[] listOfFiles = folder.listFiles();

            if (listOfFiles.length > 0) {

                try {

                    sleep(100);
                } catch (InterruptedException ie) {
                    System.out.println("I don't want to sleep");
                }

                for (File file: listOfFiles) {

                    try {

                        Message msg = mapper.readValue(new File(folder + "\\" +  file.getName()), Message.class);

                        if (!MessageProcessor.responses.toString().matches(".*" + msg.getId() + ".*") && msg.getCnt() < 50 ) {

                            System.out.println("Adding file " + folder + "\\" + file.getName() + " to the queue. Counter is " + msg.getCnt());

                            file.delete();

                            msg.setCnt(msg.getCnt() + 1);

                            saveAsJson(msg);

                            Broker.queue.add(msg);
                        }
                        else {
                            System.out.println("Message successfully resent(or timed-out to be sent), deleting backup.");
                            file.delete();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private void saveAsJson(Message msg) {

        try {

            //System.out.println("Increasing counter.");
            mapper.writeValue(new File(Broker.dsPath + msg.getId() + ".json"), msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
