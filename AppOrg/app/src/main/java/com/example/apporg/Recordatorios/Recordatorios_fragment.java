package com.example.apporg.Recordatorios;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apporg.Base_de_datos.BDSQLite;
import com.example.apporg.Base_de_datos.Utilidades;
import com.example.apporg.Eventos.Adapter_Eventos;
import com.example.apporg.Eventos.Evento;
import com.example.apporg.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Recordatorios_fragment extends Fragment implements Agregar_recordatorio_dialog.AgregarEventoDialogListener {

    protected View view;
    protected RecyclerView recycler;
    protected ArrayList<Recordatorio> recordatorios;
    protected BDSQLite conn;
    protected Adapter_Recordatorios adaptador;

    public Recordatorios_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recordatorios, container, false);

        recycler= view.findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //recycler.addItemDecoration(new Eventos_del_Dia.VerticalSpaceItemDecoration(10));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recordatorios = new ArrayList<>();

        FloatingActionButton fab = view.findViewById(R.id.fab_agregar_recordatorio);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAgregarRecordatorio();
        }});
        cargarEventos();
        return view;

    }

    public void openDialogAgregarRecordatorio(){
        Agregar_recordatorio_dialog dialog = new Agregar_recordatorio_dialog();
        dialog.setListener(this);
        dialog.show(getFragmentManager(),"dialog agregar evento");

    }
    @Override
    public boolean recordatorioRepetido(String recordatorio){
        boolean existe=false;
        conn = new BDSQLite(view.getContext(),"bd_recordatorios",null,1);
        SQLiteDatabase db = conn.getReadableDatabase();
        try{
            Cursor cursor = db.query(Utilidades.TABLA_RECORDATORIOS, null, null, null, null, null, null);
            if(cursor !=null && cursor.moveToFirst()) {
                do {
                    existe = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_RECORDATORIO)).equals(recordatorio);
                } while (cursor.moveToNext() && !existe);
            }
        }catch(Exception e){}

        db.close();

        return existe;
    }

    @Override
    public void guardarRecordatorio(String recordatorio) {

        BDSQLite conn = new BDSQLite(view.getContext(),"bd_recordatorios",null,1);
        SQLiteDatabase db = conn.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_RECORDATORIO,recordatorio);
        values.put(Utilidades.CAMPO_CUMPLIDO,"0"); //cuando no esta cumplido se guarda un 0 caso contrario un 1

        db.insert(Utilidades.TABLA_RECORDATORIOS,null,values);

        db.close();

        recordatorios.add(new Recordatorio(recordatorio,false));
        adaptador.notifyDataSetChanged();
    }

    public void cargarEventos(){
        conn = new BDSQLite(view.getContext(),"bd_recordatorios",null,1);
        SQLiteDatabase db = conn.getReadableDatabase();

        String[] columnas = {Utilidades.CAMPO_RECORDATORIO,Utilidades.CAMPO_CUMPLIDO};


        try{
            Cursor cursor = db.query(Utilidades.TABLA_RECORDATORIOS, null, null, null, null, null, null);
            if(cursor !=null && cursor.moveToFirst()) {

                do {
                    Recordatorio recordatorio= new Recordatorio();
                    recordatorio.setNombre(cursor.getString(0));
                    String c= cursor.getString(1);
                    recordatorio.setCumplido(c.equals("1"));

                    recordatorios.add(recordatorio);
                } while (cursor.moveToNext());
            }
            else{
            }
        }catch(Exception e){}

        db.close();

        adaptador = new Adapter_Recordatorios(recordatorios);
        recycler.setAdapter(adaptador);
    }


    /*public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }*/

}
