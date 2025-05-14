package main.java.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Liga implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String nombre;
    private final List<Equipo> equipos;
    private final List<Partido> partidos;

    public Liga(String nombre) {
        this.nombre = Objects.requireNonNull(nombre);
        this.equipos = new ArrayList<>();
        this.partidos = new ArrayList<>();
    }

    public boolean agregarEquipo(Equipo equipo) {
        Objects.requireNonNull(equipo);
        if (equipos.contains(equipo)) {
            return false;
        }
        equipos.add(equipo);
        return true;
    }

    public void disputarLiga() {
        partidos.clear();

        if (equipos.size() < 2) {
            System.out.println("No hay suficientes equipos para disputar la liga");
            return;
        }

        // Todos contra todos (ida)
        for (int i = 0; i < equipos.size(); i++) {
            for (int j = i + 1; j < equipos.size(); j++) {
                Partido partido = new Partido(equipos.get(i), equipos.get(j));
                partido.jugar();
                partidos.add(partido);
            }
        }
    }

    public void mostrarClasificacion() {
        if (partidos.isEmpty()) {
            System.out.println("No se han disputado partidos todavía");
            return;
        }

        List<EquipoStats> stats = new ArrayList<>();
        for (Equipo equipo : equipos) {
            stats.add(new EquipoStats(equipo, partidos));
        }

        stats.sort(Comparator.comparingInt(EquipoStats::getPuntos)
                .thenComparingInt(EquipoStats::getDiferenciaGoles)
                .reversed());

        System.out.println("=== Clasificación de la liga " + nombre + " ===");
        System.out.println("Pos. Equipo          PJ  PG  PE  PP  GF  GC  DG  Pts");

        for (int i = 0; i < stats.size(); i++) {
            EquipoStats stat = stats.get(i);
            System.out.printf("%2d. %-15s %2d  %2d  %2d  %2d  %2d  %2d  %+3d  %3d%n",
                    i + 1, stat.getEquipo().getNombre(),
                    stat.getPartidosJugados(), stat.getPartidosGanados(),
                    stat.getPartidosEmpatados(), stat.getPartidosPerdidos(),
                    stat.getGolesFavor(), stat.getGolesContra(),
                    stat.getDiferenciaGoles(), stat.getPuntos());
        }
    }

    public Equipo getEquipoMasGolesFavor() {
        if (partidos.isEmpty()) return null;
        return equipos.stream()
                .max(Comparator.comparingInt(this::getGolesFavor))
                .orElse(null);
    }

    public Equipo getEquipoMasGolesContra() {
        if (partidos.isEmpty()) return null;
        return equipos.stream()
                .max(Comparator.comparingInt(this::getGolesContra))
                .orElse(null);
    }

    private int getGolesFavor(Equipo equipo) {
        return partidos.stream()
                .filter(p -> p.getLocal().equals(equipo))
                .mapToInt(Partido::getGolesLocal)
                .sum() +
                partidos.stream()
                        .filter(p -> p.getVisitante().equals(equipo))
                        .mapToInt(Partido::getGolesVisitante)
                        .sum();
    }

    private int getGolesContra(Equipo equipo) {
        return partidos.stream()
                .filter(p -> p.getLocal().equals(equipo))
                .mapToInt(Partido::getGolesVisitante)
                .sum() +
                partidos.stream()
                        .filter(p -> p.getVisitante().equals(equipo))
                        .mapToInt(Partido::getGolesLocal)
                        .sum();
    }

    // Getters
    public final String getNombre() { return nombre; }
    public final List<Equipo> getEquipos() { return Collections.unmodifiableList(equipos); }
    public final List<Partido> getPartidos() { return Collections.unmodifiableList(partidos); }

    private static class EquipoStats {
        private final Equipo equipo;
        private int partidosJugados = 0;
        private int partidosGanados = 0;
        private int partidosEmpatados = 0;
        private int partidosPerdidos = 0;
        private int golesFavor = 0;
        private int golesContra = 0;

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

        private void procesarPartido(int golesFavor, int golesContra) {
            partidosJugados++;
            this.golesFavor += golesFavor;
            this.golesContra += golesContra;

            if (golesFavor > golesContra) {
                partidosGanados++;
            } else if (golesFavor < golesContra) {
                partidosPerdidos++;
            } else {
                partidosEmpatados++;
            }
        }

        public Equipo getEquipo() { return equipo; }
        public int getPartidosJugados() { return partidosJugados; }
        public int getPartidosGanados() { return partidosGanados; }
        public int getPartidosEmpatados() { return partidosEmpatados; }
        public int getPartidosPerdidos() { return partidosPerdidos; }
        public int getGolesFavor() { return golesFavor; }
        public int getGolesContra() { return golesContra; }
        public int getDiferenciaGoles() { return golesFavor - golesContra; }
        public int getPuntos() { return partidosGanados * 3 + partidosEmpatados; }
    }
}
