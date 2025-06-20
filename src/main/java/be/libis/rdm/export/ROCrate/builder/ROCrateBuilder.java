package be.libis.rdm.export.ROCrate.builder;
import java.util.LinkedHashMap;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;


public class ROCrateBuilder {
    final LinkedHashMap<String, ROCrateEntity> entities;

    public ROCrateBuilder() {
        this.entities = new LinkedHashMap<String, ROCrateEntity>();
    }



    public ROCrateEntity get(final String entityId) {
        final ROCrateEntity entity;
        if (this.entities.get(entityId)==null) {
            entity = new ROCrateEntity();
            this.entities.put(entityId, entity);
        } else {
            entity = this.entities.get(entityId);
            entity.putProperty("@id", entityId);
        }
        return entity;
    }

    public void put(final String entityId, final ROCrateEntity entity) {
        this.entities.put(entityId, entity);
    }


    public void upsertEntity(final ROCrateEntity entity) {
        String id = entity.get("@id").values.get(0);
        this.get(id).updateProperties(entity.getProperties());

    }

    public JsonObject build( ) {
        final JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        final  JsonArrayBuilder graph = Json.createArrayBuilder();
        for (final ROCrateEntity entity:this.entities.values()) {
            JsonObject properties = entity.asJsonArray();
            graph.add(properties);
        }
        String contextString =
        JSONObject contextObject = new JSONObject("""
        {
            "dct": "http://purl.org/dc/terms/",
            "dcat": "http://www.w3.org/ns/dcat#",
            "xsd": "http://www.w3.org/2001/XMLSchema#",
            "vcard": "http://www.w3.org/2006/vcard/ns#",
            "prov": "http://www.w3.org/ns/prov#",
            "rivm": "http://data.rivm.nl/ontology/terms#",
            "rr": "http://www.w3.org/ns/r2rml#",
            "rml": "http://semweb.mmlab.be/ns/rml#",
            "ql": "http://semweb.mmlab.be/ns/ql#",
            "ex": "http://example.com/ontology/terms#",
            "foaf": "http://xmlns.com/foaf/0.1/"
        }
        """);
        jsonObjectBuilder.add("@context", contextObject);
        jsonObjectBuilder.add("@graph", graph);
        return jsonObjectBuilder.build();
    }

}
