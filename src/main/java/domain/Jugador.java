package main.java.domain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Representa un jugador de un equipo.
 * Hereda de {@link Persona} y añade propiedades como dorsal, posición y calidad.
 * Proporciona métodos para el entrenamiento, cambio ocasional de posición y ordenación.
 */
public class Jugador extends Persona implements Comparable<Jugador> {
    public static final String[] POSICIONES = {"POR", "DEF", "MIG", "DAV"};
    private static int totalJugadores = 0;
    protected int dorsal;
    private String posicion;
    private double calidad;

    public Jugador(String nombre, String apellido, String fechaNacimiento, double sueldo, double motivacion, int dorsal, String posicion, double calidad) {
        super(nombre, apellido, fechaNacimiento, sueldo, motivacion);
        setDorsal(dorsal);
        setPosicion(posicion);
        setCalidad(calidad);
    }

    //  Métodos
    @Override
    public void entrenamiento() {
        aumentarMotivacionBase();
        double aumento = Math.random() < 0.1 ? 0.3 : (Math.random() < 0.3 ? 0.2 : 0.1);
        setCalidad(Math.min(100, getCalidad() + aumento));
    }

    public void canviDePosicio() {
        if (Math.random() < 0.05) {
            posicion = POSICIONES[new Random().nextInt(POSICIONES.length)];
            setCalidad(Math.min(100, getCalidad() + 1));
        }
    }

    // Getters&Setters
    public static int getTotalJugadores() {
        return totalJugadores;
    }
    public static void setTotalJugadores(int totalJugadores) {
        Jugador.totalJugadores = totalJugadores;
    }

    public int getDorsal() {
        return dorsal;
    }
    public void setDorsal(int dorsal) {
        if (dorsal < 1 || dorsal > 99) throw new IllegalArgumentException("Dorsal inválido");
        this.dorsal = dorsal;
    }

    public String getPosicion() {
        return posicion;
    }
    public double getCalidad() {
        return calidad;
    }

    public final void setPosicion(String posicion) {
        if (posicion == null || !Arrays.asList(POSICIONES).contains(posicion)) {
            throw new IllegalArgumentException("Posición no válida");
        }
        this.posicion = posicion;
    }
    public final void setCalidad(double calidad) {
        if (calidad < 30 || calidad > 100) throw new IllegalArgumentException("Calidad inválida");
        this.calidad = calidad;
    }

    public final void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser nulo o vacío");
        }
        this.apellido = apellido;
    }



    @Override
    public int compareTo(Jugador otro) {
        if (otro == null) {
            throw new NullPointerException("No se puede comparar con null");
        }

        int cmp = Double.compare(otro.calidad, this.calidad);
        if (cmp != 0) return cmp;

        cmp = Double.compare(otro.getMotivacion(), this.getMotivacion());
        if (cmp != 0) return cmp;

        if (this.getApellido() == null && otro.getApellido() == null) return 0;
        if (this.getApellido() == null) return -1;
        if (otro.getApellido() == null) return 1;
        return this.getApellido().compareTo(otro.getApellido());
    }

    public static Comparator<Jugador> comparadorPorPosicion() {
        return (j1, j2) -> {
            // Validar nulos primero
            if (j1 == j2) return 0;
            if (j1 == null) return -1;
            if (j2 == null) return 1;

            // 1. Comparar por posición (manejo de nulos)
            if (j1.getPosicion() == null && j2.getPosicion() == null) return 0;
            if (j1.getPosicion() == null) return -1;
            if (j2.getPosicion() == null) return 1;
            int cmp = j1.getPosicion().compareTo(j2.getPosicion());

            // 2. Si posiciones son iguales, comparar por calidad (descendente)
            return cmp != 0 ? cmp : Double.compare(j2.getCalidad(), j1.getCalidad());
        };
    }

    @Override
    public String toString() {
        return String.format("%s %s - Dorsal: %d, Pos: %s, Cal: %.1f, %s",
                getNombre(), getApellido(), dorsal, posicion, calidad, super.toString());
    }
}