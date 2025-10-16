package br.edu.ifpb.es.gerenciador.rest.controller;

import br.edu.ifpb.es.gerenciador.model.Usuario;
import br.edu.ifpb.es.gerenciador.rest.dto.TarefaRequestDTO;
import br.edu.ifpb.es.gerenciador.rest.dto.TarefaResponseDTO;
import br.edu.ifpb.es.gerenciador.rest.dto.TarefaUpdateRequestDTO;
import br.edu.ifpb.es.gerenciador.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/tarefas")
public class TarefaRestController {

    private final TarefaService tarefaService;

    public TarefaRestController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@RequestBody TarefaRequestDTO dto, @AuthenticationPrincipal Usuario usuarioLogado) {
        TarefaResponseDTO novaTarefa = tarefaService.criarTarefa(dto, usuarioLogado.getLookupId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
    }

    @GetMapping
    public ResponseEntity<List<TarefaResponseDTO>> listarTarefas(@AuthenticationPrincipal Usuario usuarioLogado) {
        List<TarefaResponseDTO> tarefas = tarefaService.listarTodasTarefasPorUsuario(usuarioLogado.getLookupId());
        return ResponseEntity.ok(tarefas);
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<TarefaResponseDTO> concluirTarefa(@PathVariable UUID id, @AuthenticationPrincipal Usuario usuarioLogado) {
        TarefaResponseDTO tarefaAtualizada = tarefaService.marcarTarefaComoConcluida(id, usuarioLogado.getLookupId());
        return ResponseEntity.ok(tarefaAtualizada);
    }

    @PutMapping("/{id}/editar")
    public ResponseEntity<TarefaResponseDTO> editarTarefa(@PathVariable UUID id, @RequestBody @Valid TarefaUpdateRequestDTO dto, @AuthenticationPrincipal Usuario usuarioLogado) {
        TarefaResponseDTO updateTarefa = tarefaService.atualizarTarefa(id, dto, usuarioLogado);
        return ResponseEntity.ok(updateTarefa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> buscarTarefa(@PathVariable UUID id, @AuthenticationPrincipal Usuario usuarioLogado) {
        TarefaResponseDTO tarefaEncontrada = tarefaService.buscarTarefaPorLookupId(id, usuarioLogado);
        return ResponseEntity.ok(tarefaEncontrada);

    }

    @DeleteMapping("/{id}/deletar")
    public ResponseEntity<TarefaResponseDTO> deletarTarefa(@PathVariable UUID id, @AuthenticationPrincipal Usuario usuarioLogado) throws Throwable {
        tarefaService.deletarTarefa(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

}