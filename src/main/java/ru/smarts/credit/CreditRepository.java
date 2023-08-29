package ru.smarts.credit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CreditRepository extends JpaRepository<Credit, Long> {

    Set<Credit> findByIdIn(Set<Long> ids);
}
