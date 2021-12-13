package com.belladati.iot.ext.example;

import com.belladati.iot.collector.generic.sender.verticle.Sender;
import com.belladati.iot.collector.generic.sender.verticle.action.Action;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class SystemOutAction implements Action {

    private JsonObject config;

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
        System.out.println(config.getString("prefix") + row.encodePrettily());
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
}
