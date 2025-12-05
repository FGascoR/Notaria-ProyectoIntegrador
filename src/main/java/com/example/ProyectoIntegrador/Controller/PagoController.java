package com.example.ProyectoIntegrador.Controller;

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

        // Buscar el trámite
        Tramite tramite = tramiteRepository.findById(idTramite).orElse(null);

        // Verificar que el trámite existe y NO ha sido pagado previamente
        if (tramite != null && !pagoRepository.existsByTramite(tramite)) {
            Pago pago = new Pago();
            pago.setTramite(tramite);
            pago.setCliente(tramite.getCliente());
            pago.setFecha(new Date());
            pago.setHora(new Date());

            // Asignar monto desde el servicio
            if (tramite.getServicio() != null) {
                pago.setMontoTotal(tramite.getServicio().getCosto());
            } else {
                pago.setMontoTotal(0.0);
            }

            pagoRepository.save(pago);
        }

        return "redirect:/Pagina?pago=exitoso";
    }
}