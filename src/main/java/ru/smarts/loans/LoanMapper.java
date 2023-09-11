package ru.smarts.loans;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.smarts.client.ClientMapper;
import ru.smarts.credit.CreditMapper;

@Component
@AllArgsConstructor
public class LoanMapper {

    private final ClientMapper clientMapper;
    private final CreditMapper creditMapper;

    public LoanDto toLoanDto(Loan loan) {
        return LoanDto.builder()
                .id(loan.getId())
                .client(clientMapper.toClientDto(loan.getClient()))
                .credit(creditMapper.toCreditDto(loan.getCredit()))
                .sumOfCredit(loan.getSumOfCredit())
                .creditTermInMonths(loan.getCreditTermInMonths())
                .build();
    }
}
