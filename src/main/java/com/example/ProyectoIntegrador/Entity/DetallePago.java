package com.example.ProyectoIntegrador.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DetallePago")
@Getter
@Setter
public class DetallePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetallePago")
    private Integer idDetallePago;

    @ManyToOne
    @JoinColumn(name = "idPago", nullable = false)
    private Pago pago;

    @ManyToOne
    @JoinColumn(name = "idServicio", nullable = false)
    private Servicio servicio;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad = 1;

    @Column(name = "subtotal")
    private Double subtotal;
}
