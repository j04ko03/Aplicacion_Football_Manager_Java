package main.java.services;

import java.util.Scanner;

/**
 * Clase utilitaria para la gestión de entradas por consola.
 * <p>
 * Esta clase proporciona métodos estáticos para leer y validar diferentes tipos de datos ingresados
 * por el usuario, como números enteros dentro de un rango especificado, fechas en formato
 * "dd/mm/aaaa" y cadenas de texto no vacías.
 * <p>
 * Los mensajes de error y solicitud de entrada se muestran en catalán.
 * </p>
 */
public class InputHelper {

    /**
     * Lee un número entero introducido por el usuario desde la consola, validando que
     * esté dentro de un rango específico [min, max].
     * <p>
     * Este método solicita al usuario un número entero de manera repetitiva hasta que introduzca
     * un dato válido que cumpla con las restricciones. Si el valor introducido no es un número entero
     * o se encuentra fuera del rango permitido, se muestra un mensaje de error.
     * </p>
     *
     * @param scanner La instancia de {@link Scanner} utilizada para leer la entrada del usuario.
     * @param min     El límite inferior permitido (inclusive).
     * @param max     El límite superior permitido (inclusive).
     * @return El número entero validado introducido por el usuario.
     * @throws NumberFormatException Si se produce un error al leer un valor no convertible a entero.
     */
    public static int leerEntero(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.printf("Introdueix un valor entre %d i %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("Valor no vàlid. Introdueix un número: ");
            }
        }
    }

    /**
     * Lee una fecha ingresada por el usuario a través de la consola, validando que coincida
     * con el formato "dd/mm/aaaa".
     * <p>
     * El método sigue el siguiente flujo:
     * <ul>
     *     <li>Muestra un mensaje al usuario solicitando el ingreso de una fecha.</li>
     *     <li>Valida que la fecha introducida cumpla con el formato correcto utilizando
     *     el método auxiliar {@link #validarFormatoFecha(String)}.</li>
     *     <li>Repite la solicitud de entrada mientras el usuario no ingrese una fecha válida.</li>
     * </ul>
     * <p>
     * Es importante notar que este método valida solo el formato de la fecha, pero no comprueba que
     * sea una fecha lógica (por ejemplo, no detectará "30/02/2000" como inválida).
     * </p>
     *
     * @param scanner La instancia de {@link Scanner} utilizada para leer la entrada del usuario.
     * @param mensaje El mensaje mostrado al usuario para solicitarle el ingreso de la fecha.
     * @return Una cadena que representa la fecha validada en formato "dd/mm/aaaa".
     */
    public static String leerFecha(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        while (true) {
            String input = scanner.nextLine();
            if (validarFormatoFecha(input)) {
                return input;
            }
            System.out.print("Formato inválido (dd/mm/aaaa). Reintente: ");
        }
    }

    /**
     * Valida si una cadena de texto coincide con el formato de fecha "dd/mm/aaaa".
     * <p>
     * Este método utiliza una expresión regular para comprobar si la cadena:
     * <ul>
     *     <li>Contiene dos dígitos seguidos de una barra inclinada (/).</li>
     *     <li>Incluye otros dos dígitos, otra barra (/) y finalmente, cuatro dígitos.</li>
     * </ul>
     * Esta validación es puramente sintáctica y no incluye el análisis lógico
     * de la fecha (por ejemplo, si "30/02/2000" es válida o no).
     * </p>
     *
     * @param fecha La cadena de texto a validar.
     * @return {@code true} si la cadena sigue el formato "dd/mm/aaaa",
     *         {@code false} en caso contrario.
     */
    private static boolean validarFormatoFecha(String fecha) {
        return fecha.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    /**
     * Lee una cadena de texto no vacía introducida por el usuario desde la consola.
     * <p>
     * El método solicita de manera repetitiva al usuario que introduzca un texto hasta que se
     * proporcione una entrada válida (es decir, una cadena no vacía ni compuesta únicamente
     * de espacios en blanco).
     * </p>
     *
     * @param scanner La instancia de {@link Scanner} utilizada para leer la entrada del usuario.
     * @param mensaje El mensaje mostrado al usuario para solicitar el ingreso de texto.
     * @return Una cadena de texto no vacía ingresada por el usuario, con los espacios
     *         en blanco iniciales y finales eliminados (mediante {@link String#trim()}).
     */
    public static String leerStringNoVacio(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.print("El camp no pot estar buit. Torna a intentar: ");
        }
    }
}