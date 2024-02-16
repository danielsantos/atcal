package com.teoali.atcal.repository;


import com.teoali.atcal.domain.Client;
import com.teoali.atcal.domain.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  List<Payment> findByClient(Client client);

  @Query("select p from Payment p inner join Client c on c.id = p.client.id inner join User u on u.id = c.user.id where u.id = ?1 and p.status = 2")
  List<Payment> getDebtPayments(Long id);

}
