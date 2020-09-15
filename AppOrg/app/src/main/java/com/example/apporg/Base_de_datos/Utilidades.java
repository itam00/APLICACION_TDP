package com.example.apporg.Base_de_datos;

/**
 * Clase usada para almacenar las palabras clave de las bases de datos y evitar
 * inconsistencias o errores
 */
public class Utilidades {
    public static final String TABLA_EVENTOS= "eventos";
    public static final String CAMPO_NOMBRE= "nombre";
    public static final String CAMPO_DESCRIPCION= "descripcion";
    public static final String CAMPO_FECHA= "fecha";
    public static final String CAMPO_HORADESDE= "horaDesde";
    public static final String CAMPO_NOTIF_ID= "notifID";
    public static final String CAMPO_TODOELDIA="todoElDia";



    public static final String CREAR_TABLA_USUARIO = "CREATE TABLE "+TABLA_EVENTOS+
            "("+CAMPO_NOMBRE+" TEXT, "+
            CAMPO_DESCRIPCION+" TEXT, "+
            CAMPO_FECHA+" TEXT, "+
            CAMPO_HORADESDE+" TEXT, "+
            CAMPO_NOTIF_ID+" TEXT, "+
            CAMPO_TODOELDIA+" TEXT)";

    public static final String TABLA_RECORDATORIOS= "recordatorios";
    public static final String CAMPO_RECORDATORIO= "tarea";
    public static final String CAMPO_CUMPLIDO = "cumplido";

    public static final String CREAR_TABLA_RECORDATORIOS = "CREATE TABLE "+TABLA_RECORDATORIOS+
            "("+CAMPO_RECORDATORIO+" TEXT, "+
            CAMPO_CUMPLIDO+" TEXT)";

}
