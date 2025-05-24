package main.java.services;

import main.java.domain.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Clase encargada de gestionar toda la lógica de los menús interactivos en consola
 * para la gestión de equipos, jugadores, entrenadores y torneos de la aplicación.
 * <p>
 * Proporciona métodos y menús que permiten realizar operaciones como:
 * <ul>
 *     <li>Consultar datos de equipos y jugadores.</li>
 *     <li>Registrar nuevos equipos o personas en el mercado.</li>
 *     <li>Configurar y disputar ligas.</li>
 *     <li>Realizar entrenamientos, transferencias y otras funciones relacionadas con los equipos.</li>
 * </ul>
 * </p>
 */
public class MenuManager {
    private final Scanner scanner;
    private final List<Equipo> equipos;
    private final List<Persona> mercado;
    private Liga ligaActual;

    /**
     * Constructor que inicializa el gestor del menú con las listas de equipos y mercado de fichajes.
     *
     * @param equipos Lista de equipos creados en la aplicación.
     * @param mercado Lista de personas (jugadores y entrenadores) disponibles en el mercado.
     */
    public MenuManager(List<Equipo> equipos, List<Persona> mercado) {
        this.scanner = new Scanner(System.in);
        this.equipos = equipos;
        this.mercado = mercado;
    }

    /**
     * Muestra el menú principal de la aplicación y permite al usuario navegar entre las opciones
     * disponibles en un bucle interactivo.
     * <p>
     * Incluye acciones como gestionar equipos, consultar datos, disputar ligas, y otras opciones
     * relacionadas con los equipos y el mercado.
     * </p>
     */
    public void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\nWelcome to Politècnics Football Manager:");
            System.out.println("1- Veure classificació lliga actual 🏆");
            System.out.println("2- Gestionar equip ⚽");
            System.out.println("3- Donar d'alta equip");
            System.out.println("4- Donar d'alta jugador/a o entrenador/a");
            System.out.println("5- Consultar dades equip");
            System.out.println("6- Consultar dades jugador/a equip");
            System.out.println("7- Disputar nova lliga");
            System.out.println("8- Realitzar sessió entrenament (mercat fitxatges)");
            System.out.println("9- Transferir jugador/a");
            System.out.println("10- Desar dades equips");
            System.out.println("0- Sortir");
            System.out.print("Selecciona una opció: ");

            opcion = InputHelper.leerEntero(scanner, 0, 10);

            switch (opcion) {
                case 1 -> mostrarClasificacion();
                case 2 -> gestionarEquipo();
                case 3 -> darAltaEquipo();
                case 4 -> darAltaPersona();
                case 5 -> consultarDatosEquipo();
                case 6 -> consultarDatosJugador();
                case 7 -> disputarNuevaLiga();
                case 8 -> realizarEntrenamientoMercado();
                case 9 -> transferirJugador();
                case 10 -> guardarDatos();
                case 0 -> System.out.println("Sortint de l'aplicació...");
                default -> System.out.println("Opció no vàlida");
            }
        } while (opcion != 0);
    }

    /**
     * Muestra la clasificación de la liga actual si esta fue configurada previamente.
     * De lo contrario, informa al usuario de que no existe ninguna liga activa.
     */
    private void mostrarClasificacion() {
        if (ligaActual == null) {
            System.out.println("No hi ha cap lliga en curs");
            return;
        }
        ligaActual.mostrarClasificacion();
    }

    /**
     * Permite al usuario seleccionar un equipo de la lista de equipos existentes.
     *
     * @return El equipo seleccionado o {@code null} si el usuario cancela la acción.
     */
    private Equipo seleccionarEquipo() {
        if (equipos.isEmpty()) {
            System.out.println("No hi ha equips disponibles.");
            return null;
        }

        System.out.println("\nSelecciona un equip:");
        for (int i = 0; i < equipos.size(); i++) {
            System.out.printf("%d- %s%n", i + 1, equipos.get(i).getNombre());
        }
        System.out.println("0- Tornar");

        System.out.print("Selecciona una opció: ");
        int opcion = InputHelper.leerEntero(scanner, 0, equipos.size());

        return opcion == 0 ? null : equipos.get(opcion - 1);
    }

    /**
     * Permite registrar (dar de alta) un nuevo equipo solicitando al usuario los datos necesarios.
     * <p>
     * Valida que el nuevo nombre del equipo no exista previamente en la lista de equipos.
     * </p>
     */
    private void darAltaEquipo() {
        System.out.println("\nDonar d'alta nou equip:");

        String nombre;
        while (true) {
            System.out.print("Nom de l'equip: ");
            nombre = scanner.nextLine();

            String finalNombre = nombre;
            if (equipos.stream().anyMatch(e -> e.getNombre().equalsIgnoreCase(finalNombre))) {
                System.out.println("Aquest nom d'equip ja existeix. Tria un altre.");
            } else {
                break;
            }
        }

        System.out.print("Any de fundació: ");
        int anioFundacion = InputHelper.leerEntero(scanner, 1800, LocalDate.now().getYear());

        System.out.print("Ciutat: ");
        String ciudad = scanner.nextLine();

        System.out.print("Vols afegir nom de l'estadi? (S/N): ");
        String estadio = scanner.nextLine().equalsIgnoreCase("S") ?
                InputHelper.leerStringNoVacio(scanner, "Nom de l'estadi: ") : null;

        System.out.print("Vols afegir nom del president/a? (S/N): ");
        String presidente = scanner.nextLine().equalsIgnoreCase("S") ?
                InputHelper.leerStringNoVacio(scanner, "Nom del president/a: ") : null;

        Equipo nuevoEquipo;
        if (estadio != null && presidente != null) {
            nuevoEquipo = new Equipo(nombre, anioFundacion, ciudad, estadio, presidente);
        } else if (estadio != null) {
            nuevoEquipo = new Equipo(nombre, anioFundacion, ciudad, estadio, null);
        } else if (presidente != null) {
            nuevoEquipo = new Equipo(nombre, anioFundacion, ciudad, null, presidente);
        } else {
            nuevoEquipo = new Equipo(nombre, anioFundacion, ciudad);
        }

        equipos.add(nuevoEquipo);
        System.out.printf("Equip %s afegit correctament.%n", nombre);
    }

    /**
     * Permite registrar a una nueva persona en el mercado, como un jugador o entrenador.
     * <p>
     * Muestra un submenú para seleccionar el tipo de alta (jugador o entrenador).
     * </p>
     */
    private void darAltaPersona() {
        System.out.println("\nDonar d'alta:");
        System.out.println("1- Jugador/a");
        System.out.println("2- Entrenador/a");
        System.out.println("0- Tornar");
        System.out.print("Selecciona una opció: ");

        int opcion = InputHelper.leerEntero(scanner, 0, 2);

        switch (opcion) {
            case 1 -> altaJugador();
            case 2 -> altaEntrenador();
            case 0 -> {}
        }
    }

    /**
     * Permite registrar un nuevo jugador, generando atributos adicionales como sueldo,
     * motivación, calidad y posición, además de los datos proporcionados por el usuario.
     * <p>
     * El nuevo jugador es añadido al mercado de fichajes.
     * </p>
     */
    private void altaJugador() {
        System.out.println("\nAlta de nou jugador/a:");

        System.out.print("Nom: ");
        String nombre = scanner.nextLine();

        System.out.print("Cognom: ");
        String apellido = scanner.nextLine();

        String fechaNacimiento = InputHelper.leerFecha(scanner, "Data de naixement (dd/mm/aaaa): ");

        System.out.print("Dorsal: ");
        int dorsal = InputHelper.leerEntero(scanner, 1, 99);

        double sueldo = 100000 + new Random().nextInt(900000);
        double motivacion = 5;
        String posicion = Jugador.POSICIONES[new Random().nextInt(Jugador.POSICIONES.length)];
        double calidad = 30 + new Random().nextInt(71);

        Jugador nuevoJugador = new Jugador(nombre, apellido, fechaNacimiento,
                sueldo, motivacion, dorsal, posicion, calidad);

        mercado.add(nuevoJugador);
        System.out.printf("%s %s afegit/da al mercat de fitxatges amb dorsal %d%n",
                nombre, apellido, dorsal);

        FileManager.guardarMercado(mercado);
    }

    /**
     * Permite registrar un nuevo entrenador, generando atributos adicionales como sueldo,
     * motivación y torneos ganados, basado en entradas del usuario.
     * <p>
     * El nuevo entrenador es añadido al mercado de fichajes.
     * </p>
     */
    private void altaEntrenador() {
        System.out.println("\nAlta de nou entrenador/a:");

        System.out.print("Nom: ");
        String nombre = scanner.nextLine();

        System.out.print("Cognom: ");
        String apellido = scanner.nextLine();

        String fechaNacimiento = InputHelper.leerFecha(scanner, "Data de naixement (dd/mm/aaaa): ");

        double sueldo = 200000 + new Random().nextInt(800000);
        double motivacion = 5;
        int torneosGanados = 0;
        boolean seleccionadorNacional = false;

        Entrenador nuevoEntrenador = new Entrenador(nombre, apellido, fechaNacimiento,
                sueldo, motivacion, torneosGanados,
                seleccionadorNacional);

        mercado.add(nuevoEntrenador);
        System.out.printf("%s %s afegit/da al mercat de fitxatges com a entrenador/a%n",
                nombre, apellido);

        FileManager.guardarMercado(mercado);
    }

    /**
     * Consulta y muestra los datos de un equipo específico, incluyendo su nombre,
     * año de fundación, ciudad, estadio, presidente, entrenador y jugadores.
     */
    private void consultarDatosEquipo() {
        Equipo equipo = seleccionarEquipo();
        if (equipo == null) return;

        System.out.println("\nDades de l'equip " + equipo.getNombre() + ":");
        System.out.println("Any de fundació: " + equipo.getAnioFundacion());
        System.out.println("Ciutat: " + equipo.getCiudad());
        if (equipo.getNombreEstadio() != null) {
            System.out.println("Estadi: " + equipo.getNombreEstadio());
        }
        if (equipo.getNombrePresidente() != null) {
            System.out.println("President/a: " + equipo.getNombrePresidente());
        }

        System.out.println("\nEntrenador:");
        if (equipo.getEntrenador() != null) {
            System.out.println(equipo.getEntrenador());
        } else {
            System.out.println("Sense entrenador assignat");
        }

        System.out.println("\nJugadors/es (" + equipo.getJugadores().size() + "):");
        equipo.getJugadores().forEach(j -> System.out.println("  " + j));
    }

    /**
     * Consulta y muestra los datos de un jugador en un equipo, identificándolo mediante
     * su nombre y dorsal, que son ingresados por el usuario.
     */
    private void consultarDatosJugador() {
        Equipo equipo = seleccionarEquipo();
        if (equipo == null) return;

        if (equipo.getJugadores().isEmpty()) {
            System.out.println("Aquest equip no té jugadors/es.");
            return;
        }

        System.out.println("\nJugadors/es de " + equipo.getNombre() + ":");
        equipo.getJugadores().forEach(j ->
                System.out.printf("  %s %s (Dorsal: %d)%n",
                        j.getNombre(), j.getApellido(), j.getDorsal()));

        System.out.print("Introdueix el nom del jugador/a: ");
        String nombre = scanner.nextLine();

        System.out.print("Introdueix el dorsal del jugador/a: ");
        int dorsal = InputHelper.leerEntero(scanner, 1, 99);

        Jugador jugador = equipo.buscarJugador(nombre, dorsal);
        if (jugador != null) {
            System.out.println("\nDades del jugador/a:");
            System.out.println(jugador);
        } else {
            System.out.println("No s'ha trobat el jugador/a amb aquest nom i dorsal.");
        }
    }

    /**
     * Configura una nueva liga solicitando al usuario los datos necesarios, como el nombre
     * de la liga y los equipos participantes.
     * <p>
     * Una vez configurada, se disputan los partidos de la liga.
     * </p>
     */
    private void disputarNuevaLiga() {
        System.out.println("\nCrear nova lliga:");

        System.out.print("Nom de la lliga: ");
        String nombreLiga = InputHelper.leerStringNoVacio(scanner, "Nom: ");

        System.out.print("Nombre d'equips participants (mínim 2): ");
        int numEquipos = InputHelper.leerEntero(scanner, 2, equipos.size());

        if (equipos.size() < numEquipos) {
            System.out.println("No hi ha prou equips creats.");
            return;
        }

        Liga nuevaLiga = new Liga(nombreLiga);
        Set<Equipo> equiposSeleccionados = new HashSet<>();

        System.out.println("Selecciona els equips participants:");
        for (int i = 0; i < numEquipos; i++) {
            Equipo equipo = seleccionarEquipo();
            if (equipo == null) {
                System.out.println("Operació cancel·lada.");
                return;
            }

            if (equiposSeleccionados.contains(equipo)) {
                System.out.println("Aquest equip ja està seleccionat.");
                i--;
                continue;
            }

            if (nuevaLiga.agregarEquipo(equipo)) {
                equiposSeleccionados.add(equipo);
                System.out.printf("Equip %s afegit a la lliga.%n", equipo.getNombre());
            } else {
                System.out.println("Error en afegir l'equip.");
                i--;
            }
        }

        System.out.println("\nDisputant partits de la lliga...");
        nuevaLiga.disputarLiga();
        ligaActual = nuevaLiga;

        System.out.println("\nLliga disputada amb èxit!");
        ligaActual.mostrarClasificacion();
    }

    /**
     * Realiza una sesión de entrenamiento para todas las personas en el mercado de fichajes,
     * permitiendo así mejorar estadísticas de jugadores y entrenadores.
     */
    private void realizarEntrenamientoMercado() {
        System.out.println("\nRealitzant sessió d'entrenament al mercat de fitxatges...");

        for (Persona persona : mercado) {
            persona.entrenamiento();

            if (persona instanceof Jugador) {
                ((Jugador) persona).canviDePosicio();
            } else if (persona instanceof Entrenador) {
                ((Entrenador) persona).incrementarSou();
            }
        }

        System.out.println("Sessió d'entrenament completada per a tots els jugadors/es i entrenadors/es del mercat.");

        FileManager.guardarMercado(mercado);
    }

    /**
     * Permite transferir un jugador de un equipo a otro, asegurándose de que no se genere
     * un conflicto en los dorsales del equipo destino.
     */
    private void transferirJugador() {
        System.out.println("\nTransferir jugador/a:");

        Equipo equipoOrigen = seleccionarEquipo();
        if (equipoOrigen == null) return;

        List<Jugador> jugadoresOrigen = equipoOrigen.getJugadores();
        if (jugadoresOrigen.isEmpty()) {
            System.out.println("Aquest equip no té jugadors/es.");
            return;
        }

        System.out.println("\nJugadors/es de " + equipoOrigen.getNombre() + ":");
        for (int i = 0; i < jugadoresOrigen.size(); i++) {
            Jugador j = jugadoresOrigen.get(i);
            System.out.printf("%d- %s %s (Dorsal: %d)%n",
                    i + 1, j.getNombre(), j.getApellido(), j.getDorsal());
        }

        System.out.print("Selecciona el jugador/a a transferir: ");
        int opcion = InputHelper.leerEntero(scanner, 1, jugadoresOrigen.size());
        Jugador jugador = jugadoresOrigen.get(opcion - 1);

        System.out.println("\nSelecciona l'equip de destí:");
        Equipo equipoDestino = seleccionarEquipo();
        if (equipoDestino == null || equipoDestino.equals(equipoOrigen)) {
            System.out.println("Operació cancel·lada.");
            return;
        }

        int nuevoDorsal = jugador.getDorsal();
        int finalNuevoDorsal = nuevoDorsal;
        if (equipoDestino.getJugadores().stream().anyMatch(j -> j.getDorsal() == finalNuevoDorsal)) {
            System.out.printf("El dorsal %d ja està ocupat a %s.%n",
                    nuevoDorsal, equipoDestino.getNombre());
            System.out.print("Introdueix un nou dorsal: ");
            nuevoDorsal = InputHelper.leerEntero(scanner, 1, 99);

            while (equipoDestino.getJugadores().stream().anyMatch(j -> j.getDorsal() == finalNuevoDorsal)) {
                System.out.print("Aquest dorsal també està ocupat. Tria un altre: ");
                nuevoDorsal = InputHelper.leerEntero(scanner, 1, 99);
            }
        }

        equipoOrigen.eliminarJugador(jugador.getNombre(), jugador.getDorsal());
        jugador.setDorsal(nuevoDorsal);
        equipoDestino.agregarJugador(jugador);

        System.out.printf("%s %s transferit/da de %s a %s amb dorsal %d%n",
                jugador.getNombre(), jugador.getApellido(),
                equipoOrigen.getNombre(), equipoDestino.getNombre(),
                nuevoDorsal);
    }

    /**
     * Guarda los datos actuales de los equipos y el mercado en los archivos correspondientes
     * utilizando la clase {@link FileManager}.
     */
    private void guardarDatos() {
        FileManager.guardarEquipos(equipos);
        FileManager.guardarMercado(mercado);
        System.out.println("Dades guardades correctament.");
    }

    /**
     * Gestiona las acciones de un equipo seleccionado ofreciendo opciones como modificar presidente,
     * destituir entrenador o realizar fichajes.
     */
    private void gestionarEquipo() {
        Equipo equipo = seleccionarEquipo();
        if (equipo == null) return;

        int opcion;
        do {
            System.out.println("\nTeam Manager: " + equipo.getNombre());
            System.out.println("1- Donar de baixa equip");
            System.out.println("2- Modificar president/a");
            System.out.println("3- Destituir entrenador/a");
            System.out.println("4- Fitxar jugador/a o entrenador/a");
            System.out.println("0- Sortir");
            System.out.print("Selecciona una opció: ");

            opcion = InputHelper.leerEntero(scanner, 0, 4);

            switch (opcion) {
                case 1 -> darBajaEquipo(equipo);
                case 2 -> modificarPresidente(equipo);
                case 3 -> destituirEntrenador(equipo);
                case 4 -> fitxarPersona(equipo);
                case 0 -> System.out.println("Tornant al menú principal...");
            }
        } while (opcion != 0);
    }

    /**
     * Permite eliminar permanentemente un equipo de la lista de equipos en memoria.
     *
     * @param equipo El equipo que se desea eliminar.
     */
    private void darBajaEquipo(Equipo equipo) {
        System.out.print("Estàs segur que vols donar de baixa l'equip " + equipo.getNombre() + "? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            equipos.remove(equipo);
            System.out.println("Equip " + equipo.getNombre() + " donat de baixa.");
        } else {
            System.out.println("Operació cancel·lada.");
        }
    }

    /**
     * Actualiza el nombre del presidente de un equipo o elimina la información del presidente
     * si el usuario así lo decide.
     *
     * @param equipo El equipo cuyos datos del presidente serán actualizados.
     */
    private void modificarPresidente(Equipo equipo) {
        String actual = equipo.getNombrePresidente();
        if (actual != null) {
            System.out.println("President/a actual: " + actual);
            System.out.print("Vols mantenir el mateix president/a? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                System.out.println("No s'ha realitzat cap canvi.");
                return;
            }
        }

        System.out.print("Nou nom del president/a (deixa en blanc per eliminar): ");
        String nuevoPresidente = scanner.nextLine().trim();

        equipo.setNombrePresidente(nuevoPresidente.isEmpty() ? null : nuevoPresidente);
        System.out.println("President/a actualitzat/da correctament.");
    }

    /**
     * Destituye al entrenador asignado a un equipo y lo transfiere al mercado de fichajes.
     *
     * @param equipo El equipo que pierde a su entrenador.
     */
    private void destituirEntrenador(Equipo equipo) {
        if (equipo.getEntrenador() == null) {
            System.out.println("Aquest equip no té entrenador/a.");
            return;
        }

        System.out.print("Estàs segur que vols destituir a " +
                equipo.getEntrenador().getNombre() + "? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            Entrenador entrenadorDestituido = equipo.getEntrenador();
            equipo.setEntrenador(null);
            mercado.add(entrenadorDestituido);
            System.out.println(entrenadorDestituido.getNombre() + " destituït/da i afegit/da al mercat.");
        } else {
            System.out.println("Operació cancel·lada.");
        }
    }

    /**
     * Permite fichar, ya sea un jugador o entrenador, del mercado al equipo seleccionado.
     *
     * @param equipo El equipo que realizará los fichajes.
     */
    private void fitxarPersona(Equipo equipo) {
        System.out.println("\nFitxar:");
        System.out.println("1- Jugador/a");
        System.out.println("2- Entrenador/a");
        System.out.println("0- Tornar");
        System.out.print("Selecciona una opció: ");

        int opcion = InputHelper.leerEntero(scanner, 0, 2);

        switch (opcion) {
            case 1 -> fitxarJugador(equipo);
            case 2 -> fitxarEntrenador(equipo);
            case 0 -> {}
        }
    }

    /**
     * Permite fichar a un jugador disponible en el mercado, asegurándose de que el
     * dorsal no esté duplicado en el equipo destino.
     *
     * @param equipo El equipo que está realizando el fichaje.
     */
    private void fitxarJugador(Equipo equipo) {
        List<Jugador> jugadoresMercado = mercado.stream()
                .filter(p -> p instanceof Jugador)
                .map(p -> (Jugador) p)
                .sorted()
                .toList();

        if (jugadoresMercado.isEmpty()) {
            System.out.println("No hi ha jugadors/es disponibles al mercat.");
            return;
        }

        System.out.println("\nJugadors/es disponibles al mercat:");
        for (int i = 0; i < jugadoresMercado.size(); i++) {
            Jugador j = jugadoresMercado.get(i);
            System.out.printf("%d- %s %s (Pos: %s, Cal: %.1f, Dorsal: %d)%n",
                    i + 1, j.getNombre(), j.getApellido(),
                    j.getPosicion(), j.getCalidad(), j.getDorsal());
        }
        System.out.println("0- Tornar");

        System.out.print("Selecciona un jugador/a per fitxar: ");
        int opcion = InputHelper.leerEntero(scanner, 0, jugadoresMercado.size());
        if (opcion == 0) return;

        Jugador jugador = jugadoresMercado.get(opcion - 1);

        int dorsal = jugador.getDorsal();
        int finalDorsal = dorsal;
        if (equipo.getJugadores().stream().anyMatch(j -> j.getDorsal() == finalDorsal)) {
            System.out.print("Aquest dorsal ja està ocupat. Introdueix un nou dorsal: ");
            dorsal = InputHelper.leerEntero(scanner, 1, 99);
        }

        jugador.setDorsal(dorsal);
        equipo.agregarJugador(jugador);
        mercado.remove(jugador);
        System.out.printf("%s %s fitxat/da per %s amb dorsal %d%n",
                jugador.getNombre(), jugador.getApellido(),
                equipo.getNombre(), dorsal);
    }

    /**
     * Permite fichar a un entrenador disponible en el mercado para el equipo seleccionado.
     * Si el equipo ya tiene un entrenador, se le da la opción de sustituirlo.
     *
     * @param equipo El equipo que está realizando el fichaje.
     */
    private void fitxarEntrenador(Equipo equipo) {
        List<Entrenador> entrenadoresMercado = mercado.stream()
                .filter(p -> p instanceof Entrenador)
                .map(p -> (Entrenador) p)
                .toList();

        if (entrenadoresMercado.isEmpty()) {
            System.out.println("No hi ha entrenadors/es disponibles al mercat.");
            return;
        }

        System.out.println("\nEntrenadors/es disponibles al mercat:");
        for (int i = 0; i < entrenadoresMercado.size(); i++) {
            Entrenador e = entrenadoresMercado.get(i);
            System.out.printf("%d- %s %s (Torneus: %d, %s)%n",
                    i + 1, e.getNombre(), e.getApellido(),
                    e.getTorneosGanados(),
                    e.isSeleccionadorNacional() ? "Seleccionador" : "No seleccionador");
        }
        System.out.println("0- Tornar");

        System.out.print("Selecciona un entrenador/a per fitxar: ");
        int opcion = InputHelper.leerEntero(scanner, 0, entrenadoresMercado.size());
        if (opcion == 0) return;

        Entrenador entrenador = entrenadoresMercado.get(opcion - 1);

        if (equipo.getEntrenador() != null) {
            System.out.print("L'equip ja té un entrenador. Vols substituir-lo? (S/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("S")) {
                System.out.println("Operació cancel·lada.");
                return;
            }
            mercado.add(equipo.getEntrenador());
        }

        equipo.setEntrenador(entrenador);
        mercado.remove(entrenador);
        System.out.printf("%s %s fitxat/da com a entrenador/a de %s%n",
                entrenador.getNombre(), entrenador.getApellido(),
                equipo.getNombre());
    }
}