package modelo;


public class OdontologoGeneral extends Odontologo {
    private boolean atiendeNinios;
    private int consultorioAsignado;

    public OdontologoGeneral(boolean atiendeNinios, int consultorioAsignado) {
        this.atiendeNinios = atiendeNinios;
        this.consultorioAsignado = consultorioAsignado;
    }

    public OdontologoGeneral(Long id, String nombre, String apellido, String matricula, double salarioBase, boolean atiendeNinios, int consultorioAsignado) {
        super(id, nombre, apellido, matricula, salarioBase);
        this.atiendeNinios = atiendeNinios;
        this.consultorioAsignado = consultorioAsignado;
    }

    //Como no tiene especialidad no cobra un bonus
    @Override
    public double calcularHonorarios() {
        return getSalarioBase();
    }

    //Un odontólogo general sí podría atender una urgencia
    @Override
    public boolean atiendeUrgencias() {
        return true;
    }
}
