package example.com.socketbesttest;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by ASUS-NB on 2019/5/8.
 */

public class ReceiveThread extends Thread {
    //输入流的获取
    private BufferedReader br = null;
    private static final int RECEIVE_THREAD = 1;
    //线程处理
    private Handler mHandler;
    public ReceiveThread(Socket socket, Handler handler) throws Exception {
        mHandler = handler;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        while (true){
            String content = null;
            try {
                while ((content = br.readLine())!=null){
                    Message msg = new Message();
                    msg.what = RECEIVE_THREAD;
                    msg.obj = content;
                    mHandler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
