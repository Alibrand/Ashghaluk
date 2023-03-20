package com.ksacp2022.ashghaluk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatCard> {
    List<Chat> chatList;
    Context context;
    FirebaseAuth firebaseAuth;

    public ChatsListAdapter(List<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
        firebaseAuth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ChatCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_card,parent,false);
        return new ChatCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatCard holder, int position) {
        Chat chat= chatList.get(position);
        String uid= firebaseAuth.getUid();
        //determine the other participant
        List<String> chat_users_ids=chat.getUsers_ids();
        List<String> chat_users_names=chat.getUsers_names();
        String receiver_name;
        String receiver_id;
        int indx=chat_users_ids.indexOf(uid);
        //the index should be either 0 or 1 only
        if(indx==0) {
            receiver_name = chat_users_names.get(1);
            receiver_id=chat_users_ids.get(1);
        }
        else {
            receiver_name = chat_users_names.get(0);
            receiver_id=chat_users_ids.get(0);
        }

        String first_letter=receiver_name.substring(0,1);
        holder.letter_avatar.setText(first_letter);
        holder.name.setText(receiver_name);

        holder.letter_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ChatActivity. class);
                intent.putExtra("chat_id",chat.getId());
                intent.putExtra("receiver_name",receiver_name);
                intent.putExtra("receiver_id",receiver_id);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}

class ChatCard extends RecyclerView.ViewHolder{
    TextView letter_avatar,name;

    public ChatCard(@NonNull View itemView) {
        super(itemView);
        letter_avatar=itemView.findViewById(R.id.letter_avatar);
        name=itemView.findViewById(R.id.name);
    }
}
