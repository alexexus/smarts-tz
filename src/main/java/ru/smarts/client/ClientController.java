package ru.smarts.client;

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
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository repository;

    @GetMapping
    public String showIndex() {
        return "redirect:/clients/index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Client client) {
        return "clients/add-client";
    }

    @PostMapping("/addClient")
    public String addClient(@Valid Client client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "clients/add-client";
        }
        repository.save(client);
        return "redirect:/clients/index";
    }

    @GetMapping("/index")
    public String showClientList(Model model) {
        model.addAttribute("clients", repository.findAll());
        return "clients/clients-index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client Id:" + id));

        model.addAttribute("client", client);
        return "clients/update-client";
    }

    @PostMapping("/update/{id}")
    public String updateClient(@PathVariable("id") long id, @Valid Client client,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            client.setId(id);
            return "clients/update-client";
        }

        repository.save(client);
        return "redirect:/clients/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable("id") long id, Model model) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client Id:" + id));
        repository.deleteById(id);
        return "redirect:/clients/index";
    }
}
