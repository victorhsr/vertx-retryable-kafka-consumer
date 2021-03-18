package io.github.victorhsr.retry.commons.date.localdatetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class LocalDateTimeParser {

    public String serialize(final LocalDateTime localDateTime, final String dateTimePattern) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        final String dateString = localDateTime.format(formatter);
        return dateString;
    }

    public LocalDateTime deserialize(final String dateTimeString, final String dateTimePattern) {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        final LocalDateTime localDate = LocalDateTime.parse(dateTimeString, formatter);

        return localDate;
    }

}
