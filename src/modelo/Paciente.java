package modelo;

import java.time.LocalDate;

public class Paciente {
    //Forma de los atributos: Encapsulamiento (siempre se usa en private) + Tipo de dato (En mayuscula) + el nombre del atributo (en minuscula y termina con ;)
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private LocalDate fechaIngreso;
    // Asociación: Relación 1:1 con la clase Domicilio
    private Domicilio domicilio;

    //Falta el domicilio
    // Acá se definen los atributos, las características necesarias
    // Despues aparecen los metodos, el que usamos es el constructor que es el metodo que permite la creación de los objetos
    // El metodo se llama como la clase misma (en este caso paciente)
    // la clase en java se escribe en singular y con la mayuscula
    // La clase constructor no tiene salida
    public Paciente() {
    }
    public Paciente(Long id, String nombre, String apellido, String dni, String email, LocalDate fechaIngreso, Domicilio domicilio){
        // En el parentecis se necesita los parametros, que son los que estan arriba,y se escribe tipo+nombre
        // Cuerpo del metodo
        // Con el this lo que hace es referenciar el valor y con el igual indica donde lo guarda
        // Ejemplo 1=id, Carlos=nombre, González=apellido y asi con todos. lo que hace es asignar los parametros que le dimos en los parentesis a los valores de los atributos
        // En
        this.id=id;
        this.nombre=nombre;
        this.apellido=apellido;
        this.dni=dni;
        this.email=email;
        this.fechaIngreso=fechaIngreso;
        this.domicilio = domicilio;

    }
    public Paciente(String nombre, String apellido, String dni, String email, Domicilio domicilio) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaIngreso = LocalDate.now(); // Se asigna sola al crear el objeto
        this.domicilio = domicilio;
    }

    //Todos los metodos son publicos
    // metodo getter y setter
    // getter viene de get, que es agarrar, y se usa para agarrar algo que ya está cargado para poderlo ver. Por ejemplo un nombre
    // Setter viene de set, poner, y se usa por ejemplo cuando escribo un nombre mal y lo quiero cambiar. En ese caso se usa el setter
    // para un getter lo que se necesita es: el encapsulamiento(como es un metodo va en publico), la salida (que espero que me devuelva el metodo),nombre(get+Nombre con la mayuscula del parametro)
    public String getNombre(){
        return this.nombre;
    }
    public String getApellido(){
        return this.apellido;
    }
    public Long getId(){
        return this.id;
    }
    public String getDni(){
        return this.dni;
    }
    public String getEmail(){
        return this.email;
    }

    public LocalDate getFechaIngreso() {
        return this.fechaIngreso;
    }
    public Domicilio getDomicilio() {
        return domicilio;
    }

    //Metodo Setter

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }
    //Metodo toString lo permite mostrar los datos en el main. Para que se puedan ver en un menú y sea legible

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", email='" + email + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", domicilio=" + domicilio +
                '}';
    }
}


