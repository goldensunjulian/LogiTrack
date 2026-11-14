package com.example.LogiTrack.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "bodega_fk")
    private Bodega bodega;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "producto_fk")
    private Producto producto;
    @Column(nullable = false)
    private Integer stock;
}
