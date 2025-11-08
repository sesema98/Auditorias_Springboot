package com.tecsup.aopserva.controllers;

import com.tecsup.aopserva.domain.entities.Curso;
import com.tecsup.aopserva.services.CursoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Map;

@Controller
@SessionAttributes("curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    // ✅ Listar cursos
    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de Cursos");
        model.addAttribute("cursos", cursoService.listar());
        return "listar";
    }

    // ✅ Crear nuevo curso
    @Secured("ROLE_ADMIN")
    @GetMapping("/form")
    public String crear(Map<String, Object> model) {
        Curso curso = new Curso();
        model.put("curso", curso);
        return "form";
    }

    // ✅ Editar curso existente
    @Secured("ROLE_ADMIN")
    @GetMapping("/form/{id}")
    public String editar(@PathVariable Integer id, Map<String, Object> model) {
        Curso curso = null;

        if (id > 0) {
            curso = cursoService.buscar(id);
        } else {
            return "redirect:/listar";
        }

        model.put("curso", curso);
        return "form";
    }

    // ✅ Guardar curso (crear o actualizar)
    @Secured("ROLE_ADMIN")
    @PostMapping("/form")
    public String guardar(@Valid Curso curso, BindingResult result, Model model, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Curso");
            return "form";
        }

        cursoService.grabar(curso);
        status.setComplete();
        return "redirect:/listar";
    }

    // ✅ Eliminar curso
    @Secured("ROLE_ADMIN")
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        if (id > 0) {
            cursoService.eliminar(id);
        }
        return "redirect:/listar";
    }

    // ✅ Ver cursos (versión adicional)
    @GetMapping("/ver")
    public String ver(Model model) {
        model.addAttribute("titulo", "Lista de cursos");
        model.addAttribute("cursos", cursoService.listar());
        return "curso/ver";
    }
}
