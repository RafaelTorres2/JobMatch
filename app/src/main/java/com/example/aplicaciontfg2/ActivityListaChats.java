package com.example.aplicaciontfg2;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aplicaciontfg2.entidades.ChatMessage;
import com.example.aplicaciontfg2.entidades.User;
import com.example.aplicaciontfg2.listeners.ConversionListener;
import com.example.aplicaciontfg2.recyclerViewUtils.RecentConversationsAdapter;
import com.example.aplicaciontfg2.utilidades.Constants;
import com.example.aplicaciontfg2.utilidades.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityListaChats extends ActivityBase implements ConversionListener {

    private PreferenceManager preferenceManager;
    private FloatingActionButton botonInsertarNuevoChat;
    private RecyclerView recyclerViewListaChats;
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBarActivityListaChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_chats);

        preferenceManager = new PreferenceManager(getApplicationContext());

        botonInsertarNuevoChat = findViewById(R.id.fabNewChat);
        recyclerViewListaChats = findViewById(R.id.conversationsRecyclerView);
        progressBarActivityListaChats = findViewById(R.id.progressBarActivityListaChats);

        init();
        listenConversations();

        botonInsertarNuevoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),ActivityUsers.class);
                startActivity(intent);

            }
        });

    }

    private void init(){

        conversations = new ArrayList<>();
        conversationsAdapter = new RecentConversationsAdapter(conversations,this);
        recyclerViewListaChats.setAdapter(conversationsAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(preferenceManager.getBoolean(Constants.KEY_ADMIN)){

            botonInsertarNuevoChat.setVisibility(View.VISIBLE);
            botonInsertarNuevoChat.setActivated(true);

        }else{

            botonInsertarNuevoChat.setVisibility(View.INVISIBLE);
            botonInsertarNuevoChat.setActivated(false);

        }

    }

    private void listenConversations(){

        firebaseFirestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS).whereEqualTo(Constants.KEY_SENDER_ID,MainActivity.firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){

                    return;

                }

                if(value != null){

                    for(DocumentChange documentChange : value.getDocumentChanges()){

                        if(documentChange.getType() == DocumentChange.Type.ADDED){

                            String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                            String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);

                            ChatMessage chatMessage = new ChatMessage();

                            chatMessage.setSenderId(senderId);
                            chatMessage.setReceiverId(receiverId);

                            if(MainActivity.firebaseAuth.getCurrentUser().getUid().equals(senderId)){

                                chatMessage.setConversionName(documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME));
                                chatMessage.setConversionId(documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID));

                            }else{

                                chatMessage.setConversionName(documentChange.getDocument().getString(Constants.KEY_SENDER_NAME));
                                chatMessage.setConversionId(documentChange.getDocument().getString(Constants.KEY_SENDER_ID));

                            }

                            chatMessage.setMessage(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE));
                            chatMessage.setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                            conversations.add(chatMessage);

                        }else if(documentChange.getType() == DocumentChange.Type.MODIFIED){

                            for(int cont = 0 ; cont < conversations.size() ; cont++){

                                String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                                String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);

                                if(conversations.get(cont).getSenderId().equals(senderId) && conversations.get(cont).getReceiverId().equals(receiverId)){

                                    conversations.get(cont).setMessage(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE));
                                    conversations.get(cont).setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                                    break;

                                }

                            }

                        }

                    }

                    Collections.sort(conversations, (obj1,obj2) -> obj2.getDateObject().compareTo(obj1.getDateObject()));
                    conversationsAdapter.notifyDataSetChanged();
                    recyclerViewListaChats.smoothScrollToPosition(0);
                    recyclerViewListaChats.setVisibility(View.VISIBLE);
                    progressBarActivityListaChats.setVisibility(View.GONE);

                }

            }
        });

        firebaseFirestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS).whereEqualTo(Constants.KEY_RECEIVER_ID,MainActivity.firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){

                    return;

                }

                if(value != null){

                    for(DocumentChange documentChange : value.getDocumentChanges()){

                        if(documentChange.getType() == DocumentChange.Type.ADDED){

                            String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                            String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);

                            ChatMessage chatMessage = new ChatMessage();

                            chatMessage.setSenderId(senderId);
                            chatMessage.setReceiverId(receiverId);

                            if(MainActivity.firebaseAuth.getCurrentUser().getUid().equals(senderId)){

                                chatMessage.setConversionName(documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME));
                                chatMessage.setConversionId(documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID));

                            }else{

                                chatMessage.setConversionName(documentChange.getDocument().getString(Constants.KEY_SENDER_NAME));
                                chatMessage.setConversionId(documentChange.getDocument().getString(Constants.KEY_SENDER_ID));

                            }

                            chatMessage.setMessage(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE));
                            chatMessage.setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                            conversations.add(chatMessage);

                        }else if(documentChange.getType() == DocumentChange.Type.MODIFIED){

                            for(int cont = 0 ; cont < conversations.size() ; cont++){

                                String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                                String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);

                                if(conversations.get(cont).getSenderId().equals(senderId) && conversations.get(cont).getReceiverId().equals(receiverId)){

                                    conversations.get(cont).setMessage(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE));
                                    conversations.get(cont).setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                                    break;

                                }

                            }

                        }

                    }

                    Collections.sort(conversations, (obj1,obj2) -> obj2.getDateObject().compareTo(obj1.getDateObject()));
                    conversationsAdapter.notifyDataSetChanged();
                    recyclerViewListaChats.smoothScrollToPosition(0);
                    recyclerViewListaChats.setVisibility(View.VISIBLE);
                    progressBarActivityListaChats.setVisibility(View.GONE);

                }

            }
        });

    }

    @Override
    public void onConversionClicked(User user) {

        Intent intent = new Intent(getApplicationContext(),ActivityChat.class);
        intent.putExtra(Constants.KEY_USER,user);

        startActivity(intent);

    }
}