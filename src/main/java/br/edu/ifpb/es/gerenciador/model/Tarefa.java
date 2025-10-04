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
    @Column(unique = true, nullable = false)
    private UUID lookupId;

    @Column(nullable = false)
    private String tituloTarefa;

    @Column
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @Column
    private LocalDateTime atualizadoEm;

    @Column
    private LocalDateTime concluidoEm;

    @ManyToOne
    @JoinColumn(name = "id_usuario_criador", nullable = false)
    private Usuario criadoPor;

    @PrePersist
    protected void onCreate() {
        if (this.lookupId == null) {
            this.lookupId = UUID.randomUUID();
        }
        if (this.criadoEm == null) {
            this.criadoEm = LocalDateTime.now();
        }
    }

    public boolean feito() {
        return this.concluidoEm != null;
    }

}