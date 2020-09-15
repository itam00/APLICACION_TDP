package com.example.apporg;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apporg.Base_de_datos.BDSQLite;
import com.example.apporg.Base_de_datos.Utilidades;
import com.example.apporg.Eventos.Evento;
import com.example.apporg.Eventos.Eventos_del_Dia;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_calendario_personalizado extends Fragment {

    Context context;

    public fragment_calendario_personalizado() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_fragment_calendario_personalizado, container, false);

        MaterialCalendarView calendar = view.findViewById(R.id.calendarView_personalizado);
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        calendar.state().edit().setMinimumDate(CalendarDay.from(anioActual-1,0,1)).setMaximumDate(CalendarDay.from(anioActual+1,11,31)).commit();
        calendar.setSelectionColor(R.color.colorAccent);
        calendar.setSelectedDate(Calendar.getInstance().getTime());

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent intent = new Intent(getContext(), Eventos_del_Dia.class);
                intent.putExtra("fecha",date.getDay()+"/"+date.getMonth()+"/"+date.getYear());
                startActivity(intent);

            }
        });
        Collection<CalendarDay> eventos = fechasConEventos();
        calendar.addDecorator(new EventDecorator(Color.BLACK,eventos));
        return view;
    }

    public Collection<CalendarDay> fechasConEventos(){
        ArrayList<CalendarDay> eventos = new ArrayList<>();
        BDSQLite conn = new BDSQLite(getActivity(),"bd_eventos",null,1);
        SQLiteDatabase db = conn.getReadableDatabase();


        try{
            Cursor cursor = db.query(Utilidades.TABLA_EVENTOS, null, null, null, null, null, null);
            if(cursor !=null && cursor.moveToFirst()) {
                do {
                    String fecha = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_FECHA));
                    String[] f = fecha.split("/");
                    eventos.add(CalendarDay.from(Integer.valueOf(f[2])+0,Integer.valueOf(f[1]),Integer.valueOf(f[0])));
                    Log.i("fecha",f[2]+""+f[1]+""+f[0]);
                } while (cursor.moveToNext());
            }
            else{
                //Toast.makeText(getApplication(), "paso algo raro"+parametros[0], Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){}

        return eventos;
    }


    public class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(10, color));
        }
    }

}
