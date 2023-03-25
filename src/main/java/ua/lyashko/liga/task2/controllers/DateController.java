package ua.lyashko.liga.task2.controllers;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.lyashko.liga.task2.service.DateServiceNew;

@RestController
@RequiredArgsConstructor
public class DateController {
    private final DateServiceNew dateService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequestMapping("/date")
    @Operation(description = "An endpoint to mark dates in HTML file")
    public String getMark ( MultipartFile file ) {
        return dateService.markDates ( file );
    }
}
