package com.example.aplicaciontfg2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicaciontfg2.utilidades.Constants;
import com.example.aplicaciontfg2.utilidades.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ActivityRegistrarse extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private TextView textoIniciarSesionRegistro;
    private Button botonRegistro;
    private EditText campoUsuarioRegistrarse, campoEmailRegistrarse, campoContrasenyaRegistrarse, campoRepetirContrasenyaRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        preferenceManager = new PreferenceManager(getApplicationContext());
        textoIniciarSesionRegistro = findViewById(R.id.textoIniciarSesionRegistro);
        botonRegistro = findViewById(R.id.botonIniciarSesion);
        campoUsuarioRegistrarse = findViewById(R.id.campoUsuarioRegistro);
        campoEmailRegistrarse = findViewById(R.id.campoEmailRegistro);
        campoContrasenyaRegistrarse = findViewById(R.id.campoContrasenyaIniciarSesion);
        campoRepetirContrasenyaRegistrarse = findViewById(R.id.campoRepetirContrasenyaRegistro);

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                botonRegistrarse();

            }
        });

        textoIniciarSesionRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                botonIniciarSesion();

            }
        });

    }

    private void botonIniciarSesion(){

        Intent intentIniciarSesion = new Intent(this,MainActivity.class);
        startActivity(intentIniciarSesion);

    }

    private void botonRegistrarse(){

        if(campoUsuarioRegistrarse.getText().length() <= 0){

            Toast.makeText(getApplicationContext(),"Debe introducir un usuario",Toast.LENGTH_SHORT).show();

        }else if(campoEmailRegistrarse.getText().length() <= 0){

            Toast.makeText(getApplicationContext(),"Debe introducir un email",Toast.LENGTH_SHORT).show();

        }else if(campoContrasenyaRegistrarse.getText().length() <= 0){

            Toast.makeText(getApplicationContext(),"Debe introducir una contraseña",Toast.LENGTH_SHORT).show();

        }else if(campoContrasenyaRegistrarse.getText().length() < 6){

            Toast.makeText(getApplicationContext(),"La contraseña debe contener como mínimo 6 carácteres",Toast.LENGTH_SHORT).show();

        }else if(!(campoContrasenyaRegistrarse.getText().toString().equals(campoRepetirContrasenyaRegistrarse.getText().toString()))){

            Toast.makeText(getApplicationContext(),"Las contraseñas deben coincidir",Toast.LENGTH_SHORT).show();

        }else{

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseFirestore fStore = FirebaseFirestore.getInstance();

            String usuario = campoUsuarioRegistrarse.getText().toString();
            String email = campoEmailRegistrarse.getText().toString();
            String contrasenya = campoContrasenyaRegistrarse.getText().toString();

            fStore.collection(Constants.KEY_COLLECION_USERS).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(error != null){

                        return;

                    }

                    if(value != null){

                        boolean cuentaExiste = false;

                        for(DocumentChange documentChange : value.getDocumentChanges()){

                            if(documentChange.getDocument().getString(Constants.KEY_EMAIL).equals(email)){

                                cuentaExiste = true;

                            }

                        }

                        if(cuentaExiste){

                            Toast.makeText(getApplicationContext(),"Ya existe una cuenta asociada a ese correo electrónico",Toast.LENGTH_SHORT).show();
                            return;

                        }else{

                        }

                    }

                }
            });

            firebaseAuth.createUserWithEmailAndPassword(email,contrasenya).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(),"Usuario registrado correctamente",Toast.LENGTH_SHORT).show();
                        DocumentReference dr = fStore.collection("cuentas").document(user.getUid());
                        Map<String,Object> userInfo = new HashMap<>();
                        userInfo.put("usuario",usuario);
                        userInfo.put("email",email);
                        userInfo.put("contraseña",contrasenya);
                        userInfo.put("isAdmin",false);
                        userInfo.put("isCompany",false);

                        dr.set(userInfo);

                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(Constants.KEY_USER_ID,firebaseAuth.getCurrentUser().getUid());
                        preferenceManager.putString(Constants.KEY_NAME,usuario);
                        preferenceManager.putString(Constants.KEY_EMAIL,email);
                        preferenceManager.putString(Constants.KEY_PASSWORD,contrasenya);
                        preferenceManager.putBoolean(Constants.KEY_ADMIN,false);
                        preferenceManager.putBoolean(Constants.KEY_COMPANY,false);

                        finish();

                    }else{

                        Toast.makeText(getApplicationContext(),"Ha ocurrido un error durante la creación del usuario",Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

    }
}