package br.edu.ifpb.es.gerenciador.rest.dto;

import java.time.LocalDateTime;

public record TarefaUpdateRequestDTO (

        String titulo,
        String descricao,
        Boolean concluido

) {}