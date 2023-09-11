package ru.smarts.loans;

import lombok.Data;
import ru.smarts.client.Client;
import ru.smarts.credit.Credit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @Min(1)
    @Max(value = 2_000_000_000, message = "Должно быть не больше 2 млрд.")
    private double sumOfCredit;

    @Min(1)
    @Max(360)
    private double creditTermInMonths;
}
