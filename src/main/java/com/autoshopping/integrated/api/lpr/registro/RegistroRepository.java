package com.autoshopping.integrated.api.lpr.registro;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface RegistroRepository extends JpaRepository<Registro, Integer> {


    Iterable<Registro> getAcessosByPlaca(String placa);
}
