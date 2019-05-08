package example.com.socketbesttest;

/**
 * Created by ASUS-NB on 2019/5/7.
 */

public class ChatEntity {
    private String content;
    private String time;
    //id为true表示是自己，false表示是对方发来的信息
    private boolean id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }
    public ChatEntity(){

    }
    public ChatEntity(String content, String time, boolean id) {
        this.content = content;
        this.time = time;
        this.id = id;

    }
}
