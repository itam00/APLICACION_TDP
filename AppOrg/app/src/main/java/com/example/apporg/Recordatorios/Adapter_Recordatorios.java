package com.example.apporg.Recordatorios;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apporg.Base_de_datos.BDSQLite;
import com.example.apporg.Base_de_datos.Utilidades;
import com.example.apporg.Eventos.Editar_Evento_Activity;
import com.example.apporg.Eventos.Evento;
import com.example.apporg.R;

import java.util.ArrayList;

public class Adapter_Recordatorios extends RecyclerView.Adapter<Adapter_Recordatorios.ViewHolderDatos> {
    protected ArrayList<Recordatorio> listDatos;
    protected Context context;


    public Adapter_Recordatorios(ArrayList<Recordatorio> lista) {
        listDatos = lista;
    }


    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_list_recordatorios,null,false);
        context = view.getContext();
        return new ViewHolderDatos(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    /////////////////////////////////////////////

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        protected CheckBox cbRecordatorio;
        protected ImageButton ibEliminar;
        protected Adapter_Recordatorios adaptador;

        public ViewHolderDatos(@NonNull final View itemView, Adapter_Recordatorios adapter) {
            super(itemView);
            adaptador = adapter;
            cbRecordatorio = itemView.findViewById(R.id.checkBox_recordatorio);
            ibEliminar = itemView.findViewById(R.id.imageButton_eliminar_recordatorio);
            int widthPixels = itemView.getResources().getDisplayMetrics().widthPixels;
            //int heightPixels = itemView.getResources().getDisplayMetrics().heightPixels;

            ViewGroup.LayoutParams params = cbRecordatorio.getLayoutParams();
            params.width = (int) (widthPixels*0.80);
            cbRecordatorio.setLayoutParams(params);



        }
        public void asignarDatos(Recordatorio recordatorio) {
            cbRecordatorio.setText(recordatorio.getNombre());
            cbRecordatorio.setChecked(recordatorio.isCumplido());
            if(recordatorio.isCumplido()){
                ibEliminar.setVisibility(View.VISIBLE);
            }
            else{
                ibEliminar.setVisibility(View.INVISIBLE);
            }

            cbRecordatorio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        ibEliminar.setVisibility(View.VISIBLE);
                    }
                    else{
                        ibEliminar.setVisibility(View.INVISIBLE);
                    }
                    actualizarRecordatorio();
                }
            });

            ibEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BDSQLite conn = new BDSQLite(context, "bd_recordatorios", null, 1);
                    SQLiteDatabase db = conn.getWritableDatabase();
                    String[] parametros = {cbRecordatorio.getText().toString()};
                    db.delete(Utilidades.TABLA_RECORDATORIOS, Utilidades.CAMPO_RECORDATORIO + "=?", parametros);
                    Toast.makeText(context, "El evento fue eliminado correctamente", Toast.LENGTH_SHORT).show();
                    db.close();
                    int pos = 0;
                    while (listDatos.get(pos).getNombre() != cbRecordatorio.getText().toString())
                        pos++;
                    listDatos.remove(pos);
                    adaptador.notifyItemRemoved(pos);
                }
            });
        }

        public void actualizarRecordatorio(){
            BDSQLite conn = new BDSQLite(context, "bd_recordatorios", null, 1);
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros = {cbRecordatorio.getText().toString()};

            ContentValues values = new ContentValues();
            if(cbRecordatorio.isChecked()) {
                values.put(Utilidades.CAMPO_CUMPLIDO, "1");
            }
            else {
                values.put(Utilidades.CAMPO_CUMPLIDO, "0");
            }
            db.update(Utilidades.TABLA_RECORDATORIOS, values, Utilidades.CAMPO_RECORDATORIO + "=?", parametros);
            db.close();

        }


    }
}
