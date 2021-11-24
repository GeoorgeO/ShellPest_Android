package com.example.shellpest_android;

public class ItemRiego {
    private String Fecha,Hora,Nombre_Bloque,Id_Bloque,Precipitacion_Sistema,Caudal_Inicio,Caudal_Fin,Horas_Riego,cEPS,Temperatura,ET;

    public ItemRiego(String Fecha,String Hora,String Nombre_Bloque,String Id_Bloque,String Precipitacion_Sistema,String Caudal_Inicio,String Caudal_Fin,String Horas_Riego,String cEPS,String Temperatura,String ET){
        this.Fecha= Fecha;
        this.Hora=Hora;
        this.Id_Bloque=Id_Bloque;
        this.Precipitacion_Sistema=Precipitacion_Sistema;
        this.Caudal_Inicio=Caudal_Inicio;
        this.Caudal_Fin=Caudal_Fin;
        this.Horas_Riego=Horas_Riego;
        this.Nombre_Bloque=Nombre_Bloque;
        this.cEPS=cEPS;
        this.Temperatura=Temperatura;
        this.ET=ET;
    }

    public String getFecha(){
        return Fecha;
    }
    public void setFecha(String Fecha){
        this.Fecha=Fecha;
    }

    public String getHora(){
        return Hora;
    }
    public void setHora(String Hora){
        this.Hora=Hora;
    }

    public String getNombre_Bloque(){
        return Nombre_Bloque;
    }
    public void setNombre_Bloque(String Nombre_Bloque){
        this.Nombre_Bloque=Nombre_Bloque;
    }

    public String getId_Bloque(){
        return Id_Bloque;
    }
    public void setId_Bloque(String Id_Bloque){
        this.Id_Bloque=Id_Bloque;
    }

    public String getPrecipitacion_Sistema(){
        return Precipitacion_Sistema;
    }
    public void setPrecipitacion_Sistema(String Precipitacion_Sistema){
        this.Precipitacion_Sistema=Precipitacion_Sistema;
    }

    public String getCaudal_Inicio(){
        return Caudal_Inicio;
    }
    public void setCaudal_Inicio(String Caudal_Inicio){
        this.Caudal_Inicio=Caudal_Inicio;
    }

    public String getCaudal_Fin(){
        return Caudal_Fin;
    }
    public void setCaudal_Fin(String Caudal_Fin){
        this.Caudal_Fin=Caudal_Fin;
    }

    public String getHoras_Riego(){
        return Horas_Riego;
    }
    public void setHoras_Riego(String Horas_Riego){
        this.Horas_Riego=Horas_Riego;
    }

    public String getTemperatura(){
        if(Temperatura==null){
           return "0";
        }else{
            return Temperatura;
        }
    }
    public void setTemperatura(String Temperatura){
        this.Temperatura=Temperatura;
    }

    public String getET(){
        if(ET==null){
            return "0";
        }else{
            return ET;
        }
    }
    public void setET(String ET){
        this.ET=ET;
    }

    public String getCEPS(){
        return cEPS;
    }

}
