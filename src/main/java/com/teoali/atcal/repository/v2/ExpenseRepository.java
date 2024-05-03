package com.teoali.atcal.repository.v2;

import com.teoali.atcal.domain.Payment;
import com.teoali.atcal.domain.v2.Expense;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  @Query("select e from Expense e where YEAR(e.date) = ?1 and MONTH(e.date) = ?2")
  List<Expense> getByReferMonth(Integer year, Integer month);

}
