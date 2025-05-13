package com.autoshopping.integrated.api.lpr.leitura;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    @PostMapping("/")
    public ResponseEntity<String> teste(@RequestBody String body) {
        System.out.println("Recebido: " + body);
        return ResponseEntity.ok("OK");
    }
}
