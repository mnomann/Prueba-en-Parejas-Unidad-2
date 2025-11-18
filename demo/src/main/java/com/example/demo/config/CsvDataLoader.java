package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.model.Usuario;
import com.example.demo.service.AnalisisService;

import java.util.List;

@Component
public class CsvDataLoader implements CommandLineRunner {

    private final AnalisisService analisisService;

    public CsvDataLoader(AnalisisService analisisService) {
        this.analisisService = analisisService;
    }

    @Override
    public void run(String... args) throws Exception {
        analisisService.cargarYProcesarDatos();

        // punto 4
        List<Usuario> recientes = analisisService.getUltimos10Conectados();
        System.out.println("--------------------------------------------------------");
        System.out.println("Peticion 4: Los ultimos 10 usuarios conectados recientemente");
        System.out.println("--------------------------------------------------------");
        recientes.forEach(u -> System.out.printf("ID: %d | Correo: %s | Última Conexión: %s\n", 
                                                u.getId(), u.getCorreo(), u.getUltimaConexion()));

        // punto 5
        List<Usuario> populares = analisisService.getUsuarioMasPopular();
        System.out.println("\n--------------------------------------------------------");
        System.out.println("Peticion 5: Usuario mas popular");
        System.out.println("--------------------------------------------------------");
        populares.forEach(u -> System.out.printf("ID: %d | Correo: %s | Seguidores: %d\n", 
                                                u.getId(), u.getCorreo(), u.getSeguidoresCount()));

        // punto 6
        List<Usuario> inactivosPopulares = analisisService.getInactivoMasSeguido();
        System.out.println("\n--------------------------------------------------------");
        System.out.println("Peticion 6: Usuario inactivo con mas seguidores");
        System.out.println("--------------------------------------------------------");
        inactivosPopulares.forEach(u -> System.out.printf("ID: %d | Correo: %s | Ultima Conexion: %s | Seguidores: %d\n", 
                                                        u.getId(), u.getCorreo(), u.getUltimaConexion(), u.getSeguidoresCount()));
    }
}