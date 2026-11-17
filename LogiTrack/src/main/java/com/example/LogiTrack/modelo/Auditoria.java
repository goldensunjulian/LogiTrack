package com.example.LogiTrack.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "operacion")
    private TipoOperacion tipoOperacion;
    @Column(nullable = false, insertable = false, updatable = false)
    private LocalDateTime fecha;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "usuario_fk")
    private Usuario usuario;
    @Column(nullable = false, length = 50)
    private String entidad;
    @Column(nullable = false)
    private Long entidadId;
    @Column(columnDefinition = "JSON")
    private String valoresAntes;
    @Column(columnDefinition = "JSON")
    private String valoresDespues;
}
