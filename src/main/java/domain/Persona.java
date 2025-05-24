package main.java.domain;

import java.util.Objects;

/**
 * Clase base que representa una persona en el sistema.
 * <p>
 * Una persona tiene atributos comunes como nombre, apellido, fecha de nacimiento, sueldo,
 * y motivación. Esta clase sirve como superclase para otras entidades como jugadores y entrenadores.
 */
public abstract class Persona {
    protected String nombre;
    protected String apellido;
    protected final String fechaNacimiento;
    private double sueldo;
    protected double motivacion;

    /**
     * Constructor para inicializar los atributos comunes de una persona.
     *
     * @param nombre          El nombre de la persona. No puede ser nulo ni vacío.
     * @param apellido        El apellido de la persona. No puede ser nulo ni vacío.
     * @param fechaNacimiento La fecha de nacimiento de la persona (formato de cadena, dd/MM/yyyy). No puede ser nulo.
     * @param sueldo          El sueldo de la persona. Debe ser mayor o igual a 0.
     * @param motivacion      La motivación de la persona (entre 0 y 10).
     *                        Si la motivación está fuera de este rango, se ajustará automáticamente.
     * @throws IllegalArgumentException si el sueldo es negativo.
     * @throws NullPointerException     si el nombre, apellido o fecha de nacimiento son nulos.
     */
    public Persona(String nombre, String apellido, String fechaNacimiento, double sueldo, double motivacion) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo.");
        this.apellido = Objects.requireNonNull(apellido, "El apellido no puede ser nulo.");
        this.fechaNacimiento = Objects.requireNonNull(fechaNacimiento, "La fecha de nacimiento no puede ser nula.");
        setSueldo(sueldo);
        this.motivacion = ajustarRangoMotivacion(motivacion);
    }

    /**
     * Método abstracto que debe ser implementado por las clases derivadas para simular una sesión de entrenamiento.
     */
    public abstract void entrenamiento();

    /**
     * Incrementa la motivación de la persona en 0.1, hasta un máximo de 10.
     */
    public final void aumentarMotivacionBase() {
        motivacion = Math.min(10, motivacion + 0.1);
    }

    /**
     * Obtiene el nombre de la persona.
     *
     * @return El nombre de la persona.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la persona.
     *
     * @param nombre El nuevo nombre de la persona. No puede ser nulo ni vacío.
     * @throws IllegalArgumentException si el nombre es nulo o está vacío.
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido de la persona.
     *
     * @return El apellido de la persona.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido de la persona.
     *
     * @param apellido El nuevo apellido de la persona. No puede ser nulo ni vacío.
     * @throws IllegalArgumentException si el apellido es nulo o está vacío.
     */
    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser nulo o vacío.");
        }
        this.apellido = apellido;
    }

    /**
     * Obtiene la fecha de nacimiento de la persona.
     *
     * @return La fecha de nacimiento de la persona.
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Obtiene el sueldo de la persona.
     *
     * @return El sueldo de la persona.
     */
    public double getSueldo() {
        return sueldo;
    }

    /**
     * Establece el sueldo de la persona.
     *
     * @param sueldo El nuevo sueldo de la persona. Debe ser mayor o igual a 0.
     * @throws IllegalArgumentException si el sueldo es negativo.
     */
    public void setSueldo(double sueldo) {
        if (sueldo < 0) {
            throw new IllegalArgumentException("El sueldo no puede ser negativo.");
        }
        this.sueldo = sueldo;
    }

    /**
     * Obtiene el nivel de motivación de la persona.
     *
     * @return La motivación de la persona (entre 0 y 10).
     */
    public double getMotivacion() {
        return motivacion;
    }

    /**
     * Establece el nivel de motivación de la persona, ajustando el rango automáticamente entre 0 y 10.
     *
     * @param motivacion El nuevo nivel de motivación. Si está fuera del rango, se ajustará automáticamente.
     */
    public final void setMotivacion(double motivacion) {
        this.motivacion = ajustarRangoMotivacion(motivacion);
    }

    /**
     * Ajusta un valor de motivación al rango permitido (0 a 10).
     *
     * @param motivacion El nivel de motivación que se quiere ajustar.
     * @return El valor de motivación ajustado al rango permitido (0 a 10).
     */
    private double ajustarRangoMotivacion(double motivacion) {
        return Math.max(0, Math.min(10, motivacion));
    }

    /**
     * Devuelve una representación en cadena de la persona.
     * <p>
     * Incluye el nombre, apellido, fecha de nacimiento, sueldo y motivación.
     *
     * @return Una cadena que representa a la persona y sus atributos.
     */
    @Override
    public String toString() {
        return String.format("%s %s (Fecha Nac: %s, Sueldo: %.2f, Motivación: %.2f)",
                nombre, apellido, fechaNacimiento, sueldo, motivacion);
    }
}