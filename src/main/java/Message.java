import java.io.Serializable;

/**
 * Created by Dennis on 15-Oct-17.
 **/
public class Message implements Serializable{
    private String from;
    private String to;
    private String content;

    Message(String from, String to, String content) {

        this.from = from;
        this.to = to;
        this.content = content;
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
