package com.example.aplicaciontfg2.recyclerViewUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicaciontfg2.R;
import com.example.aplicaciontfg2.entidades.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ChatMessage> chatMessages;
    private String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(){


    }

    public ChatAdapter(List<ChatMessage> chatMessages,String senderId){

        this.chatMessages = chatMessages;
        this.senderId = senderId;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == VIEW_TYPE_SENT){

            return new SentMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemsentmessage_view,parent,false));

        }else{

            return new ReceivedMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemreceivedmessage_view,parent,false));

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == VIEW_TYPE_SENT){

            ((SentMessageViewHolder)holder).setData(chatMessages.get(position));

        }else{

            ((ReceivedMessageViewHolder)holder).setData(chatMessages.get(position));

        }

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(chatMessages.get(position).getSenderId().equals(senderId)){

            return VIEW_TYPE_SENT;

        }else{

            return VIEW_TYPE_RECEIVED;

        }

    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewItemMensajeEnviado, textViewItemFechaHoraEnviado;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewItemMensajeEnviado = itemView.findViewById(R.id.textViewItemMensajeEnviado);
            textViewItemFechaHoraEnviado = itemView.findViewById(R.id.textViewFechaHoraItemMensajeEnviado);

        }

        private void setData(ChatMessage chatMessage){

            textViewItemMensajeEnviado.setText(chatMessage.getMessage());
            textViewItemFechaHoraEnviado.setText(chatMessage.getDateTime());

        }

    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewItemMensajeRecibido, textViewItemFechaHoraRecibido;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewItemMensajeRecibido = itemView.findViewById(R.id.textViewItemMensajeRecibido);
            textViewItemFechaHoraRecibido = itemView.findViewById(R.id.textViewFechaHoraItemMensajeRecibido);

        }

        private void setData(ChatMessage chatMessage){

            textViewItemMensajeRecibido.setText(chatMessage.getMessage());
            textViewItemFechaHoraRecibido.setText(chatMessage.getDateTime());

        }

    }

}
