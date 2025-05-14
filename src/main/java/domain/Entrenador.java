package main.java.domain;

import java.time.LocalDate;

public class Entrenador extends Persona {
    private static int totalEntrenadores = 0;
    private int torneosGanados;
    private boolean seleccionadorNacional;

//    Constructor
public Entrenador(String nombre, String apellido, String fechaNacimiento, double sueldo, double motivacion, int torneosGanados, boolean seleccionadorNacional) {
    super(nombre, apellido, fechaNacimiento, sueldo, motivacion);
        setTorneosGanados(torneosGanados);
        this.seleccionadorNacional = seleccionadorNacional;
        totalEntrenadores++;
    }

//    Metodos
    @Override
    public void entrenamiento() {
        double aumento = seleccionadorNacional ? 0.3 : 0.15;
        setMotivacion(Math.min(10, getMotivacion() + aumento));
    }

    public void incrementarSou() {
        setSueldo(getSueldo() * 1.005);
    }

    // Getters
    public final int getTorneosGanados() { return torneosGanados; }
    public final boolean isSeleccionadorNacional() { return seleccionadorNacional; }
    public static int getTotalEntrenadores() { return totalEntrenadores; }

    // Setters
    public final void setTorneosGanados(int torneosGanados) {
        if (torneosGanados < 0) throw new IllegalArgumentException("Torneos no pueden ser negativos");
        this.torneosGanados = torneosGanados;
    }

    public final void setSeleccionadorNacional(boolean seleccionadorNacional) {
        this.seleccionadorNacional = seleccionadorNacional;
    }

    @Override
    public String toString() {
        return String.format("%s %s - Torneos: %d, %s seleccionador nacional. %s",
                getNombre(), getApellido(), torneosGanados,
                seleccionadorNacional ? "Es" : "No es", super.toString());
    }
}