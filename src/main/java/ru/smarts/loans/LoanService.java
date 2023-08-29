package ru.smarts.loans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Getter
@Setter
public class LoanService {

    private int enteredCreditAmount; // Введенная сумма кредита в поле ввода
    private double enteredPercentRate; // Введенная процентная ставка в поле ввода
    private int enteredCreditTerm; // Введенный срок кредита в поле ввода
    private Date enteredCurrentDate; // Введенная дата выдачи кредита в поле ввода
    private double overPayment; // Суммарная переплата по кредиту
    private double overPaymentPercent; // Суммарная переплата по кредиту в процентах
    private double totalSum;


    public List<Calculation> getCalculationList(Calculation calculation) {

        // Запоминание введенных в форму значений
        initEnteredData(calculation);

        List<Calculation> calculations = new ArrayList<>();

        getFirstLineOfPayment(calculation, calculations);

        // Остаток долга на момент 1-го платежа
        double balanceOwed = calculation.getCreditAmount();

        // Доля процентной ставки (в месяц)
        double shareOfPercentRate = calculation.getPercentRate() / 100.0 / 12.0;

        // Сумма ежемесячного платежа. Значение постоянное
        double monthlyPaymentAmount = calculation.getCreditAmount() * (shareOfPercentRate + shareOfPercentRate /
                (Math.pow((1 + shareOfPercentRate), calculation.getCreditTerm()) - 1));

        // Ежемесячный начисленный процент на момент 1-го платежа
        double monthlyPercent = calculation.getCreditAmount() * shareOfPercentRate;

        // Тело кредита на момент 1-го платежа
        double bodyOfCredit = monthlyPaymentAmount - monthlyPercent;

        for (int i = 1; i <= calculation.getCreditTerm() + 1; i++) {
            if (balanceOwed < monthlyPaymentAmount) {
                monthlyPercent = balanceOwed * shareOfPercentRate;
                balanceOwed = 0.0;
                monthlyPaymentAmount = monthlyPercent + bodyOfCredit;
            } else {
                monthlyPercent = balanceOwed * shareOfPercentRate;
                balanceOwed = balanceOwed - bodyOfCredit;
                bodyOfCredit = monthlyPaymentAmount - monthlyPercent;
            }

            overPayment += monthlyPercent;
            overPaymentPercent = calculation.getCreditAmount() / overPayment;
            totalSum = calculation.getCreditAmount() + overPayment;

            Calculation newCalculation = new Calculation(getDateOfNextPayment(calculation, i),
                    monthlyPaymentAmount, monthlyPercent, bodyOfCredit, balanceOwed);

            calculations.add(newCalculation);
        }

        return calculations;
    }

    private void getFirstLineOfPayment(Calculation calculation, List<Calculation> calculations) {
        calculation.setRemainder(calculation.getCreditAmount());
        calculations.add(calculation);
    }

    private Date getDateOfNextPayment(Calculation calculation, int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calculation.getCurrentDate());
        calendar.add(Calendar.MONTH, +count);
        return calendar.getTime();
    }

    private void initEnteredData(Calculation calculation) {
        enteredCreditAmount = calculation.getCreditAmount();
        enteredPercentRate = calculation.getPercentRate();
        enteredCreditTerm = calculation.getCreditTerm();
        enteredCurrentDate = calculation.getCurrentDate();
    }
}
