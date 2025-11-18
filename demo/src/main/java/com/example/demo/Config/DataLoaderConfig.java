package com.example.demo.Config;


import org.springframework.context.annotation.Configuration;

import com.example.demo.Service.UsuarioService;

import jakarta.annotation.PostConstruct;

@Configuration
public class DataLoaderConfig {

    private final UsuarioService usuarioService;

    public DataLoaderConfig(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostConstruct
    public void runAfterStartup() {
        System.out.println("\n==== DESAFÍO 1 ====");

        System.out.println("\n1) Usuarios inactivos:");
        usuarioService.usuariosInactivos().forEach(System.out::println);

        System.out.println("\n2) Mitad de seguidos inactivos:");
        usuarioService.usuariosMitadInactivos().forEach(System.out::println);

        System.out.println("\n3) Usuarios con más seguidores:");
        usuarioService.usuariosConMasSeguidores().forEach(System.out::println);
    }
}
