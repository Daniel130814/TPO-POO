package modelo;


public class OdontologoGeneral extends Odontologo {
    private boolean atiendeNinios;
    private int consultorioAsignado;

    public OdontologoGeneral() {
    }

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

    public boolean isAtiendeNinios() {
        return atiendeNinios;
    }

    public void setAtiendeNinios(boolean atiendeNinios) {
        this.atiendeNinios = atiendeNinios;
    }

    public int getConsultorioAsignado() {
        return consultorioAsignado;
    }

    public void setConsultorioAsignado(int consultorioAsignado) {
        this.consultorioAsignado = consultorioAsignado;
    }

    @Override
    public String toString() {
        return "OdontologoGeneral{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", matricula='" + getMatricula() + '\'' +
                ", salarioBase=" + getSalarioBase() +
                ", atiendeNinios=" + atiendeNinios +
                ", consultorioAsignado=" + consultorioAsignado +
                '}';
    }
}
