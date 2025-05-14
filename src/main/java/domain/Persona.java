package main.java.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Persona {
    protected  String nombre;
    protected  String apellido;
    protected String fechaNacimiento;
    protected double sueldo;
    protected double motivacion;

//    Constructores
    public Persona(String nombre, String apellido, String fechaNacimiento, double sueldo, double motivacion) {
        this.nombre = Objects.requireNonNull(nombre);
        this.apellido = Objects.requireNonNull(apellido);
        this.fechaNacimiento = Objects.requireNonNull(fechaNacimiento);
        setSueldo(sueldo);
        setMotivacion(motivacion);
    }

    public Persona() {

    }

//    Metodos
    public void entrenamiento() {

    }

    public final void aumentarMotivacionBase() {
        setMotivacion(Math.min(10, getMotivacion() + 0.2));
    }

    public void aumentarSueldoBase() {setSueldo(getSueldo() + 0.2);}

    // Getters
    public final String getNombre() { return nombre; }
    public final String getApellido() { return apellido; }
    public final String getFechaNacimiento() { return fechaNacimiento; }
    public final double getSueldo() { return sueldo; }
    public final double getMotivacion() { return motivacion; }

    // Setters
    public final void setSueldo(double sueldo) {
        if (sueldo < 0) throw new IllegalArgumentException("El sueldo no puede ser negativo");
        this.sueldo = sueldo;
    }

    public final void setMotivacion(double motivacion) {

        if (motivacion < 1 || motivacion > 10)
            throw new IllegalArgumentException("La motivación debe estar entre 1 y 10");
        this.motivacion = motivacion;
    }

    @Override
    public String toString() {
        return String.format("%s %s (Nacimiento: %s, Sueldo: %.2f, Motivación: %.1f)",
                nombre, apellido, fechaNacimiento, sueldo, motivacion);
    }
}