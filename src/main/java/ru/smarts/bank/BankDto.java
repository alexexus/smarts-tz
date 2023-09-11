package ru.smarts.bank;

import lombok.Builder;
import lombok.Data;
import ru.smarts.client.ClientDto;
import ru.smarts.credit.CreditDto;

import java.util.Set;

@Data
@Builder
public class BankDto {

    private long id;
    private Set<ClientDto> clients;
    private Set<CreditDto> credits;
}
