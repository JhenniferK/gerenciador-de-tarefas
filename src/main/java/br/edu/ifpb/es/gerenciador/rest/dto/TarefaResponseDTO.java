package br.edu.ifpb.es.gerenciador.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TarefaResponseDTO (

        UUID lookupId,
        String nome,
        String descricao,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        LocalDateTime concluidoEm,
        String criadoPorEmail

) {}
