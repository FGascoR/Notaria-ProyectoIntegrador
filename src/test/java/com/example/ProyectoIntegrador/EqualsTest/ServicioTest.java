package com.example.ProyectoIntegrador.EqualsTest;

import com.example.ProyectoIntegrador.Entity.Servicio;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class ServicioTest {

    @Test
    public void testEqualsYHashCodeContract() {
        EqualsVerifier.forClass(Servicio.class)
                
                .suppress(Warning.NONFINAL_FIELDS)
                
                
                .suppress(Warning.STRICT_INHERITANCE)
                
                
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
                
                .verify();
    }
}