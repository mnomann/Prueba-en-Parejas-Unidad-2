package com.example.demo.Model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Usuario {

    private String correo;
    private LocalDate ultimaConexion;
    private Set<String> siguiendo = new HashSet<>();
    private Set<String> seguidores = new HashSet<>();

    public Usuario(String correo) {
        this.correo = correo;
    }

    public String getCorreo() { return correo; }
    public LocalDate getUltimaConexion() { return ultimaConexion; }
    public void setUltimaConexion(LocalDate ultimaConexion) { this.ultimaConexion = ultimaConexion; }

    public Set<String> getSiguiendo() { return siguiendo; }
    public Set<String> getSeguidores() { return seguidores; }

    public boolean inactivo(LocalDate ref) {
        return ultimaConexion == null ||
                ultimaConexion.isBefore(ref.minusYears(4).plusDays(1));
    }

    @Override
    public String toString() {
        return correo + " | Última conexión: " + ultimaConexion +
                " | Sigue: " + siguiendo.size() +
                " | Seguidores: " + seguidores.size();
    }
}
