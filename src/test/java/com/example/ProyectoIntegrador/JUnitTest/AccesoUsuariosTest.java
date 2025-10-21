package com.example.ProyectoIntegrador.JUnitTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
public class AccesoUsuariosTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void probarLoginYMostrarMensajeEnConsola() throws Exception {
        String usuario = "luisb";
        String contrasena = "1234";

        MvcResult resultado = mockMvc.perform(formLogin("/login")
                        .user(usuario)
                        .password(contrasena))
                .andReturn();

        int codigoEstado = resultado.getResponse().getStatus();
        String urlRedirigida = resultado.getResponse().getRedirectedUrl();

        if (codigoEstado == 302 && "/SistemaNotario".equals(urlRedirigida)) {
            System.out.println("Acceso concedido: usuario y contraseña correctos.");
        } else {
            System.out.println("Acceso denegado: usuario o contraseña incorrectos.");
        }
    }
}
