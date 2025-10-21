package com.example.ProyectoIntegrador.SeleniumTests;
import com.example.ProyectoIntegrador.Entity.Usuario;
import com.example.ProyectoIntegrador.Repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginSeleniumTest {
    private WebDriver driver;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final String baseUrl = "http://localhost:8080/Login";

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();

        if (!usuarioRepository.findByNombreUsuario("UsuarioTest").isPresent()) {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario("UsuarioTest");
            usuario.setContrasena("{noop}1234");
            usuario.setRol(Usuario.Rol.notario);
            usuarioRepository.save(usuario);
        }

        driver.get(baseUrl);
    }

    @Test
    public void testLoginAndRedirect() throws InterruptedException {
        WebElement username = driver.findElement(By.name("username"));
        WebElement password = driver.findElement(By.name("password"));
        WebElement submit = driver.findElement(By.cssSelector("button[type='submit']"));

        username.sendKeys("UsuarioTest");
        password.sendKeys("1234");
        submit.click();

        Thread.sleep(2000);

        assertEquals("http://localhost:8080/SistemaNotario", driver.getCurrentUrl());

        WebElement nombreNotario = driver.findElement(By.tagName("strong"));
        assertEquals("UsuarioTest", nombreNotario.getText());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}