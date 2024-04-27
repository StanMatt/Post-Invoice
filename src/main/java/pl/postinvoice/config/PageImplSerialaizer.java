package pl.postinvoice.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;

public class PageImplSerialaizer extends JsonSerializer<PageImpl> {
    @Override
    public void serialize(PageImpl page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("content", page.getContent());
        jsonGenerator.writeNumberField("totalNumbers", page.getTotalElements());
        jsonGenerator.writeEndObject();

    }
}
