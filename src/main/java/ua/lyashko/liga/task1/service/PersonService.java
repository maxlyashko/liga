package ua.lyashko.liga.task1.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.text.WordUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.lyashko.liga.task1.model.Person;
import ua.lyashko.liga.task1.enums.MarkedWords;
import ua.lyashko.liga.task1.enums.PersonType;
import ua.lyashko.liga.task1.enums.SurnameAffixes;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final List<Person> people = new ArrayList<> ( );
    private final String pathToNameFile = "src/main/resources/first_names.json";

    @SneakyThrows
    public List<Person> getPersonList ( MultipartFile file ) {
        Object obj = new JSONParser ( ).parse ( new FileReader ( pathToNameFile ) );
        JSONObject json = (JSONObject) obj;
        List<String> names = getLinesFromFile ( file );
        removeIfMarked ( names );
        for (String string : names) {
            service ( string , json );
        }
        return people;
    }

    @SneakyThrows
    private List<String> getLinesFromFile ( MultipartFile file ) {
        BufferedReader reader = new BufferedReader ( new InputStreamReader ( file.getInputStream ( ) ) );
        List<String> lines = new ArrayList<> ( );
        while ( reader.ready ( ) ) {
            lines.add ( reader.readLine ( ) );
        }
        return lines;
    }

    private boolean isPersonType ( String string ) {
        for (PersonType type : PersonType.values ( )) {
            if (type.name ( ).equals ( string.toUpperCase ( ) )) {
                return true;
            }
        }
        return false;
    }

    private boolean isAffix ( String string ) {
        for (SurnameAffixes affix : SurnameAffixes.values ( )) {
            if (affix.name ( ).equals ( string.toUpperCase ( ) )) {
                return true;
            }
        }
        return false;
    }

    private boolean isName ( String string , JSONObject json ) {
        return json.containsKey ( capitalize ( string ) );
    }

    private static boolean isMarked ( String string ) {
        for (MarkedWords words : MarkedWords.values ( )) {
            if (words.name ( ).equals ( string.toUpperCase ( ) )) {
                return true;
            }
        }
        return false;
    }

    private void removeIfMarked ( List<String> list ) {
        for (int i = 0; i < list.size ( ); i++) {
            String[] tArrays = list.get ( i ).replaceAll ( "[^a-zA-Z0-9]" , " " ).split ( "[ ]|[.]|[,]|[ \t]" );
            List<String> line = new ArrayList<> ( Arrays.asList ( tArrays ) );
            line.removeAll ( Arrays.asList ( "" , null ) );
            line.removeIf ( PersonService::isMarked );
            list.set ( i , String.join ( " " , line ) );
        }
    }

    private String capitalize ( String string ) {
        return WordUtils.capitalizeFully ( string );
    }

    private void fillPersonType ( Person person , String str , List<String> list ) {
        person.setPersonType ( str );
        if (list.size ( ) == 2 && list.indexOf ( str ) == 1) {
            person.setSurname ( list.get ( 0 ) );
        } else if (list.size ( ) == 2 && list.indexOf ( str ) == 0) {
            person.setSurname ( list.get ( 1 ) );
        }
    }

    private void fillAffix ( Person person , String str ) {
        if (person.getSurname ( ) == null) {
            person.setSurname ( str );
        } else {
            String temp = person.getSurname ( );
            person.setSurname ( str + " " + temp );
        }
    }

    private void isNameOnFirstPlace ( Person person , String str , List<String> list ) {
        person.setName ( str );
        person.setSurname ( list.get ( 1 ) );
    }

    private void isNameOnSecondPlace ( Person person , String str , List<String> list ) {
        person.setName ( str );
        person.setSurname ( list.get ( 0 ) );
    }

    private void isNameInBigSentence ( Person person , String str , List<String> list ) {
        person.setName ( str );
        String nextWord = ( list.get ( list.indexOf ( str ) + 1 ) );
        String tempSurname = "";
        if (person.getSurname ( ) != null) {
            tempSurname = person.getSurname ( );
        } else {
            person.setSurname ( nextWord );
        }
        while ( isAffix ( nextWord ) ) {
            person.setSurname ( tempSurname + " " + nextWord );
            nextWord = list.get ( list.indexOf ( nextWord ) + 1 );
            tempSurname = person.getSurname ( );
        }
        if (person.getSurname ( ) != null) {
            person.setSurname ( tempSurname + " " + nextWord );
        }
    }


    private void fillName ( Person person , String str , List<String> list ) {
        if (list.size ( ) == 2 && list.indexOf ( str ) == 0) {
            isNameOnFirstPlace ( person , str , list );
        } else if (list.size ( ) == 2 && list.indexOf ( str ) == 1) {
            isNameOnSecondPlace ( person , str , list );
        } else if (list.indexOf ( str ) + 1 < list.size ( )) {
            isNameInBigSentence ( person , str , list );
        }
    }

    private void fillSurname ( Person person , String str ) {
        if (person.getSurname ( ) == null
                && !str.equals ( person.getPersonType ( ) )
                && !str.equals ( person.getName ( ) )) {
            person.setSurname ( str );
        }
        if (person.getName ( ) != null && person.getSurname ( ) != null) {
            person.setSurname ( person.getSurname ( ) + " " + str );
        }
    }

    private void service ( String string , JSONObject json ) {
        Person person = new Person ( );
        List<String> list = Arrays.stream ( string.split ( " " ) ).toList ( );
        for (String str : list) {
            if (isPersonType ( str )) {
                fillPersonType ( person , str , list );
            }
            if (isAffix ( str )) {
                fillAffix ( person , str );
            }
            if (isName ( str , json )
                    && !str.equals ( person.getPersonType ( ) )
                    && !str.equals ( person.getSurname ( ) )) {
                fillName ( person , str , list );
                break;
            }
            fillSurname ( person , str );
        }
        if (person.getPersonType ( ) != null || person.getName ( ) != null || person.getSurname ( ) != null) {
            people.add ( person );
        }
    }
}
