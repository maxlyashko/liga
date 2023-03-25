package ua.lyashko.liga.task2.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DateServiceNew {

    @Value("${mark.pattern}")
    private String markPattern;

    @Value("${date.regular.expression}")
    private String regex;

    @SneakyThrows
    public String markDates ( MultipartFile file ) {
        final var content = new String ( file.getBytes ( ) , StandardCharsets.UTF_8 );
        List<String> dates = getAllDates ( content );
        return markDates ( content , dates );
    }

    private String markDates ( String content , List<String> dates ) {
        String result = content;
        for (String date : dates) {
            result = StringUtils.replace ( result , date ,
                    getMarkPattern ( date ) );
        }
        return result;
    }

    private String getMarkPattern ( String date ) {
        return markPattern.replace ( "date" , date );
    }

    private List<String> getAllDates ( String content ) {
        Pattern pattern = Pattern.compile ( regex );
        Matcher matcher = pattern.matcher ( content );
        List<String> dateList = new ArrayList<> ( );

        while ( matcher.find ( ) ) {
            String match = matcher.group ( );
            dateList.add ( match.trim ( ) );
        }
        return dateList;
    }

}
