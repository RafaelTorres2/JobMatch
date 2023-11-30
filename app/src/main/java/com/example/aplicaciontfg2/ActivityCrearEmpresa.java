package com.example.aplicaciontfg2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aplicaciontfg2.utilidades.Constants;
import com.example.aplicaciontfg2.utilidades.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityCrearEmpresa extends AppCompatActivity {

    private ImageView imageViewSeleccionarImagenEmpresa;
    private String encodedImage;
    private EditText tituloEmpresa, tipoEmpresa, descripcionEmpresa, sitioWebEmpresa, sectoresEmpresa, fundacionEmpresa, contactoCorreoEmpresa, telefonoEmpresa, correoAsociadoCuenta;
    private CheckBox checkBoxempleados, checkBoxbecarios;
    private Button botonCrearEmpresa;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_empresa);

        preferenceManager = new PreferenceManager(getApplicationContext());

        imageViewSeleccionarImagenEmpresa = findViewById(R.id.imageViewSeleccionarImagenActivityCrearEmpresa);
        tituloEmpresa = findViewById(R.id.editTextTituloEmpresaActivityCrearEmpresa);
        tipoEmpresa = findViewById(R.id.editTextTipoEmpresaActivityCrearEmpresa);
        descripcionEmpresa = findViewById(R.id.editTextDescripcionEmpresaActivityCrearEmpresa);
        sitioWebEmpresa = findViewById(R.id.editTextSitioWebEmpresaActivityCrearEmpresa);
        sectoresEmpresa = findViewById(R.id.editTextSectoresEmpresaActivityCrearEmpresa);
        fundacionEmpresa = findViewById(R.id.editTextFundacionEmpresaActivityCrearEmpresa);
        contactoCorreoEmpresa = findViewById(R.id.editTextCorreoEmpresaActivityCrearEmpresa);
        telefonoEmpresa = findViewById(R.id.editTextNumeroEmpresaActivityCrearEmpresa);
        correoAsociadoCuenta = findViewById(R.id.editTextCorreoCuentaEmpresaCrearEmpresa);
        checkBoxempleados = findViewById(R.id.checkBoxAceptanEmpleadosCreacionEmpresa);
        checkBoxbecarios = findViewById(R.id.checkBoxAceptanBecariosCreacionEmpresa);
        botonCrearEmpresa = findViewById(R.id.botonCrearPublicacionEmpresa);

        if(!(preferenceManager.getBoolean(Constants.KEY_ADMIN))){

            correoAsociadoCuenta.setText(MainActivity.firebaseAuth.getCurrentUser().getEmail());
            correoAsociadoCuenta.setActivated(false);
            correoAsociadoCuenta.setInputType(InputType.TYPE_NULL);

        }

        imageViewSeleccionarImagenEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);

            }
        });

        botonCrearEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                botonCrearEmpresa();

            }
        });

    }

    private String encodeImage(Bitmap bitmap){

        int previewWidth = 100;
        int previewHeight = 100;
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(bytes,Base64.DEFAULT);

    }

    private ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if(result.getResultCode() == RESULT_OK){

                if(result.getData() != null){

                    Uri imageUri = result.getData().getData();

                    try{

                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageViewSeleccionarImagenEmpresa.setImageBitmap(Bitmap.createScaledBitmap(bitmap,100,100,false));
                        encodedImage = encodeImage(bitmap);


                    }catch (FileNotFoundException e){

                        e.printStackTrace();

                    }

                }

            }

        }
    });

    private void botonCrearEmpresa(){

        String titutoAux = tituloEmpresa.getText().toString();
        String tipoAux = tipoEmpresa.getText().toString();
        String descripcionAux = descripcionEmpresa.getText().toString();
        String sitioWebAux = sitioWebEmpresa.getText().toString();
        String sectoresAux = sectoresEmpresa.getText().toString();
        String fundacionAux = fundacionEmpresa.getText().toString();
        String correoAux = contactoCorreoEmpresa.getText().toString();
        String telefonoAux = telefonoEmpresa.getText().toString();
        String correoCuentaAux = correoAsociadoCuenta.getText().toString();

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(correoCuentaAux);

        if(titutoAux.length() <= 0){

            Toast.makeText(getApplicationContext(),"La empresa debe tener un título",Toast.LENGTH_SHORT).show();

        }else if(tipoAux.length() <= 0){

            Toast.makeText(getApplicationContext(),"La empresa debe especificar de que tipo es",Toast.LENGTH_SHORT).show();

        }else if(descripcionAux.length() <= 0){

            Toast.makeText(getApplicationContext(),"La empresa debe tener una descripción",Toast.LENGTH_SHORT).show();

        }else if(correoCuentaAux.length() <= 0){

            Toast.makeText(getApplicationContext(),"La empresa debe estar asoaciada a un correo electrónico",Toast.LENGTH_SHORT).show();

        }else if(!(matcher.matches())){

            Toast.makeText(getApplicationContext(),"El campo 'Correo asociado' debe ser un correo electrónico",Toast.LENGTH_SHORT).show();

        }else{

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            firebaseFirestore.collection(Constants.KEY_COLLECION_USERS).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(error != null){

                        return;

                    }

                    if(value != null){

                        boolean cuentaExiste = false;

                        for(DocumentChange documentChange : value.getDocumentChanges()){

                            if(documentChange.getDocument().getString(Constants.KEY_EMAIL).equals(correoCuentaAux)){

                                cuentaExiste = true;

                            }

                        }

                        if(!(cuentaExiste)){

                            Toast.makeText(getApplicationContext(),"No existe una cuenta asociada a ese correo electrónico",Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }

                }
            });

            DocumentReference documentReference = firebaseFirestore.collection(Constants.KEY_COLLECTION_COMPANIES).document(correoCuentaAux);

            Map<String,Object> companyInfo = new HashMap<>();

            companyInfo.put("imagen",encodedImage);
            companyInfo.put("titulo",titutoAux);
            companyInfo.put("tipo",tipoAux);
            companyInfo.put("aceptanEmpleados",checkBoxempleados.isChecked());
            companyInfo.put("aceptanBecarios",checkBoxbecarios.isChecked());
            companyInfo.put("descripcion",descripcionAux);
            companyInfo.put("web",sitioWebAux);
            companyInfo.put("sector",sectoresAux);
            companyInfo.put("fundacion",fundacionAux);
            companyInfo.put("correo",correoAux);
            companyInfo.put("telefono",telefonoAux);
            companyInfo.put("correoCuenta",correoCuentaAux);

            documentReference.set(companyInfo);

            Toast.makeText(getApplicationContext(),"Publicación de empresa creada",Toast.LENGTH_SHORT).show();
            finish();

        }

    }

}