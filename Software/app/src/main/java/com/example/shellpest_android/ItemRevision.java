package com.example.shellpest_android;

public class ItemRevision {
    private String Fecha,Id_Bloque,Fruta,Floracion,N_Arboles,Observaciones,Nivel_Humedad;

    public ItemRevision(String fecha, String id_Bloque, String fruta, String floracion,String n_arboles, String observaciones, String nivel_Humedad) {
        Fecha = fecha;
        Id_Bloque = id_Bloque;
        Fruta = fruta;
        Floracion = floracion;
        Observaciones = observaciones;
        Nivel_Humedad = nivel_Humedad;
        N_Arboles=n_arboles;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getId_Bloque() {
        return Id_Bloque;
    }

    public String getFruta() {
        return Fruta;
    }

    public String getFloracion() {
        return Floracion;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public String getNivel_Humedad() {
        return Nivel_Humedad;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public void setId_Bloque(String id_Bloque) {
        Id_Bloque = id_Bloque;
    }

    public void setFruta(String fruta) {
        Fruta = fruta;
    }

    public void setFloracion(String floracion) {
        Floracion = floracion;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

    public void setNivel_Humedad(String nivel_Humedad) {
        Nivel_Humedad = nivel_Humedad;
    }

    public String getN_Arboles() {
        return N_Arboles;
    }

    public void setN_Arboles(String n_Arboles) {
        N_Arboles = n_Arboles;
    }
}
