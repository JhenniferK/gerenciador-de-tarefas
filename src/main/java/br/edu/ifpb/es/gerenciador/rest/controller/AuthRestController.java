package br.edu.ifpb.es.gerenciador.rest.controller;

import br.edu.ifpb.es.gerenciador.model.Usuario;
import br.edu.ifpb.es.gerenciador.rest.dto.LoginRequestDTO;
import br.edu.ifpb.es.gerenciador.rest.dto.LoginResponseDTO;
import br.edu.ifpb.es.gerenciador.rest.dto.UsuarioRequestDTO;
import br.edu.ifpb.es.gerenciador.rest.dto.UsuarioResponseDTO;
import br.edu.ifpb.es.gerenciador.service.JwtBlacklistService;
import br.edu.ifpb.es.gerenciador.service.UsuarioService;
import br.edu.ifpb.es.gerenciador.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    private final UsuarioService usuarioService;

    private final JwtBlacklistService jwtBlacklistService;

    public AuthRestController(UsuarioService usuarioService, AuthenticationManager authenticationManager, TokenService tokenService, JwtBlacklistService jwtBlacklistService) {
        this.usuarioService = usuarioService;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> register(@RequestBody @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO newUser = usuarioService.criarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorizationHeader, Authentication authentication) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String jwt = authorizationHeader.substring(7);

        Instant expiration = tokenService.getExpirationDate(jwt);
        if (expiration == null) {
            return ResponseEntity.badRequest().build();
        }

        jwtBlacklistService.blacklistToken(jwt, expiration);
        System.out.println("Usuário " + authentication.getName() + " deslogado.");
        return ResponseEntity.ok().build();
    }

}
