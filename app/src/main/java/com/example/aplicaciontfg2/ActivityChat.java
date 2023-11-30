package com.example.aplicaciontfg2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicaciontfg2.entidades.ChatMessage;
import com.example.aplicaciontfg2.entidades.User;
import com.example.aplicaciontfg2.network.ApiClient;
import com.example.aplicaciontfg2.network.ApiService;
import com.example.aplicaciontfg2.recyclerViewUtils.ChatAdapter;
import com.example.aplicaciontfg2.utilidades.Constants;
import com.example.aplicaciontfg2.utilidades.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChat extends ActivityBase {

    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore firebaseFirestore;
    private User receiverUser;
    private TextView textViewNameChat, textViewAvailabilityChat;
    private EditText editTextMensajeChat;
    private FrameLayout frameLayoutEnviarMensajeChat;
    private ProgressBar progressBarChat;
    private ImageView imageViewIconoVolverChat;
    private RecyclerView recyclerViewChat;
    private String conversionId = null;
    private Boolean isReceiverAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        textViewNameChat = findViewById(R.id.textViewNameChat);
        imageViewIconoVolverChat = findViewById(R.id.imageViewIconoVolverChat);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMensajeChat = findViewById(R.id.editTextMensajeChat);
        frameLayoutEnviarMensajeChat = findViewById(R.id.layoutSend);
        progressBarChat = findViewById(R.id.progressBarActivityChat);
        textViewAvailabilityChat = findViewById(R.id.textViewAvailabilityChat);

        imageViewIconoVolverChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        frameLayoutEnviarMensajeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();

            }
        });

        loadReceiverDetails();
        init();
        listenMessages();

    }

    private void init(){

        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages,MainActivity.firebaseAuth.getCurrentUser().getUid());
        recyclerViewChat.setAdapter(chatAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    private void sendMessage(){

        HashMap<String,Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, MainActivity.firebaseAuth.getCurrentUser().getUid());
        message.put(Constants.KEY_RECEIVER_ID,receiverUser.getId());
        message.put(Constants.KEY_MESSAGE,editTextMensajeChat.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());

        firebaseFirestore.collection(Constants.KEY_COLLECTION_CHAT).add(message);

        if(conversionId != null){

            updateConversion(editTextMensajeChat.getText().toString());

        }else{

            HashMap<String,Object> conversion = new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID,MainActivity.firebaseAuth.getCurrentUser().getUid());
            conversion.put(Constants.KEY_SENDER_NAME,preferenceManager.getString(Constants.KEY_NAME));
            conversion.put(Constants.KEY_RECEIVER_ID,receiverUser.getId());
            conversion.put(Constants.KEY_RECEIVER_NAME,receiverUser.getName());
            conversion.put(Constants.KEY_LAST_MESSAGE,editTextMensajeChat.getText().toString());
            conversion.put(Constants.KEY_TIMESTAMP,new Date());

            addConversion(conversion);

        }

        if(!isReceiverAvailable){

            try {

                JSONArray tokens = new JSONArray();
                tokens.put(receiverUser.getToken());

                JSONObject data = new JSONObject();
                data.put(Constants.KEY_USER_ID,MainActivity.firebaseAuth.getCurrentUser().getUid());
                data.put(Constants.KEY_NAME,preferenceManager.getString(Constants.KEY_NAME));
                data.put(Constants.KEY_FCM_TOKEN,preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                data.put(Constants.KEY_MESSAGE,editTextMensajeChat.getText().toString());

                JSONObject body = new JSONObject();
                body.put(Constants.REMOTE_MSG_DATA,data);
                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);

                //sendNotification(body.toString());

            }catch (Exception e){

                //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }

        }

        editTextMensajeChat.setText(null);

    }

    /*private void sendNotification(String messageBody){

        ApiClient.getClient().create(ApiService.class).sendMessage(Constants.getRemoteMsgHeaders(),messageBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                if(response.isSuccessful()){

                    try{

                        if(response.body() != null){

                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");

                            if(responseJson.getInt("failure") == 1){

                                JSONObject error = (JSONObject) results.get(0);

                                Toast.makeText(getApplicationContext(),error.getString("error"),Toast.LENGTH_SHORT).show();
                                return;

                            }

                        }

                    }catch (JSONException e){

                        e.printStackTrace();

                    }

                    Toast.makeText(getApplicationContext(),"Notificacion enviada correctamente",Toast.LENGTH_SHORT).show();

                }else{

                    Toast.makeText(getApplicationContext(),"Error " + response.code(),Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {

                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }*/

    private void listenAvailabilityOfReceiver(){

        firebaseFirestore.collection(Constants.KEY_COLLECION_USERS).document(receiverUser.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){

                    return;

                }

                if(value != null){

                    if(value.getLong(Constants.KEY_AVAILABILITY) != null){

                        int availability = Objects.requireNonNull(value.getLong(Constants.KEY_AVAILABILITY).intValue());
                        isReceiverAvailable = availability == 1;

                    }

                    receiverUser.setToken(value.getString(Constants.KEY_FCM_TOKEN));

                }

                if(isReceiverAvailable){

                    textViewAvailabilityChat.setVisibility(View.VISIBLE);

                }else{

                    textViewAvailabilityChat.setVisibility(View.GONE);

                }

            }
        });

    }

    private void listenMessages(){

        firebaseFirestore.collection(Constants.KEY_COLLECTION_CHAT).whereEqualTo(Constants.KEY_SENDER_ID, MainActivity.firebaseAuth.getCurrentUser().getUid())
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.getId()).addSnapshotListener(eventListener);

        firebaseFirestore.collection(Constants.KEY_COLLECTION_CHAT).whereEqualTo(Constants.KEY_SENDER_ID,receiverUser.getId()).
                whereEqualTo(Constants.KEY_RECEIVER_ID,MainActivity.firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(eventListener);

    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {

        if(error != null){

            return;

        }

        if(value != null){

            int count = chatMessages.size();

            for(DocumentChange documentChange : value.getDocumentChanges()){

                if(documentChange.getType() == DocumentChange.Type.ADDED){

                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSenderId(documentChange.getDocument().getString(Constants.KEY_SENDER_ID));
                    chatMessage.setReceiverId(documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID));
                    chatMessage.setMessage(documentChange.getDocument().getString(Constants.KEY_MESSAGE));
                    chatMessage.setDateTime(getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP)));
                    chatMessage.setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));

                    chatMessages.add(chatMessage);

                }

            }

            Collections.sort(chatMessages, (obj1, obj2) -> obj1.getDateObject().compareTo(obj2.getDateObject()));

            if(count == 0){

                chatAdapter.notifyDataSetChanged();

            }else{

                chatAdapter.notifyItemRangeInserted(chatMessages.size(),chatMessages.size());
                recyclerViewChat.smoothScrollToPosition(chatMessages.size() - 1);

            }

            recyclerViewChat.setVisibility(View.VISIBLE);

        }

        progressBarChat.setVisibility(View.GONE);

        if(conversionId == null){

            checkForConversion();

        }

    });

    private void loadReceiverDetails(){

        receiverUser = (User)getIntent().getSerializableExtra(Constants.KEY_USER);
        textViewNameChat.setText(receiverUser.getName());

    }

    private String getReadableDateTime(Date date){

        return new SimpleDateFormat("dd MMMM, yyyy - hh:mm a", Locale.getDefault()).format(date);

    }

    private void addConversion(HashMap<String,Object> conversion){

        firebaseFirestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS).add(conversion).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                conversionId = documentReference.getId();

            }
        });

    }

    private void updateConversion(String message){

        DocumentReference documentReference = firebaseFirestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversionId);

        documentReference.update(Constants.KEY_LAST_MESSAGE,message,Constants.KEY_TIMESTAMP,new Date());

    }

    private void checkForConversion(){

        if(chatMessages.size() != 0){

            checkForConversionRemotely(MainActivity.firebaseAuth.getCurrentUser().getUid(),receiverUser.getId());
            checkForConversionRemotely(receiverUser.getId(),MainActivity.firebaseAuth.getCurrentUser().getUid());

        }

    }

    private void checkForConversionRemotely(String senderId,String receiverId){

        firebaseFirestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS).whereEqualTo(Constants.KEY_SENDER_ID,senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,receiverId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){

                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            conversionId = documentSnapshot.getId();

                        }

                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();

        listenAvailabilityOfReceiver();

    }
}