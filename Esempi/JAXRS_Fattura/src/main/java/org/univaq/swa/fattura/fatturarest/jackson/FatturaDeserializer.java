package org.univaq.swa.fattura.fatturarest.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.univaq.swa.fattura.fatturarest.model.Anagrafica;
import org.univaq.swa.fattura.fatturarest.model.Fattura;
import org.univaq.swa.fattura.fatturarest.model.Prodotto;

public class FatturaDeserializer extends JsonDeserializer<Fattura> {

    @Override
    public Fattura deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        Fattura f = new Fattura();
        
        JsonNode node = jp.getCodec().readTree(jp);

        if (node.has("numero")) {
            f.setNumero(node.get("numero").asInt());
        }

        if (node.has("data")) {
            f.setData(jp.getCodec().treeToValue(node.get("data"), Calendar.class));
        }
        if (node.has("intestatario")) {
            f.setIntestatario(jp.getCodec().treeToValue(node.get("intestatario"), Anagrafica.class));
        }

        if (node.has("elementi")) {
            JsonNode ne = node.get("elementi");
            List<Prodotto> elementi = new ArrayList<>();
            f.setElementi(elementi);
            for (int i = 0; i < ne.size(); ++i) {
                elementi.add(jp.getCodec().treeToValue(ne.get(i), Prodotto.class));
            }           
        }
        if (node.has("totali")) {
            JsonNode nt = node.get("totali");
            if (nt.has("totaleIVAEsclusa")) {
                f.setTotaleIVAEsclusa(nt.get("totaleIVAEsclusa").asDouble());
            }
            if (nt.has("totaleIVA")) {
                f.setTotaleIVA(nt.get("totaleIVA").asDouble());
            }
            if (nt.has("totaleIVAInclusa")) {
                f.setTotaleIVAInclusa(nt.get("totaleIVAInclusa").asDouble());
            }
        }

        return f;
    }
}
