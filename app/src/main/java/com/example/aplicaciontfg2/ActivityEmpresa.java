package com.example.aplicaciontfg2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aplicaciontfg2.entidades.User;
import com.example.aplicaciontfg2.listeners.UserListener;
import com.example.aplicaciontfg2.utilidades.Constants;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class ActivityEmpresa extends AppCompatActivity implements UserListener {

    private Button botonIniciarConversacionEmpresa;
    private ImageView imageViewEmpresa, imageViewIconoEmpleadoEmpresa, imageViewIconoBecarioEmpresa;
    private TextView textViewTituloEmpresa, textViewTipoEmpresa, textViewDescripcionEmpresa, textViewWebEmpresa, textViewSectorEmpresa, textViewFundacionEmpresa, textViewCorreoEmpresa, textViewTelefonoEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        botonIniciarConversacionEmpresa = findViewById(R.id.botonIniciarConversacionEmpresa);
        imageViewEmpresa = findViewById(R.id.imageViewPantallaEmpresa);
        imageViewIconoEmpleadoEmpresa = findViewById(R.id.iconoAceptanEmpleadosEmpresa);
        imageViewIconoBecarioEmpresa = findViewById(R.id.iconoAceptanBecariosEmpresa);
        textViewTituloEmpresa = findViewById(R.id.tituloEmpresaPantallaInformacion);
        textViewTipoEmpresa = findViewById(R.id.trabajosEmpresaPantallaInformacion);
        textViewDescripcionEmpresa = findViewById(R.id.textoDescripcionEmpresa);
        textViewWebEmpresa = findViewById(R.id.textViewSitioWebEmpresa);
        textViewSectorEmpresa = findViewById(R.id.textViewSectoresEmpresa);
        textViewFundacionEmpresa = findViewById(R.id.textViewFundacionEmpresa);
        textViewCorreoEmpresa = findViewById(R.id.textViewCorreoEmpresa);
        textViewTelefonoEmpresa = findViewById(R.id.textViewTelefonoEmpresa);

        Intent intent = getIntent();

        imageViewEmpresa.setImageBitmap(getImage(intent.getStringExtra("imagen")));
        textViewTituloEmpresa.setText(intent.getStringExtra("titulo"));
        textViewTipoEmpresa.setText(intent.getStringExtra("tipo"));

        if(intent.getExtras().getBoolean("aceptanEmpleados")){

            imageViewIconoEmpleadoEmpresa.setImageResource(R.drawable.iconotickverde);

        }else{

            imageViewIconoEmpleadoEmpresa.setImageResource(R.drawable.iconocruzroja);

        }

        if(intent.getExtras().getBoolean("aceptanBecarios")){

            imageViewIconoBecarioEmpresa.setImageResource(R.drawable.iconotickverde);

        }else{

            imageViewIconoBecarioEmpresa.setImageResource(R.drawable.iconocruzroja);

        }

        textViewDescripcionEmpresa.setText(intent.getStringExtra("descripcion"));
        textViewWebEmpresa.setText(intent.getStringExtra("web"));
        textViewSectorEmpresa.setText(intent.getStringExtra("sectores"));
        textViewFundacionEmpresa.setText(intent.getStringExtra("fundacion"));
        textViewCorreoEmpresa.setText(intent.getStringExtra("correo"));
        textViewTelefonoEmpresa.setText(intent.getStringExtra("telefono"));

        botonIniciarConversacionEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore.collection(Constants.KEY_COLLECION_USERS).whereEqualTo(Constants.KEY_EMAIL,intent.getStringExtra("correoCuenta")).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){

                            return;

                        }

                        if(value != null){

                            for(DocumentChange documentChange : value.getDocumentChanges()){

                                User user = new User();

                                user.setName(documentChange.getDocument().getString(Constants.KEY_NAME));
                                user.setEmail(documentChange.getDocument().getString(Constants.KEY_EMAIL));
                                user.setToken(documentChange.getDocument().getString(Constants.KEY_FCM_TOKEN));
                                user.setId(documentChange.getDocument().getId());

                                onUserClicked(user);

                            }

                        }

                    }
                });

            }
        });

    }

    @Override
    public void onUserClicked(User user) {

        Intent intent = new Intent(getApplicationContext(),ActivityChat.class);
        intent.putExtra(Constants.KEY_USER,user);

        startActivity(intent);

    }

    private Bitmap getImage(String imagenEncoded){

        byte[] bytes = Base64.decode(imagenEncoded,Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);

    }

}