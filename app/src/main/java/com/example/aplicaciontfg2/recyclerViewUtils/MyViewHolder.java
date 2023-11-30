package com.example.aplicaciontfg2.recyclerViewUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.aplicaciontfg2.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{

    private TextView textoTituloEmpresaItem, textoDescripcionEmpresaItem;
    private ImageView imagenEmpresaItem;
    private LinearLayout rootLayoutEmpresa;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        textoTituloEmpresaItem = itemView.findViewById(R.id.textoTituloEmpresaItem);
        textoDescripcionEmpresaItem = itemView.findViewById(R.id.textoDescripcionEmpresaItem);
        imagenEmpresaItem = itemView.findViewById(R.id.imagenEmpresaItem);
        rootLayoutEmpresa = itemView.findViewById(R.id.rootLayoutListaEmpresas);

    }

    public TextView getTextoTituloEmpresaItem() {
        return textoTituloEmpresaItem;
    }

    public void setTextoTituloEmpresaItem(TextView textoTituloEmpresaItem) {
        this.textoTituloEmpresaItem = textoTituloEmpresaItem;
    }

    public TextView getTextoDescripcionEmpresaItem() {
        return textoDescripcionEmpresaItem;
    }

    public void setTextoDescripcionEmpresaItem(TextView textoDescripcionEmpresaItem) {
        this.textoDescripcionEmpresaItem = textoDescripcionEmpresaItem;
    }

    public ImageView getImagenEmpresaItem() {
        return imagenEmpresaItem;
    }

    public void setImagenEmpresaItem(ImageView imagenEmpresaItem) {
        this.imagenEmpresaItem = imagenEmpresaItem;
    }

    public LinearLayout getRootLayoutEmpresa() {
        return rootLayoutEmpresa;
    }

    public void setRootLayoutEmpresa(LinearLayout rootLayoutEmpresa) {
        this.rootLayoutEmpresa = rootLayoutEmpresa;
    }
}
