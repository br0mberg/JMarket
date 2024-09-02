package ru.brombin.JMarket.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.dto.PersonDTO;
import ru.brombin.JMarket.model.Person;
import ru.brombin.JMarket.model.PersonRole;
import ru.brombin.JMarket.services.PersonService;
import ru.brombin.JMarket.util.PersonValidator;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonService personService;
    private final PersonValidator personValidator;
    private final ModelMapper modelMapper;
    @Autowired
    public PeopleController(PersonService personService, PersonValidator personValidator, ModelMapper modelMapper) {
        this.personService = personService;
        this.personValidator = personValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personService.findAll());
        return "item/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person person = personService.findOne(id);
        model.addAttribute("person", person);
        return "item/show";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("roles", PersonRole.values());
        return "person/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid PersonDTO personDTO, BindingResult bindingResult) {
        personValidator.validate(personDTO, bindingResult);
        if (bindingResult.hasErrors()) return "item/new";

        personService.save(convertToPerson(personDTO));
        return "redirect:/people";
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        PersonDTO personDTO = convertToPersonDTO(personService.findOne(id));
        model.addAttribute("person", personDTO);
        return "person/edit";
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid PersonDTO personDTO, BindingResult bindingResult, @PathVariable("id") int id) {
        personValidator.validate(personDTO, bindingResult);

        if (bindingResult.hasErrors()) return "person/edit";

        personService.update(id, convertToPerson(personDTO));
        return "redirect:/items/" + id;
    }

    //DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.delete(id);
        return "redirect:/people";
    }
}
