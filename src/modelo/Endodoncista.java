package modelo;


public class Endodoncista extends Odontologo {
    private boolean usaMicroscopio;

    public Endodoncista(boolean usaMicroscopio) {
        this.usaMicroscopio = usaMicroscopio;
    }

    public Endodoncista(Long id, String nombre, String apellido, String matricula, double salarioBase, boolean usaMicroscopio) {
        super(id, nombre, apellido, matricula, salarioBase);
        this.usaMicroscopio = usaMicroscopio;
    }

    @Override
    public double calcularHonorarios() {
        return getSalarioBase()*1.50; // los odontólogos que realizan endodoncias cobran un 50% más por su especialidad
    }

    @Override
    public boolean atiendeUrgencias() {
        return true;
    }
}
