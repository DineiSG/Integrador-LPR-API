package com.autoshopping.integrated.api.lpr.leitura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeituraRepository extends JpaRepository<Leitura, Long> {
    Optional<Leitura> findByPlacaIgnoreCase(String placa);
}
