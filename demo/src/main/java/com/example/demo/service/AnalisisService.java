package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalisisService {

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final LocalDate FECHA_ACTUAL = LocalDate.of(2023, 5, 22);
    private final long DIAS_INACTIVO = 4 * 365 + 1;

    private List<Usuario> usuarios = new ArrayList<>();

    public void cargarYProcesarDatos() throws IOException {
        String data = Files.readString(Paths.get("demo\\src\\main\\resources\\dataset2.csv"));
        String[] lineas = data.split("\n");

        for (int i = 1; i < lineas.length; i++) {
            try {
                Usuario usuario = parsearLinea(lineas[i]);
                usuarios.add(usuario);
            } catch (Exception e) {
            }
        }

        calcularSeguidores();
        identificarInactivos();
    }

    private Usuario parsearLinea(String linea) {

        String[] partes = linea.split(",", 4);

        Usuario u = new Usuario();
        u.setId(Integer.parseInt(partes[0].trim()));
        u.setCorreo(partes[1].trim());
        u.setUltimaConexion(LocalDate.parse(partes[2].trim(), DATE_FORMATTER));

        // Limpiar y parsear lista
        String siguiendoRaw = partes[3].trim().replace("\"", "");
        if (siguiendoRaw.isEmpty() || siguiendoRaw.equals("0")) {
            u.setSiguiendo(Collections.emptyList());
        } else {
            u.setSiguiendo(
                Arrays.stream(siguiendoRaw.split(","))
                      .map(String::trim)
                      .filter(s -> !s.isEmpty())
                      .map(Integer::parseInt)
                      .collect(Collectors.toList())
            );
        }
        return u;
    }

    private void calcularSeguidores() {
        Map<Integer, Integer> conteoSeguidores = new HashMap<>();

        for (Usuario u : usuarios) {
            for (int seguidoId : u.getSiguiendo()) {
                if (seguidoId >= 1 && seguidoId <= usuarios.size()) {
                    conteoSeguidores.put(seguidoId, conteoSeguidores.getOrDefault(seguidoId, 0) + 1);
                }
            }
        }

        for (Usuario u : usuarios) {
            u.setSeguidoresCount(conteoSeguidores.getOrDefault(u.getId(), 0));
        }
    }

    private void identificarInactivos() {
        for (Usuario u : usuarios) {
            long diasDiferencia = java.time.temporal.ChronoUnit.DAYS.between(u.getUltimaConexion(), FECHA_ACTUAL);
            u.setInactivo(diasDiferencia > DIAS_INACTIVO);
        }
    }

    // punto 4
    public List<Usuario> getUltimos10Conectados() {
        return usuarios.stream()
                .sorted(Comparator.comparing(Usuario::getUltimaConexion).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    // punto 5
    public List<Usuario> getUsuarioMasPopular() {
        int maxSeguidores = usuarios.stream()
                .mapToInt(Usuario::getSeguidoresCount)
                .max()
                .orElse(0);

        return usuarios.stream()
                .filter(u -> u.getSeguidoresCount() == maxSeguidores)
                .collect(Collectors.toList());
    }


    // punto 6
    public List<Usuario> getInactivoMasSeguido() {
        List<Usuario> inactivos = usuarios.stream()
                .filter(Usuario::isInactivo)
                .collect(Collectors.toList());

        if (inactivos.isEmpty()) {
            return Collections.emptyList();
        }

        int maxSeguidores = inactivos.stream()
                .mapToInt(Usuario::getSeguidoresCount)
                .max()
                .orElse(0);

        return inactivos.stream()
                .filter(u -> u.getSeguidoresCount() == maxSeguidores)
                .collect(Collectors.toList());
    }
}