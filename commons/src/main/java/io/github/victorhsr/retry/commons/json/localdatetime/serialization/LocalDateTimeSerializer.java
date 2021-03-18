package io.github.victorhsr.retry.commons.json.localdatetime.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.victorhsr.retry.commons.date.localdatetime.LocalDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    public static String LOCAL_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    private final LocalDateTimeParser localDateTimeParser = new LocalDateTimeParser();

    public LocalDateTimeSerializer() {
        this(null);
    }

    public LocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(final LocalDateTime value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
        String dateTimeString = this.localDateTimeParser.serialize(value, LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMAT);
        gen.writeString(dateTimeString);
    }

}