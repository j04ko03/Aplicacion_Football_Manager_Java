package main;

import main.java.domain.*;
import main.java.services.*;
import java.util.*;
/**
 * Clase principal de la aplicación Football Manager.
 * <p>
 * Se encarga de iniciar la carga de los datos de equipos y personas del mercado,
 * mostrar estadísticas iniciales, gestionar el ciclo principal del menú interactivo,
 * y guardar los datos al finalizar la ejecución.
 */

public class Main {
    /**
     * Punto de entrada principal para la aplicación Football Manager.
     * <p>
     * Este método orquesta el flujo principal de la aplicación:
     * <ol>
     *     <li>Carga los datos existentes de {@code Persona} (mercado) y {@code Equipo} utilizando {@code FileManager}.</li>
     *     <li>Imprime en la consola estadísticas iniciales, como el número de equipos cargados,
     *     jugadores y entrenadores en el mercado, y el total de jugadores y entrenadores creados.</li>
     *     <li>Crea una instancia de {@code MenuManager} y muestra el menú principal para la interacción del usuario.</li>
     *     <li>Una vez que el usuario sale del menú, guarda el estado actual de los equipos y el mercado
     *     de vuelta a los archivos utilizando {@code FileManager}.</li>
     * </ol>
     */

    public static void main(String[] args) {
        // Cargar datos existentes
        List<Persona> mercado = FileManager.cargarMercado();
        List<Equipo> equipos = FileManager.cargarEquipos();

        // Mostrar estadísticas iniciales
        System.out.println("\n=== Football Manager ===");
        System.out.printf("Equipos cargados: %d%n", equipos.size());
        System.out.printf("Jugadores en mercado: %d%n",
                mercado.stream().filter(p -> p instanceof Jugador).count());
        System.out.printf("Entrenadores en mercado: %d%n",
                mercado.stream().filter(p -> p instanceof Entrenador).count());
        System.out.printf("Total jugadores creados: %d%n", Jugador.getTotalJugadores());
        System.out.printf("Total entrenadores creados: %d%n", Entrenador.getTotalEntrenadores());

        // Iniciar menú principal
        MenuManager menu = new MenuManager(equipos, mercado);
        menu.mostrarMenuPrincipal();

        // Guardar datos al salir
        FileManager.guardarEquipos(equipos);
        FileManager.guardarMercado(mercado);
    }
}