package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalAttributesController {

    @Autowired
    private ServicioRepository servicioRepository;

    @ModelAttribute("servicios")
    public Iterable<?> getServicios() {
        return servicioRepository.findAll();
    }
}