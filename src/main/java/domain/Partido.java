package main.java.domain;

import java.util.Objects;
import java.util.Random;

/**
 * Representa un partido de fútbol disputado entre dos equipos: un equipo local y un equipo visitante.
 * <p>
 * Esta clase se encarga de almacenar los equipos involucrados, el resultado del partido (goles)
 * una vez que se ha jugado, y el estado del partido (si ya se ha disputado o no).
 * Incluye la lógica para simular el resultado de un partido basándose en la calidad media
 * y la motivación del entrenador de cada equipo.
 */
public class Partido {
    private final Equipo local;
    private final Equipo visitante;
    private int golesLocal;
    private int golesVisitante;
    private boolean jugado;

    /**
     * Constructor para crear un nuevo partido entre un equipo local y un equipo visitante.
     * El partido se inicializa como no jugado.
     *
     * @param local     El equipo que juega como local. No puede ser {@code null}.
     * @param visitante El equipo que juega como visitante. No puede ser {@code null}.
     * @throws NullPointerException si el equipo local o visitante es {@code null}.
     */
    public Partido(Equipo local, Equipo visitante) {
        this.local = Objects.requireNonNull(local, "El equipo local no puede ser nulo.");
        this.visitante = Objects.requireNonNull(visitante, "El equipo visitante no puede ser nulo.");
        this.jugado = false;
    }

    /**
     * Simula el partido entre el equipo local y el visitante.
     * <p>
     * El resultado (goles) se calcula basándose en un factor derivado de la calidad media
     * de los jugadores de cada equipo y la motivación de sus entrenadores.
     * Una vez que el partido se ha jugado, su estado cambia a 'jugado' y no puede
     * volver a jugarse. El resultado se imprime en la consola.
     *
     * @throws IllegalStateException si se intenta jugar un partido que ya ha sido disputado.
     */
    public void jugar() {
        if (jugado) {
            throw new IllegalStateException("El partido ya ha sido jugado y no puede jugarse de nuevo.");
        }

        // Calcula el factor de rendimiento para el equipo local
        double factorLocal = local.calcularCalidadMedia() *
                (1 + (local.getEntrenador() != null ? local.getEntrenador().getMotivacion() : 5.0) / 20.0);
        // Calcula el factor de rendimiento para el equipo visitante
        double factorVisitante = visitante.calcularCalidadMedia() *
                (1 + (visitante.getEntrenador() != null ? visitante.getEntrenador().getMotivacion() : 5.0) / 20.0);

        Random rand = new Random();
        // Calcula los goles para cada equipo usando una distribución de Poisson simulada
        golesLocal = calcularGoles(factorLocal, factorVisitante, rand);
        golesVisitante = calcularGoles(factorVisitante, factorLocal, rand);

        this.jugado = true; // Marca el partido como jugado

        // Imprime el resultado del partido en la consola
        System.out.printf("Resultado: %s %d - %d %s%n",
                local.getNombre(), golesLocal,
                golesVisitante, visitante.getNombre());
    }

    /**
     * Calcula el número de goles que un equipo podría marcar, basado en su factor de rendimiento
     * y el de su rival, utilizando una simulación basada en la distribución de Poisson.
     *
     * @param factorPropio  El factor de rendimiento del equipo para el cual se calculan los goles.
     * @param factorRival   El factor de rendimiento del equipo rival.
     * @param rand          Una instancia de {@link Random} para la generación de números aleatorios.
     * @return El número de goles calculados para el equipo.
     */
    private int calcularGoles(double factorPropio, double factorRival, Random rand) {
        // La diferencia de factores influye en la media de goles esperada
        double diferencia = factorPropio - factorRival;
        // Calcula la tasa base (lambda) para la distribución de Poisson, ajustada por la diferencia de factores
        double baseLambda = Math.max(0, 0.5 + diferencia / 50.0); // Asegura que lambda no sea negativo

        // Simulación de la distribución de Poisson usando el método de Knuth
        // L = e^(-lambda)
        double l = Math.exp(-baseLambda);
        int goles = 0;
        double p = 1.0;

        do {
            goles++;
            p *= rand.nextDouble(); // Genera un número aleatorio U entre 0 y 1
        } while (p > l);

        return goles - 1; // Ajusta el contador de goles
    }

    /**
     * Obtiene el equipo local del partido.
     *
     * @return El {@link Equipo} local.
     */
    public final Equipo getLocal() {
        return local;
    }

    /**
     * Obtiene el equipo visitante del partido.
     *
     * @return El {@link Equipo} visitante.
     */
    public final Equipo getVisitante() {
        return visitante;
    }

    /**
     * Obtiene el número de goles marcados por el equipo local.
     *
     * @return El número de goles del equipo local.
     * @throws IllegalStateException si el partido aún no ha sido jugado.
     */
    public final int getGolesLocal() {
        if (!jugado) {
            throw new IllegalStateException("El partido no ha sido jugado todavía. No se pueden obtener los goles del local.");
        }
        return golesLocal;
    }

    /**
     * Obtiene el número de goles marcados por el equipo visitante.
     *
     * @return El número de goles del equipo visitante.
     * @throws IllegalStateException si el partido aún no ha sido jugado.
     */
    public final int getGolesVisitante() {
        if (!jugado) {
            throw new IllegalStateException("El partido no ha sido jugado todavía. No se pueden obtener los goles del visitante.");
        }
        return golesVisitante;
    }

    /**
     * Comprueba si el partido ya ha sido jugado.
     *
     * @return {@code true} si el partido ha sido jugado, {@code false} en caso contrario.
     */
    public final boolean isJugado() {
        return jugado;
    }

    /**
     * Devuelve una representación en cadena del partido.
     * <p>
     * Si el partido ha sido jugado, muestra el resultado.
     * Si no ha sido jugado, indica los equipos y que está pendiente.
     *
     * @return Una cadena que representa el partido y su estado/resultado.
     */
    @Override
    public String toString() {
        if (!jugado) {
            return String.format("%s vs %s (no jugado)", local.getNombre(), visitante.getNombre());
        }
        return String.format("%s %d - %d %s", local.getNombre(), golesLocal, golesVisitante, visitante.getNombre());
    }
}