package es.ies.puerto.model;

import java.util.Objects;

public class UsuarioEntity {

    private String email;
    private String nombre;
    private String contrasenia;

    private int victoriasFacil;
    private int victoriasNormal;
    private int victoriasDificil;
    private Integer mejorTiempoNormal; // Puede ser null
    private int victoriasContrareloj;
    private int derrotasTotales;

    // Constructor vacío
    public UsuarioEntity() {
    }

    // Constructor básico
    public UsuarioEntity(String email, String nombre, String contrasenia) {
        this.email = email;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.victoriasFacil = 0;
        this.victoriasNormal = 0;
        this.victoriasDificil = 0;
        this.mejorTiempoNormal = null;
        this.victoriasContrareloj = 0;
        this.derrotasTotales = 0;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int getVictoriasFacil() {
        return victoriasFacil;
    }

    public void setVictoriasFacil(int victoriasFacil) {
        this.victoriasFacil = victoriasFacil;
    }

    public int getVictoriasNormal() {
        return victoriasNormal;
    }

    public void setVictoriasNormal(int victoriasNormal) {
        this.victoriasNormal = victoriasNormal;
    }

    public int getVictoriasDificil() {
        return victoriasDificil;
    }

    public void setVictoriasDificil(int victoriasDificil) {
        this.victoriasDificil = victoriasDificil;
    }

    public Integer getMejorTiempoNormal() {
        return mejorTiempoNormal;
    }

    public void setMejorTiempoNormal(Integer mejorTiempoNormal) {
        this.mejorTiempoNormal = mejorTiempoNormal;
    }

    public int getVictoriasContrareloj() {
        return victoriasContrareloj;
    }

    public void setVictoriasContrareloj(int victoriasContrareloj) {
        this.victoriasContrareloj = victoriasContrareloj;
    }

    public int getDerrotasTotales() {
        return derrotasTotales;
    }

    public void setDerrotasTotales(int derrotasTotales) {
        this.derrotasTotales = derrotasTotales;
    }

    // equals y hashCode basados en el email (clave única)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioEntity)) return false;
        UsuarioEntity that = (UsuarioEntity) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    // toString para debug
    @Override
    public String toString() {
        return "email='" + email + '\'' +
               ", nombre='" + nombre + '\'' +
               ", contrasenia='" + contrasenia + '\'';
    }
}
