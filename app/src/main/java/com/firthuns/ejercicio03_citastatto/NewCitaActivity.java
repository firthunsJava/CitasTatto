package com.firthuns.ejercicio03_citastatto;

import androidx.appcompat.app.AppCompatActivity;

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

import com.firthuns.ejercicio03_citastatto.Configuraciones.configuraciones;
import com.firthuns.ejercicio03_citastatto.modelos.CitasTatto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class NewCitaActivity extends AppCompatActivity {


    private EditText txtNombre, txtApellidos, txtFechaNacimiento, txtFechaCita, txtAdelanto;
    private Switch swColor, swAutorizado;
    private Button btnEliminar, btnGuardar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cita);

        inicializarInterfaz();

        final LocalDate hoy = LocalDate.now();
        /**
         *  FUncion que me va a detectar la mayoria/minoria de edad del cliente,
         *  ya que en caso de que sea menor me tiene que aparecer el switch
         *  que me indicque que tiene que tener la autorizacion
         */
        txtFechaNacimiento.addTextChangedListener(new TextWatcher() {
            @Override // evento que detecta antes de escribir
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override // evento que detecta mientras estas esscribiendo
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override // evento q salta cuando se ha terminado de escribir
            public void afterTextChanged(Editable s) {
                /*  Esto me comprabará si la persona registrada es o no es mayor de edad  */
                try {

                    Date fecha = configuraciones.simpleDateFormat.parse(s.toString());
                       // PARA COGER UN OBJETO DATE, Y TRANSDORMARLO EN LOCALDATE,
                    //1 En Project Structur, module> Default config, hay que poner min sdk version>26
                    //2 , EN QUE ZONA ESTA, Y QUE ME LO SAQUE LA INFOR DEL MOVIL
                    /*  Atzone.> me saca la zona horaria de la configuraicon interna del movil en
                    * la que se esté usando          */
                    LocalDate fechaNacimiento = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                            if ( hoy.getYear() - fechaNacimiento.getYear() < 18){
                                        swAutorizado.setVisibility(View.VISIBLE);
                                        swAutorizado.setChecked(false);
                                        btnGuardar.setEnabled(false);
                            }else{
                                        swAutorizado.setVisibility(View.GONE);
                                        swAutorizado.setChecked(false);
                                        btnGuardar.setEnabled(true);
                            }
                } catch (ParseException e) {
                    Toast.makeText(NewCitaActivity.this, "Algo ha explotado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        swAutorizado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if ( !isChecked)       btnGuardar.setEnabled(false);
                btnGuardar.setEnabled(isChecked); // hACE LO MISMO QUE LA CONDICION DELA LINEA 76
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
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");

                    try {
                        citasTatto.setFechaCita(configuraciones.simpleDateFormat.parse(txtFechaCita.getText().toString()));
                        citasTatto.setFechaNacimiento(configuraciones.simpleDateFormat.parse(txtFechaNacimiento.getText().toString()));
                        if (txtAdelanto.getText().toString().isEmpty())  citasTatto.setAdelanto(0);
                            else   citasTatto.setAdelanto(Float.parseFloat(txtAdelanto.getText().toString()));


                            citasTatto.setColor(swColor.isChecked());
                            citasTatto.swAutorizado(swAutorizado.isChecked());


                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("CITA", citasTatto);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();

                    } catch (ParseException e) {
                        Toast.makeText(NewCitaActivity.this, "Fecha Incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(NewCitaActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }

            }
        });

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

        btnEliminar.setVisibility(View.GONE);
        swAutorizado.setVisibility(View.GONE);



    }
}