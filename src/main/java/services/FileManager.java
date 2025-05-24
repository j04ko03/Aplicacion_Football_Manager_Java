package main.java.services;

import main.java.domain.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Clase utilitaria para gestionar las operaciones de entrada y salida de archivos
 * relacionadas con el mercado de fichajes y los equipos.
 * <p>
 * Proporciona métodos para cargar y guardar jugadores, entrenadores y equipos desde y hacia
 * los archivos, tanto en formato de texto como binario.
 * Incluye validaciones y manejo de errores para garantizar robustez.
 * </p>
 */
public class FileManager {

    /**
     * Ruta del archivo de texto donde se almacenan los datos del mercado de fichajes.
     */
    private static final String MERCADO_FILE = "src/main/resources/mercat_fitxatges.txt";

    /**
     * Ruta del archivo binario donde se guardan los datos de los equipos.
     */
    private static final String EQUIPOS_FILE = "src/main/resources/equipos.txt";

    /**
     * Formateador de fechas para trabajar con la representación "dd/MM/yyyy".
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Carga la lista de personas (jugadores y entrenadores) desde un archivo de texto.
     * <p>
     * El archivo debe seguir un formato específico:
     * <ul>
     *     <li><strong>Jugador:</strong> J;Nombre;Apellido;FechaNacimiento;Motivacion;Sueldo;Dorsal;Posicion;Calidad</li>
     *     <li><strong>Entrenador:</strong> E;Nombre;Apellido;FechaNacimiento;Motivacion;Sueldo;TorneosGanados;Seleccionador</li>
     * </ul>
     * Las líneas que no cumplan con este formato serán ignoradas, mostrando un mensaje de error
     * correspondiente.
     * </p>
     *
     * @return Una lista de objetos {@link Persona} (jugadores y entrenadores) cargados
     *         correctamente desde el archivo. Devuelve una lista vacía si no se puede leer
     *         el archivo o si los datos están vacíos.
     */
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

    /**
     * Guarda una lista de equipos en un archivo binario mediante serialización.
     * <p>
     * Serializa la lista de objetos {@link Equipo} y los guarda en el archivo binario especificado.
     * Si ocurre un error durante la operación de escritura, se muestra un mensaje en la consola.
     * </p>
     *
     * @param equipos Lista de equipos a guardar.
     */
    public static void guardarEquipos(List<Equipo> equipos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EQUIPOS_FILE))) {
            oos.writeObject(equipos);
            System.out.println("Datos de equipos guardados correctamente.");
        } catch (IOException e) {
            System.err.println("Error guardando equipos: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de equipos desde un archivo binario mediante deserialización.
     * <p>
     * Intenta leer los datos de equipos desde el archivo binario indicado. Si el archivo no existe,
     * o si ocurre algún error durante la lectura, se devuelve una lista vacía.
     * </p>
     *
     * @return Lista de equipos cargados desde el archivo. Devuelve una lista vacía si no
     *         se encuentra el archivo o si ocurre un error.
     */
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

    /**
     * Guarda una lista de personas (jugadores y entrenadores) en un archivo de texto.
     * <p>
     * El archivo generado tiene un formato que permite ser cargado de vuelta usando
     * {@link #cargarMercado()}.
     * </p>
     *
     * @param mercado Lista de objetos {@link Persona} a guardar.
     *                Cada persona debe ser un {@link Jugador} o un {@link Entrenador}.
     */
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