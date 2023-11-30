package com.example.aplicaciontfg2;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplicaciontfg2.utilidades.Constants;
import com.example.aplicaciontfg2.utilidades.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityBase extends AppCompatActivity {

    private DocumentReference documentReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection(Constants.KEY_COLLECION_USERS).document(MainActivity.firebaseAuth.getCurrentUser().getUid());

    }

    @Override
    protected void onPause() {
        super.onPause();

        documentReference.update(Constants.KEY_AVAILABILITY, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        documentReference.update(Constants.KEY_AVAILABILITY,1);

    }
}
