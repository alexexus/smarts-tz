package ru.smarts.loans;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.smarts.client.ClientRepository;
import ru.smarts.credit.CreditRepository;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;
    private final LoanRepository repository;
    private final LoanMapper loanMapper;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;

    @GetMapping
    public String showIndex() {
        return "redirect:/loans/index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Loan loan, Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("credits", creditRepository.findAll());
        return "loans/add-loan";
    }

    @PostMapping("/addLoan")
    public String addLoan(@Valid Loan loan, BindingResult result, Model model) {
        loan.setClient(clientRepository.findById(loan.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid client Id:" + loan.getClient().getId())));
        loan.setCredit(creditRepository.findById(loan.getCredit().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credit Id:" + loan.getCredit().getId())));
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("credits", creditRepository.findAll());
        if (result.hasErrors()) {
            return "loans/add-loan";
        }
        checkClientAndCredit(loan);
        repository.save(loan);
        return "redirect:/loans/index";
    }

    @GetMapping("/index")
    public String showLoansList(Model model) {
        List<LoanDto> loans = repository.findAll().stream().map(loanMapper::toLoanDto).collect(Collectors.toList());
        model.addAttribute("loans", loans);
        return "loans/loans-index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Loan loan = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + id));

        model.addAttribute("loan", loan);
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("credits", creditRepository.findAll());
        return "loans/update-loan";
    }

    @PostMapping("/update/{id}")
    public String updateLoan(@PathVariable("id") long id, @Valid Loan loan,
                             BindingResult result, Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("credits", creditRepository.findAll());
        if (result.hasErrors()) {
            loan.setId(id);
            return "loans/update-loan";
        }
        checkClientAndCredit(loan);
        repository.save(loan);
        return "redirect:/loans/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteLoan(@PathVariable("id") long id, Model model) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + id));
        repository.deleteById(id);
        return "redirect:/loans/index";
    }

    @GetMapping("/calculations/{id}")
    public String create(@PathVariable long id, Model model) {
        Loan loan = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + id));
        Calculation calculation = new Calculation((int) loan.getSumOfCredit(), loan.getCredit().getPercent(),
                (int) loan.getCreditTermInMonths(), new Date());

        model.addAttribute("calculations", service.getCalculationList(calculation));
        model.addAttribute("overPayment", service.getOverPayment());
        model.addAttribute("overPaymentPercent", service.getOverPaymentPercent());
        model.addAttribute("totalSum", service.getTotalSum());

        service.setOverPayment(0.0);
        service.setOverPaymentPercent(0.0);
        service.setTotalSum(0.0);

        return "loans/calcs";
    }

    private void checkClientAndCredit(Loan loan) {
        if (loan.getClient() == null) {
            throw new IllegalArgumentException("Invalid client Id");
        }
        if (loan.getCredit() == null) {
            throw new IllegalArgumentException("Invalid credit Id");
        }
        if (loan.getSumOfCredit() > loan.getCredit().getLimit()) {
            throw new ValidationException("Sum of credit > limit of credit");
        }
    }
}
