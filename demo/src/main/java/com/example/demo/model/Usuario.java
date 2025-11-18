package com.example.demo.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class Usuario {
    private int id;
    private String correo;
    private LocalDate ultimaConexion;
    private List<Integer> siguiendo;
    private int seguidoresCount = 0;
    private boolean inactivo = false;

    public LocalDate getUltimaConexion() {
        return ultimaConexion;
    }

    public int getSeguidoresCount() {
        return seguidoresCount;
    }
}