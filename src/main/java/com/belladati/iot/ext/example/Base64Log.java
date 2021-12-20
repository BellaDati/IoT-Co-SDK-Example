package com.belladati.iot.ext.example;

import com.belladati.iot.collector.sender.Action;
import com.belladati.iot.collector.sender.Sender;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Log implements Action {

    private static final Logger log = LoggerFactory.getLogger(Base64Log.class);

    @Override
    public String actionId() {
        return "BASE64";
    }

    @Override
    public String actionName() {
        return "Base 64 Logger";
    }

    @Override
    public Future<Void> process(JsonObject row) {
        final String b64 = Base64.getEncoder().encodeToString(row.encode().getBytes(StandardCharsets.UTF_8));
        log.info("Encoded message: {}", b64);
        return Future.succeededFuture();
    }

    @Override
    public void configure(Sender sender, JsonObject jsonObject) {

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
