package example.com.socketbesttest;

import android.content.Context;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.*;

/**
 * Created by ASUS-NB on 2019/5/7.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context mContext;
    private List<ChatEntity> chatList = new ArrayList<>();
    public ChatAdapter(Context context,List<ChatEntity> chatList){
        mContext =context;
        this.chatList = chatList;
    }
    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_chat,parent,false);
        return new ChatViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        holder.chatTime.setText(chatList.get(position).getTime());
        holder.chatContent.setText(chatList.get(position).getContent());
        holder.chatAvatar.setImageResource(chatList.get(position).isId()?R.drawable.mygirl:R.drawable.mygirl1);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
    class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView chatTime,chatContent;
        de.hdodenhof.circleimageview.CircleImageView chatAvatar;
        public ChatViewHolder(View itemView) {
            super(itemView);
            chatAvatar = itemView.findViewById(R.id.chat_avatar);
            chatContent = itemView.findViewById(R.id.chat_task_content);
            chatTime = itemView.findViewById(R.id.chat_time);
        }
    }
}
