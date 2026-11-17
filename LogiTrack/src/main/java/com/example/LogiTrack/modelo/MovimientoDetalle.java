package com.example.LogiTrack.modelo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "movimiento_detalle")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "movimiento_fk")
    private Movimiento movimiento;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "producto_fk")
    private Producto producto;
    @Column(nullable = false)
    private Integer cantidad;
}
