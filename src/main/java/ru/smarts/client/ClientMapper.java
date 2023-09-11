package ru.smarts.client;

import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientDto toClientDto(Client client) {
        return ClientDto.builder()
                .fio(client.getFio())
                .email(client.getEmail())
                .phone(client.getPhone())
                .build();
    }
}
