package com.example.LogiTrack.modelo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "credenciales")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Credenciales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String username;
    @Column(nullable = false, length = 225)
    private String password;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "usuario_fk")
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "rol_fk")
    private Rol rol;
}
