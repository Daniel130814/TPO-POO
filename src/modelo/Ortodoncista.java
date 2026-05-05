package modelo;

public class Ortodoncista extends Odontologo {
    private String tipoBrackets;

    public Ortodoncista(String tipoBrackets) {
        this.tipoBrackets = tipoBrackets;
    }

    public Ortodoncista(Long id, String nombre, String apellido, String matricula, double salarioBase, String tipoBrackets) {
        super(id, nombre, apellido, matricula, salarioBase);
        this.tipoBrackets = tipoBrackets;
    }

    @Override //Los odontólogos con especialidad cobrar un 25% más por su especialidad
    public double calcularHonorarios() {
        return getSalarioBase()*1.25;
    }

    @Override
    public boolean atiendeUrgencias() {
        return false;
    }
}
