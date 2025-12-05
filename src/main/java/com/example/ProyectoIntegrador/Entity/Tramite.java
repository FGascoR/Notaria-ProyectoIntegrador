package com.example.ProyectoIntegrador.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate; // Usaremos LocalDate para mejor manejo de fechas

@Entity
@Table(name = "Tramite")
@Getter
@Setter
public class Tramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTramite")
    private Integer idTramite;

    @ManyToOne
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente;

    // CAMBIO: Usamos Servicio en lugar de TipoTramite
    @ManyToOne
    @JoinColumn(name = "idServicio", nullable = false)
    private Servicio servicio;

    @Column(name = "fechaInicio")
    private LocalDate fechaInicio; // Cambiado a LocalDate

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado = Estado.pendiente;

    @Column(name = "observaciones")
    private String observaciones;

    public enum Estado {
        pendiente, en_proceso, completado, rechazado, aceptado // <--- Agregado
    }

    @PrePersist
    public void prePersist() {
        this.fechaInicio = LocalDate.now();
    }
}