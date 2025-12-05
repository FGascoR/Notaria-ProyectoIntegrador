package com.example.ProyectoIntegrador.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects; // <-- Importante

@Entity
@Table(name = "Servicio")
@Getter
@Setter
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idServicio")
    private Integer idServicio;

    @Column(name = "img", length = 255)
    private String img;

    @Column(name = "nombre", length = 100)
    private String nombre; // Asumimos que este es la Clave de Negocio

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "costo")
    private Double costo;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Servicio)) return false;
        Servicio servicio = (Servicio) o;


        if (this.nombre == null || servicio.nombre == null) {
            return false;
        }
        return Objects.equals(nombre, servicio.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}