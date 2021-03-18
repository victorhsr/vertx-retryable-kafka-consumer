package io.github.victorhsr.retry.commons.json.localdate.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.victorhsr.retry.commons.date.localdate.LocalDateParser;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class LocalDateSerializer extends StdSerializer<LocalDate> {

    public static String LOCAL_DATE_FORMAT = "dd/MM/yyyy";

    private final LocalDateParser localDateParser = new LocalDateParser();

    public LocalDateSerializer() {
        this(null);
    }

    public LocalDateSerializer(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public void serialize(final LocalDate value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
        final String dateString = this.localDateParser.serialize(value, LocalDateSerializer.LOCAL_DATE_FORMAT);
        gen.writeString(dateString);
    }

}