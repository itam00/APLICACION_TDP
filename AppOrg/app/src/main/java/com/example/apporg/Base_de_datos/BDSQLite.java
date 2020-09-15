package com.example.apporg.Base_de_datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BDSQLite extends SQLiteOpenHelper {


    public BDSQLite(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_USUARIO); //se crea la base de datos, si ya existe la adapta ala nueva version
        db.execSQL(Utilidades.CREAR_TABLA_RECORDATORIOS); //se crea la base de datos, si ya existe la adapta ala nueva version

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_EVENTOS);// si instala la aplicacion y encuentra una version antigua entonces se elimina y se vuelve a generar
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_RECORDATORIOS);
        onCreate(db);

    }
}
