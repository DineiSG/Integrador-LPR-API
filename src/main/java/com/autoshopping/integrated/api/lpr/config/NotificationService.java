package com.autoshopping.integrated.api.lpr.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notificar(String mensagem) {
        // Envia para todos os clientes inscritos em /topic/mensagens
        messagingTemplate.convertAndSend("/topic/mensagens", mensagem);
    }
}
