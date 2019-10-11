package npc.sapsan.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class ReverseVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.eventBus().consumer("reverse-address", message -> {
      message.reply(new StringBuilder(message.body().toString()).reverse().toString());
    });
  }
}

