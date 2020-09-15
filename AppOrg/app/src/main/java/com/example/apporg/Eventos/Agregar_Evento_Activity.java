package com.example.apporg.Eventos;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apporg.Base_de_datos.BDSQLite;
import com.example.apporg.Base_de_datos.Utilidades;
import com.example.apporg.R;

import java.util.Calendar;

public class Agregar_Evento_Activity extends AppCompatActivity {

    protected EditText etNombreEvento,etDescripcionEvento;
    protected ImageButton ibFechaDesde,btAgregar,btCancelar;
    protected TextView etHoraDesde;
    protected String fecha;
    protected Bundle extras;
    protected Intent volverIntent;
    protected Switch schTodoElDia;
    protected TimePickerDialog pickerDialog;
    protected int hora=0,minutos=0;
    protected Calendar calendarioNotif = Calendar.getInstance();
    protected final Calendar calendarioActual = Calendar.getInstance();

    /**
     * Se muestra un AlertDialog para verificar si el usuario desea salir sin guardar el recordatorio
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
        myBuild.setMessage(("Salir sin guardar?"));
        myBuild.setTitle("Salir");
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(volverIntent);
            }
        });
        myBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = myBuild.create();
        dialog.show();
    }

    /**
     * Se Carga la fecha recibida en la barra de tareas y se agrega un listener al switch para
     * verificar si el usuario selecciono la opcion de notificacion todo el dia o indico una hora especifica
     * Se configuran los botones de fecha, guardar y cancelar
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_evento);

        volverIntent  = new Intent(getApplication(),Eventos_del_Dia.class);
        extras = getIntent().getExtras();
        fecha = extras.getString("fecha");
        volverIntent.putExtra("fecha",fecha);

        etNombreEvento = findViewById(R.id.editText_nombre_evento);
        etDescripcionEvento = findViewById(R.id.editText_descripcion_evento);
        etHoraDesde = findViewById(R.id.editText_hora_desde);
        schTodoElDia = findViewById(R.id.switch_todo_el_dia);

        schTodoElDia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    etHoraDesde.setTextColor(getResources().getColor(R.color.grisClaro));
                    ibFechaDesde.setEnabled(false);
                    ibFechaDesde.setClickable(false);

                }
                else{
                    etHoraDesde.setTextColor(getResources().getColor(R.color.grisOscuro));
                    ibFechaDesde.setClickable(true);
                    ibFechaDesde.setEnabled(true);
                }
            }
        });


        configurarBotonesFecha();
        configurarBotonCancelar();
        configurarBotonGuardar();

    }


    /**
     * Se configura el boton cancelar el cual vuelve al activity anterior
     */
    public void configurarBotonCancelar(){
        btCancelar = findViewById(R.id.button_cancelar_evento);
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(volverIntent);
            }
        });
    }

    /**
     * Se configura el boton guardar el cual guarda el evento en la case de datos en caso de que
     * los datos ingresados sean correctos
     */
    public void configurarBotonGuardar(){
        btAgregar = findViewById(R.id.button_guardar_evento);
        btAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String evento =etNombreEvento.getText().toString();
                if(!evento.equals("")) {
                    if(!eventoRepetido(evento)){
                        guardarEvento();
                        startActivity(volverIntent);
                    }
                    else{
                        etNombreEvento.setError("Evento repetido");
                    }
                }
                else{
                    etNombreEvento.setError("Ingrese un nombre");
                }
            }
        });

    }

    /**
     * Lee el contenido de los elementos graficos guarda su contenido en la base de datos.
     * Debe recibir un evento valido para guardar.
     */
    public void guardarEvento(){
        BDSQLite conn = new BDSQLite(getApplication(),"bd_eventos",null,1);
        SQLiteDatabase db = conn.getWritableDatabase();

        String nombre,descripcion,horaDesde,horaHasta;
        nombre=etNombreEvento.getText().toString();
        descripcion = etDescripcionEvento.getText().toString();
        if(schTodoElDia.isChecked()){
            horaDesde = "00:00 a.m";
        }
        else{
            horaDesde = etHoraDesde.getText().toString();
        }
        int notifId = getNotifID();
        crearNotificacion(notifId);

        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE,nombre);
        values.put(Utilidades.CAMPO_DESCRIPCION,descripcion);
        values.put(Utilidades.CAMPO_HORADESDE,horaDesde);
        values.put(Utilidades.CAMPO_FECHA,fecha);
        values.put(Utilidades.CAMPO_NOTIF_ID,notifId+"");

        db.insert(Utilidades.TABLA_EVENTOS, Utilidades.CAMPO_NOMBRE,values);

        db.close();
    }


    /**
     * Se crea el canal para las notificaciones, se crea la notificacion correespondiente al evento
     * y se guarda el codigo de la notificacion en la base de datos para poder borrala o editarla
     * en caso de que el usuario lo solicite.
     */
    public void crearNotificacion(int requestCode){
       // Toast.makeText(getApplication(), calendar.get(Calendar.YEAR)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DATE)+" a las "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplication(), ((calendarioNotif.getTimeInMillis()-System.currentTimeMillis())/1000)+"", Toast.LENGTH_LONG).show();

        CharSequence name = "LemubitReminderChannel";
        String description = "Channel for Lemubit Reminder";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("notifyLemubit", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Toast.makeText(getApplication(), requestCode+"", Toast.LENGTH_LONG).show();
        Intent intent =new Intent(this, Notification_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,requestCode,intent,0);

        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        if(schTodoElDia.isChecked()){
            String[] f = fecha.split("/");
            calendarioNotif.set(Integer.valueOf(f[2]),Integer.valueOf(f[1]),Integer.valueOf(f[0]),0,0);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendarioNotif.getTimeInMillis(),pendingIntent);


    }

    public int getNotifID(){
        BDSQLite conn = new BDSQLite(this,"bd_eventos",null,1);
        SQLiteDatabase db = conn.getReadableDatabase();
        int codigo =0;
        try{
            Cursor cursor = db.query(Utilidades.TABLA_EVENTOS, null, null, null, null, null, null);
            if(cursor !=null && cursor.moveToLast()) {
                 String aux= cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOTIF_ID));
                 codigo = Integer.valueOf(aux)+1;
            }

        }catch(Exception e){}

        db.close();
        return codigo;
    }

    /**
     * Se configuran los botones para seleccionar una fecha
     */
    public void configurarBotonesFecha(){
        ibFechaDesde = findViewById(R.id.imageButton_hora_desde);
        ibFechaDesde.setEnabled(false);
        ibFechaDesde.setClickable(false);
        ibFechaDesde.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                configurarHoraCalendario();
            }
        });
    }

    /**
     * Se configura el TimeDialogPicker para que cargue la hora en los campos de texto cuando
     * el usuario seleccione una hora especifica.
     */

    public void configurarHoraCalendario(){
        pickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hora = hourOfDay;
                minutos = minute;
                String horaFormateada =  (hourOfDay < 10)? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10)? String.valueOf("0" + minute):String.valueOf(minute);
                String AM_PM;

                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }

                etHoraDesde.setText(horaFormateada + ":" + minutoFormateado + " " + AM_PM);
                String[] f = fecha.split("/");
                calendarioNotif.set(Integer.valueOf(f[2]),Integer.valueOf(f[1]),Integer.valueOf(f[0]),hourOfDay,minute);
            }
        }, calendarioActual.get(Calendar.HOUR_OF_DAY), calendarioActual.get(Calendar.MINUTE), false);
        pickerDialog.show();

    }

    public boolean eventoRepetido(String evento){
        boolean existe=false;
        BDSQLite conn = new BDSQLite(this,"bd_eventos",null,1);
        SQLiteDatabase db = conn.getReadableDatabase();
        try{
            Cursor cursor = db.query(Utilidades.TABLA_EVENTOS, null, null, null, null, null, null);
            if(cursor !=null && cursor.moveToFirst()) {
                do {
                    existe = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOMBRE)).equals(evento);
                } while (cursor.moveToNext() && !existe);
            }

        }catch(Exception e){}

        db.close();

        return existe;
    }



}
