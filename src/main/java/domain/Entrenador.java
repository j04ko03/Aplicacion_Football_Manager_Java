package main.java.domain;

/**
 * Representa a un Entrenador en el sistema, extendiendo la clase {@link Persona}.
 * Un entrenador tiene atributos específicos como el número de torneos ganados y si es
 * seleccionador nacional, además de las características heredadas de Persona.
 * Mantiene un contador estático del número total de entrenadores creados.
 */
public class Entrenador extends Persona {
    /**
     * Contador estático que lleva la cuenta del número total de instancias de Entrenador creadas.
     */
    private static int totalEntrenadores = 0;

    /**
     * Número de torneos ganados por el entrenador.
     */
    private int torneosGanados;

    /**
     * Indica si el entrenador es también un seleccionador nacional.
     * {@code true} si es seleccionador nacional, {@code false} en caso contrario.
     */
    private boolean seleccionadorNacional;

    /**
     * Constructor para crear un nuevo objeto Entrenador.
     * Inicializa los atributos del entrenador e incrementa el contador total de entrenadores.
     *
     * @param nombre                El nombre del entrenador.
     * @param apellido              El apellido del entrenador.
     * @param fechaNacimiento       La fecha de nacimiento del entrenador (formato cadena, dd/MM/yyyy).
     * @param sueldo                El sueldo del entrenador.
     * @param motivacion            El nivel de motivación del entrenador (habitualmente entre 0 y 10).
     * @param torneosGanados        El número de torneos ganados por el entrenador. No puede ser negativo.
     * @param seleccionadorNacional {@code true} si el entrenador es seleccionador nacional, {@code false} en caso contrario.
     * @throws IllegalArgumentException si {@code torneosGanados} es negativo.
     */
    public Entrenador(String nombre, String apellido, String fechaNacimiento, double sueldo, double motivacion,
                      int torneosGanados, boolean seleccionadorNacional) {
        super(nombre, apellido, fechaNacimiento, sueldo, motivacion);
        setTorneosGanados(torneosGanados);
        this.seleccionadorNacional = seleccionadorNacional;
        totalEntrenadores++;
    }

    /**
     * Simula una sesión de entrenamiento, aumentando la motivación del entrenador.
     * <p>
     * El aumento es mayor si el entrenador es seleccionador nacional (incremento de 0.3),
     * o menor (incremento de 0.15) si no lo es. La motivación no excederá un valor máximo de 10.
     */
    @Override
    public void entrenamiento() {
        double aumento = seleccionadorNacional ? 0.3 : 0.15;
        setMotivacion(Math.min(10, getMotivacion() + aumento));
    }

    /**
     * Incrementa el sueldo del entrenador en un 0.5%.
     */
    public void incrementarSou() {
        setSueldo(getSueldo() * 1.005);
    }

    /**
     * Obtiene el número de torneos ganados por el entrenador.
     *
     * @return El número de torneos ganados.
     */
    public final int getTorneosGanados() {
        return torneosGanados;
    }

    /**
     * Comprueba si el entrenador es seleccionador nacional.
     *
     * @return {@code true} si es seleccionador nacional, {@code false} en caso contrario.
     */
    public final boolean isSeleccionadorNacional() {
        return seleccionadorNacional;
    }

    /**
     * Obtiene el número total de entrenadores creados en el sistema.
     *
     * @return El contador total de instancias de Entrenador creadas.
     */
    public static int getTotalEntrenadores() {
        return totalEntrenadores;
    }

    /**
     * Establece el número de torneos ganados por el entrenador.
     *
     * @param torneosGanados El nuevo número de torneos ganados. Debe ser un valor no negativo.
     * @throws IllegalArgumentException si {@code torneosGanados} es negativo.
     */
    public final void setTorneosGanados(int torneosGanados) {
        if (torneosGanados < 0) throw new IllegalArgumentException("El número de torneos no puede ser negativo.");
        this.torneosGanados = torneosGanados;
    }

    /**
     * Establece si el entrenador es seleccionador nacional.
     *
     * @param seleccionadorNacional {@code true} para marcar como seleccionador nacional, {@code false} en caso contrario.
     */
    public final void setSeleccionadorNacional(boolean seleccionadorNacional) {
        this.seleccionadorNacional = seleccionadorNacional;
    }

    /**
     * Devuelve una representación en cadena del objeto Entrenador.
     * <p>
     * La representación incluye:
     * <ul>
     *     <li>Nombre y apellido</li>
     *     <li>Número de torneos ganados</li>
     *     <li>Si es o no seleccionador nacional</li>
     *     <li>Información heredada de la clase base {@link Persona}</li>
     * </ul>
     *
     * @return Una cadena que representa al entrenador con sus atributos.
     */
    @Override
    public String toString() {
        return String.format("%s %s - Torneos: %d, %s seleccionador nacional. %s",
                getNombre(), getApellido(), torneosGanados,
                seleccionadorNacional ? "Es" : "No es", super.toString());
    }
}