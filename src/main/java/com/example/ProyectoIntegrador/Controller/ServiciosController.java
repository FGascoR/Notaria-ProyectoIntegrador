package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Servicio;
import com.example.ProyectoIntegrador.Repository.ServicioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/servicios")
public class ServiciosController {

    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioRepository.findAll());
        model.addAttribute("servicio", new Servicio());
        return "SistemaNotario";
    }

    @PostMapping("/guardar")
    public String guardarServicio(@ModelAttribute Servicio servicio) {
        servicioRepository.save(servicio);
        return "redirect:/servicios?seccion=servicios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable Integer id) {
        servicioRepository.deleteById(id);
        return "redirect:/servicios?seccion=servicios";
    }

    @GetMapping("/editar/{id}")
    @ResponseBody
    public Servicio obtenerServicio(@PathVariable Integer id) {
        return servicioRepository.findById(id).orElse(null);
    }

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
        for (Servicio servicio : servicios) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(servicio.getIdServicio());
            row.createCell(1).setCellValue(servicio.getNombre());
            row.createCell(2).setCellValue(servicio.getDescripcion());
            row.createCell(3).setCellValue(servicio.getCosto());
        }

        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
