package com.example.apporg.Eventos;


public class Evento {
    protected String nombre,descripcion,horaDesde,fecha;
    protected int posicion,codigoNotif;

    public void setCodigoNotif(int codigoNotif) {
        this.codigoNotif = codigoNotif;
    }

    public int getCodigoNotif() {
        return codigoNotif;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getFecha(){
        return fecha;
    }
    public void setFecha(String fecha){
        this.fecha = fecha;
    }

    public Evento() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHoraDesde() {
        return horaDesde;
    }

    public void setHoraDesde(String horaDesde) {
        this.horaDesde = horaDesde;
    }

}
