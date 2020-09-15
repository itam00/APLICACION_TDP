package com.example.apporg.Recordatorios;

public class Recordatorio {
    protected String nombre;
    protected boolean cumplido;
    public Recordatorio(String nombre,Boolean cumplido){
        this.nombre = nombre;
        this.cumplido = cumplido;
    }
    public Recordatorio(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isCumplido() {
        return cumplido;
    }

    public void setCumplido(boolean cumplido) {
        this.cumplido = cumplido;
    }
}
