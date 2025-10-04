package br.edu.ifpb.es.gerenciador.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO (

        @NotBlank(message = "O campo email é obrigatório.")
        @Email(message = "Formato de email inválido!")
        String email,

        @NotBlank(message = "O campo senha é obrigatório.")
        String senha

) {}
