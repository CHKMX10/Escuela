package com.jesus.escuela.repositories;

import com.jesus.escuela.entities.Maestros;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaestrosRepository extends JpaRepository<Maestros, Long> {

        boolean existsByEmailIgnoreCase(String email);

        boolean existsByTelefono(String telefono);

        boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

        boolean existsByTelefonoAndIdNot(String telefono, Long id);


}
