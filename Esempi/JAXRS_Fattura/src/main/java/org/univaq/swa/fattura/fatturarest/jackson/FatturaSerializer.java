package org.univaq.swa.fattura.fatturarest.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.univaq.swa.fattura.fatturarest.model.Fattura;

public class FatturaSerializer extends JsonSerializer<Fattura> {

    @Override
    public void serialize(Fattura item, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject(); // {
        jgen.writeNumberField("numero", item.getNumero()); // "numero": 1
        jgen.writeObjectField("data", item.getData()); // "data": "4/5/2020"
        jgen.writeObjectField("intestatario", item.getIntestatario());
        jgen.writeObjectField("elementi", item.getElementi());
        jgen.writeObjectFieldStart("totali"); // "totali": {
        jgen.writeNumberField("totaleIVAEsclusa", item.getTotaleIVAEsclusa());
        jgen.writeNumberField("totaleIVA", item.getTotaleIVA());
        jgen.writeNumberField("totaleIVAInclusa", item.getTotaleIVAInclusa());
        jgen.writeEndObject(); // }
        jgen.writeEndObject(); // }
    }
}
