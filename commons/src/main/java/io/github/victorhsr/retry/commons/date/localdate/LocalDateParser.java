package io.github.victorhsr.retry.commons.date.localdate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class LocalDateParser {

    public String serialize(final LocalDate localDate, final String datePattern) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        final String dateString = localDate.format(formatter);
        return dateString;
    }

    public LocalDate deserialize(final String dateString, final String datePattern) {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        final LocalDate localDate = LocalDate.parse(dateString, formatter);

        return localDate;
    }

}
