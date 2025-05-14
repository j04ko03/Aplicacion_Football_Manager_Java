package test.java.services;

import main.java.services.InputHelper;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class InputHelperTest {

    @Test
    public void testLeerEntero_ValorDentroDelRango() {
        Scanner scanner = new Scanner(new StringReader("5\n"));
        int resultado = InputHelper.leerEntero(scanner, 1, 10);
        assertEquals(5, resultado);
    }

    @Test
    public void testLeerEntero_ValorFueraYLuegoValido() {
        Scanner scanner = new Scanner(new StringReader("20\n3\n")); // 20 es inválido (fuera de rango), 3 es válido
        int resultado = InputHelper.leerEntero(scanner, 1, 10);
        assertEquals(3, resultado);
    }

    @Test
    public void testLeerEntero_EntradaNoNumericaYLuegoValida() {
        Scanner scanner = new Scanner(new StringReader("abc\n7\n"));
        int resultado = InputHelper.leerEntero(scanner, 1, 10);
        assertEquals(7, resultado);
    }

    @Test
    public void testLeerFecha_ConFormatoValido() {
        Scanner scanner = new Scanner(new StringReader("12/05/2024\n"));
        String resultado = InputHelper.leerFecha(scanner, "Introduce una fecha: ");
        assertEquals("12/05/2024", resultado);
    }

    @Test
    public void testLeerFecha_FormatoInvalidoYLuegoValido() {
        Scanner scanner = new Scanner(new StringReader("12-05-2024\n01/01/2025\n"));
        String resultado = InputHelper.leerFecha(scanner, "Introduce una fecha: ");
        assertEquals("01/01/2025", resultado);
    }

    @Test
    public void testLeerStringNoVacio_EntradaValida() {
        Scanner scanner = new Scanner(new StringReader("Hola mundo\n"));
        String resultado = InputHelper.leerStringNoVacio(scanner, "Introduce texto: ");
        assertEquals("Hola mundo", resultado);
    }

    @Test
    public void testLeerStringNoVacio_EntradaVaciaYLuegoValida() {
        Scanner scanner = new Scanner(new StringReader("\n    \nTexto válido\n"));
        String resultado = InputHelper.leerStringNoVacio(scanner, "Introduce texto: ");
        assertEquals("Texto válido", resultado);
    }
}
