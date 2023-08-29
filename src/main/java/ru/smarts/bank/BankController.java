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
import ru.smarts.credit.Credit;

import javax.validation.Valid;

@Controller
@RequestMapping("/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankRepository repository;

    @GetMapping
    public String showIndex() {
        return "redirect:/banks/index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Bank bank) {
        return "banks/add-bank";
    }

    @PostMapping("/addBank")
    public String addBank(Bank bank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "banks/add-bank";
        }
        checkClientsAndCredits(bank);
        repository.save(bank);
        return "redirect:/banks/index";
    }

    @GetMapping("/index")
    public String showBanksList(Model model) {
        model.addAttribute("banks", repository.findAll());
        return "banks/banks-index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Bank bank = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bank Id:" + id));

        model.addAttribute("bank", bank);
        return "banks/update-bank";
    }

    @PostMapping("/update/{id}")
    public String updateBank(@PathVariable("id") long id, @Valid Bank bank,
                             BindingResult result, Model model) {
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
