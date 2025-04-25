package es.ies.puerto.model;

public class UsuarioEstadisticasEntity {
    private String dificultad;
    private int victoriasNormal;
    private int mejorTiempoNormal;
    private int victoriasContrareloj;

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public int getVictoriasNormal() {
        return victoriasNormal;
    }

    public void setVictoriasNormal(int victoriasNormal) {
        this.victoriasNormal = victoriasNormal;
    }

    public int getMejorTiempoNormal() {
        return mejorTiempoNormal;
    }

    public void setMejorTiempoNormal(int mejorTiempoNormal) {
        this.mejorTiempoNormal = mejorTiempoNormal;
    }

    public int getVictoriasContrareloj() {
        return victoriasContrareloj;
    }

    public void setVictoriasContrareloj(int victoriasContrareloj) {
        this.victoriasContrareloj = victoriasContrareloj;
    }


    @Override
    public String toString() {
        return "{" +
            " dificultad='" + getDificultad() + "'" +
            ", victoriasNormal='" + getVictoriasNormal() + "'" +
            ", mejorTiempoNormal='" + getMejorTiempoNormal() + "'" +
            ", victoriasContrareloj='" + getVictoriasContrareloj() + "'" +
            "}";
    }

}
