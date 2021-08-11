package com.firthuns.ejercicio03_citastatto;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firthuns.ejercicio03_citastatto.Configuraciones.configuraciones;
import com.firthuns.ejercicio03_citastatto.modelos.CitasTatto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EditCitaActivity extends AppCompatActivity {

    /*
    ATENTO ESTA JAVA CLASS SE HA CREADO MANUALMENTE  EN LA CARPETA MANIFESTS NO SE HA CREADO
    <!--      <activity android:name=".EditCitaActivity"></activity> se ha creado manualmente esxplicacion con el video     -->
        <activity android:name=".EditCitaActivity"></activity>
     */
    private EditText txtNombre, txtApellidos, txtFechaNacimiento, txtFechaCita, txtAdelanto;
    private Switch swColor, swAutorizado;
    private Button btnEliminar, btnGuardar;

    private CitasTatto citasTatto;
    private  int posicion;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cita); // MUY IMPORTANTE METER ESTA LINEA

        // paso 2: enlazar codigo con el layout, siempre depues del  setContentView(R.layout.activity_gestion_cita); // MUY IMPORTANTE METER ESTA LINEA
    citasTatto = getIntent().getExtras().getParcelable("CITA");
    posicion = getIntent().getExtras().getInt("POS");

        inicializarInterfaz();
        rellenaInformacion();
        /* ME TRAIGO LA NOTA Y COMPRUEBO QUE NO VENGA CON NULL POR EL INTENT    */
        final LocalDate hoy = LocalDate.now();


        swAutorizado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if ( !isChecked)       btnGuardar.setEnabled(false);
                btnGuardar.setEnabled(isChecked); // hACE LO MISMO QUE LA CONDICION DELA LINEA 76
            }
        });

        txtFechaNacimiento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            // mirar video por el localDate.now -> como se ha conseuigo quitar el fallo
            @Override
            public void afterTextChanged(Editable s) {


                try {
                    Date fecha = configuraciones.simpleDateFormat.parse(s.toString());

                    // PARA COGER UN OBJETO DATE, Y TRANSDORMARLO EN LOCALDATE,
                    //1
                    //2 , EN QUE ZONA ESTA, Y QUE ME LO SAQUE LA INFOR DEL MOVIL
                    LocalDate fechaNacimiento = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if ( hoy.getYear() - fechaNacimiento.getYear() < 18){
                             swAutorizado.setVisibility(View.VISIBLE);
                           btnGuardar.setEnabled(false);
                    }else{
                        swAutorizado.setVisibility(View.GONE);
                        swAutorizado.setChecked(false);
                        btnGuardar.setEnabled(true);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( !txtNombre.getText().toString().isEmpty() &&
                        !txtFechaCita.getText().toString().isEmpty() &&
                        !txtFechaNacimiento.getText().toString().isEmpty()   ){

                    CitasTatto citasTatto = new CitasTatto();
                    citasTatto.setNombre(txtNombre.getText().toString());
                    citasTatto.setApellidos(txtApellidos.getText().toString());


                    try {
                        citasTatto.setFechaCita(configuraciones.simpleDateFormat.parse(txtFechaCita.getText().toString()));
                        citasTatto.setFechaNacimiento(configuraciones.simpleDateFormat.parse(txtFechaNacimiento.getText().toString()));
                        if (txtAdelanto.getText().toString().isEmpty())
                                    citasTatto.setAdelanto(0);
                        else
                            citasTatto.setAdelanto(Float.parseFloat(txtAdelanto.getText().toString()));

                        citasTatto.setColor(swColor.isChecked());
                        citasTatto.swAutorizado(swAutorizado.isChecked());

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("CITA", citasTatto);
                        bundle.putInt("POS", posicion);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();

                    } catch (ParseException e) {
                        Toast.makeText(EditCitaActivity.this, "Fecha Incorrectas", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putInt("POS", posicion);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    } //FIN onCreate

    private void rellenaInformacion() {

        txtNombre.setText(citasTatto.getNombre());
        txtApellidos.setText(citasTatto.getApellidos());
        txtFechaCita.setText(configuraciones.simpleDateFormat.format(citasTatto.getFechaCita()));
        txtFechaNacimiento.setText(configuraciones.simpleDateFormat.format(citasTatto.getFechaNacimiento()));
        txtAdelanto.setText(String.valueOf(citasTatto.getAdelanto()));/*Hay que tener cuidado con esto estamos ante un double*/

            if (citasTatto.isAutorizado()){
                swAutorizado.setVisibility(View.VISIBLE);
                swAutorizado.setChecked(citasTatto.isAutorizado());
            }
            swColor.setChecked(citasTatto.isColor());

    }


    private void inicializarInterfaz() {
        txtNombre = findViewById(R.id.txtNombreCita);
        txtApellidos = findViewById(R.id.txtApellidosCita);
        txtFechaNacimiento = findViewById(R.id.txtFechaNacimientoCita);
        txtFechaCita = findViewById(R.id.txtFechaCita);
        txtAdelanto = findViewById(R.id.txtAdelantoCita);
        swAutorizado = findViewById(R.id.swAutorizadoCita);
        swColor = findViewById(R.id.swColorCita);
        btnEliminar = findViewById(R.id.btnEliminarCita);
        btnGuardar = findViewById(R.id.btnGuardarCita);

//        btnEliminar.setVisibility(View.GONE);
//        swAutorizado.setVisibility(View.GONE);

        if(citasTatto.isAutorizado()){
            swAutorizado.setVisibility(View.VISIBLE);
            swAutorizado.setSelected(citasTatto.isAutorizado());
        }else
        {
            swAutorizado.setVisibility(View.GONE);
            swAutorizado.setSelected(false);
        }


    }
}
