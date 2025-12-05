package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Entity.Pago;
import com.example.ProyectoIntegrador.Entity.Tramite;
import com.example.ProyectoIntegrador.Repository.PagoRepository;
import com.example.ProyectoIntegrador.Repository.TramiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private TramiteRepository tramiteRepository;

    @PostMapping("/pago/procesar")
    public String procesarPago(@RequestParam("idTramite") Integer idTramite,
                               @RequestParam("metodoPago") String metodoPago) {

        Tramite tramite = tramiteRepository.findById(idTramite).orElse(null);

        if (tramite != null && !pagoRepository.existsByTramite(tramite)) {
            Pago pago = new Pago();
            pago.setTramite(tramite);
            pago.setCliente(tramite.getCliente());
            pago.setFecha(new Date());
            pago.setHora(new Date()); // O usa LocalTime si cambiaste la entidad

            // Asumimos que el monto es el costo del servicio
            pago.setMontoTotal(tramite.getServicio().getCosto());

            pagoRepository.save(pago);
        }

        return "redirect:/Pagina?pago=exitoso";
    }
}