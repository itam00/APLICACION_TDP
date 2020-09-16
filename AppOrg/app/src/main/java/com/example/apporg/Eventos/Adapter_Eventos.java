package com.example.apporg.Eventos;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apporg.Base_de_datos.BDSQLite;
import com.example.apporg.Base_de_datos.Utilidades;
import com.example.apporg.R;

import java.util.ArrayList;



public class Adapter_Eventos extends RecyclerView.Adapter<Adapter_Eventos.ViewHolderDatos> {
    protected ArrayList<Evento> listDatos;
    protected Context context;


    public Adapter_Eventos(ArrayList<Evento> lista) {
        listDatos = lista;
    }


    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_list_evento_fragment,null,false);
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
        protected TextView tvNombreEvento,tvDescripcionEvento,tvHoraDesde;
        protected ImageButton ibEliminar,ibEditar;
        protected Adapter_Eventos adaptador;


        public ViewHolderDatos(@NonNull final View itemView, Adapter_Eventos adapter) {
            super(itemView);
            adaptador = adapter;
            tvNombreEvento = itemView.findViewById(R.id.textView_nombre_evento);
            tvDescripcionEvento = itemView.findViewById(R.id.textView_descripcion_evento);
            tvHoraDesde = itemView.findViewById(R.id.textView_hora_desde_evento);
            ibEliminar = itemView.findViewById(R.id.imageButton_eliminar_evento);
            ibEditar = itemView.findViewById(R.id.imageButton_editar_evento);
            int widthPixels = itemView.getResources().getDisplayMetrics().widthPixels;
            tvNombreEvento.setWidth((int) (widthPixels*0.80));


        }

        /**
         * Carga los datos de los eventos en los componentes graficos: el nombre, la descripcion y la hora.
         * Se setean los listener de los botones eliminar y editar.
         * @param evento Evento de donde se obtienen los datos
         */
        public void asignarDatos(Evento evento) {
            tvNombreEvento.setText(evento.getNombre());
            tvDescripcionEvento.setText(evento.getDescripcion());
            tvHoraDesde.setText(evento.getHoraDesde());

            ibEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BDSQLite conn = new BDSQLite(context,"bd_eventos",null,1);
                    SQLiteDatabase db = conn.getWritableDatabase();
                    String[] parametros={tvNombreEvento.getText().toString()};

                    db.delete(Utilidades.TABLA_EVENTOS,Utilidades.CAMPO_NOMBRE+"=?",parametros);
                    Toast.makeText(context,"El evento fue eliminado correctamente",Toast.LENGTH_SHORT).show();
                    db.close();

                    int pos=0,codigoNotif;
                    while(listDatos.get(pos).getNombre()!=tvNombreEvento.getText().toString())pos++;
                    codigoNotif = listDatos.get(pos).getCodigoNotif();
                    listDatos.remove(pos);
                    adaptador.notifyItemRemoved(pos);


                    Intent intent = new Intent(context, Notification_receiver.class);

                    PendingIntent sender = PendingIntent.getBroadcast(context, codigoNotif, intent, 0);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                    alarmManager.cancel(sender);


                }
            });
            ibEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,Editar_Evento_Activity.class);
                    intent.putExtra("nombreEvento",tvNombreEvento.getText().toString());
                    int pos=0;
                    while(listDatos.get(pos).getNombre()!=tvNombreEvento.getText().toString())pos++;
                    intent.putExtra("fecha",listDatos.get(pos).getFecha());

                    context.startActivity(intent);
                }
            });
        }


    }
}
