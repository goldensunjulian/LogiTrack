package com.example.LogiTrack.modelo;

import com.example.LogiTrack.util.AuditoriaListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="usuario")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditoriaListener.class)
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String nombre;
    @Column(nullable = false, length = 225)
    private String email;
    @Column(nullable = false)
    private boolean activo;

}
