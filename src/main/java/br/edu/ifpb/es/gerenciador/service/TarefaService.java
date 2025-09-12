package br.edu.ifpb.es.gerenciador.service;

import br.edu.ifpb.es.gerenciador.model.Tarefa;
import br.edu.ifpb.es.gerenciador.model.Usuario;
import br.edu.ifpb.es.gerenciador.repository.TarefaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    private Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof Usuario)) {
            throw new IllegalStateException("Nenhum usuário autenticado.");
        }
        return (Usuario) authentication.getPrincipal();
    }

    public Tarefa criarNovaTarefa(Tarefa tarefa) {
        Usuario usuarioLogado = getUsuarioLogado();
        tarefa.setCriadoPor(usuarioLogado);
        return tarefaRepository.save(tarefa);
    }

    public List<Tarefa> listarTodasTarefasDoUsuario() {
        Usuario usuarioLogado = getUsuarioLogado();
        return tarefaRepository.findByCriadoPor(usuarioLogado);
    }

    public Optional<Tarefa> buscarTarefaPorLookupId(UUID lookupId) throws AccessDeniedException {
        Usuario usuarioLogado = getUsuarioLogado();
        Optional<Tarefa> tarefaOptional = tarefaRepository.findByLookupId(lookupId);

        if (tarefaOptional.isPresent()) {
            Tarefa tarefa = tarefaOptional.get();

            if (!tarefa.getCriadoPor().equals(usuarioLogado)) {
                throw new AccessDeniedException("Você não tem permissão para acessar essa tarefa.");
            }
        }
        return tarefaOptional;
    }

    public Tarefa atualizarTarefa(UUID lookupId, Tarefa tarefaAtualizada) {
        Usuario usuarioLogado = getUsuarioLogado();
        return tarefaRepository.findByLookupId(lookupId)
                .map(existingTarefa -> {
                    if (!existingTarefa.getCriadoPor().equals(usuarioLogado)) {
                        try {
                            throw new AccessDeniedException("Você não tem permissão para modificar essa tarefa.");
                        } catch (AccessDeniedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    existingTarefa.setDescricao(tarefaAtualizada.getDescricao());
                    existingTarefa.setConcluidoEm(tarefaAtualizada.getConcluidoEm());
                    return tarefaRepository.save(existingTarefa);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada."));
    }

    public void deletarTarefa(UUID lookupId) {
        Usuario usuarioLogado = getUsuarioLogado();
        Tarefa tarefa = tarefaRepository.findByLookupId(lookupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada."));

        if (!tarefa.getCriadoPor().equals(usuarioLogado)) {
            throw new org.springframework.security.access.AccessDeniedException("Você não tem permissão para deletar essa tarefa.");
        }
        tarefaRepository.delete(tarefa);
    }
}
