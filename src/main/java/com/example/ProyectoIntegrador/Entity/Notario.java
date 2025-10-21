package com.example.ProyectoIntegrador.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Notario")
@Getter
@Setter
public class Notario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNotario")
    private Integer idNotario;

    @OneToOne
    @JoinColumn(name = "idUsuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "apellido", length = 100)
    private String apellido;

    @Column(name = "colegiado", length = 50, unique = true)
    private String colegiado;

    @OneToMany(mappedBy = "notario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reunion> reuniones;
}
