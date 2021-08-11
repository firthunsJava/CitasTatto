package com.firthuns.ejercicio03_citastatto.Configuraciones;

import java.text.SimpleDateFormat;

public class configuraciones {

    public static final String SP_DATOS = "sp_datos";
    public static final String D_LISTA = "lista";

    public static SimpleDateFormat simpleDateFormat;

    static {
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

}
