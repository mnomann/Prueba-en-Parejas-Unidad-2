package com.example.demo.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.Model.Usuario;

@Service
public class CsvService {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Map<String, Usuario> cargarArchivos() throws Exception {
        cargarCSV("dataset1.csv");
        cargarCSV("dataset2.csv");
        construirSeguidores();
        return usuarios;
    }

    public void cargarCSV(String nombre) throws Exception {

        var inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(nombre);

        if (inputStream == null) {
            System.out.println("ERROR: No se encontró el archivo " + nombre);
            return;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String linea;
        boolean skip = true;
        List<String[]> filas = new ArrayList<>();

        while ((linea = br.readLine()) != null) {
            if (skip) { skip = false; continue; }
            if (linea.trim().isEmpty()) continue;

            linea = linea.replace("\"", "");

            String[] columnas = linea.split(";", -1);

            filas.add(columnas);
        }

        for (String[] p : filas) {

            if (p.length < 4) continue; 

            String id = p[0].trim();
            String correo = p[1].trim().toLowerCase();
            String fechaStr = p[2].trim();
            String siguiendoStr = p[3].trim();

            usuarios.putIfAbsent(correo, new Usuario(correo));

            if (!fechaStr.isEmpty()) {
                try {
                    LocalDate f = LocalDate.parse(fechaStr, DATE_FORMAT);

                    Usuario u = usuarios.get(correo);
                    if (u.getUltimaConexion() == null || f.isAfter(u.getUltimaConexion())) {
                        u.setUltimaConexion(f);
                    }

                } catch (Exception e) {
                    System.out.println("⚠ Fecha inválida \"" + fechaStr + "\" en " + correo);
                }
            }
        }

        // segunda pasada: construir mapa id -> correo
        Map<String, String> idMap = new HashMap<>();
        for (String[] p : filas) {
            if (p.length >= 2) {
                idMap.put(p[0].trim(), p[1].trim().toLowerCase());
            }
        }

        for (String[] p : filas) {

            if (p.length < 4) continue;

            String correo = p[1].trim().toLowerCase();
            String siguiendoStr = p[3].trim();

            if (siguiendoStr.isEmpty()) continue;

        
            String[] ids = siguiendoStr.split(",");

            for (String id : ids) {
                id = id.trim();

                if (id.isEmpty()) continue;

                String targetCorreo = idMap.get(id);
                if (targetCorreo != null && usuarios.containsKey(targetCorreo)) {
                    usuarios.get(correo).getSiguiendo().add(targetCorreo);
                }
            }
        }
    }

    private void construirSeguidores() {
        for (Usuario u : usuarios.values()) {
            for (String seguido : u.getSiguiendo()) {
                usuarios.get(seguido).getSeguidores().add(u.getCorreo());
            }
        }
    }
}
