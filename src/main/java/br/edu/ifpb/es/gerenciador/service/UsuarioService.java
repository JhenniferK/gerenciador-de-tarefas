package br.edu.ifpb.es.gerenciador.service;

import br.edu.ifpb.es.gerenciador.model.Role;
import br.edu.ifpb.es.gerenciador.model.Usuario;
import br.edu.ifpb.es.gerenciador.repository.UsuarioRepository;
import br.edu.ifpb.es.gerenciador.rest.dto.UsuarioRequestDTO;
import br.edu.ifpb.es.gerenciador.rest.dto.UsuarioResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = encoder;
    }

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .role(dto.role() != null ? dto.role() : Role.USUARIO)
                .build();
        usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(
                usuario.getLookupId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getCriadoEm()
        );
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponseDTO(
                        u.getLookupId(),
                        u.getNome(),
                        u.getEmail(),
                        u.getRole(),
                        u.getCriadoEm()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletarUsuario(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        usuarioRepository.delete(usuario);
    }

}
