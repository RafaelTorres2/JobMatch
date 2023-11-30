package com.example.aplicaciontfg2;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aplicaciontfg2.entidades.User;
import com.example.aplicaciontfg2.listeners.UserListener;
import com.example.aplicaciontfg2.recyclerViewUtils.UsersAdapter;
import com.example.aplicaciontfg2.utilidades.Constants;
import com.example.aplicaciontfg2.utilidades.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityUsers extends ActivityBase implements UserListener {

    private PreferenceManager preferenceManager;
    private TextView textViewErrorMessage;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewUsers;
    private ImageView imageViewIconoVolverItemUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        preferenceManager = new PreferenceManager(getApplicationContext());

        textViewErrorMessage = findViewById(R.id.textViewErrorMessageUsers);
        progressBar = findViewById(R.id.progressBarActivityUser);
        recyclerViewUsers = findViewById(R.id.usersRecyclerView);
        imageViewIconoVolverItemUsers = findViewById(R.id.imageViewIconoVolverUsers);

        getUsers();

        imageViewIconoVolverItemUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

    }

    private void getUsers(){

       loading(true);
       FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

       firebaseFirestore.collection(Constants.KEY_COLLECION_USERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {

                 loading(false);
                 String currentUserId = MainActivity.firebaseAuth.getCurrentUser().getUid();

                 if(task.isSuccessful() && task.getResult() != null){

                    List<User> users = new ArrayList<>();

                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                        if(currentUserId.equals(queryDocumentSnapshot.getId())){

                            continue;

                        }

                        User user = new User();

                        user.setName(queryDocumentSnapshot.getString(Constants.KEY_NAME));
                        user.setEmail(queryDocumentSnapshot.getString(Constants.KEY_EMAIL));
                        user.setToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
                        user.setId(queryDocumentSnapshot.getId());

                        users.add(user);

                    }

                    if(users.size() > 0){

                        UsersAdapter usersAdapter = new UsersAdapter(users, ActivityUsers.this);
                        recyclerViewUsers.setAdapter(usersAdapter);
                        recyclerViewUsers.setVisibility(View.VISIBLE);

                    }else{

                        showErrorMessage();

                    }

                 }else{

                     showErrorMessage();

                 }

           }
       });

    }

    private void showErrorMessage(){

        textViewErrorMessage.setText(String.format("%s","No hay usuarios disponibles"));
        textViewErrorMessage.setVisibility(View.VISIBLE);

    }

    private void loading(Boolean isLoading){

        if(isLoading){

            progressBar.setVisibility(View.VISIBLE);

        }else{

            progressBar.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public void onUserClicked(User user) {

        Intent intent = new Intent(getApplicationContext(),ActivityChat.class);
        intent.putExtra(Constants.KEY_USER,user);
        startActivity(intent);

    }
}