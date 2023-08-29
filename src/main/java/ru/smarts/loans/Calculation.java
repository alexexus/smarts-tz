package ru.smarts.loans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;


@Getter
@Setter
@ToString
public class Calculation {

    @Min(value = 10000, message = "Min sum 10 000 RUB")
    private int creditAmount; // Сумма кредита

    @Min(value = 1, message = "Min percent rate 1%")
    private double percentRate; // Процентная ставка

    @Min(value = 1, message = "Min credit term 1 month")
    @Max(value = 360, message = "Max credit term 360 months")
    private int creditTerm; // Срок кредитования

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date currentDate; // Дата выдачи кредита
    private double monthlyPaymentAmount;  // Сумма ежемесячного платежа
    private double monthlyPercent; // Ежемесячный начисленный процент
    private double bodyOfCredit; // Тело кредита
    private double remainder; // Остаток долга

    public Calculation() {
    }

    public Calculation(int creditAmount, double percentRate, int creditTerm, Date currentDate) {
        this.creditAmount = creditAmount;
        this.percentRate = percentRate;
        this.creditTerm = creditTerm;
        this.currentDate = currentDate;
    }

    public Calculation(Date currentDate, double monthlyPaymentAmount, double monthlyPercent, double bodyOfCredit, double remainder) {
        this.currentDate = currentDate;
        this.monthlyPaymentAmount = monthlyPaymentAmount;
        this.monthlyPercent = monthlyPercent;
        this.bodyOfCredit = bodyOfCredit;
        this.remainder = remainder;
    }
}
