package modelo;


public class Ortodoncista extends Odontologo  {
    private String tipoBrackets;

    public Ortodoncista() {
    }

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

    public String getTipoBrackets() {
        return tipoBrackets;
    }

    public void setTipoBrackets(String tipoBrackets) {
        this.tipoBrackets = tipoBrackets;
    }

    @Override
    public String toString() {
        return "Ortodoncista{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", matricula='" + getMatricula() + '\'' +
                ", salarioBase=" + getSalarioBase() +
                ", tipoBrackets='" + tipoBrackets + '\'' +
                '}';
    }
}
