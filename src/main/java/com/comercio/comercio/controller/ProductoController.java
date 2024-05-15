package com.comercio.comercio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @GetMapping("")
    public String show(){
        return "RUTA PARA MOSTRAR PRODUCTOS";
    }
}
