package com.teoali.atcal.repository;


import com.teoali.atcal.domain.Group;
import com.teoali.atcal.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

  List<Group> findByUser(User user);

}
