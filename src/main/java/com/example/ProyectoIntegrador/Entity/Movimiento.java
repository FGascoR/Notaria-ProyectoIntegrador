package com.example.ProyectoIntegrador.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Movimiento")
@Getter
@Setter
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMovimiento")
    private Integer idMovimiento;

    @ManyToOne
    @JoinColumn(name = "idTramite", nullable = false)
    private Tramite tramite;

    @ManyToOne
    @JoinColumn(name = "idNotario", nullable = false)
    private Notario notario;

    @Column(name = "accion", length = 100)
    private String accion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "detalle")
    private String detalle;
}
