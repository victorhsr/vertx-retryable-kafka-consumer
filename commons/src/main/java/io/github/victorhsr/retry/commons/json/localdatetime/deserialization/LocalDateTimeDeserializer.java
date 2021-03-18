package io.github.victorhsr.retry.commons.json.localdatetime.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.victorhsr.retry.commons.date.localdatetime.LocalDateTimeParser;
import io.github.victorhsr.retry.commons.json.localdatetime.serialization.LocalDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    private final LocalDateTimeParser localDateTimeParser = new LocalDateTimeParser();

    public LocalDateTimeDeserializer() {
        this(null);
    }

    public LocalDateTimeDeserializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public LocalDateTime deserialize(final JsonParser jsonparser, final DeserializationContext ctxt) throws IOException {

        final String date = jsonparser.getText();
        final LocalDateTime localDateTime = this.localDateTimeParser.deserialize(date, LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMAT);

        return localDateTime;
    }
}
