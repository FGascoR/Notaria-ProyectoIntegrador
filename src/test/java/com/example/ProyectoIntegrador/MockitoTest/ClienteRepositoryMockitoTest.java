package com.example.ProyectoIntegrador.MockitoTest;

import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ClienteRepositoryMockitoTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Test
    void testFindAllReturnsList() {
        // Creamos mocks de Cliente (no usamos setters/getters reales)
        Cliente c1 = Mockito.mock(Cliente.class);
        Cliente c2 = Mockito.mock(Cliente.class);

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Cliente> result = clienteRepository.findAll();

        assertNotNull(result, "El resultado de findAll() no debe ser null");
        assertEquals(2, result.size(), "La lista debería tener 2 elementos mockeados");
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Cliente c = Mockito.mock(Cliente.class);
        when(clienteRepository.findById(1)).thenReturn(Optional.of(c));

        Optional<Cliente> opt = clienteRepository.findById(1);
        assertTrue(opt.isPresent(), "findById(1) debería devolver Optional con valor");
        verify(clienteRepository, times(1)).findById(1);
    }
}