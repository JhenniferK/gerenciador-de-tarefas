package br.edu.ifpb.es.gerenciador.controller;

import br.edu.ifpb.es.gerenciador.model.Tarefa;
import br.edu.ifpb.es.gerenciador.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@Valid @RequestBody Tarefa tarefa) {
        Tarefa novaTarefa = tarefaService.criarNovaTarefa(tarefa);
        return new ResponseEntity<>(novaTarefa, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> getTodasTarefasDoUsuario() {
        List<Tarefa> tarefas = tarefaService.listarTodasTarefasDoUsuario();
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{lookupId}")
    public ResponseEntity<Tarefa> getTarefaPorId(@PathVariable UUID lookupId) throws AccessDeniedException {
        return tarefaService.buscarTarefaPorLookupId(lookupId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada."));
    }

    @PutMapping("/{lookupId}")
    public ResponseEntity<Tarefa> atualizarTodo(@PathVariable UUID lookupId, @Valid @RequestBody Tarefa todo) {
        Tarefa todoAtualizada = tarefaService.atualizarTarefa(lookupId, todo);
        return ResponseEntity.ok(todoAtualizada);
    }

    @DeleteMapping("/{lookupId}")
    public ResponseEntity<Void> deletarTodo(@PathVariable UUID lookupId) {
        tarefaService.deletarTarefa(lookupId);
        return ResponseEntity.noContent().build();
    }
}
