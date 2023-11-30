package com.example.aplicaciontfg2.entidades;

public class ItemEmpresa {

    private String titulo, descripcion;
    private String imagenEmpresa, correoAsociado;

    public ItemEmpresa(){


    }

    public ItemEmpresa(String titulo, String descripcion,String imagenEmpresa, String correoAsociado){

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenEmpresa = imagenEmpresa;
        this.correoAsociado = correoAsociado;

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenEmpresa() {
        return imagenEmpresa;
    }

    public void setImagenEmpresa(String imagenEmpresa) {
        this.imagenEmpresa = imagenEmpresa;
    }

    public String getCorreoAsociado() {
        return correoAsociado;
    }

    public void setCorreoAsociado(String correoAsociado) {
        this.correoAsociado = correoAsociado;
    }
}
