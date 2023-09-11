package ru.smarts.credit;

import org.springframework.stereotype.Component;

@Component
public class CreditMapper {

    public CreditDto toCreditDto(Credit credit) {
        return CreditDto.builder()
                .limit(credit.getLimit())
                .percent(credit.getPercent())
                .build();
    }
}
