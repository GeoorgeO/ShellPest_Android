package com.example.shellpest_android;

public class ItemDatoSpinner {
    private String Texto, Valor;
    public ItemDatoSpinner(String texto,String valor){
        Texto=texto;
        Valor=valor;
    }

    public String getTexto(){
        return Texto;
    }
    public String getValor(){return Valor;}
    public void setTexto(String texto){
        Texto=texto;
    }
}
