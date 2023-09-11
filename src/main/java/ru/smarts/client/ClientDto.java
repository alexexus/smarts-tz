package ru.smarts.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDto {

    private String fio;
    private String phone;
    private String email;

    @Override
    public String toString() {
        return "Client{" +
                "fio='" + fio + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
