package ua.lyashko.liga.task1.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ua.lyashko.liga.task1.model.Person;
import ua.lyashko.liga.task1.service.PersonService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequestMapping("/person")
    @Operation(description = "An endpoint to extract list of persons from file")
    public List<Person> getPeople ( MultipartFile file ) throws IOException {
        return personService.getPersonList ( file );
    }
}
