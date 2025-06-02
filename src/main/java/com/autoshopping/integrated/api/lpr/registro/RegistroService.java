package com.autoshopping.integrated.api.lpr.registro;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RegistroService {

        @Autowired
        private RegistroRepository registroRepository;


        private Iterable<Registro> optional;
        private Integer id_registro;

        //Buscando todos os registros de acesso
        public Iterable <Registro> getRegistro(){return registroRepository.findAll();}

        public Registro insert(Registro registro) {return registroRepository.save(registro);}

        public Iterable <Registro> getAcessosByPlaca(String placa){return  registroRepository.getAcessosByPlaca(placa);};







}
