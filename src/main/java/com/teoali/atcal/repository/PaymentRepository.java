package com.teoali.atcal.repository;


import com.teoali.atcal.domain.Client;
import com.teoali.atcal.domain.Payment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  List<Payment> findByClient(Client client);

  @Query("select p from Payment p inner join Client c on c.id = p.client.id inner join User u on u.id = c.user.id where u.id = ?1 and p.status = 2")
  List<Payment> getDebtPayments(Long userId);

  @Query("select SUM(p.amount) from Payment p inner join Client c on c.id = p.client.id inner join User u on u.id = c.user.id where u.id = ?1 and p.status = 0 and p.dueDate between ?2 and ?3")
  BigDecimal getSumAmountToReceive(Long userId, LocalDate startDate, LocalDate endDate);

  @Query("select p from Payment p inner join Client c on c.id = p.client.id inner join User u on u.id = c.user.id where u.id = ?1 and p.status = 0 and p.dueDate between ?2 and ?3")
  List<Payment> getPaymentsToReceive(Long userId, LocalDate startDate, LocalDate endDate);

}
