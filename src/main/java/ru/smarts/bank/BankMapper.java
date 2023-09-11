package ru.smarts.bank;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.smarts.client.ClientMapper;
import ru.smarts.credit.CreditMapper;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BankMapper {

    private final ClientMapper clientMapper;
    private final CreditMapper creditMapper;

    public BankDto toBankDto(Bank bank) {
        return BankDto.builder()
                .id(bank.getId())
                .clients(bank.getClients().stream().map(clientMapper::toClientDto).collect(Collectors.toSet()))
                .credits(bank.getCredits().stream().map(creditMapper::toCreditDto).collect(Collectors.toSet()))
                .build();
    }
}
