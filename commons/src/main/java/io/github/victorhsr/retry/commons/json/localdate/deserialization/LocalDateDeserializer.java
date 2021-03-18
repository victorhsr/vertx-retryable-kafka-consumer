package io.github.victorhsr.retry.commons.json.localdate.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.victorhsr.retry.commons.date.localdate.LocalDateParser;
import io.github.victorhsr.retry.commons.json.localdate.serialization.LocalDateSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class LocalDateDeserializer extends StdDeserializer<LocalDate> {

    private final LocalDateParser localDateParser = new LocalDateParser();

    public LocalDateDeserializer() {
        this(null);
    }

    public LocalDateDeserializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public LocalDate deserialize(final JsonParser jsonparser, final DeserializationContext ctxt) throws IOException {

        final String date = jsonparser.getText();
        final LocalDate localDate = this.localDateParser.deserialize(date, LocalDateSerializer.LOCAL_DATE_FORMAT);
        return localDate;
    }
}
