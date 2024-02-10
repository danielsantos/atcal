package com.teoali.atcal.repository;


import com.teoali.atcal.domain.Client;
import com.teoali.atcal.domain.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  List<Payment> findByClient(Client client);

}
