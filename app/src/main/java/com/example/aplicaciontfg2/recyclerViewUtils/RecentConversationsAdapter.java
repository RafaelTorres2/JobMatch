package com.example.aplicaciontfg2.recyclerViewUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicaciontfg2.R;
import com.example.aplicaciontfg2.entidades.ChatMessage;
import com.example.aplicaciontfg2.entidades.User;
import com.example.aplicaciontfg2.listeners.ConversionListener;

import java.util.List;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder>{

    private List<ChatMessage> chatMessages;
    private ConversionListener conversionListener;

    public RecentConversationsAdapter(){


    }

    public RecentConversationsAdapter(List<ChatMessage> chatMessages, ConversionListener conversionListener){

        this.chatMessages = chatMessages;
        this.conversionListener = conversionListener;

    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemrecentchat_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {

        holder.setData(chatMessages.get(position));

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNameItemRecentChat, textViewMensajeRecienteRecentChat;
        private ConstraintLayout rootLayoutItemRecentChat;

        public ConversionViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNameItemRecentChat = itemView.findViewById(R.id.textViewNameItemRecentChat);
            textViewMensajeRecienteRecentChat = itemView.findViewById(R.id.textViewMensajeRecienteItemRecentChat);
            rootLayoutItemRecentChat = itemView.findViewById(R.id.rootLayoutItemRecentChat);

        }

        private void setData(ChatMessage chatMessage){

            textViewNameItemRecentChat.setText(chatMessage.getConversionName());
            textViewMensajeRecienteRecentChat.setText(chatMessage.getMessage());

            rootLayoutItemRecentChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    User user = new User();

                    user.setId(chatMessage.getConversionId());
                    user.setName(chatMessage.getConversionName());

                    conversionListener.onConversionClicked(user);

                }
            });

        }

    }

}
