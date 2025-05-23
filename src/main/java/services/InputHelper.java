package main.java.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
/**
 * Clase de utilidad para la gestión de entradas por consola.
 * <p>
 * Proporciona métodos estáticos para leer y validar diferentes tipos de información
 * introducida por el usuario, como enteros dentro de un rango, fechas con formato específico
 * y cadenas de texto no vacías.
 */

public class InputHelper {

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

    private static boolean validarFormatoFecha(String fecha) {
        return fecha.matches("\\d{2}/\\d{2}/\\d{4}");
    }

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