package com.teoali.atcal.repository;


import com.teoali.atcal.domain.Client;
import com.teoali.atcal.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
