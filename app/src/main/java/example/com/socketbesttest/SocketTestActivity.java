package example.com.socketbesttest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by ASUS-NB on 2019/5/7.
 */

public class SocketTestActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Subscription subscriForChangeText;
    private Subscription subscrForSendMsg;
    private ChatAdapter mAdapter;
    private EditText editText;
    private Button btnSendMsg;
    private static final int RECEIVE_THREAD = 1;
    private static final int UPDATE_IN_MAIN_THREAD = 2;
    private Handler mHandler;
    private ChatService.ServiceController mController;
    private List<ChatEntity> chatList = new ArrayList<>();
    //加入该元素到chatlist中
    private void addChatEntity(String content,boolean isMe){
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("MM/dd/yyyy HH:mm");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String time = sdf.format(date); // 输出已经格式化的现在时间（24小时制）
        //表示这是我发出的信息
        ChatEntity chatEntity = new ChatEntity(content,time,isMe);
        chatList.add(chatEntity);
    }
    //创建RxBus，异步处理消息。
    private void initRxBus(){
        subscrForSendMsg = RxBus.getInstance().toObserverable(NotifyEntity.class).subscribe(new Action1<NotifyEntity>() {
            @Override
            public void call(NotifyEntity notifyEntity) {
                String content = editText.getText().toString();
                editText.setText("");
                //true表示是自己发出的消息
                addChatEntity(content,true);
                Message msg = new Message();
                msg.what = UPDATE_IN_MAIN_THREAD;
                mHandler.sendMessage(msg);
            }
        });
        subscriForChangeText = RxBus.getInstance().toObserverable(ConnectEntity.class).subscribe(new Action1<ConnectEntity>() {
            @Override
            public void call(ConnectEntity connectEntity) {
                if(connectEntity.isConnectSucess()){
                    btnSendMsg.setText("发送消息");
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscrForSendMsg.unsubscribe();
        subscriForChangeText.unsubscribe();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatui);
        initView();
        initData();
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.group_recycler_view);
        btnSendMsg = (Button) findViewById(R.id.sendmsg_btn);
        btnSendMsg.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.sendmsg_tv);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void initData(){
        initRxBus();
        mHandler=new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case RECEIVE_THREAD:
                        addChatEntity(msg.obj.toString(),false);
                        updateAdapter();
                    case UPDATE_IN_MAIN_THREAD:
                        updateAdapter();
                        break;
                }
            }
        };
    }
    //更新recycleview
    private void updateAdapter(){
        mAdapter = new ChatAdapter(SocketTestActivity.this,chatList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SocketTestActivity.this));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendmsg_btn:
                if(btnSendMsg.getText().toString().equals("连接服务")){
                    Intent intent = new Intent(this,ChatService.class);
                    bindService(intent,connection,BIND_AUTO_CREATE);
                }else{
                    try {
                        if(!editText.getText().toString().equals("")){
                            mController.setmContent(editText.getText().toString());
                            mController.sendMessage();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mController= (ChatService.ServiceController) iBinder;
            mController.getControll(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
