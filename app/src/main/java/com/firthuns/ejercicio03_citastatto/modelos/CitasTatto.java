package com.firthuns.ejercicio03_citastatto.modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class CitasTatto implements Parcelable {

    /**
     * Como los atributos iran cambiando entre actividades, hacemos este modelo
     * Parcelable
     */
    private String nombre;
    private String apellidos;
    private Date fechaNacimiento; // tipo compuesto no se puiede inicializar
    private Date fechaCita;
    private float adelanto;
    private boolean color;
    private  boolean autorizado = false; // dados valor simple es posible


    public CitasTatto(String nombre, String apellidos, Date fechaNacimiento, Date fechaCita, float adelanto, boolean color) {
        this.nombre = nombre;
       this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaCita = fechaCita;
        this.adelanto = adelanto;
        this.color = color;
    }

    public CitasTatto() {
    }
    // OJO AL ATRIBUTO DE TIPO COMPUESTO DATE
    protected CitasTatto(Parcel in) {
        nombre = in.readString();
        apellidos = in.readString();
        adelanto = in.readFloat();
        color = in.readByte() != 0;
        autorizado = in.readByte() != 0;
         fechaCita = new Date (in.readLong());
        fechaNacimiento = new Date (in.readLong());
    }
    // OJO AL ATRIBUTO DE TIPO COMPUESTO DATE
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(apellidos);
        dest.writeFloat(adelanto);
        dest.writeByte((byte) (color ? 1 : 0));
        dest.writeByte((byte) (autorizado ? 1 : 0));

        dest.writeLong(fechaCita.getTime());
        dest.writeLong(fechaNacimiento.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CitasTatto> CREATOR = new Creator<CitasTatto>() {
        @Override
        public CitasTatto createFromParcel(Parcel in) {
            return new CitasTatto(in);
        }

        @Override
        public CitasTatto[] newArray(int size) {
            return new CitasTatto[size];
        }
    };

    // GETTERS Y SETTERS
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public float getAdelanto() {
        return adelanto;
    }

    public void setAdelanto(float adelanto) {
        this.adelanto = adelanto;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean isAutorizado() {
        return autorizado;
    }

    public void swAutorizado(boolean checked) {
    }
}
