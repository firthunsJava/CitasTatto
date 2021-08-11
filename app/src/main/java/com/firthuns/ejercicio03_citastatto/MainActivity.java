package com.firthuns.ejercicio03_citastatto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firthuns.ejercicio03_citastatto.Configuraciones.configuraciones;
import com.firthuns.ejercicio03_citastatto.adapters.AdapterCitasTattoo;
import com.firthuns.ejercicio03_citastatto.modelos.CitasTatto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

        private final int    ADD_CITA = 77;
        private final int   EDIT_CITA = 100;
    // Modelo Datos
        private ArrayList<CitasTatto> listasCitas;
    // Contenedor  - Adapter Asociado
        private ListView contenedorCitas;
        private AdapterCitasTattoo adapterCitasTattoo;// se encargara de rellenar nuestro ListView

    // Fila o elemento que se repite
    private int filaCita;

//    Almacenamiento Persistente
    private SharedPreferences spLista;
    // Gson hay que ir Project structure> dependencies > + all dependencies > com.google.code.gson, para añadir una librería
//    de google para convertir mis datos en un archivo gson
    private Gson parser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spLista = getSharedPreferences(configuraciones.SP_DATOS, MODE_PRIVATE);
        parser = new Gson();

        // inicializamos los elementos
        listasCitas = new ArrayList<>();
        // ->>>>>>><
        cargaLista();

        contenedorCitas = findViewById(R.id.contenedorCitas);
        filaCita = R.layout.fila_cita_tattoo;
        adapterCitasTattoo = new AdapterCitasTattoo(this, filaCita, listasCitas);

        // vamos a asociar a mi contenedorCitas, se le asigna mi adapterCitasTattoo
        contenedorCitas.setAdapter(adapterCitasTattoo);

/** setOnItemClickListener ->  este item listener, es para una linea en concretro
 * y no todos los elementos, se utiliza para cuando modifiquemos una linea, eliminar,...
 */
        contenedorCitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle= new Bundle();
                bundle.putInt("POS", position);

                CitasTatto citasTatto= listasCitas.get(position);
                bundle.putParcelable("CITA", citasTatto);
                Intent intent= new Intent(MainActivity.this , EditCitaActivity.class);

                intent.putExtras(bundle);
                startActivityForResult(intent, EDIT_CITA);
            }
        });
        //***********************************


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        startActivityForResult(
                                             new Intent(MainActivity.this, NewCitaActivity.class)
                                            , ADD_CITA);
            }
        });
    }

    /**
     *  EXPLICACION
     */
    private void cargaLista() {
        String tempString = spLista.getString(configuraciones.D_LISTA, null);
        if (tempString != null) {
            ArrayList<CitasTatto> temp = parser.fromJson(tempString, new TypeToken<ArrayList<CitasTatto>>() {
            }.getType());
            listasCitas.addAll(temp);
        }
    }
    /**
     *
     *
     */
    private void guardaLista(){
        String tempString = parser.toJson(listasCitas);
        SharedPreferences.Editor editor = spLista.edit();
        editor.putString(configuraciones.D_LISTA, tempString);
        editor.apply();
    }

    /**
     *
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        contenedorCitas.setAdapter(adapterCitasTattoo);
    }

    /**
     *
     *
     */



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// && -> lo que hace es que me comprueba de izquierda a derecha las condiciones,
//     y en el momento que falle una condicion salta la funcion
//     en cambio si solo hay &, me hace todas las comprobacioes como condicionantes haya
        if( requestCode == ADD_CITA && resultCode== RESULT_OK){
            if (data != null && data.getExtras() != null){
                CitasTatto citasTatto = data.getExtras().getParcelable("CITA");
                listasCitas.add(citasTatto);
                adapterCitasTattoo.notifyDataSetInvalidated();
// Explicar por que .....
                guardaLista();
            }
        }

        if ( requestCode == EDIT_CITA && resultCode == RESULT_OK){
            if(data != null && data.getExtras() != null){
                int posicion =  data.getExtras().getInt("POS");
                CitasTatto citasTatto = data.getExtras().getParcelable("CITA");
                if ( citasTatto == null){
                    listasCitas.remove(posicion);
                }else
                {
                    listasCitas.set(posicion,citasTatto);
                }
                //al adapter le avisaremos de que existen datos
                adapterCitasTattoo.notifyDataSetChanged();

                guardaLista();
            }
        }

    } // Fin onactiviryResult

    /** a continuacion veremos dos metodoss para guardar la informacion que desee
     *  que ambos van enlazados.
     * 1 . se encarga de guardar la informacion que no queremos que se destruya
     *2. se envarga de recuperar la informacion  que se haya guardado, cuando
     * se haya destruido la actividad, y ADEMAS SE HAYA GUARDADO ALGO
     */
    /**
     * Se eejcuta automaticamente antes de que la actividad se destruya.
     * @param outState -> Sirve : es un Bundle donde guardo la información
     *                    es el que se envia a oonRestoreInstanceState
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("LISTA", listasCitas);

    }

    /**
     * Si se ha guardado algo, se ejecuta este metodo para resctar la informacion
     * @param savedInstanceState _> COntiene los datos guardados en el metodo
     *                              onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<CitasTatto> temp = savedInstanceState.getParcelableArrayList("LISTA");
        listasCitas.clear();
        listasCitas.addAll(temp);
        Log.d("ELEMENTOS", ""+listasCitas.size());
    }
}