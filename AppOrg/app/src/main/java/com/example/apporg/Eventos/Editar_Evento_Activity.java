package com.example.apporg.Eventos;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.apporg.Base_de_datos.BDSQLite;
import com.example.apporg.Base_de_datos.Utilidades;
import com.example.apporg.R;

public class Editar_Evento_Activity extends Agregar_Evento_Activity {

    protected BDSQLite conn;
    protected String nombreEvento;
    protected int codigoNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        nombreEvento = extras.getString("nombreEvento");

        conn = new BDSQLite(getApplication(),"bd_eventos",null,1);
        SQLiteDatabase db = conn.getReadableDatabase();

        String[] parametros= {nombreEvento};
        String[] campos= {Utilidades.CAMPO_NOMBRE,Utilidades.CAMPO_DESCRIPCION,Utilidades.CAMPO_HORADESDE,Utilidades.CAMPO_NOTIF_ID,Utilidades.CAMPO_TODOELDIA};

        try{
            Cursor cursor = db.query(Utilidades.TABLA_EVENTOS,campos,Utilidades.CAMPO_NOMBRE+"=?",parametros,null,null,null);
            if(cursor !=null && cursor.moveToFirst()) {
                etNombreEvento.setText(nombreEvento);
                etDescripcionEvento.setText(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_DESCRIPCION)));
                etHoraDesde.setText(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_HORADESDE)));
                String aux= cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOTIF_ID));
                codigoNotif = Integer.valueOf(aux);
                if(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_TODOELDIA)).equals("0")){
                    schTodoElDia.setChecked(true);
                }
            }
            else{
                //Toast.makeText(getApplication(), parametros[0], Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){

        }

    }

    /**
     * Se configura el boton guardan, al ser presionado modifica los campos correspondientes en el evento
     */
    @Override
    public void configurarBotonGuardar(){
        btAgregar = findViewById(R.id.button_guardar_evento);
        btAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreActual = etNombreEvento.getText().toString();
                if (nombreActual == nombreEvento) {
                    actualizarEvento(nombreActual);
                }
                else{
                    eliminarEvento(nombreEvento);
                    guardarEvento();
                }

                Toast.makeText(getApplication(), "El evento fue actualizado correctamente "+ fecha, Toast.LENGTH_SHORT).show();

                startActivity(volverIntent);

            }
        });

    }

    /**
     *Se actualizan en la base de datos los campos del evento editado
     * @param nombreEvento nombre del evento a ser actualizado
     */

    public void actualizarEvento(String nombreEvento){
        String descripcion,horaDesde;
        descripcion = etDescripcionEvento.getText().toString();
        horaDesde = etHoraDesde.getText().toString();

        BDSQLite conn = new BDSQLite(getApplication(), "bd_eventos", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = {nombreEvento};

        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_DESCRIPCION, descripcion);
        values.put(Utilidades.CAMPO_HORADESDE, horaDesde);
        values.put(Utilidades.CAMPO_FECHA, fecha);
        db.update(Utilidades.TABLA_EVENTOS, values, Utilidades.CAMPO_NOMBRE + "=?", parametros);
        db.close();


        if(!horaDesde.equals(etHoraDesde.getText().toString())) {
            crearNotificacion(codigoNotif);
        }
    }

    /**
     * Se elimina un evento de la base de datos
     * @param nombre nombre del evento a ser eliminado
     */
    public void eliminarEvento(String nombre){
        BDSQLite conn = new BDSQLite(getApplication(),"bd_eventos",null,1);
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros={nombre};

        db.delete(Utilidades.TABLA_EVENTOS,Utilidades.CAMPO_NOMBRE+"=?",parametros);
        db.close();

        Intent intent = new Intent(this, Notification_receiver.class);

        PendingIntent sender = PendingIntent.getBroadcast(this, codigoNotif, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
