package com.autoshopping.integrated.api.lpr.registro;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/acessos")
public class RegistroController {

    @Autowired
    private RegistroService service;

    /*Buscando todos os registros de acesso*/
    @GetMapping
    public ResponseEntity<Iterable<Registro>> get(){return ResponseEntity.ok (service.getRegistro()); }

    @GetMapping("/placa/{placa}")
    public Iterable<Registro>getAcessosByPlaca(@PathVariable ("placa") String placa){
        return service.getAcessosByPlaca(placa);
    }

    @PostMapping
    public ResponseEntity post (@RequestBody Registro registro){
        Registro novo = service.insert(registro);
        System.out.println("Movimentação de veículo registrada com sucesso.");
        return ResponseEntity.ok("Movimentação de veículo registrada com sucesso.");
    }




}
