package main.java.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Representa un equipo deportivo en el sistema.
 * <p>
 * Un equipo tiene un nombre, año de fundación, ciudad, y puede tener un estadio,
 * un presidente, un entrenador y una lista de jugadores.
 * Proporciona funcionalidades para gestionar la plantilla de jugadores (agregar, eliminar, buscar),
 * calcular la calidad media del equipo, y realizar entrenamientos colectivos.
 * Implementa {@link Serializable} para permitir la persistencia de sus instancias.
 */
public class Equipo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String nombre;
    private final int anioFundacion;
    private final String ciudad;
    private String nombreEstadio;
    private String nombrePresidente;
    private Entrenador entrenador;
    private final List<Jugador> jugadores;

    /**
     * Constructor principal para crear un nuevo objeto Equipo con todos los detalles.
     *
     * @param nombre          El nombre del equipo. No puede ser nulo.
     * @param anioFundacion   El año de fundación del equipo. Debe ser un año válido (entre 1800 y el año actual).
     * @param ciudad          La ciudad de origen del equipo. No puede ser nula.
     * @param nombreEstadio   El nombre del estadio del equipo. Puede ser nulo.
     * @param nombrePresidente El nombre del presidente del equipo. Puede ser nulo.
     * @throws IllegalArgumentException si el año de fundación es inválido.
     * @throws NullPointerException     si el nombre o la ciudad son nulos.
     */
    public Equipo(String nombre, int anioFundacion, String ciudad, String nombreEstadio, String nombrePresidente) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");

        if (anioFundacion < 1800 || anioFundacion > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Año de fundación inválido: " + anioFundacion);
        }

        this.anioFundacion = anioFundacion;
        this.ciudad = Objects.requireNonNull(ciudad, "La ciudad no puede ser nula");
        this.nombreEstadio = nombreEstadio;
        this.nombrePresidente = nombrePresidente;
        this.jugadores = new ArrayList<>();
    }

    /**
     * Constructor simplificado para crear un nuevo objeto Equipo con información básica.
     * El nombre del estadio y del presidente se inicializan a {@code null}.
     *
     * @param nombre          El nombre del equipo. No puede ser nulo.
     * @param anioFundacion   El año de fundación del equipo. Debe ser un año válido.
     * @param ciudad          La ciudad de origen del equipo. No puede ser nula.
     */
    public Equipo(String nombre, int anioFundacion, String ciudad) {
        this(nombre, anioFundacion, ciudad, null, null);
    }

    /**
     * Calcula la calidad media de los jugadores del equipo.
     *
     * @return La calidad media de los jugadores, o 0 si el equipo no tiene jugadores.
     */
    public double calcularCalidadMedia() {
        if (jugadores.isEmpty()) return 0;
        return jugadores.stream().mapToDouble(Jugador::getCalidad).average().orElse(0);
    }

    /**
     * Agrega un jugador a la plantilla del equipo.
     * No permite agregar jugadores con un dorsal que ya exista en el equipo.
     *
     * @param jugador El jugador a agregar. No puede ser nulo.
     * @throws NullPointerException     si el jugador es nulo.
     * @throws IllegalArgumentException si el dorsal del jugador ya existe en el equipo.
     */
    public void agregarJugador(Jugador jugador) {
        Objects.requireNonNull(jugador, "El jugador no puede ser nulo");
        if (jugadores.stream().anyMatch(j -> j.getDorsal() == jugador.getDorsal()))
            throw new IllegalArgumentException("El dorsal " + jugador.getDorsal() + " ya existe en el equipo.");
        jugadores.add(jugador);
    }

    /**
     * Elimina un jugador de la plantilla del equipo, buscándolo por nombre y dorsal.
     *
     * @param nombre El nombre del jugador a eliminar.
     * @param dorsal El dorsal del jugador a eliminar.
     * @return {@code true} si el jugador fue encontrado y eliminado, {@code false} en caso contrario.
     */
    public boolean eliminarJugador(String nombre, int dorsal) {
        return jugadores.removeIf(j -> j.getNombre().equals(nombre) && j.getDorsal() == dorsal);
    }

    /**
     * Busca un jugador en la plantilla por su nombre y dorsal.
     *
     * @param nombre El nombre del jugador a buscar.
     * @param dorsal El dorsal del jugador a buscar.
     * @return El objeto {@link Jugador} si se encuentra, o {@code null} si no existe.
     */
    public Jugador buscarJugador(String nombre, int dorsal) {
        return jugadores.stream()
                .filter(j -> j.getNombre().equals(nombre) && j.getDorsal() == dorsal)
                .findFirst().orElse(null);
    }

    /**
     * Realiza una sesión de entrenamiento para el equipo.
     * <p>
     * Si el equipo tiene un entrenador, se entrena al entrenador. Todos los jugadores del equipo realizan su entrenamiento individual
     * y tienen una posibilidad de cambiar de posición.
     */
    public void realizarEntrenamiento() {
        if (entrenador != null) {
            entrenador.entrenamiento();
        }
        jugadores.forEach(j -> {
            j.entrenamiento();
            j.canviDePosicio(); // Simula un cambio de posición.
        });
    }

    /**
     * Obtiene el nombre del equipo.
     *
     * @return El nombre del equipo.
     */
    public final String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el año de fundación del equipo.
     *
     * @return El año de fundación.
     */
    public final int getAnioFundacion() {
        return anioFundacion;
    }

    /**
     * Obtiene la ciudad del equipo.
     *
     * @return La ciudad del equipo.
     */
    public final String getCiudad() {
        return ciudad;
    }

    /**
     * Obtiene el nombre del estadio del equipo.
     *
     * @return El nombre del estadio, o {@code null} si no está definido.
     */
    public final String getNombreEstadio() {
        return nombreEstadio;
    }

    /**
     * Obtiene el nombre del presidente del equipo.
     *
     * @return El nombre del presidente, o {@code null} si no está definido.
     */
    public final String getNombrePresidente() {
        return nombrePresidente;
    }

    /**
     * Obtiene el entrenador actual del equipo.
     *
     * @return El objeto {@link Entrenador} del equipo, o {@code null} si no tiene.
     */
    public final Entrenador getEntrenador() {
        return entrenador;
    }

    /**
     * Obtiene una lista no modificable de los jugadores del equipo, ordenados por posición y luego por calidad descendente.
     * <p>
     * La lista devuelta es una copia, por lo que las modificaciones a esta lista no afectarán la lista interna del equipo.
     *
     * @return Una lista no modificable de jugadores ordenados.
     */
    public final List<Jugador> getJugadores() {
        List<Jugador> copia = new ArrayList<>(jugadores);
        copia.sort(Jugador.comparadorPorPosicion());
        return Collections.unmodifiableList(copia);
    }

    /**
     * Obtiene una lista no modificable de los jugadores del equipo, ordenados por calidad descendente.
     * <p>
     * La lista devuelta es una copia.
     *
     * @return Una lista no modificable de jugadores ordenados por calidad.
     */
    public final List<Jugador> getJugadoresPorCalidad() {
        List<Jugador> copia = new ArrayList<>(jugadores);
        Collections.sort(copia); // Ordena por calidad descendente.
        return Collections.unmodifiableList(copia);
    }

    /**
     * Establece el nombre del estadio del equipo.
     *
     * @param nombreEstadio El nuevo nombre del estadio.
     */
    public final void setNombreEstadio(String nombreEstadio) {
        this.nombreEstadio = nombreEstadio;
    }

    /**
     * Establece el nombre del presidente del equipo.
     *
     * @param nombrePresidente El nuevo nombre del presidente.
     */
    public final void setNombrePresidente(String nombrePresidente) {
        this.nombrePresidente = nombrePresidente;
    }

    /**
     * Establece el entrenador del equipo.
     *
     * @param entrenador El nuevo entrenador para el equipo. Puede ser {@code null}.
     */
    public final void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    /**
     * Devuelve una representación en cadena del objeto Equipo.
     * <p>
     * Muestra el nombre, año de fundación, ciudad, nombre del entrenador (si existe) y número de jugadores.
     *
     * @return Una cadena que representa al equipo.
     */
    @Override
    public String toString() {
        return String.format("Equipo: %s (Fund: %d, Ciudad: %s)%nEntrenador: %s%nJugadores: %d",
                nombre, anioFundacion, ciudad,
                entrenador != null ? entrenador.getNombre() + " " + entrenador.getApellido() : "Ninguno",
                jugadores.size());
    }
}