package com.example.aplicaciontfg2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView textoRegistrarseAcceso;
    private EditText campoEmailIniciarSesion, campoContrasenyaIniciarSesion;
    private Button botonIniciarSesion;
    public static FirebaseAuth firebaseAuth;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();

        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){

            if(NetworkIsConnected() && InternetIsConnected()){

                firebaseAuth.signInWithEmailAndPassword(preferenceManager.getString(Constants.KEY_EMAIL),preferenceManager.getString(Constants.KEY_PASSWORD));
                pasarMenuEmpresas(preferenceManager.getString(Constants.KEY_EMAIL));

            }else{

                Toast.makeText(getApplicationContext(),"No hay conexión a internet!",Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,false);

            }

        }

        textoRegistrarseAcceso = findViewById(R.id.textoIniciarSesionRegistro);
        campoEmailIniciarSesion = findViewById(R.id.campoEmailIniciarSesion);
        campoContrasenyaIniciarSesion = findViewById(R.id.campoContrasenyaIniciarSesion);
        botonIniciarSesion = findViewById(R.id.botonIniciarSesion);

        textoRegistrarseAcceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                botonRegistrarse();

            }
        });

        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                botonIniciarSesion();

            }
        });

    }

    private void botonRegistrarse(){

        Intent intentRegistrarse = new Intent(this,ActivityRegistrarse.class);
        startActivity(intentRegistrarse);

    }

    private void botonIniciarSesion(){

        String email = campoEmailIniciarSesion.getText().toString();
        String contrasenya = campoContrasenyaIniciarSesion.getText().toString();

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);

        if(campoEmailIniciarSesion.getText().length() <= 0){

            Toast.makeText(getApplicationContext(),"Debe introducir un correo electrónico",Toast.LENGTH_SHORT).show();

        }else if(!(matcher.matches())){

            Toast.makeText(getApplicationContext(),"El campo 'Email' debe ser un correo electrónico",Toast.LENGTH_SHORT).show();

        }else if(campoContrasenyaIniciarSesion.getText().length() <= 0){

            Toast.makeText(getApplicationContext(),"Debe introducir una contraseña",Toast.LENGTH_SHORT).show();

        }else{

            ProgressDialog cargaAplicacion = new ProgressDialog(this);
            cargaAplicacion.setMessage("Realizando el acceso...");

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            cargaAplicacion.show();

            firebaseAuth.signInWithEmailAndPassword(email,contrasenya).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        DocumentReference dr = firebaseFirestore.collection("cuentas").document(user.getUid());
                        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isSuccessful()){

                                    DocumentSnapshot document = task.getResult();

                                    if(document.exists()){

                                        String usuario = document.getString("usuario");
                                        String contrasenya = document.getString("contraseña");
                                        boolean isAdmin = document.getBoolean("isAdmin");
                                        boolean isCompany = document.getBoolean("isCompany");

                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                                        preferenceManager.putString(Constants.KEY_USER_ID,user.getUid());
                                        preferenceManager.putString(Constants.KEY_NAME,usuario);
                                        preferenceManager.putString(Constants.KEY_EMAIL,user.getEmail());
                                        preferenceManager.putString(Constants.KEY_PASSWORD,contrasenya);
                                        preferenceManager.putBoolean(Constants.KEY_ADMIN,isAdmin);
                                        preferenceManager.putBoolean(Constants.KEY_COMPANY,isCompany);

                                        campoEmailIniciarSesion.getText().clear();
                                        campoContrasenyaIniciarSesion.getText().clear();

                                        Toast.makeText(getApplicationContext(),"¡Bienvenido " + usuario + "!",Toast.LENGTH_SHORT).show();
                                        pasarMenuEmpresas(email);

                                    }

                                }

                            }
                        });

                    }else{

                        Toast.makeText(getApplicationContext(),"Ha ocurrido un error durante el acceso",Toast.LENGTH_SHORT).show();

                    }

                    cargaAplicacion.dismiss();

                }
            });

        }

    }

    private void pasarMenuEmpresas(String email){

        Intent intentMenu = new Intent(this,ActivityMenuEmpresas.class);
        intentMenu.putExtra("email",email);
        startActivity(intentMenu);

    }

    private boolean NetworkIsConnected() {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }

    private boolean InternetIsConnected() {

        try {

            String command = "ping -c 1 google.com";

            return (Runtime.getRuntime().exec(command).waitFor() == 0);

        } catch (Exception e) {

            return false;

        }
    }

}