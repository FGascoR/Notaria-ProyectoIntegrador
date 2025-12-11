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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder; // Necesario para crear usuario válido

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginSeleniumTest {

    private WebDriver driver;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder; // Inyectamos para guardar la clave correctamente

    private final String baseUrl = "http://localhost:8080/Login";

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();

        
        if (!usuarioRepository.findByNombreUsuario("UsuarioTestSelenium").isPresent()) {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario("UsuarioTestSelenium");
            
            usuario.setContrasena(passwordEncoder.encode("1234")); 
            usuario.setRol(Usuario.Rol.notario);
            usuarioRepository.save(usuario);
        }

        driver.get(baseUrl);
    }

    @Test
    public void testLoginYRedireccionCorrecta() {
        
        WebElement usernameInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));

        
        usernameInput.sendKeys("UsuarioTestSelenium");
        passwordInput.sendKeys("1234");
        submitButton.click();

        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        
        wait.until(ExpectedConditions.urlContains("/SistemaNotario"));

        
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/SistemaNotario"), "Debería redirigir a /SistemaNotario");

        
        WebElement bienvenidaUsuario = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1/strong")));
        assertEquals("UsuarioTestSelenium", bienvenidaUsuario.getText());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        
    }
}