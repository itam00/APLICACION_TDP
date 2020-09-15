package com.example.apporg.Eventos;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.apporg.R;

import java.util.Calendar;

public class Notification_receiver extends BroadcastReceiver {
    protected final Calendar calendar = Calendar.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intentAbrirNotif = new Intent(context, Eventos_del_Dia.class);
        String fecha = calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR);
        intentAbrirNotif.putExtra("fecha",fecha);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentAbrirNotif, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyLemubit")
                .setContentTitle("Eventos pendientes")
                .setContentText("Tiene eventos pendientes para el dia de hoy")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_calendario);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200,builder.build());

    }
}
