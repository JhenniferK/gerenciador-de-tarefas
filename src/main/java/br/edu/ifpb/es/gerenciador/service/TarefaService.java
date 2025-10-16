package br.edu.ifpb.es.gerenciador.service;

import br.edu.ifpb.es.gerenciador.exception.AutorizacaoNegadaException;
import br.edu.ifpb.es.gerenciador.exception.NaoEncontradoException;
import br.edu.ifpb.es.gerenciador.model.Tarefa;
import br.edu.ifpb.es.gerenciador.model.Usuario;
import br.edu.ifpb.es.gerenciador.repository.TarefaRepository;
import br.edu.ifpb.es.gerenciador.repository.UsuarioRepository;
import br.edu.ifpb.es.gerenciador.rest.dto.TarefaRequestDTO;
import br.edu.ifpb.es.gerenciador.rest.dto.TarefaResponseDTO;
import br.edu.ifpb.es.gerenciador.rest.dto.TarefaUpdateRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;

    public TarefaService(TarefaRepository tarefaRepository, UsuarioRepository usuarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public TarefaResponseDTO criarTarefa(TarefaRequestDTO dto, UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Tarefa tarefa = Tarefa.builder()
                .tituloTarefa(dto.titulo())
                .descricao(dto.descricao())
                .criadoPor(usuario)
                .build();
        tarefaRepository.save(tarefa);
        return new TarefaResponseDTO(
                tarefa.getLookupId(),
                tarefa.getTituloTarefa(),
                tarefa.getDescricao(),
                tarefa.getCriadoEm(),
                tarefa.getAtualizadoEm(),
                tarefa.getConcluidoEm(),
                tarefa.getCriadoPor().getEmail()
        );
    }

    public List<TarefaResponseDTO> listarTodasTarefasPorUsuario(UUID usuarioId) {
        return tarefaRepository.findByCriadoPor_lookupId(usuarioId).stream()
                .map(t -> new TarefaResponseDTO(
                        t.getLookupId(),
                        t.getTituloTarefa(),
                        t.getDescricao(),
                        t.getCriadoEm(),
                        t.getAtualizadoEm(),
                        t.getConcluidoEm(),
                        t.getCriadoPor().getEmail()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public TarefaResponseDTO marcarTarefaComoConcluida(UUID tarefaId, UUID usuarioId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new NaoEncontradoException("Tarefa não encontrada."));

        if (!tarefa.getCriadoPor().getLookupId().equals(usuarioId)) {
            throw new AutorizacaoNegadaException("Você não tem permisão para acessar essa tarefa.");
        }

        if (!tarefa.feito()) {
            tarefa.setConcluidoEm(LocalDateTime.now());
            tarefaRepository.save(tarefa);
        }
        return new TarefaResponseDTO(
                tarefa.getLookupId(),
                tarefa.getTituloTarefa(),
                tarefa.getDescricao(),
                tarefa.getCriadoEm(),
                tarefa.getAtualizadoEm(),
                tarefa.getConcluidoEm(),
                tarefa.getCriadoPor().getEmail()
        );
    }

    @Transactional
    public TarefaResponseDTO atualizarTarefa(UUID tarefaId, TarefaUpdateRequestDTO dto, Usuario usuarioLogado) throws AutorizacaoNegadaException {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new NaoEncontradoException("Tarefa não encontrada."));

        if (!tarefa.getCriadoPor().getLookupId().equals(usuarioLogado.getLookupId())) {
            throw new AutorizacaoNegadaException("Você não tem permissão para acessar essa tarefa.");
        }

        tarefa.setTituloTarefa(dto.titulo());
        tarefa.setDescricao(dto.descricao());

        if (dto.concluido() != null) {
            if (dto.concluido() && !tarefa.feito()) {
                tarefa.setConcluidoEm(LocalDateTime.now());
            } else if (!(dto.concluido() && tarefa.feito())) {
                tarefa.setConcluidoEm(null);
            }
        }

        tarefa.setAtualizadoEm(LocalDateTime.now());
        tarefaRepository.save(tarefa);

        return new TarefaResponseDTO(
                tarefa.getLookupId(),
                tarefa.getTituloTarefa(),
                tarefa.getDescricao(),
                tarefa.getCriadoEm(),
                tarefa.getAtualizadoEm(),
                tarefa.getConcluidoEm(),
                tarefa.getCriadoPor().getEmail()
        );
    }

    public TarefaResponseDTO buscarTarefaPorLookupId(UUID lookupId, Usuario usuarioLogado) throws AutorizacaoNegadaException {
        Tarefa tarefa = tarefaRepository.findByLookupId(lookupId)
                .orElseThrow(() -> new NaoEncontradoException("Tarefa não encontrada."));

        if (!tarefa.getCriadoPor().getLookupId().equals(usuarioLogado.getLookupId())) {
            throw new AutorizacaoNegadaException("Você não tem permissão para acessar essa tarefa.");
        }

        return new TarefaResponseDTO(
                tarefa.getLookupId(),
                tarefa.getTituloTarefa(),
                tarefa.getDescricao(),
                tarefa.getCriadoEm(),
                tarefa.getAtualizadoEm(),
                tarefa.getConcluidoEm(),
                tarefa.getCriadoPor().getEmail()
        );
    }

    @Transactional
    public void deletarTarefa(UUID lookupId, Usuario usuarioLogado) throws Throwable {
        Tarefa tarefa = tarefaRepository.findByLookupId(lookupId)
                .orElseThrow(() -> new NaoEncontradoException("Tarefa não encontrada."));

        if (!tarefa.getCriadoPor().getLookupId().equals(usuarioLogado.getLookupId())) {
            throw new java.lang.Throwable("Você não tem permissão para deletar essa tarefa.");
        }

        tarefaRepository.delete(tarefa);
    }

}
