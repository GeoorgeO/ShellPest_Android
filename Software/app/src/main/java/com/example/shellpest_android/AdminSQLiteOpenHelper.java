package com.example.shellpest_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper{
    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase BD) {
        BD.execSQL("create table t_Receta_Huerta " +
                "(Id_Huerta text not null," +
                "Id_Receta text not null," +
                "c_codigo_eps text , " +
                "primary key(Id_Huerta,Id_Receta))");

        BD.execSQL("create table UsuarioLogin " +
                "(Id_Usuario text primary key," +
                "Contrasena text, " +
                "Id_Perfil text," +
                "Id_Huerta text," +
                "Nombre text)");

        BD.execSQL("create table FechaSincroniza" +
                "(id_Sincroniza text primary key," +
                "Fecha_Sincroniza text)");

        BD.execSQL("create table t_Calidad" +
                "(Id_Calidad text primary key," +
                "Nombre_Calidad text )");

        BD.execSQL("create table t_Cultivo" +
                "(Id_Cultivo text primary key," +
                "Nombre_Cultivo text)");

        BD.execSQL("create table t_Duenio" +
                "(Id_Duenio text primary key," +
                "Nombre_Duenio text)");

        BD.execSQL("create table t_Deteccion" +
                "(Id_Deteccion text primary key," +
                "Nombre_Deteccion text)");

        BD.execSQL("create table t_Enfermedad" +
                "(Id_Enfermedad text primary key," +
                "Nombre_Enfermedad text)");

        BD.execSQL("create table t_Humbral" +
                "(Id_Humbral text primary key," +
                "Valor_Humbral text," +
                "Nombre_Humbral text," +
                "Color_Humbral text)");

        BD.execSQL("create table t_Plagas" +
                "(Id_Plagas text primary key," +
                "Nombre_Plagas text)");

        BD.execSQL("create table t_Productor" +
                "(Id_Productor text not null," +
                "Nombre_Productor text," +
                "c_codigo_eps text not null," +
                "primary key(Id_Productor,c_codigo_eps))");

        BD.execSQL("create table t_Pais" +
                "(Id_Pais text primary key," +
                "Nombre_Pais text)");

        BD.execSQL("create table t_Estado" +
                "(Id_Estado text primary key," +
                "Nombre_Estado text," +
                "Id_Pais text)");

        BD.execSQL("create table t_Ciudades" +
                "(Id_Ciudad text primary key," +
                "Nombre_Ciudad text," +
                "Id_Estado text)");

        BD.execSQL("create table t_Huerta" +
                "(Id_Huerta text not null," +
                "Nombre_Huerta text," +
                "Registro_Huerta text," +
                "Id_Productor text," +
                "Id_Estado text," +
                "Id_Ciudad text," +
                "Id_Calidad text," +
                "Id_Cultivo text," +
                "Id_Tratamiento text," +
                "zona_Huerta text," +
                "banda_Huerta text," +
                "este_Huerta text," +
                "norte_Huerta text," +
                "asnm_Huerta text," +
                "latitud_Huerta text," +
                "longitud_Huerta text," +
                "Activa_Huerta text," +
                "Id_zona text," +
                "c_codigo_eps text not null," +
                "primary key(Id_Huerta,c_codigo_eps))");

        BD.execSQL("create table t_Bloque" +
                "(Id_Bloque text primary key," +
                "Id_Huerta text," +
                "Nombre_Bloque text," +
                "c_codigo_eps text," +
                "TipoBloque text)");

        BD.execSQL("create table t_Puntocontrol" +
                "(Id_PuntoControl text not null," +
                "Id_Bloque text," +
                "Nombre_PuntoControl text," +
                "n_coordenadaX text," +
                "n_coordenadaY text," +
                "c_codigo_eps text not null," +
                "primary key(Id_PuntoControl,c_codigo_eps))");

        BD.execSQL("create table t_Zona" +
                "(Id_zona text primary key," +
                "Nombre_zona text)");

        BD.execSQL("create table t_Monitoreo_PEEncabezado" +
                "(Fecha text not null," +
                "Id_Huerta text," +
                "Id_PuntoControl text not null," +
                "Id_Usuario text," +
                "n_coordenadaX text," +
                "n_coordenadaY text," +
                "Hora text," +
                "c_codigo_eps text not null," +
                "Primary key(Fecha,Id_PuntoControl,c_codigo_eps))");

        BD.execSQL("create table t_Monitoreo_PEDetalle" +
                "(Fecha text not null," +
                "Id_Plagas text," +
                "Id_Enfermedad text," +
                "Id_PuntoControl text not null," +
                "Id_Deteccion text ," +
                "Id_Individuo text ," +
                "Id_Humbral text," +
                "Hora text," +
                "c_codigo_eps text not null," +
                "Cant_Individuos float," +
                "Id_Fenologico text," +
                "primary key (Fecha,Id_Plagas,Id_Enfermedad,Id_PuntoControl,Id_Deteccion,Id_Individuo,c_codigo_eps))");

        BD.execSQL("create table t_Monitoreo_PE" +
                "(Fecha text," +
                "Id_Huerta text," +
                "Id_Plagas text," +
                "Id_Enfermedad text," +
                "Id_PuntoControl text," +
                "Id_Deteccion text," +
                "Id_Individuo text," +
                "Id_Usuario text," +
                "Id_Humbral text," +
                "n_coordenadaX text," +
                "n_coordenadaY text," +
                "Hora text)");

        BD.execSQL("create table t_Monitoreo_Eliminados_PEEncabezado" +
                "(Fecha text not null," +
                "Id_Huerta text," +
                "Id_PuntoControl text not null," +
                "Id_Usuario text," +
                "n_coordenadaX text," +
                "n_coordenadaY text," +
                "Hora text," +
                "c_codigo_eps text not null," +
                "primary key(Fecha,Id_PuntoControl,c_codigo_eps))");

        BD.execSQL("create table t_Monitoreo_Eliminados_PEDetalle" +
                "(Fecha text not null," +
                "Id_Plagas text," +
                "Id_Enfermedad text," +
                "Id_PuntoControl text not null," +
                "Id_Deteccion text not null," +
                "Id_Individuo text not null," +
                "Id_Humbral text," +
                "Hora text," +
                "c_codigo_eps not null," +
                "Cant_Individuos float," +
                "Id_Fenologico text," +
                "primary key(Fecha,Id_Plagas,Id_Enfermedad,Id_PuntoControl,Id_Deteccion,Id_Individuo,c_codigo_eps))");

        BD.execSQL("create table t_Monitoreo_Eliminados_PE" +
                "(Fecha text," +
                "Id_Huerta text," +
                "Id_enfermedad text," +
                "Id_Plagas text," +
                "Id_PuntoControl text," +
                "Id_Deteccion text," +
                "Id_Individuo text," +
                "Id_Usuario text," +
                "Id_Humbral text," +
                "n_coordenadaX text," +
                "n_coordenadaY text)");

        BD.execSQL("create table t_Individuo " +
                "(Id_Individuo text primary key," +
                "No_Individuo text," +
                "No_Inicial float," +
                "No_Final float)");

        BD.execSQL("create table t_Monitoreo " +
                "(Id_monitoreo text primary key," +
                "Id_zona text," +
                "Id_Plagas text," +
                "Id_Enfermedad text," +
                "Id_Deteccion text," +
                "Id_Individuo text," +
                "Id_Humbral text," +
                "Id_Fenologico text)");

        BD.execSQL("create table t_Riego " +
                "(Fecha text not null," +
                "Hora text," +
                "Id_Bloque text not null," +
                "Precipitacion_Sistema float," +
                "Caudal_Inicio float," +
                "Caudal_Fin float," +
                "Horas_Riego float," +
                "Id_Usuario text," +
                "c_codigo_eps text not null," +
                "Temperatura float," +
                "ET float)");

        BD.execSQL("create table t_RiegoEliminado " +
                "(Fecha text not null," +
                "Hora text," +
                "Id_Bloque text not null," +
                "Precipitacion_Sistema float," +
                "Caudal_Inicio float," +
                "Caudal_Fin float," +
                "Horas_Riego float," +
                "Id_Usuario text," +
                "c_codigo_eps text not null," +
                "Temperatura float," +
                "ET float)");

        BD.execSQL("create table t_Usuario_Huerta " +
                "(Id_Usuario text ," +
                "Id_Huerta text," +
                "c_codigo_eps text)");

        BD.execSQL("create table t_Productos" +
                "(c_codigo_pro text not null," +
                "v_nombre_pro text," +
                "c_codigo_uni text," +
                "Existencia float," +
                "Stock_Min float," +
                "Movimientos float," +
                "c_codigo_eps text not null," +
                "primary key (c_codigo_pro, c_codigo_eps))");

        BD.execSQL("create table t_Unidad" +
                "(c_codigo_uni text not null," +
                "v_nombre_uni text," +
                "v_abrevia_uni text," +
                "c_codigo_eps text not null," +
                "primary key(c_codigo_uni,c_codigo_eps))");

        BD.execSQL("create table t_Presentacion" +
                "(Id_Presentacion text, " +
                "Nombre_Presentacion text," +
                "Id_TipoAplicacion text," +
                "Id_Unidad text," +
                "c_codigo_eps text," +
                "primary key(Id_Presentacion,c_codigo_eps))");

        BD.execSQL("create table t_TipoAplicacion" +
                "(Id_TipoAplicacion text not null," +
                "Nombre_TipoAplicacion text," +
                "c_codigo_eps text not null," +
                "primary key (Id_TipoAplicacion,c_codigo_eps))");

        BD.execSQL("create table t_Aplicaciones " +
                "(Id_Aplicacion text not null," +
                "Id_Huerta text," +
                "Observaciones text," +
                "Id_TipoAplicacion text," +
                "Id_Presentacion text ," +
                "Id_Usuario text, " +
                "F_Creacion text," +
                "Enviado text," +
                "Id_Receta text," +
                "Unidades_aplicadas text, " +
                "Centro_Costos text ," +
                "c_codigo_eps text not null, " +
                "primary key(Id_Aplicacion,c_codigo_eps))");

        BD.execSQL("create table t_Aplicaciones_Det " +
                "(Id_Aplicacion text not null," +
                "Fecha text," +
                "c_codigo_pro text," +
                "Dosis text," +
                "Id_Usuario text, " +
                "F_Creacion text," +
                "Enviado text, " +
                "Centro_Costos text," +
                "c_codigo_eps text not null)");

        BD.execSQL("create table t_Usuario_Empresa " +
                "(Id_Usuario text ," +
                "c_codigo_eps text)");

        BD.execSQL("create table conempresa " +
                "(c_codigo_eps text primary key," +
                "v_nombre_eps text," +
                "v_rfc_eps text," +
                "v_abrevia_eps text)");

        BD.execSQL("create table t_Almacen " +
                "(Id_Almacen text not null," +
                "Nombre_Almacen text," +
                "Id_Huerta text not null," +
                "c_codigo_eps text NOT NULL," +
                "primary key(Id_Almacen,Id_Huerta,c_codigo_eps))");

        BD.execSQL("create table t_Salidas " +
                "(Id_Salida text not null," +
                "c_codigo_eps text not null," +
                "Id_Responsable text," +
                "Id_Almacen text, " +
                "Id_Aplicacion text," +
                "Fecha text," +
                "Id_Usuario text, " +
                "F_Creacion text," +
                "primary key(Id_Salida,c_codigo_eps))");

        BD.execSQL("create table t_Salidas_Det " +
                "(Id_Salida text," +
                "c_codigo_pro text," +
                "Cantidad text, " +
                "Id_Bloque text," +
                "Id_Usuario text, " +
                "F_Creacion text," +
                "c_codigo_eps text," +
                "n_exiant_mov float)");

        BD.execSQL("create table invmovimiento " +
                "(c_coddoc_mov text ," +
                "Secuencia text," +
                "c_tipodoc_mov text," +
                "c_codigo_pro text," +
                "n_movipro_mov float," +
                "n_exiant_mov float," +
                "n_cantidad_mov float," +
                "c_codigo_eps text)");

        BD.execSQL("create table t_existencias " +
                "(c_codigo_eps text not null ," +
                "c_codigo_pro text ," +
                "c_codigo_alm text not null," +
                "Existencia float ," +
                "primary key(c_codigo_eps,c_codigo_pro,c_codigo_alm))");

        BD.execSQL("create table t_Receta " +
                "(Id_Receta text not null ," +
                "Fecha_Receta text ," +
                "Id_AsesorTecnico text ," +
                "Id_MonitoreoPE float ," +
                "Id_Cultivo text, " +
                "Id_TipoAplicacion text," +
                "Id_Presentacion text," +
                "Observaciones text," +
                "Intervalo_Seguridad float," +
                "Intervalo_Reingreso float," +
                "Id_Huerta text," +
                "c_codigo_eps text not null," +
                "Para text," +
                "primary key(Id_Receta,c_codigo_eps))");

        BD.execSQL("create table t_RecetaDet " +
                "(Id_Receta text not null ," +
                "Secuencia text ," +
                "c_codigo_pro text ," +
                "v_nombre_pro float ," +
                "c_codigo_cac text, " +
                "c_codigo_uni text," +
                "Dosis float," +
                "Cantidad_Unitaria float," +
                "Descripcion text," +
                "c_codigo_eps text not null, " +
                "primary key(Id_Receta,c_codigo_eps,Secuencia))");

        BD.execSQL("create table t_Est_Fenologico " +
                "(Id_Fenologico text not null," +
                "Nombre_Fenologico text," +
                "PoE text, " +
                "primary key(Id_Fenologico))");

        BD.execSQL("create table cosactividad " +
                "(c_codigo_act text not null," +
                "v_nombre_act text," +
                "primary key(c_codigo_act))");

        BD.execSQL("create table t_Podas " +
                "(Fecha text not null, " +
                "Id_bloque text not null, " +
                "N_arboles float, " +
                "Observaciones text, " +
                "Id_Usuario text not null, " +
                "F_Creacion text not null, " +
                "c_codigo_eps text not null, " +
                "primary key(Fecha,Id_bloque,c_codigo_eps))");

        BD.execSQL("create table t_PodasDet " +
                "(Fecha text not null, " +
                "Id_bloque text not null, " +
                "c_codigo_eps text not null, " +
                "Actividad text not null, " +
                "primary key(Fecha,Id_bloque,c_codigo_eps,actividad))");

        BD.execSQL("create table t_Empleados_Huerta"+
                "( c_codigo_emp text not null, " +
                "Id_Huerta text not null, " +
                "Nombre_Completo text not null)");

        BD.execSQL("create table t_Cosecha"+
                "( Fecha text not null, " +
                "Id_Bloque text not null, " +
                "c_codigo_eps text not null," +
                "BICO text," +
                "Cajas_Cosecha float," +
                "Cajas_Desecho float," +
                "Cajas_Pepena float," +
                "Cajas_RDiaria float," +
                "Id_Usuario text not null, " +
                "F_Creacion text not null, " +
                "primary key (Fecha,Id_Bloque,c_codigo_eps))");

        BD.execSQL("create table t_Activos_Huerta"+
                "( Id_ActivosGas text not null, " +
                "v_descripcorta_act text not null, " +
                "v_serie_act text,"+
                "c_codigo_fam text not null,"+
                "c_codigo_are text not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS UsuarioLogin");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS FechaSincroniza");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Calidad");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Cultivo");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Duenio");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Deteccion");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Enfermedad");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Humbral");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Plagas");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Productor");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Pais");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Estado");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Ciudades");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Huerta");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Bloque");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Puntocontrol");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Zona");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Monitoreo_PE");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Monitoreo_Eliminados_PE");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Individuo");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Monitoreo");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Monitoreo_PEEncabezado");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Monitoreo_Eliminados_PEEncabezado");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Riego");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_RiegoEliminado");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Usuario_Huerta");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Productos");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Unidad");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Presentacion");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_TipoAplicacion");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Aplicaciones");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Aplicaciones_Det");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Usuario_Empresa");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS conempresa");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Salidas");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Salidas_Det");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Almacen");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS invmovimiento");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_existencias");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Receta");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_RecetaDet");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Receta_Huerta");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Est_Fenologico");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS cosactividad");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Podas");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_PodasDet");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Empleados_Huerta");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Cosecha");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Activos_Huerta");

        onCreate(sqLiteDatabase);
    }
}
