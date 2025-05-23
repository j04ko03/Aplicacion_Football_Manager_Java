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