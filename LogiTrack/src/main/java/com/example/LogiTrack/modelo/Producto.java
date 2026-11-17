package com.example.LogiTrack.modelo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "producto")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String nombre;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "categoria_fk")
    private Categoria categoria;
    @Column(nullable = false)
    private BigDecimal precio;
    @Column(nullable = false)
    private boolean activo;
}
