package com.example.shellpest_android;

public class ItemCosecha {
    private String Fecha,Id_Bloque,Nombre_Blq,c_codigo_eps,BICO;
    private int n_c_cosecha,n_c_desecho,n_c_pepena,n_c_diario;

    public ItemCosecha(String fecha, String id_Bloque,String Nombre_Blq, String c_codigo_eps, String BICO, int n_c_cosecha, int n_c_desecho, int n_c_pepena, int n_c_diario) {
        this.Fecha = fecha;
        this.Id_Bloque = id_Bloque;
        this.Nombre_Blq=Nombre_Blq;
        this.c_codigo_eps = c_codigo_eps;
        this.BICO = BICO;
        this.n_c_cosecha = n_c_cosecha;
        this.n_c_desecho = n_c_desecho;
        this.n_c_pepena = n_c_pepena;
        this.n_c_diario = n_c_diario;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getId_Bloque() {
        return Id_Bloque;
    }

    public void setId_Bloque(String id_Bloque) {
        Id_Bloque = id_Bloque;
    }

    public String getNombre_Blq() {
        return Nombre_Blq;
    }

    public void setNombre_Blq(String Nombre_Blq) {
        this.Nombre_Blq = Nombre_Blq;
    }

    public String getC_codigo_eps() {
        return c_codigo_eps;
    }

    public void setC_codigo_eps(String c_codigo_eps) {
        this.c_codigo_eps = c_codigo_eps;
    }

    public String getBICO() {
        return BICO;
    }

    public void setBICO(String BICO) {
        this.BICO = BICO;
    }

    public int getN_c_cosecha() {
        return n_c_cosecha;
    }

    public void setN_c_cosecha(int n_c_cosecha) {
        this.n_c_cosecha = n_c_cosecha;
    }

    public int getN_c_desecho() {
        return n_c_desecho;
    }

    public void setN_c_desecho(int n_c_desecho) {
        this.n_c_desecho = n_c_desecho;
    }

    public int getN_c_pepena() {
        return n_c_pepena;
    }

    public void setN_c_pepena(int n_c_pepena) {
        this.n_c_pepena = n_c_pepena;
    }

    public int getN_c_diario() {
        return n_c_diario;
    }

    public void setN_c_diario(int n_c_diario) {
        this.n_c_diario = n_c_diario;
    }
}
