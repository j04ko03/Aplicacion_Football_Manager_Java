package main.java.services;

import main.java.domain.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Clase utilitaria para gestionar las operaciones de entrada y salida de archivos
 * relacionados con el mercado de fichajes y los equipos.
 * <p>
 * Permite cargar y guardar información de jugadores, entrenadores y equipos desde/para
 * archivos de texto y binarios.
 */

public class FileManager {
    private static final String MERCADO_FILE = "src/main/resources/mercat_fitxatges.txt";
    private static final String EQUIPOS_FILE = "src/main/resources/equipos.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public static List<Persona> cargarMercado() {
        List<Persona> personas = new ArrayList<>();
        int jugadoresCargados = 0;
        int entrenadoresCargados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(MERCADO_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");

                if (partes.length < 6) {
                    System.err.println("Línea con campos insuficientes: " + linea);
                    continue;
                }

                try {
                    String tipo = partes[0];
                    String nombre = partes[1].trim();
                    String apellido = partes[2].trim();
                    String fechaNacimiento = partes[3].trim();
                    double motivacion = Double.parseDouble(partes[4]);
                    double sueldo = Double.parseDouble(partes[5]);

                    if (nombre.isEmpty() || apellido.isEmpty()) {
                        System.err.println("Nombre o apellido vacío en línea: " + linea);
                        continue;
                    }

                    if (tipo.equalsIgnoreCase("J")) {
                        if (partes.length < 9) {
                            System.err.println("Jugador con campos insuficientes: " + linea);
                            continue;
                        }
                        int dorsal = Integer.parseInt(partes[6].trim());
                        String posicion = partes[7].trim();
                        double calidad = Double.parseDouble(partes[8].trim());

                        personas.add(new Jugador(nombre, apellido, fechaNacimiento, sueldo, motivacion, dorsal, posicion, calidad));
                        jugadoresCargados++;

                    } else if (tipo.equalsIgnoreCase("E")) {
                        if (partes.length < 8) {
                            System.err.println("Entrenador con campos insuficientes: " + linea);
                            continue;
                        }
                        int torneosGanados = Integer.parseInt(partes[6].trim());
                        boolean seleccionador = Boolean.parseBoolean(partes[7].trim());

                        personas.add(new Entrenador(nombre, apellido, fechaNacimiento, sueldo, motivacion, torneosGanados, seleccionador));
                        entrenadoresCargados++;
                    } else {
                        System.err.println("Tipo desconocido: " + tipo + " en línea: " + linea);
                    }

                } catch (Exception e) {
                    System.err.println("Error procesando línea: " + linea);
                    e.printStackTrace();  // Importante para debug
                }
            }

        } catch (IOException e) {
            System.err.println("Error leyendo mercado: " + e.getMessage());
        }

        System.out.println("Jugadores cargados: " + jugadoresCargados);
        System.out.println("Entrenadores cargados: " + entrenadoresCargados);
        return personas;
    }


    public static void guardarEquipos(List<Equipo> equipos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EQUIPOS_FILE))) {
            oos.writeObject(equipos);
            System.out.println("Datos de equipos guardados correctamente.");
        } catch (IOException e) {
            System.err.println("Error guardando equipos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Equipo> cargarEquipos() {
        File file = new File(EQUIPOS_FILE);
        if (!file.exists()) {
            System.out.println("No se encontró archivo de equipos. Se creará uno nuevo.");
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Equipo>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando equipos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void guardarMercado(List<Persona> mercado) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(MERCADO_FILE))) {
            for (Persona persona : mercado) {
                if (persona instanceof Jugador j) {
                    pw.printf("J;%s;%s;%s;%.1f;%.1f;%d;%s;%.1f%n",
                            j.getNombre(), j.getApellido(),
                            j.getFechaNacimiento(),
                            j.getMotivacion(), j.getSueldo(),
                            j.getDorsal(), j.getPosicion(), j.getCalidad());
                } else if (persona instanceof Entrenador e) {
                    pw.printf("E;%s;%s;%s;%.1f;%.1f;%d;%b%n",
                            e.getNombre(), e.getApellido(),
                            e.getFechaNacimiento(),
                            e.getMotivacion(), e.getSueldo(),
                            e.getTorneosGanados(), e.isSeleccionadorNacional());
                }
            }
            System.out.println("Mercado de fichajes actualizado correctamente.");
        } catch (IOException e) {
            System.err.println("Error guardando mercado: " + e.getMessage());
        }
    }
}