package example.com.socketbesttest;

/**
 * Created by ASUS-NB on 2019/5/8.
 */

public class ConnectEntity {
    private boolean connectSucess;

    public boolean isConnectSucess() {
        return connectSucess;
    }

    public void setConnectSucess(boolean connectSucess) {
        this.connectSucess = connectSucess;
    }

    public ConnectEntity(boolean connectSucess) {

        this.connectSucess = connectSucess;
    }
}
