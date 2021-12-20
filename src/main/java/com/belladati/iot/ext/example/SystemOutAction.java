package com.belladati.iot.ext.example;

import com.belladati.iot.collector.common.Field;
import com.belladati.iot.collector.common.FieldType;
import com.belladati.iot.collector.sender.Action;
import com.belladati.iot.collector.sender.Sender;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class SystemOutAction implements Action {

    private JsonObject config;
    private int iterator = 0;

    @Override
    public String actionId() {
        return "SYSOUT";
    }

    @Override
    public String actionName() {
        return "System Out";
    }

    @Override
    public Future<Void> process(JsonObject row) {
        System.out.println((Boolean.parseBoolean(config.getString("counter")) ? ++iterator + ": " : "")+config.getString("prefix") + row.encodePrettily());
        return Future.succeededFuture();
    }

    @Override
    public void configure(Sender sender, JsonObject jsonObject) {
        config = jsonObject;
    }

    @Override
    public Future<Void> init() {
        return Future.succeededFuture();
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public Future<Void> close() {
        return Future.succeededFuture();
    }

    @Override
    public Map<String, Field> configurationFields() {
        Map<String, Field> f = new HashMap<>();
        f.put("prefix", new Field("Prefix", FieldType.TEXT));
        f.put("counter", new Field("Counter", FieldType.BOOLEAN));
        return f;
    }
}
