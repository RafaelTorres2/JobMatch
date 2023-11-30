package com.example.aplicaciontfg2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aplicaciontfg2.utilidades.Constants;
import com.example.aplicaciontfg2.utilidades.PreferenceManager;

public class ActivityPerfil extends AppCompatActivity {

    private ImageView imageViewIconoVolver;
    private TextView textViewNombre, textViewCorreo, textViewAdmin, textViewEmpresa;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        preferenceManager = new PreferenceManager(getApplicationContext());
        imageViewIconoVolver = findViewById(R.id.activityPerfilimageViewIconoVolverChat);
        textViewNombre = findViewById(R.id.textViewNombreActivityPerfil);
        textViewCorreo = findViewById(R.id.textViewCorreoPerfil);
        textViewAdmin = findViewById(R.id.textViewAdminPerfil);
        textViewEmpresa = findViewById(R.id.textViewEmpresaPerfil);

        imageViewIconoVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        textViewNombre.setText(preferenceManager.getString(Constants.KEY_NAME));
        textViewCorreo.setText(preferenceManager.getString(Constants.KEY_EMAIL));

        if(preferenceManager.getBoolean(Constants.KEY_ADMIN)){

            textViewAdmin.setText("Si");

        }else{

            textViewAdmin.setText("No");

        }

        if(preferenceManager.getBoolean(Constants.KEY_COMPANY)){

            textViewEmpresa.setText("Si");

        }else{

            textViewEmpresa.setText("No");

        }

    }
}