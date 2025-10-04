package br.edu.ifpb.es.gerenciador.rest.dto;


import jakarta.validation.constraints.NotBlank;

public record TarefaRequestDTO (

        @NotBlank(message = "O título da tarefa é obrigatório.")
        String titulo,

        String descricao

) {}
