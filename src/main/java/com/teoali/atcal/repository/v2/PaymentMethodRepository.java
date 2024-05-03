package com.teoali.atcal.repository.v2;

import com.teoali.atcal.domain.v2.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
