package es.ies.puerto.model;

import java.util.Objects;

public class UsuarioEntity {

    private String email;
    private String nombre;
    private String contrasenia;

    private int victoriasFacil;
    private int victoriasMedio;
    private int victoriasDificil;
    private Integer mejorTiempoNormal; 
    private int victoriasContrareloj;
    private int derrotasTotales;
    private int rachaVictoria;
    private int rachaDerrota;
    private int mayorRacha;

    // Constructor vacío
    public UsuarioEntity() {
    }


    /**
     * Metodo con los parametros de registro del usuario
     * @param email email del usuario
     * @param nombre nombre del usuario
     * @param contrasenia contrasenia del usuario
     */
    public UsuarioEntity(String email, String nombre, String contrasenia) {
        this.email = email;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.victoriasFacil = 0;
        this.victoriasMedio = 0;
        this.victoriasDificil = 0;
        this.mejorTiempoNormal = null;
        this.victoriasContrareloj = 0;
        this.derrotasTotales = 0;
        this.mayorRacha = 0;
    }


    // Getters y setters

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return this.contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int getVictoriasFacil() {
        return this.victoriasFacil;
    }

    public void setVictoriasFacil(int victoriasFacil) {
        this.victoriasFacil = victoriasFacil;
    }

    public int getVictoriasMedio() {
        return this.victoriasMedio;
    }

    public void setVictoriasMedio(int victoriasMedio) {
        this.victoriasMedio = victoriasMedio;
    }

    public int getVictoriasDificil() {
        return this.victoriasDificil;
    }

    public void setVictoriasDificil(int victoriasDificil) {
        this.victoriasDificil = victoriasDificil;
    }

    public Integer getMejorTiempoNormal() {
        return this.mejorTiempoNormal;
    }

    public void setMejorTiempoNormal(Integer mejorTiempoNormal) {
        this.mejorTiempoNormal = mejorTiempoNormal;
    }

    public int getVictoriasContrareloj() {
        return this.victoriasContrareloj;
    }

    public void setVictoriasContrareloj(int victoriasContrareloj) {
        this.victoriasContrareloj = victoriasContrareloj;
    }

    public int getDerrotasTotales() {
        return this.derrotasTotales;
    }

    public void setDerrotasTotales(int derrotasTotales) {
        this.derrotasTotales = derrotasTotales;
    }

    public int getRachaVictoria() {
        return this.rachaVictoria;
    }

    public void setRachaVictoria(int rachaVictoria) {
        this.rachaVictoria = rachaVictoria;
    }

    public int getRachaDerrota() {
        return this.rachaDerrota;
    }

    public void setRachaDerrota(int rachaDerrota) {
        this.rachaDerrota = rachaDerrota;
    }

    public int getMayorRacha() {
        return this.mayorRacha;
    }

    public void setMayorRacha(int mayorRacha) {
        this.mayorRacha = mayorRacha;
    }


    /**
     * Metodo equals
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UsuarioEntity)) {
            return false;
        }
        UsuarioEntity usuarioEntity = (UsuarioEntity) o;
        return Objects.equals(email, usuarioEntity.email) && Objects.equals(nombre, usuarioEntity.nombre) && Objects.equals(contrasenia, usuarioEntity.contrasenia) && victoriasFacil == usuarioEntity.victoriasFacil && victoriasMedio == usuarioEntity.victoriasMedio && victoriasDificil == usuarioEntity.victoriasDificil && Objects.equals(mejorTiempoNormal, usuarioEntity.mejorTiempoNormal) && victoriasContrareloj == usuarioEntity.victoriasContrareloj && derrotasTotales == usuarioEntity.derrotasTotales && rachaVictoria == usuarioEntity.rachaVictoria && rachaDerrota == usuarioEntity.rachaDerrota && mayorRacha == usuarioEntity.mayorRacha;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, nombre, contrasenia, victoriasFacil, victoriasMedio, victoriasDificil, mejorTiempoNormal, victoriasContrareloj, derrotasTotales, rachaVictoria, rachaDerrota, mayorRacha);
    }

    /**
     * Metodo toString
     */
    @Override
    public String toString() {
        return "{" +
            " email='" + getEmail() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", contrasenia='" + getContrasenia() + "'" +
            ", victoriasFacil='" + getVictoriasFacil() + "'" +
            ", victoriasMedio='" + getVictoriasMedio() + "'" +
            ", victoriasDificil='" + getVictoriasDificil() + "'" +
            ", mejorTiempoNormal='" + getMejorTiempoNormal() + "'" +
            ", victoriasContrareloj='" + getVictoriasContrareloj() + "'" +
            ", derrotasTotales='" + getDerrotasTotales() + "'" +
            ", rachaVictoria='" + getRachaVictoria() + "'" +
            ", rachaDerrota='" + getRachaDerrota() + "'" +
            ", mayorRacha='" + getMayorRacha() + "'" +
            "}";
    }


    /**
     * Obtener el total de victorias
     * @return el total de victorias
     */
    public int getVictoriasTotal() {
        return getVictoriasDificil()+getVictoriasFacil()+getVictoriasMedio();
    }
}
