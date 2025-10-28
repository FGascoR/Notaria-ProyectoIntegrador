package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/usuario/{idCliente}")
    @ResponseBody
    public Object obtenerUsuarioCliente(@PathVariable Integer idCliente) {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);

        if (cliente.isPresent() && cliente.get().getUsuario() != null) {
            return new Object() {
                public final Integer idUsuario = cliente.get().getUsuario().getIdUsuario();
                public final String nombreUsuario = cliente.get().getUsuario().getNombreUsuario();
            };
        }
        return null;
    }
}