package modelo;

import java.util.Objects;

public abstract class Odontologo {
    private Long id;//tipo long (puede ser un int)
    private String nombre;
    private String apellido;
    private String matricula;
    private double salarioBase;


    // Metodo Constructor
    public Odontologo() {
    }

    public Odontologo(Long id, String nombre, String apellido, String matricula, double salarioBase) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.matricula = matricula;
        this.salarioBase = salarioBase;
    }

    // Metodo que vamos a sobrescribir por polimorfismo
    public abstract double calcularHonorarios();

    //No todos los especialistas pueden atender urgencias
    public abstract boolean atiendeUrgencias();



    //Metodo Getter
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNombreCompleto(){return nombre+" "+apellido;}

    public double getSalarioBase() {
        return salarioBase;
    }


    //Metodo Setter


    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String toString() {
        return "Odontologo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", matricula='" + matricula + '\'' +
                ", salarioBase=" + salarioBase +
                '}';
    }

    //Hashcode y equals

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Odontologo that = (Odontologo) o;
        return Objects.equals(matricula, that.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matricula);
    }
}
