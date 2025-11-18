package com.example.demo.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.demo.Model.Usuario;

@Service
public class UsuarioService {

    public static final LocalDate FECHA_REFERENCIA = LocalDate.of(2023, 5, 22);

    private final Map<String, Usuario> usuarios;

    public UsuarioService(CsvService csvService) throws Exception {
        this.usuarios = csvService.cargarArchivos();
    }

    public Stream<Usuario> usuariosInactivos() {
        return usuarios.values().stream()
                .filter(u -> u.inactivo(FECHA_REFERENCIA));
    }

    public Stream<Usuario> usuariosMitadInactivos() {
        return usuarios.values().stream()
                .filter(u -> {
                    var seg = u.getSiguiendo();
                    if (seg.isEmpty()) return false;

                    long total = seg.size();
                    long inactivos = seg.stream()
                            .map(usuarios::get)
                            .filter(v -> v.inactivo(FECHA_REFERENCIA))
                            .count();

                    return inactivos * 2 >= total;
                });
    }

    public Stream<Usuario> usuariosConMasSeguidores() {
        int max = usuarios.values().stream()
                .mapToInt(u -> u.getSeguidores().size())
                .max()
                .orElse(0);

        return usuarios.values().stream()
                .filter(u -> u.getSeguidores().size() == max);
    }
}
