package ru.smarts.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Set<Client> findByIdIn(Set<Long> ids);
}
