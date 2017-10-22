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
    private boolean isSaved = false;
    private int cnt = 0;

    public Message() {

    }

    public Message(String id, String from, String to, String content) {

        this.id = id;
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getContent() {
        return content;
    }

    public boolean getIsSaved() {
        return isSaved;
    }

    public int getCnt() {
        return cnt;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

}
