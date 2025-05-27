package com.autoshopping.integrated.api.lpr.leitura;

import com.autoshopping.integrated.api.lpr.message.MensagemController;
import com.autoshopping.integrated.api.lpr.registro.Registro;
import com.autoshopping.integrated.api.lpr.registro.RegistroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("api/v1/veiculos")  // Este é o caminho da tabela, as placas serao consultadas nela
public class LeituraController {

    @Autowired
    private LeituraRepository leituraRepository;

    @Autowired
    private RegistroRepository registroRepository;

    private final MensagemController mensagemController;
    public LeituraController(MensagemController mensagemController){
        this.mensagemController = mensagemController;
    }


    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> receberEvento(@RequestBody String json) {
        try {

            //Recebendo o JSON do Midleware
            System.out.println("JSON recebido da câmera:");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            //Extraindo a placa
            String placa = root.path("AlarmInfoPlate").path("result").path("PlateResult").path("license").asText(null);
            if (placa == null || placa.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Placa não encontrada no JSON"));
            }
            //Informando a placa recebida
            System.out.println("Placa recebida: " + placa);

            //Verificando se a placa está cadastrada no banco de dados
            boolean placaCadastrada = leituraRepository.findByPlacaIgnoreCase(placa).isPresent();


            //Mensagem que será enviada ao frontend
            String mensagem;

            if (!placaCadastrada) {
                String deviceName = root.path("AlarmInfoPlate").path("deviceName").asText();
                mensagem = "Veículo de placa " + placa + " não cadastrada se encontra na cancela de " +deviceName+ ". Acesso negado";
                mensagemController.enviarMensagemParaFrontend(mensagem);
                System.out.println(mensagem);

            } else {


                String ipaddr = root.path("AlarmInfoPlate").path("ipaddr").asText(); //extraindo o ip da câmera
                String deviceName = root.path("AlarmInfoPlate").path("deviceName").asText(); // extraindo o nome da câmera

                mensagem = "Veículo de placa " + placa + " cadastrada. Acesso liberado";

                System.out.println(mensagem);


                // Enviando para o Registro Repository a placa, ip e nome da câmera para guardar na tabela acessos
                Registro registro = new Registro();
                registro.setPlaca(placa);
                registro.setIpaddr(ipaddr);
                registro.setDeviceName(deviceName);
                registroRepository.save(registro);

                mensagem = "Veículo placa " + placa + " passou pela cancela de " + deviceName + ".";

                System.out.println(mensagem);
            }

            // Codificando em Base64 as mensagens de abertura ou negação de acesso
            String comandoSerial = placaCadastrada ? "ABRIR123" : "NEGADO123";
            String base64Data = Base64.getEncoder().encodeToString(comandoSerial.getBytes());

            if (comandoSerial == "ABRIR123"){
            Map<String, Object> canal1 = new HashMap<>();
            canal1.put("serialChannel", 0);
            canal1.put("data", base64Data);
            canal1.put("dataLen", comandoSerial.length());

            Map<String, Object> canal2 = new HashMap<>();
            canal2.put("serialChannel", 1);
            canal2.put("data", base64Data);
            canal2.put("dataLen", comandoSerial.length());


            //Montando a estrutura do JSON que a camera espera:
            // Formatando o JSON que sera enviado para a camera via midleware
            Map<String, Object> resposta = new LinkedHashMap<>();
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("info", "ok");
            body.put("content", "retransfer_stop");
            body.put("is_pay", "true");
            body.put("serialData", Arrays.asList(canal1, canal2));

            resposta.put("Response_AlarmInfoPlate", body);

            // Para fins de debug (exibe JSON de forma legível no console)
            ObjectMapper debugMapper = new ObjectMapper();
            System.out.println("JSON ENVIADO PARA CÂMERA:");
            System.out.println(debugMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resposta));

            return ResponseEntity.ok(resposta);
        }


        } catch (Exception e) {
            System.err.println("Erro ao processar o evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Erro interno ao processar o evento"));
        }

        return null;
    }




}