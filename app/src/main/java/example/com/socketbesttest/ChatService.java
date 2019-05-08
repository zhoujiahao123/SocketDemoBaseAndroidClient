package example.com.socketbesttest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by ASUS-NB on 2019/5/8.
 */

public class ChatService extends Service {
    //输出流
    private OutputStream os;
    //控制对象
    private ServiceController mIbinder;
    //处理传来的消息
    private Handler mHandler;
    //判断是否连接成功
    @Override
    public void onCreate() {
        super.onCreate();
        mIbinder = new ServiceController();
        new StartThread().start();
    }
    private class StartThread extends Thread{
        @Override
        public void run() {
            Socket socket;
            try {
                socket = new Socket("222.182.102.230",8888);
                os = socket.getOutputStream();
                new ReceiveThread(socket,mHandler).start();
                RxBus.getInstance().post(new ConnectEntity(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIbinder;
    }

    public class ServiceController extends Binder{
        String mContent;
        public void getControll(Handler handler){
            mHandler = handler;
        }
        public void setmContent(String content){
            mContent = content;
        }
        public void sendMessage() throws Exception{
            new SendMessageThread().start();
        }
        public class SendMessageThread extends Thread{
            @Override
            public void run() {
                try {
                    os.write((mContent+"\r\n").getBytes(("UTF-8")));
                    RxBus.getInstance().post(new NotifyEntity(""));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
