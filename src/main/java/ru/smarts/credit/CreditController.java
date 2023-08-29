package ru.smarts.credit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController {

    private final CreditRepository repository;

    @GetMapping
    public String showIndex() {
        return "redirect:/credits/index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Credit credit) {
        return "credits/add-credit";
    }

    @PostMapping("/addCredit")
    public String addCredit(@Valid Credit credit, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "credits/add-credit";
        }
        repository.save(credit);
        return "redirect:/credits/index";
    }

    @GetMapping("/index")
    public String showCreditsList(Model model) {
        model.addAttribute("credits", repository.findAll());
        return "credits/credits-index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Credit credit = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credit Id:" + id));

        model.addAttribute("credit", credit);
        return "credits/update-credit";
    }

    @PostMapping("/update/{id}")
    public String updateCredit(@PathVariable("id") long id, @Valid Credit credit,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            credit.setId(id);
            return "credits/update-credit";
        }

        repository.save(credit);
        return "redirect:/credits/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteCredit(@PathVariable("id") long id, Model model) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credit Id:" + id));
        repository.deleteById(id);
        return "redirect:/credits/index";
    }
}
