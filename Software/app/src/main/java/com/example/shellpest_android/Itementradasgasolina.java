package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Itementradasgasolina {
    private String d_fechacrea_gas, c_folio_gas, d_fechaingreso_gas, c_codigo_eps, Id_Huerta, c_codigo_emp,
            v_tipo_gas,v_cantingreso_gas, v_observaciones_gas;

    public Itementradasgasolina(String d_fechacrea_gas, String c_folio_gas, String d_fechaingreso_gas, String c_codigo_eps, String Id_Huerta, String c_codigo_emp,
                                String v_tipo_gas, String v_cantingreso_gas, String v_observaciones_gas){
        this.d_fechacrea_gas = d_fechacrea_gas;
        this.c_folio_gas = c_folio_gas;
        this.d_fechaingreso_gas = d_fechaingreso_gas;
        this.c_codigo_eps = c_codigo_eps;
        this.Id_Huerta = Id_Huerta;
        this.c_codigo_emp = c_codigo_emp;
        this.v_tipo_gas =v_tipo_gas;
        this.v_cantingreso_gas = v_cantingreso_gas;
        this.v_observaciones_gas = v_observaciones_gas;
    }

    public String getD_fechacrea_gas() {
        return d_fechacrea_gas;
    }

    public void setD_fechacrea_gas(String d_fechacrea_gas) {
        this.d_fechacrea_gas = d_fechacrea_gas;
    }

    public String getC_folio_gas() {
        return c_folio_gas;
    }

    public void setC_folio_gas(String c_folio_gas) {
        this.c_folio_gas = c_folio_gas;
    }

    public String getD_fechaingreso_gas() {
        return d_fechaingreso_gas;
    }

    public void setD_fechaingreso_gas(String d_fechaingreso_gas) {
        this.d_fechaingreso_gas = d_fechaingreso_gas;
    }

    public String getC_codigo_eps() {
        return c_codigo_eps;
    }

    public void setC_codigo_eps(String c_codigo_eps) {
        this.c_codigo_eps = c_codigo_eps;
    }

    public String getId_Huerta() {
        return Id_Huerta;
    }

    public void setId_Huerta(String id_Huerta) {
        Id_Huerta = id_Huerta;
    }

    public String getC_codigo_emp() {
        return c_codigo_emp;
    }

    public void setC_codigo_emp(String c_codigo_emp) {
        this.c_codigo_emp = c_codigo_emp;
    }

    public String getV_tipo_gas() {
        return v_tipo_gas;
    }

    public void setV_tipo_gas(String v_tipo_gas) {
        this.v_tipo_gas = v_tipo_gas;
    }

    public String getV_cantingreso_gas() {
        return v_cantingreso_gas;
    }

    public void setV_cantingreso_gas(String v_cantingreso_gas) {
        this.v_cantingreso_gas = v_cantingreso_gas;
    }

    public String getV_observaciones_gas() {
        return v_observaciones_gas;
    }

    public void setV_observaciones_gas(String v_observaciones_gas) {
        this.v_observaciones_gas = v_observaciones_gas;
    }
}