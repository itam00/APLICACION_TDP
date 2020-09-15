package com.example.apporg.Recordatorios;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.apporg.R;


public class Agregar_recordatorio_dialog extends AppCompatDialogFragment {

    protected EditText etRecordatorio;
    protected AgregarEventoDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savesInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.agregar_recordatorio,null);

        builder.setView(view)
                .setTitle("Agregar Recordatorio").setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //aca no va nada xq solo se tendria que cerrar cuando se cancele
                    }
                })
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //aca no va nada xq despues de hace un override
                    }
                });
        etRecordatorio = view.findViewById(R.id.editText_agregar_recordatorio);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



    }
    public void setListener(AgregarEventoDialogListener listener){
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recordatorio = etRecordatorio.getText().toString();
                    if(!recordatorio.equals("")) {
                        if (!listener.recordatorioRepetido(recordatorio)) {
                            listener.guardarRecordatorio(recordatorio);
                            dismiss();
                        } else {
                            etRecordatorio.setError("Recordatorio repetido");
                        }
                    }
                    else{
                        etRecordatorio.setError("Ingrese un recordatorio");
                    }
                }
            });
        }
    }

    public interface  AgregarEventoDialogListener{
        void guardarRecordatorio(String recordatorio);
        boolean recordatorioRepetido(String recordatorio);
    }
}
