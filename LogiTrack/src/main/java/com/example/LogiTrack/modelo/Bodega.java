package com.example.LogiTrack.modelo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bodega")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bodega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String nombre;
    @Column(nullable = false, length = 100)
    private String ciudad;
    @Column(nullable = false, length = 150)
    private String direccion;
    @Column(nullable = false)
    private Integer capacidad;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_encargado_fk")
    private Usuario usuarioEncargado;
    @Column(nullable = false)
    private Boolean activado;
}
