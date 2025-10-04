package br.edu.ifpb.es.gerenciador.repository;

import br.edu.ifpb.es.gerenciador.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, UUID> {

    Optional<Tarefa> findByLookupId(UUID lookupId);

    List<Tarefa> findByCriadoPor_lookupId(UUID usuarioId);

}
