package com.example.shellpest_android;

public class Itemgasolina {
    private String Nombre_Responsable, Folio_vale, Fecha_Inicio, Fecha_final, Cantidad_ingreso, Cantidad_saldo,
        Empresa, Activo, Huerta, Km_Inicial, Km_Final, Horometro, Tipo_Combustible, Actividad, Observaciones;

    public Itemgasolina(String Nombre_Responsable, String Folio_vale, String Fecha_Inicio, String Fecha_final, String Cantidad_ingreso, String Cantidad_saldo,
                        String Empresa, String Activo, String Huerta, String Km_Inicial, String Km_Final, String Horometro, String Tipo_Combustible, String Actividad, String Observaciones){
        this.Nombre_Responsable = Nombre_Responsable;
        this.Folio_vale=Folio_vale;
        this.Fecha_Inicio = Fecha_Inicio;
        this.Fecha_final = Fecha_final;
        this.Cantidad_ingreso = Cantidad_ingreso;
        this.Cantidad_saldo = Cantidad_saldo;
        this.Empresa = Empresa;
        this.Activo = Activo;
        this.Huerta = Huerta;
        this.Km_Inicial = Km_Inicial;
        this.Km_Final = Km_Final;
        this.Horometro = Horometro;
        this.Tipo_Combustible = Tipo_Combustible;
        this.Actividad = Actividad;
        this.Observaciones = Observaciones;

    }

    public String getNombre_Responsable() {
        return Nombre_Responsable;
    }

    public void setNombre_Responsable(String nombre_Responsable) {
        Nombre_Responsable = nombre_Responsable;
    }

    public String getFolio_vale() {
        return Folio_vale;
    }

    public void setFolio_vale(String folio_vale) {
        Folio_vale = folio_vale;
    }

    public String getFecha_Inicio() {
        return Fecha_Inicio;
    }

    public void setFecha_Inicio(String fecha_Inicio) {
        Fecha_Inicio = fecha_Inicio;
    }

    public String getFecha_final() {
        return Fecha_final;
    }

    public void setFecha_final(String fecha_final) {
        Fecha_final = fecha_final;
    }

    public String getCantidad_ingreso() {
        return Cantidad_ingreso;
    }

    public void setCantidad_ingreso(String cantidad_ingreso) {
        Cantidad_ingreso = cantidad_ingreso;
    }

    public String getCantidad_saldo() {
        return Cantidad_saldo;
    }

    public void setCantidad_saldo(String cantidad_saldo) {
        Cantidad_saldo = cantidad_saldo;
    }

    public String getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }

    public String getActivo() {
        return Activo;
    }

    public void setActivo(String activo) {
        Activo = activo;
    }

    public String getHuerta() {
        return Huerta;
    }

    public void setHuerta(String huerta) {
        Huerta = huerta;
    }

    public String getKm_Inicial() {
        return Km_Inicial;
    }

    public void setKm_Inicial(String km_Inicial) {
        Km_Inicial = km_Inicial;
    }

    public String getKm_Final() {
        return Km_Final;
    }

    public void setKm_Final(String km_Final) {
        Km_Final = km_Final;
    }

    public String getHorometro() {
        return Horometro;
    }

    public void setHorometro(String horometro) {
        Horometro = horometro;
    }

    public String getTipo_Combustible() {
        return Tipo_Combustible;
    }

    public void setTipo_Combustible(String tipo_Combustible) {
        Tipo_Combustible = tipo_Combustible;
    }

    public String getActividad() {
        return Actividad;
    }

    public void setActividad(String actividad) {
        Actividad = actividad;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }
}
