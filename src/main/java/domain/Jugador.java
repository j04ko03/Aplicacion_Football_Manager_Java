package main.java.domain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Objects;

/**
 * Representa un jugador de un equipo, extendiendo la clase {@link Persona}.
 * Un jugador tiene atributos específicos como dorsal, posición en el campo y nivel de calidad.
 * <p>
 * Implementa {@link Comparable} para permitir la ordenación de jugadores, por defecto por calidad
 * (descendente), luego por motivación (descendente) y finalmente por apellido.
 * Mantiene un contador estático del número total de jugadores creados.
 */
public class Jugador extends Persona implements Comparable<Jugador> {
    /**
     * Array constante que define las posibles posiciones de un jugador.
     * Las posiciones son: "POR" (Portero), "DEF" (Defensa), "MIG" (Centrocampista), "DAV" (Delantero).
     */
    public static final String[] POSICIONES = {"POR", "DEF", "MIG", "DAV"};

    /**
     * Contador estático que lleva la cuenta del número total de instancias de Jugador creadas.
     */
    private static int totalJugadores = 0;

    /**
     * Número de dorsal del jugador (entre 1 y 99).
     */
    protected int dorsal;

    /**
     * Posición del jugador en el campo (por ejemplo, "POR", "DEF").
     */
    private String posicion;

    /**
     * Nivel de calidad del jugador (entre 30 y 100).
     */
    private double calidad;

    /**
     * Constructor para crear un nuevo objeto Jugador.
     * Inicializa los atributos del jugador.
     *
     * @param nombre          El nombre del jugador. No puede ser nulo.
     * @param apellido        El apellido del jugador. No puede ser nulo.
     * @param fechaNacimiento La fecha de nacimiento del jugador (formato de cadena).
     * @param sueldo          El sueldo del jugador.
     * @param motivacion      El nivel de motivación del jugador (normalmente entre 0 y 10).
     * @param dorsal          El número de dorsal del jugador (entre 1 y 99).
     * @param posicion        La posición del jugador en el campo (debe ser una de las definidas en {@link #POSICIONES}).
     * @param calidad         El nivel de calidad del jugador (entre 30 y 100).
     * @throws IllegalArgumentException si el dorsal, posición o calidad no son válidos.
     */
    public Jugador(String nombre, String apellido, String fechaNacimiento, double sueldo, double motivacion, int dorsal,
                   String posicion, double calidad) {
        super(nombre, apellido, fechaNacimiento, sueldo, motivacion);
        setDorsal(dorsal);
        setPosicion(posicion);
        setCalidad(calidad);
        totalJugadores++;
    }

    /**
     * Simula una sesión de entrenamiento para el jugador.
     * <p>
     * Aumenta la motivación base y luego aumenta la calidad del jugador.
     * El aumento de calidad tiene una probabilidad:
     * <ul>
     *     <li>10% de aumentar 0.3,</li>
     *     <li>20% de aumentar 0.2,</li>
     *     <li>y el resto de aumentar 0.1.</li>
     * </ul>
     * La calidad se ajusta para no exceder el valor máximo de 100.
     */
    @Override
    public void entrenamiento() {
        aumentarMotivacionBase();
        Random random = new Random();
        double incrementoCalidad;
        int probabilidad = random.nextInt(10);
        if (probabilidad == 0) {
            incrementoCalidad = 0.3;
        } else if (probabilidad <= 2) {
            incrementoCalidad = 0.2;
        } else {
            incrementoCalidad = 0.1;
        }
        setCalidad(Math.min(100, calidad + incrementoCalidad));
    }

    /**
     * Simula la posibilidad de que un jugador cambie de posición aleatoriamente.
     * <p>
     * Hay un 5% de probabilidad de que el jugador cambie a una nueva posición aleatoria de {@link #POSICIONES}.
     * Si cambia de posición, su calidad aumenta en 1 punto (limitado a 100).
     */
    public void canviDePosicio() {
        Random random = new Random();
        if (random.nextDouble() < 0.05) {
            String nuevaPosicion;
            do {
                nuevaPosicion = POSICIONES[random.nextInt(POSICIONES.length)];
            } while (nuevaPosicion.equals(posicion));
            setPosicion(nuevaPosicion);
            setCalidad(Math.min(100, calidad + 1));
        }
    }

    /**
     * Obtiene el número total de jugadores creados.
     *
     * @return El contador total de instancias de Jugador creadas.
     */
    public static int getTotalJugadores() {
        return totalJugadores;
    }

    /**
     * Establece el contador total de jugadores.
     * Este método debería usarse con precaución, típicamente al cargar datos persistidos.
     *
     * @param totalJugadores El nuevo valor para el contador total de jugadores.
     */
    public static void setTotalJugadores(int totalJugadores) {
        Jugador.totalJugadores = totalJugadores;
    }

    /**
     * Obtiene el dorsal del jugador.
     *
     * @return El número de dorsal.
     */
    public int getDorsal() {
        return dorsal;
    }

    /**
     * Establece el dorsal del jugador.
     *
     * @param dorsal El nuevo número de dorsal. Debe estar entre 1 y 99.
     * @throws IllegalArgumentException si el dorsal está fuera del rango válido.
     */
    public void setDorsal(int dorsal) {
        if (dorsal < 1 || dorsal > 99) {
            throw new IllegalArgumentException("El dorsal debe estar entre 1 y 99.");
        }
        this.dorsal = dorsal;
    }

    /**
     * Obtiene la posición del jugador en el campo.
     *
     * @return La cadena que representa la posición del jugador (por ejemplo, "POR", "DEF").
     */
    public String getPosicion() {
        return posicion;
    }

    /**
     * Establece la posición del jugador.
     * <p>
     * La posición debe ser una de las definidas en {@link #POSICIONES}.
     *
     * @param posicion La nueva posición del jugador.
     * @throws IllegalArgumentException si la posición no es válida o es nula.
     */
    public final void setPosicion(String posicion) {
        if (!Arrays.asList(POSICIONES).contains(posicion)) {
            throw new IllegalArgumentException("Posición no válida. Debe ser POR, DEF, MIG o DAV.");
        }
        this.posicion = posicion;
    }

    /**
     * Obtiene la calidad del jugador.
     *
     * @return El nivel de calidad del jugador (entre 30 y 100).
     */
    public double getCalidad() {
        return calidad;
    }

    /**
     * Establece la calidad del jugador.
     *
     * @param calidad El nuevo nivel de calidad. Debe estar entre 30 y 100.
     * @throws IllegalArgumentException si la calidad está fuera del rango permitido.
     */
    public final void setCalidad(double calidad) {
        if (calidad < 30 || calidad > 100) {
            throw new IllegalArgumentException("La calidad debe estar entre 30 y 100.");
        }
        this.calidad = calidad;
    }

    /**
     * Establece el apellido del jugador, sobrescribiendo la implementación de la superclase.
     *
     * @param apellido El nuevo apellido del jugador. No puede ser nulo ni vacío.
     * @throws IllegalArgumentException si el apellido es nulo o está vacío.
     */
    public final void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser nulo o vacío.");
        }
        this.apellido = apellido;
    }

    /**
     * Compara este jugador con otro para determinar su orden.
     * <p>
     * La comparación se realiza por los siguientes criterios, en orden:
     * <ol>
     *     <li>Calidad (descendente)</li>
     *     <li>Motivación (descendente)</li>
     *     <li>Apellido (ascendente, alfabéticamente)</li>
     * </ol>
     *
     * @param otro El otro jugador con el que comparar.
     * @return Un número negativo, cero o positivo según el orden.
     * @throws NullPointerException si {@code otro} es nulo.
     */
    @Override
    public int compareTo(Jugador otro) {
        Objects.requireNonNull(otro);
        int comparacionCalidad = Double.compare(otro.calidad, this.calidad);
        if (comparacionCalidad != 0) {
            return comparacionCalidad;
        }
        int comparacionMotivacion = Double.compare(otro.motivacion, this.motivacion);
        if (comparacionMotivacion != 0) {
            return comparacionMotivacion;
        }
        return this.apellido.compareTo(otro.apellido);
    }

    /**
     * Devuelve un comparador para ordenar jugadores principalmente por posición y luego por calidad.
     * <p>
     * La ordenación:
     * <ul>
     *     <li>Por posición (orden alfabético, ascendente)</li>
     *     <li>Por calidad (descendiente)</li>
     * </ul>
     *
     * @return Un {@link Comparator} para jugadores.
     */
    public static Comparator<Jugador> comparadorPorPosicion() {
        return Comparator.comparing(Jugador::getPosicion, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(Comparator.comparingDouble(Jugador::getCalidad).reversed());
    }

    /**
     * Devuelve una representación en cadena del objeto Jugador.
     * <p>
     * Incluye el nombre, apellido, dorsal, posición, calidad y la información heredada de la clase base {@link Persona}.
     *
     * @return Una cadena que representa al jugador.
     */
    @Override
    public String toString() {
        return String.format("%s %s (Dorsal: %d, Posición: %s, Calidad: %.2f) %s",
                nombre, apellido, dorsal, posicion, calidad, super.toString());
    }
}