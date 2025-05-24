package main.java.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Representa una liga deportiva compuesta por varios equipos.
 * <p>
 * Una liga tiene un nombre, una lista de equipos participantes y una lista de partidos disputados.
 * Permite agregar equipos, simular la disputa de todos los partidos de la liga (formato todos contra todos a una vuelta),
 * y mostrar una clasificación detallada basada en los resultados de los partidos. También puede identificar al equipo
 * con más goles a favor y más goles en contra.
 * <p>
 * Implementa {@link Serializable} para permitir la persistencia de sus instancias.
 */
public class Liga implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Nombre de la liga.
     */
    private final String nombre;

    /**
     * Equipos participantes en la liga.
     */
    private final List<Equipo> equipos;

    /**
     * Partidos disputados en la liga.
     */
    private final List<Partido> partidos;

    /**
     * Constructor para crear una nueva Liga.
     *
     * @param nombre El nombre de la liga. No puede ser nulo.
     * @throws NullPointerException si el nombre es nulo.
     */
    public Liga(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre de la liga no puede ser nulo.");
        this.equipos = new ArrayList<>();
        this.partidos = new ArrayList<>();
    }

    /**
     * Agrega un equipo a la liga.
     * <p>
     * No se agregará el equipo si ya está presente en la liga.
     *
     * @param equipo El equipo a agregar. No puede ser nulo.
     * @return {@code true} si el equipo fue agregado exitosamente, {@code false} si el equipo ya existía.
     * @throws NullPointerException si el equipo es nulo.
     */
    public boolean agregarEquipo(Equipo equipo) {
        Objects.requireNonNull(equipo, "El equipo no puede ser nulo.");
        if (!equipos.contains(equipo)) {
            equipos.add(equipo);
            return true;
        }
        return false;
    }

    /**
     * Simula la disputa de todos los partidos de la liga.
     * <p>
     * Los partidos se juegan en un formato de todos contra todos a una sola vuelta (cada equipo juega una vez contra cada otro).
     * Si hay menos de dos equipos, no se disputa la liga y se muestra un mensaje.
     * Los resultados de los partidos se almacenan internamente.
     * Antes de disputar, se limpian los partidos anteriores.
     */
    public void disputarLiga() {
        partidos.clear();
        if (equipos.size() < 2) {
            System.out.println("No hay suficientes equipos para disputar la liga.");
            return;
        }
        for (int i = 0; i < equipos.size(); i++) {
            for (int j = i + 1; j < equipos.size(); j++) {
                Partido partido = new Partido(equipos.get(i), equipos.get(j));
                partido.jugar();
                partidos.add(partido);
            }
        }
    }

    /**
     * Muestra la clasificación de la liga en la consola.
     * <p>
     * La clasificación incluye posición, nombre del equipo, partidos jugados (PJ), ganados (PG),
     * empatados (PE), perdidos (PP), goles a favor (GF), goles en contra (GC),
     * diferencia de goles (DG) y puntos (Pts).
     * <p>
     * Si no se han disputado partidos, se muestra un mensaje indicándolo.
     */
    public void mostrarClasificacion() {
        if (partidos.isEmpty()) {
            System.out.println("No se han disputado partidos en la liga.");
            return;
        }

        Map<Equipo, EquipoStats> estadisticas = new HashMap<>();
        for (Partido partido : partidos) {
            estadisticas.computeIfAbsent(partido.getLocal(), e -> new EquipoStats(e, partidos));
            estadisticas.computeIfAbsent(partido.getVisitante(), e -> new EquipoStats(e, partidos));
        }

        List<EquipoStats> clasificacion = new ArrayList<>(estadisticas.values());
        clasificacion.sort(Comparator.comparingInt(EquipoStats::getPuntos).reversed()
                .thenComparingInt(EquipoStats::getDiferenciaGoles).reversed());

        System.out.println("Clasificación de la Liga:");
        System.out.printf("%-20s %-2s %-2s %-2s %-2s %-2s %-2s %-2s %-2s%n",
                "Equipo", "PJ", "PG", "PE", "PP", "GF", "GC", "DG", "Pts");

        for (EquipoStats stats : clasificacion) {
            System.out.printf("%-20s %-2d %-2d %-2d %-2d %-2d %-2d %-3d %-2d%n",
                    stats.getEquipo().getNombre(),
                    stats.getPartidosJugados(),
                    stats.getPartidosGanados(),
                    stats.getPartidosEmpatados(),
                    stats.getPartidosPerdidos(),
                    stats.getGolesFavor(),
                    stats.getGolesContra(),
                    stats.getDiferenciaGoles(),
                    stats.getPuntos());
        }
    }

    /**
     * Obtiene el equipo que ha marcado más goles a favor en la liga.
     *
     * @return El {@link Equipo} con más goles a favor, o {@code null} si no se han disputado partidos.
     */
    public Equipo getEquipoMasGolesFavor() {
        return equipos.stream()
                .max(Comparator.comparingInt(this::getGolesFavorDeEquipo))
                .orElse(null);
    }

    /**
     * Obtiene el equipo que ha recibido más goles en contra en la liga.
     *
     * @return El {@link Equipo} con más goles en contra, o {@code null} si no se han disputado partidos.
     */
    public Equipo getEquipoMasGolesContra() {
        return equipos.stream()
                .max(Comparator.comparingInt(this::getGolesContraDeEquipo))
                .orElse(null);
    }

    /**
     * Calcula el total de goles a favor para un equipo específico en la liga.
     *
     * @param equipo El equipo para el cual calcular los goles.
     * @return El número total de goles marcados por el equipo.
     */
    private int getGolesFavorDeEquipo(Equipo equipo) {
        return partidos.stream()
                .filter(p -> p.getLocal().equals(equipo) || p.getVisitante().equals(equipo))
                .mapToInt(p -> p.getLocal().equals(equipo) ? p.getGolesLocal() : p.getGolesVisitante())
                .sum();
    }

    /**
     * Calcula el total de goles en contra para un equipo específico en la liga.
     *
     * @param equipo El equipo para el cual calcular los goles en contra.
     * @return El número total de goles recibidos por el equipo.
     */
    private int getGolesContraDeEquipo(Equipo equipo) {
        return partidos.stream()
                .filter(p -> p.getLocal().equals(equipo) || p.getVisitante().equals(equipo))
                .mapToInt(p -> p.getLocal().equals(equipo) ? p.getGolesVisitante() : p.getGolesLocal())
                .sum();
    }

    /**
     * Obtiene el nombre de la liga.
     *
     * @return El nombre de la liga.
     */
    public final String getNombre() {
        return nombre;
    }

    /**
     * Obtiene una lista no modificable de los equipos que participan en la liga.
     *
     * @return Una lista de equipos.
     */
    public final List<Equipo> getEquipos() {
        return Collections.unmodifiableList(equipos);
    }

    /**
     * Obtiene una lista no modificable de los partidos disputados en la liga.
     *
     * @return Una lista de partidos.
     */
    public final List<Partido> getPartidos() {
        return Collections.unmodifiableList(partidos);
    }

    /**
     * Clase interna estática para almacenar y calcular las estadísticas de un equipo en la liga.
     * <p>
     * Esta clase se utiliza para generar la tabla de clasificación.
     */
    private static class EquipoStats {
        private final Equipo equipo;
        private int partidosJugados = 0;
        private int partidosGanados = 0;
        private int partidosEmpatados = 0;
        private int partidosPerdidos = 0;
        private int golesFavor = 0;
        private int golesContra = 0;

        /**
         * Constructor de EquipoStats. Calcula las estadísticas del equipo basándose en la lista de partidos.
         *
         * @param equipo   El equipo para el cual se calculan las estadísticas.
         * @param partidos La lista de todos los partidos disputados en la liga.
         */
        public EquipoStats(Equipo equipo, List<Partido> partidos) {
            this.equipo = equipo;
            for (Partido partido : partidos) {
                if (partido.getLocal().equals(equipo)) {
                    procesarPartido(partido.getGolesLocal(), partido.getGolesVisitante());
                } else if (partido.getVisitante().equals(equipo)) {
                    procesarPartido(partido.getGolesVisitante(), partido.getGolesLocal());
                }
            }
        }

        /**
         * Procesa el resultado de un partido para actualizar las estadísticas del equipo.
         *
         * @param golesMarcados  Goles marcados por el equipo en el partido.
         * @param golesRecibidos Goles recibidos por el equipo en el partido.
         */
        private void procesarPartido(int golesMarcados, int golesRecibidos) {
            partidosJugados++;
            golesFavor += golesMarcados;
            golesContra += golesRecibidos;
            if (golesMarcados > golesRecibidos) {
                partidosGanados++;
            } else if (golesMarcados == golesRecibidos) {
                partidosEmpatados++;
            } else {
                partidosPerdidos++;
            }
        }

        public Equipo getEquipo() {
            return equipo;
        }

        public int getPartidosJugados() {
            return partidosJugados;
        }

        public int getPartidosGanados() {
            return partidosGanados;
        }

        public int getPartidosEmpatados() {
            return partidosEmpatados;
        }

        public int getPartidosPerdidos() {
            return partidosPerdidos;
        }

        public int getGolesFavor() {
            return golesFavor;
        }

        public int getGolesContra() {
            return golesContra;
        }

        public int getDiferenciaGoles() {
            return golesFavor - golesContra;
        }

        public int getPuntos() {
            return (partidosGanados * 3) + partidosEmpatados;
        }
    }
}