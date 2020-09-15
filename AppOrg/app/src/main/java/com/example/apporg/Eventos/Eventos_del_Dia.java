package com.example.apporg.Eventos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;

import com.example.apporg.Base_de_datos.BDSQLite;
import com.example.apporg.Base_de_datos.Utilidades;
import com.example.apporg.MainActivity;
import com.example.apporg.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Eventos_del_Dia extends AppCompatActivity {
    protected ArrayList<Evento> listDatos;
    protected RecyclerView recycler;
    protected String fecha;
    protected Bundle extras;
    protected BDSQLite conn;


    /**
     * Agrega la fecha a la barra de tareas, carga los eventos a la parte grafica y configura el boton
     * para agregar un nuevo evento
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_del__dia);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        extras = getIntent().getExtras();

        fecha = extras.getString("fecha");

        Toolbar bar = findViewById(R.id.toolbar_eventos_del_dia);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(fecha);

        recycler= findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new VerticalSpaceItemDecoration(10));
        recycler.setItemAnimator(new DefaultItemAnimator());
        listDatos = new ArrayList<>();

        cargarEventos();


        FloatingActionButton fab = findViewById(R.id.fab_agregar_evento);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Agregar_Evento_Activity.class);
                intent.putExtra("fecha",fecha);
                startActivity(intent);
            }
        });


    }


    /**
     * accede a la  base de datos para enviar una lista de los eventos de la fecha actual al adapter
     */
    public void cargarEventos(){
        conn = new BDSQLite(getApplication(),"bd_eventos",null,1);
        SQLiteDatabase db = conn.getReadableDatabase();

        String[] parametros= {fecha};
        String[] campos= {Utilidades.CAMPO_NOMBRE,Utilidades.CAMPO_DESCRIPCION,Utilidades.CAMPO_HORADESDE,Utilidades.CAMPO_FECHA};

        try{
            Cursor cursor = db.query(Utilidades.TABLA_EVENTOS,campos,Utilidades.CAMPO_FECHA+"=?",parametros,null,null,null);
            if(cursor !=null && cursor.moveToFirst()) {
                do {
                    Evento evento = new Evento();
                    evento.setNombre(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOMBRE)));
                    evento.setDescripcion(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_DESCRIPCION)));
                    evento.setHoraDesde(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_HORADESDE)));
                    evento.setFecha(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_FECHA)));
                    listDatos.add(evento);
                } while (cursor.moveToNext());


            }
            else{
                Toast.makeText(getApplication(), "paso algo raro"+parametros[0], Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){}
        Adapter_Eventos adaptador = new Adapter_Eventos(listDatos);
        recycler.setAdapter(adaptador);


    }

    /**
     * Al intentear volver siempre es dirigido hacia la actividad principal
     */
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * Clase decoradora para agregar un espacio entre loo elemetos de la lista.
     */
    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
    private String getFechaFormateada(String f){
        String ff[] = f.split("/");
        int mes = Integer.valueOf(ff[1])+1;
        return "Eventos del dia "+ff[0]+"/"+mes+"/"+ff[2];
    }
}
