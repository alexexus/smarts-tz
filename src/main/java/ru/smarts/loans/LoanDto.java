package ru.smarts.loans;

import lombok.Builder;
import lombok.Data;
import ru.smarts.client.ClientDto;
import ru.smarts.credit.CreditDto;

@Data
@Builder
public class LoanDto {

    private long id;
    private ClientDto client;
    private CreditDto credit;
    private double sumOfCredit;
    private double creditTermInMonths;
}
