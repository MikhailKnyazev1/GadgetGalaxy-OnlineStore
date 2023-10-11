package com.example.gadgetgalaxy.converter;

import org.springframework.core.convert.converter.Converter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        // Преобразование строки в LocalDate (без времени) и добавление времени 00:00:00
        return LocalDateTime.of(LocalDate.parse(source), LocalTime.MIDNIGHT);
    }
}
