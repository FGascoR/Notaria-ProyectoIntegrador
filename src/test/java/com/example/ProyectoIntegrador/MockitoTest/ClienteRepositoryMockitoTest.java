package com.example.ProyectoIntegrador.MockitoTest;

import com.example.ProyectoIntegrador.Controller.ClienteController;
import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Entity.Usuario;
import com.example.ProyectoIntegrador.Repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteRepositoryMockitoTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void testObtenerUsuarioCliente_EncuentraCliente() {
        
        Integer idCliente = 1;
        
        
        Usuario usuarioMock = new Usuario();
        usuarioMock.setIdUsuario(10);
        usuarioMock.setNombreUsuario("usuarioPrueba");

        
        Cliente clienteMock = new Cliente();
        clienteMock.setIdCliente(idCliente);
        clienteMock.setUsuario(usuarioMock);

       
        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(clienteMock));

        
        Object resultado = clienteController.obtenerUsuarioCliente(idCliente);

        
        assertNotNull(resultado, "El controlador no debería devolver null");
        
        
        verify(clienteRepository, times(1)).findById(idCliente);
        
        
    }

    @Test
    void testObtenerUsuarioCliente_NoEncuentraCliente() {
        
        Integer idNoExistente = 99;
        when(clienteRepository.findById(idNoExistente)).thenReturn(Optional.empty());

        
        Object resultado = clienteController.obtenerUsuarioCliente(idNoExistente);

        
        assertNull(resultado, "Debería devolver null si el cliente no existe");
        verify(clienteRepository, times(1)).findById(idNoExistente);
    }
}