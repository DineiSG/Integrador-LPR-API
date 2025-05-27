package com.autoshopping.integrated.api.lpr.message;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MensagemController {

    private final SimpMessagingTemplate template;

    public MensagemController(SimpMessagingTemplate template){
        this.template = template;
    }

    public void enviarMensagemParaFrontend(String mensagem){
        this.template.convertAndSend("/topic/mensagens", mensagem);
    }

}
