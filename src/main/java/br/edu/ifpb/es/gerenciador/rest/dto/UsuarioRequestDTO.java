package br.edu.ifpb.es.gerenciador.rest.dto;

import br.edu.ifpb.es.gerenciador.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

        @NotBlank(message = "O nome é obrigatório.")
        String nome,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "Formato de email inválido!")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(message = "A senha deve ter no mínimo 6 caracteres.")
        String senha,

        Role role

) {}
