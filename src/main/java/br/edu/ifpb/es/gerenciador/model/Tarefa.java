package br.edu.ifpb.es.gerenciador.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Tarefa {

    @Id
    @Column(nullable = false)
    private UUID lookupID;

    @Column(nullable = false)
    private String descricao;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    private LocalDateTime concluidoEm;

    @ManyToOne
    @Column(nullable = false)
    private Usuario criadoPor;

    @PrePersist
    private void init() {
        this.lookupID = UUID.randomUUID();
        this.criadoEm = LocalDateTime.now();
    }

    public boolean feito() {
        return this.concluidoEm != null;
    }

}