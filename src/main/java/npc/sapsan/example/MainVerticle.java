package npc.sapsan.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {

//      deploy second verticle
      vertx.deployVerticle(ReverseVerticle.class.getName(), res -> {
        if (res.succeeded()) {
          System.out.println("Secibd verticle deployed ok, deploymentID = " + res.result());
        } else {
          res.cause().printStackTrace();
        }
      });

//      processing request
      req.bodyHandler(b -> {
        vertx.eventBus().request("reverse-address", b.toString(), reply -> {
          if (reply.succeeded()) {
            req.response()
              .putHeader("content-type", "text/plain")
              .end(reply.result().body().toString());
          } else {
            System.out.println("No reply");
          }
        });

      });
    })
      .listen(8888, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }
}

