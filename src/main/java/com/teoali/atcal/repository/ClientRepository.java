package com.teoali.atcal.repository;


import com.teoali.atcal.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
