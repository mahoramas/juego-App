package es.ies.puerto.model;

import java.util.Objects;

public class UsuarioEntity {

    private String email;
    private String nombre;
    private String contrasenia;

    private int victoriasFacil;
    private int victoriasNormal;
    private int victoriasDificil;
    private Integer mejorTiempoNormal; 
    private int victoriasContrareloj;
    private int derrotasTotales;
    private int rachaVictoria;
    private int rachaDerrota;

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

    public int getVictoriasNormal() {
        return this.victoriasNormal;
    }

    public void setVictoriasNormal(int victoriasNormal) {
        this.victoriasNormal = victoriasNormal;
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

    public int getVictoriasTotal() {
        return getVictoriasDificil()+getVictoriasFacil()+getVictoriasNormal();
    }
}
