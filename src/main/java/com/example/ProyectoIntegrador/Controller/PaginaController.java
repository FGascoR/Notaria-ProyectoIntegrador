package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping("/Notaria")
    public String inicio(Model model) {
        model.addAttribute("servicios", servicioRepository.findAll());
        return "Pagina";
    }
}
