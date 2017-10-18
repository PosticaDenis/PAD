package message;

import java.io.Serializable;

/**
 * Created by Dennis on 15-Oct-17.
 **/
public class Message implements Serializable{

    private String id;
    private String from;
    private String to;
    private String content;

    public Message(String id, String from, String to, String content) {

        this.id = id;
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getSenderAddress() {
        return from;
    }

    public String getReceiverAddress() {
        return to;
    }

    public String getContent() {
        return content;
    }
}
