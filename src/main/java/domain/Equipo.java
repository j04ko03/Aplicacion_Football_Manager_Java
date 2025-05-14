package main.java.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

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

//Constructores

    public Equipo(String nombre, int anioFundacion, String ciudad, String nombreEstadio, String nombrePresidente) {
        this.nombre = Objects.requireNonNull(nombre);

        if (anioFundacion < 1800 || anioFundacion > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Año fundación inválido");
        }

        this.anioFundacion = anioFundacion;
        this.ciudad = Objects.requireNonNull(ciudad);
        this.nombreEstadio = nombreEstadio;
        this.nombrePresidente = nombrePresidente;
        this.jugadores = new ArrayList<>();
    }

    public Equipo(String nombre, int anioFundacion, String ciudad) {
        this(nombre, anioFundacion, ciudad, null, null);
    }

//    Metodos
    public double calcularCalidadMedia() {
        if (jugadores.isEmpty()) return 0;
        return jugadores.stream().mapToDouble(Jugador::getCalidad).average().orElse(0);
    }

    public void agregarJugador(Jugador jugador) {
        Objects.requireNonNull(jugador);
        if (jugadores.stream().anyMatch(j -> j.getDorsal() == jugador.getDorsal()))
            throw new IllegalArgumentException("Dorsal ya existe");
        jugadores.add(jugador);
    }

    public boolean eliminarJugador(String nombre, int dorsal) {
        return jugadores.removeIf(j -> j.getNombre().equals(nombre) && j.getDorsal() == dorsal);
    }

    public Jugador buscarJugador(String nombre, int dorsal) {
        return jugadores.stream()
                .filter(j -> j.getNombre().equals(nombre) && j.getDorsal() == dorsal)
                .findFirst().orElse(null);
    }

    public void realizarEntrenamiento() {
        if (entrenador != null) {
            entrenador.entrenamiento();
        }
        jugadores.forEach(j -> {
            j.entrenamiento();
            j.canviDePosicio();
        });
    }

    // Getters
    public final String getNombre() { return nombre; }
    public final int getAnioFundacion() { return anioFundacion; }
    public final String getCiudad() { return ciudad; }
    public final String getNombreEstadio() { return nombreEstadio; }
    public final String getNombrePresidente() { return nombrePresidente; }
    public final Entrenador getEntrenador() { return entrenador; }

    public final List<Jugador> getJugadores() {
        List<Jugador> copia = new ArrayList<>(jugadores);
        copia.sort(Jugador.comparadorPorPosicion());
        return Collections.unmodifiableList(copia);
    }

    public final List<Jugador> getJugadoresPorCalidad() {
        List<Jugador> copia = new ArrayList<>(jugadores);
        Collections.sort(copia);
        return Collections.unmodifiableList(copia);
    }

    // Setters
    public final void setNombreEstadio(String nombreEstadio) {
        this.nombreEstadio = nombreEstadio;
    }

    public final void setNombrePresidente(String nombrePresidente) {
        this.nombrePresidente = nombrePresidente;
    }

    public final void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    @Override
    public String toString() {
        return String.format("Equipo: %s (Fund: %d, Ciudad: %s)%nEntrenador: %s%nJugadores: %d",
                nombre, anioFundacion, ciudad,
                entrenador != null ? entrenador.getNombre() : "Ninguno",
                jugadores.size());
    }
}