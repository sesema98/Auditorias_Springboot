package com.tecsup.aopserva.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LocaleController {

    @GetMapping("/locale")
    public String locale(HttpServletRequest request) {
        // Obtiene la URL previa desde el encabezado HTTP Referer
        String ultimaUrl = request.getHeader("referer");

        // Si no hay URL previa, redirige a la raíz
        if (ultimaUrl == null || ultimaUrl.isEmpty()) {
            return "redirect:/";
        }

        // Redirige a la última página visitada
        return "redirect:".concat(ultimaUrl);
    }
}
