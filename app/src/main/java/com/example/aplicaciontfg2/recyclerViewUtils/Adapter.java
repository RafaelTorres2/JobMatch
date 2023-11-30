package com.example.aplicaciontfg2.recyclerViewUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicaciontfg2.*;
import com.example.aplicaciontfg2.entidades.ItemEmpresa;
import com.example.aplicaciontfg2.utilidades.Constants;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<MyViewHolder>{

    private Context context;
    private List<ItemEmpresa> listaEmpresas;

    public Adapter(){


    }

    public Adapter(Context context,List<ItemEmpresa> listaEmpresas){

        this.context = context;
        this.listaEmpresas = listaEmpresas;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.itemempresa_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.getTextoTituloEmpresaItem().setText(listaEmpresas.get(position).getTitulo());
        holder.getTextoDescripcionEmpresaItem().setText(listaEmpresas.get(position).getDescripcion());
        holder.getImagenEmpresaItem().setImageBitmap(getImage(listaEmpresas.get(position).getImagenEmpresa()));
        holder.getRootLayoutEmpresa().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore.collection(Constants.KEY_COLLECTION_COMPANIES).document(listaEmpresas.get(holder.getAdapterPosition()).getCorreoAsociado()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){

                            return;

                        }

                        if(value != null){

                            String titulo = value.getString("titulo");
                            String tipo = value.getString("tipo");
                            String descripcion = value.getString("descripcion");
                            String imagenEncode = value.getString("imagen");
                            boolean aceptanEmpleados = value.getBoolean("aceptanEmpleados");
                            boolean aceptanBecarios = value.getBoolean("aceptanBecarios");
                            String web = value.getString("web");
                            String sectores = value.getString("sector");
                            String fundacion = value.getString("fundacion");
                            String correo = value.getString("correo");
                            String telefono = value.getString("telefono");
                            String correoCuenta = value.getString("correoCuenta");

                            Intent intent = new Intent(context,ActivityEmpresa.class);
                            intent.putExtra("titulo",titulo);
                            intent.putExtra("tipo",tipo);
                            intent.putExtra("descripcion",descripcion);
                            intent.putExtra("imagen",imagenEncode);
                            intent.putExtra("aceptanEmpleados",aceptanEmpleados);
                            intent.putExtra("aceptanBecarios",aceptanBecarios);
                            intent.putExtra("web",web);
                            intent.putExtra("sectores",sectores);
                            intent.putExtra("fundacion",fundacion);
                            intent.putExtra("correo",correo);
                            intent.putExtra("telefono",telefono);
                            intent.putExtra("correoCuenta",correoCuenta);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            context.startActivity(intent);

                        }

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return listaEmpresas.size();
    }

    private Bitmap getImage(String imagenEncoded){

        byte[] bytes = Base64.decode(imagenEncoded,Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);

    }

}
