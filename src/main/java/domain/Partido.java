package main.java.domain;

import java.util.Objects;
import java.util.Random;

public class Partido {
    private final Equipo local;
    private final Equipo visitante;
    private int golesLocal;
    private int golesVisitante;
    private boolean jugado;

    public Partido(Equipo local, Equipo visitante) {
        this.local = Objects.requireNonNull(local);
        this.visitante = Objects.requireNonNull(visitante);
        this.jugado = false;
    }

    public void jugar() {
        if (jugado) {
            throw new IllegalStateException("El partido ya ha sido jugado");
        }

        double factorLocal = local.calcularCalidadMedia() *
                (1 + (local.getEntrenador() != null ? local.getEntrenador().getMotivacion() : 5) / 20.0);
        double factorVisitante = visitante.calcularCalidadMedia() *
                (1 + (visitante.getEntrenador() != null ? visitante.getEntrenador().getMotivacion() : 5) / 20.0);

        Random rand = new Random();
        golesLocal = calcularGoles(factorLocal, factorVisitante, rand);
        golesVisitante = calcularGoles(factorVisitante, factorLocal, rand);

        jugado = true;

        System.out.printf("Resultado: %s %d - %d %s%n",
                local.getNombre(), golesLocal,
                golesVisitante, visitante.getNombre());
    }

    private int calcularGoles(double factorPropio, double factorRival, Random rand) {
        double diferencia = factorPropio - factorRival;
        double base = Math.max(0, 0.5 + diferencia / 50.0);

        double l = Math.exp(-base);
        int goles = 0;
        double p = 1.0;

        do {
            goles++;
            p *= rand.nextDouble();
        } while (p > l);

        return goles - 1;
    }

    // Getters
    public final Equipo getLocal() { return local; }
    public final Equipo getVisitante() { return visitante; }
    public final int getGolesLocal() {
        if (!jugado) {
            throw new IllegalStateException("El partido no ha sido jugado todavía");
        }
        return golesLocal;
    }
    public final int getGolesVisitante() {
        if (!jugado) {
            throw new IllegalStateException("El partido no ha sido jugado todavía");
        }
        return golesVisitante;
    }
    public final boolean isJugado() { return jugado; }

    @Override
    public String toString() {
        if (!jugado) {
            return String.format("%s vs %s (no jugado)", local.getNombre(), visitante.getNombre());
        }
        return String.format("%s %d - %d %s", local.getNombre(), golesLocal, golesVisitante, visitante.getNombre());
    }
}