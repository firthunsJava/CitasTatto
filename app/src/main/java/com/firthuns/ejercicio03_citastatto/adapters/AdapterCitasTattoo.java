package com.firthuns.ejercicio03_citastatto.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firthuns.ejercicio03_citastatto.R;
import com.firthuns.ejercicio03_citastatto.modelos.CitasTatto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterCitasTattoo  extends ArrayAdapter<CitasTatto> {

    private Context context;
    private int resource;
    private ArrayList<CitasTatto> objects;

    public AdapterCitasTattoo(@NonNull Context context, int resource, @NonNull ArrayList<CitasTatto> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
        }
    /***  Metodo getView -->
     se va a encargar a partir de la lista de objetos que ha recibido del contexto     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//LayoutInflater-> a partir del archivo xml obtengo los datos
       View fila = LayoutInflater.from(context).inflate(resource, null);
       CitasTatto  citasTatto = objects.get(position);

        TextView txtNombre = fila.findViewById(R.id.txtNombrefila);
        TextView txtFechaCita = fila.findViewById(R.id.txtFechaFila);

        txtNombre.setText(citasTatto.getNombre());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtFechaCita.setText(simpleDateFormat.format(citasTatto.getFechaCita()));
/*dado que no hemos definido que aparezca ningun boton o otro elemento el codigo se quedará tal
* cual*/
        return fila;
    } // FIN
}


