package ru.smarts.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.smarts.client.Client;
import ru.smarts.client.ClientRepository;
import ru.smarts.credit.Credit;
import ru.smarts.credit.CreditRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankRepository repository;
    private final BankMapper bankMapper;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;

    @GetMapping
    public String showIndex() {
        return "redirect:/banks/index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Bank bank, Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("credits", creditRepository.findAll());
        return "banks/add-bank";
    }

    @PostMapping("/addBank")
    public String addBank(Bank bank, BindingResult result, Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("credits", creditRepository.findAll());
        bank.setClients(clientRepository.findByIdIn(Collections.singleton(bank.getClient().getId())));
        bank.setCredits(creditRepository.findByIdIn(Collections.singleton(bank.getCredit().getId())));

        if (result.hasErrors()) {
            return "banks/add-bank";
        }
        checkClientsAndCredits(bank);
        repository.save(bank);
        return "redirect:/banks/index";
    }

    @GetMapping("/index")
    public String showBanksList(Model model) {
        List<BankDto> banks = repository.findAll().stream().map(bankMapper::toBankDto).collect(Collectors.toList());
        model.addAttribute("banks", banks);
        return "banks/banks-index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Bank bank = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bank Id:" + id));
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("credits", creditRepository.findAll());
        model.addAttribute("bank", bank);
        return "banks/update-bank";
    }

    @PostMapping("/update/{id}")
    public String updateBank(@PathVariable("id") long id, @Valid Bank bank,
                             BindingResult result, Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("credits", creditRepository.findAll());
        bank.setClients(clientRepository.findByIdIn(Collections.singleton(bank.getClient().getId())));
        bank.setCredits(creditRepository.findByIdIn(Collections.singleton(bank.getCredit().getId())));
        if (result.hasErrors()) {
            bank.setId(id);
            return "banks/update-bank";
        }
        checkClientsAndCredits(bank);
        repository.save(bank);
        return "redirect:/banks/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteBank(@PathVariable("id") long id, Model model) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bank Id:" + id));
        repository.deleteById(id);
        return "redirect:/banks/index";
    }

    private void checkClientsAndCredits(Bank bank) {
        for (Client c : bank.getClients()) {
            if (c == null) {
                throw new IllegalArgumentException("Invalid client Id");
            }
        }
        for (Credit c : bank.getCredits()) {
            if (c == null) {
                throw new IllegalArgumentException("Invalid credit Id");
            }
        }
    }
}
