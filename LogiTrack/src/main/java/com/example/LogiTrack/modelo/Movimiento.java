package com.example.LogiTrack.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, insertable = false, updatable = false)
    private LocalDateTime fecha;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipoMovimiento;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "usuario_fk")
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bodega_origen_fk")
    private Bodega bodegaOrigen;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bodega_destino_fk")
    private Bodega bodegaDestino;
}
