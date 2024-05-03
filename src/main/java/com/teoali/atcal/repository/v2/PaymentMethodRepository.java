package com.teoali.atcal.repository.v2;

import com.teoali.atcal.domain.v2.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

}
