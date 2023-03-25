package ua.lyashko.liga.task1.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Person {
    private String name;
    private String surname;
    private String personType;
}
