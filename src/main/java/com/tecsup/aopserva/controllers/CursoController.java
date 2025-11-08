package com.tecsup.aopserva.controllers;

import com.tecsup.aopserva.domain.entities.Curso;
import com.tecsup.aopserva.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("curso")
public class CursoController {

    @RequestMapping(value="/ver")
    public String ver(Model model){
        model.addAttribute("cursos", cursoService.listar());
        model.addAttribute("titulo","Lista de cursos");

        return "curso/ver";
    }

    @Autowired
    private CursoService cursoService;

    // Listar todos los cursos
    @GetMapping("/listar")
    public String listarCurso(Model model) {
        model.addAttribute("cursos", cursoService.listar());
        return "listar";
    }

    // Mostrar formulario para crear un nuevo curso
    @GetMapping("/nuevo")
    public String nuevoCurso(Model model) {
        model.addAttribute("curso", new Curso());
        return "formCurso";
    }

    // Guardar o actualizar un curso
    @PostMapping("/guardar")
    public String guardarCurso(@Valid @ModelAttribute("curso") Curso curso, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "formCurso";
        }
        cursoService.grabar(curso);
        status.setComplete();
        return "redirect:/listar";
    }

    // Editar curso existente
    @GetMapping("/editar/{id}")
    public String editarCurso(@PathVariable("id") int id, Model model) {
        Curso curso = cursoService.buscar(id);
        if (curso != null) {
            model.addAttribute("curso", curso);
            return "formCurso"; // usa el mismo formulario
        } else {
            return "redirect:/listar";
        }
    }

    // Eliminar curso
    @GetMapping("/eliminar/{id}")
    public String eliminarCurso(@PathVariable("id") int id) {
        if (id > 0) {
            cursoService.eliminar(id);
        }
        return "redirect:/listar";
    }

    // Vista adicional opcional (ver cursos)
    @GetMapping("/ver")
    public String verCursos(Model model) {
        model.addAttribute("cursos", cursoService.listar());
        model.addAttribute("titulo", "Lista de cursos");
        return "curso/ver";
    }
}
