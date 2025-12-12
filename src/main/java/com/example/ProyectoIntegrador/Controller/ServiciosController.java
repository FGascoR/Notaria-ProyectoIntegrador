package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Servicio;
import com.example.ProyectoIntegrador.Repository.ServicioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/servicios")
public class ServiciosController {

    private static final String UPLOAD_DIR = "/app/uploads/servicios/";
    @Autowired
    private ServicioRepository servicioRepository;

    @PostMapping("/guardar")
    public String guardarServicio(@ModelAttribute Servicio servicio,
                                  @RequestParam("imagenArchivo") MultipartFile imagen) throws IOException {

        if (imagen != null && !imagen.isEmpty()) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String extension = imagen.getOriginalFilename().substring(imagen.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;

            Files.write(Paths.get(UPLOAD_DIR + fileName), imagen.getBytes());
            servicio.setImg(fileName);
        }

        servicioRepository.save(servicio);
        // CORRECCIÓN: Redirigir al controlador principal, NO a /servicios
        return "redirect:/SistemaNotario?seccion=servicios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable Integer id) {
        servicioRepository.findById(id).ifPresent(s -> {
            if (s.getImg() != null) {
                try {
                    Files.deleteIfExists(Paths.get(UPLOAD_DIR + s.getImg()));
                } catch (IOException ignored) {}
            }
            servicioRepository.deleteById(id);
        });
        // CORRECCIÓN: Redirigir al controlador principal
        return "redirect:/SistemaNotario?seccion=servicios";
    }

    @GetMapping("/editar/{id}")
    @ResponseBody
    public Servicio obtenerServicio(@PathVariable Integer id) {
        return servicioRepository.findById(id).orElse(null);
    }

    @PostMapping("/editar")
    public String editarServicio(@ModelAttribute Servicio servicio,
                                 @RequestParam("imagenArchivo") MultipartFile imagen) throws IOException {

        Servicio existente = servicioRepository.findById(servicio.getIdServicio()).orElse(null);
        if (existente == null) return "redirect:/SistemaNotario?seccion=servicios";

        existente.setNombre(servicio.getNombre());
        existente.setDescripcion(servicio.getDescripcion());
        existente.setCosto(servicio.getCosto());

        if (imagen != null && !imagen.isEmpty()) {
            if (existente.getImg() != null) {
                Files.deleteIfExists(Paths.get(UPLOAD_DIR + existente.getImg()));
            }

            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String extension = imagen.getOriginalFilename().substring(imagen.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;

            Files.write(Paths.get(UPLOAD_DIR + fileName), imagen.getBytes());
            existente.setImg(fileName);
        }

        servicioRepository.save(existente);
        // CORRECCIÓN: Redirigir al controlador principal
        return "redirect:/SistemaNotario?seccion=servicios&edit=success";
    }

    // Exportar Excel (Esto sí se queda aquí porque descarga un archivo)
    @GetMapping("/exportar-excel")
    public void exportarServiciosExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=servicios.xlsx");

        List<Servicio> servicios = servicioRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Servicios");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Nombre");
        header.createCell(2).setCellValue("Descripción");
        header.createCell(3).setCellValue("Costo");

        int rowNum = 1;
        for (Servicio s : servicios) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(s.getIdServicio());
            row.createCell(1).setCellValue(s.getNombre());
            row.createCell(2).setCellValue(s.getDescripcion());
            row.createCell(3).setCellValue(s.getCosto());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}