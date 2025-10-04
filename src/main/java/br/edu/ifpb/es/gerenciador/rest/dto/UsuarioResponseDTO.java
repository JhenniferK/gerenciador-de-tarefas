package br.edu.ifpb.es.gerenciador.rest.dto;

import br.edu.ifpb.es.gerenciador.model.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDTO (

        UUID lookupId,
        String nome,
        String email,
        Role role,
        LocalDateTime criadoEm

) {}
