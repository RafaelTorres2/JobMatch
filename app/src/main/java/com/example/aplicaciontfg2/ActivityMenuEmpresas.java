package com.example.aplicaciontfg2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicaciontfg2.entidades.ItemEmpresa;
import com.example.aplicaciontfg2.recyclerViewUtils.Adapter;
import com.example.aplicaciontfg2.utilidades.Constants;
import com.example.aplicaciontfg2.utilidades.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityMenuEmpresas extends AppCompatActivity {

    public static List<ItemEmpresa> listaEmpresas = new ArrayList<>();
    private String prueba = null;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private TextView textoEmailMenuLateral, textoUsuarioMenuLateral;
    private PreferenceManager preferenceManager;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item)){

            textoEmailMenuLateral = findViewById(R.id.textoEmailMenuLateral);
            textoUsuarioMenuLateral = findViewById(R.id.textoUsuarioMenuLateral);
            textoEmailMenuLateral.setText(getIntent().getExtras().getString("email"));
            textoUsuarioMenuLateral.setText(preferenceManager.getString(Constants.KEY_NAME));
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empresas);

        preferenceManager = new PreferenceManager(getApplicationContext());
        drawerLayout = findViewById(R.id.drawerLayoutMenu);
        navigationView = findViewById(R.id.menuLateralPrincipal);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.bringToFront();
        getToken();
        cargarEmpresas();

        if(preferenceManager.getBoolean(Constants.KEY_ADMIN)){

            navigationView.inflateMenu(R.menu.menuadmin_view);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch(item.getItemId()){

                        case R.id.perfilAdmin:

                            Intent intent = new Intent(getApplicationContext(),ActivityPerfil.class);
                            startActivity(intent);
                            break;

                        case R.id.chatsAdmin:

                            Intent intent2 = new Intent(getApplicationContext(),ActivityListaChats.class);
                            startActivity(intent2);
                            break;

                        case R.id.crearPublicacionAdmin:

                            Intent intent3 = new Intent(getApplicationContext(),ActivityCrearEmpresa.class);
                            startActivity(intent3);
                            break;

                        case R.id.altaCuentaEmpresa:

                            Intent intent4 = new Intent(getApplicationContext(),ActivityCrearCuentaEmpresa.class);
                            startActivity(intent4);
                            break;

                        case R.id.cerrarSesionAdmin:

                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            DocumentReference dr = firebaseFirestore.collection(Constants.KEY_COLLECION_USERS).document(MainActivity.firebaseAuth.getCurrentUser().getUid());

                            Map<String,Object> updates = new HashMap<>();
                            updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());

                            dr.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        preferenceManager.clear();
                                        listaEmpresas.clear();
                                        MainActivity.firebaseAuth.signOut();


                                        finish();

                                    }else{

                                        Toast.makeText(getApplicationContext(),"No se ha podido cerrar la sesión",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                            Toast.makeText(getApplicationContext(),"Cerrando sesión...",Toast.LENGTH_SHORT).show();
                            break;

                    }

                    return false;

                }
            });

        }else if(preferenceManager.getBoolean(Constants.KEY_COMPANY)){

            navigationView.inflateMenu(R.menu.menuempresa_view);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch(item.getItemId()){

                        case R.id.perfilEmpresa:

                            Intent intent = new Intent(getApplicationContext(),ActivityPerfil.class);
                            startActivity(intent);
                            break;

                        case R.id.chatsEmpresa:

                            Intent intent2 = new Intent(getApplicationContext(),ActivityListaChats.class);
                            startActivity(intent2);
                            break;

                        case R.id.crearPublicacionEmpresa:

                            Intent intent3 = new Intent(getApplicationContext(),ActivityCrearEmpresa.class);
                            startActivity(intent3);
                            break;

                        case R.id.cerrarSesionEmpresa:

                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            DocumentReference dr = firebaseFirestore.collection(Constants.KEY_COLLECION_USERS).document(MainActivity.firebaseAuth.getCurrentUser().getUid());

                            Map<String,Object> updates = new HashMap<>();
                            updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());

                            dr.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        preferenceManager.clear();
                                        listaEmpresas.clear();
                                        MainActivity.firebaseAuth.signOut();


                                        finish();

                                    }else{

                                        Toast.makeText(getApplicationContext(),"No se ha podido cerrar la sesión",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                            Toast.makeText(getApplicationContext(),"Cerrando sesión",Toast.LENGTH_SHORT).show();
                            break;

                    }

                    return false;
                }
            });

        }else{

            navigationView.inflateMenu(R.menu.menu_view);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch(item.getItemId()){

                        case R.id.perfil:

                            Intent intent = new Intent(getApplicationContext(),ActivityPerfil.class);
                            startActivity(intent);
                            break;

                        case R.id.chats:

                            Intent intent2 = new Intent(getApplicationContext(),ActivityListaChats.class);
                            startActivity(intent2);
                            break;

                        case R.id.cerrarSesion:

                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            DocumentReference dr = firebaseFirestore.collection(Constants.KEY_COLLECION_USERS).document(MainActivity.firebaseAuth.getCurrentUser().getUid());

                            Map<String,Object> updates = new HashMap<>();
                            updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());

                            dr.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        preferenceManager.clear();
                                        listaEmpresas.clear();
                                        MainActivity.firebaseAuth.signOut();


                                        finish();

                                    }else{

                                        Toast.makeText(getApplicationContext(),"No se ha podido cerrar la sesión",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                            Toast.makeText(getApplicationContext(),"Cerrando sesión...",Toast.LENGTH_SHORT).show();
                            break;

                    }

                    return false;
                }
            });

        }

    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);

        }else{

            super.onBackPressed();

        }

    }

    private void getToken(){

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);

    }

    private void updateToken(String token){

        preferenceManager.putString(Constants.KEY_FCM_TOKEN,token);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference dr = firebaseFirestore.collection(Constants.KEY_COLLECION_USERS).document(MainActivity.firebaseAuth.getCurrentUser().getUid());

        dr.update(Constants.KEY_FCM_TOKEN,token).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {



            }
        });

    }

    private void cargarEmpresas(){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection(Constants.KEY_COLLECTION_COMPANIES).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){



                }else {

                    for (DocumentChange doc : value.getDocumentChanges()){

                        if (doc.getType() == DocumentChange.Type.ADDED){

                            String tituloAux = doc.getDocument().getString("titulo");
                            String descripcionRealAux = doc.getDocument().getString("descripcion");
                            String descripcionAux = "";
                            String imagenEncode = doc.getDocument().getString("imagen");
                            String correoAsociado = doc.getDocument().getString("correoCuenta");

                            if(descripcionRealAux.length() < 50){

                                descripcionAux = descripcionRealAux;

                            }else{

                                for(int cont = 0 ; cont < 50 ; cont++){

                                    descripcionAux += descripcionRealAux.charAt(cont);

                                }

                            }

                            listaEmpresas.add(new ItemEmpresa(tituloAux,descripcionAux,imagenEncode,correoAsociado));

                            EmpresaFragment.recyclerView.setAdapter(new Adapter(getApplicationContext(),listaEmpresas));

                        }

                    }
                }

            }
        });

    }

}