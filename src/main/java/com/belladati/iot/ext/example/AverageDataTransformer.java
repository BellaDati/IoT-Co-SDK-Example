package com.belladati.iot.ext.example;

import com.belladati.iot.collector.sender.DataTransformer;
import com.belladati.iot.collector.sender.Sender;
import com.belladati.iot.collector.sender.TransformedData;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class AverageDataTransformer implements DataTransformer {

    private String[] fields;

    @Override
    public String transformationId() {
        return "AVERAGE";
    }

    @Override
    public String transformationName() {
        return "Average transformation";
    }

    @Override
    public void init(JsonObject jsonObject, Sender sender) {
        fields = new JsonObject(jsonObject.getString("jsonConfig")).getString("fields").split(",");
    }

    @Override
    public TransformedData transform(JsonArray jsonArray) {
        Map<String, Double> totals = new HashMap<>();
        for (String field : fields) {
            totals.put(field, 0d);
        }
        JsonObject lastRow = null;
        for (Object row : jsonArray) {
            lastRow = (JsonObject) row;
            JsonObject o = new JsonObject(lastRow.getString("JSON"));
            for (String field : fields) {
                totals.put(field, totals.get(field) + o.getDouble(field));
            }
        }
        JsonObject data = new JsonObject(lastRow.getString("JSON"));
        for (String field : fields) {
            data.put(field, totals.get(field) / jsonArray.size());
        }
        lastRow.put("JSON", data.toString());

        return new TransformedData(null, new JsonArray().add(lastRow));
    }
}
