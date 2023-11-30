package com.example.aplicaciontfg2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aplicaciontfg2.utilidades.Constants;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityCrearCuentaEmpresa extends AppCompatActivity {

    private EditText editTextCampoUsuario, editTextCampoEmail, editTextCampoContrasenya, editTextCampoRepetirContrasenya;
    private ImageView iconoVolver;
    private Button botonCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta_empresa);

        editTextCampoUsuario = findViewById(R.id.activityCrearCuentaEmpresaEditTextUsuario);
        editTextCampoEmail = findViewById(R.id.activityCrearCuentaEmpresaEditTextEmail);
        editTextCampoContrasenya = findViewById(R.id.activityCrearCuentaEmpresaEditTextContrasenya);
        editTextCampoRepetirContrasenya = findViewById(R.id.activityCrearCuentaEmpresaEditTextRepetirContrasenya);
        iconoVolver = findViewById(R.id.activityCrearCuentaEmpresaimageViewIconoVolverChat);
        botonCrearCuenta = findViewById(R.id.activityCrearCuentaEmpresaBotonCrear);

        iconoVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        botonCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                botonCrearCuentaEmpresa();

            }
        });

    }

    private void botonCrearCuentaEmpresa(){

        String usuarioAux = editTextCampoUsuario.getText().toString();
        String emailAux = editTextCampoEmail.getText().toString();
        String contrasenyaAux = editTextCampoContrasenya.getText().toString();
        String repetirContrasenyaAux = editTextCampoRepetirContrasenya.getText().toString();

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(emailAux);

        if(usuarioAux.length() <= 0){

            Toast.makeText(getApplicationContext(),"Debe introducir un usuario",Toast.LENGTH_SHORT).show();

        }else if(emailAux.length() <= 0){

            Toast.makeText(getApplicationContext(),"Debe introducir un email",Toast.LENGTH_SHORT).show();

        }else if(!(matcher.matches())){

            Toast.makeText(getApplicationContext(),"El campo 'Email' debe ser un correo electrónico",Toast.LENGTH_SHORT).show();

        }else if(contrasenyaAux.length() <= 0){

            Toast.makeText(getApplicationContext(),"Debe introducir una contraseña",Toast.LENGTH_SHORT).show();

        }else if(repetirContrasenyaAux.length() < 6){

            Toast.makeText(getApplicationContext(),"La contraseña debe contener como mínimo 6 carácteres",Toast.LENGTH_SHORT).show();

        }else if(!(contrasenyaAux.equals(repetirContrasenyaAux))){

            Toast.makeText(getApplicationContext(),"Las contraseñas deben coincidir",Toast.LENGTH_SHORT).show();

        }else{

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            firebaseFirestore.collection(Constants.KEY_COLLECION_USERS).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(error != null){

                        return;

                    }

                    if(value != null){

                        boolean cuentaExistente = false;

                        for(DocumentChange documentChange : value.getDocumentChanges()){

                            if(documentChange.getDocument().getString(Constants.KEY_EMAIL).equals(emailAux)){

                                cuentaExistente = true;

                            }

                        }

                        if(cuentaExistente){

                            Toast.makeText(getApplicationContext(),"¡Ya existe una cuenta asociada a ese correo!",Toast.LENGTH_SHORT).show();

                        }else{

                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            firebaseAuth.createUserWithEmailAndPassword(emailAux,contrasenyaAux).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        DocumentReference dr = firebaseFirestore.collection(Constants.KEY_COLLECION_USERS).document(user.getUid());

                                        Map<String,Object> userInfo = new HashMap<>();
                                        userInfo.put("usuario",usuarioAux);
                                        userInfo.put("email",emailAux);
                                        userInfo.put("contraseña",contrasenyaAux);
                                        userInfo.put("isAdmin",false);
                                        userInfo.put("isCompany",true);

                                        dr.set(userInfo);

                                        Toast.makeText(getApplicationContext(),"Cuenta de compañia creada",Toast.LENGTH_SHORT).show();

                                        finish();

                                    }

                                }
                            });

                        }

                    }

                }
            });

        }

    }

}