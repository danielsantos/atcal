package com.teoali.atcal.repository;

import com.teoali.atcal.domain.Client;
import com.teoali.atcal.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

  List<Client> findByUser(User user);

}
