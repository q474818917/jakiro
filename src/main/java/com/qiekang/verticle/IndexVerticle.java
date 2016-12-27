package com.qiekang.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiekang.utils.Runner;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

public class IndexVerticle extends AbstractVerticle {
	
	private static Logger logger = LoggerFactory.getLogger(IndexVerticle.class);
	
	public static void main(String[] args) {
	    Runner.runExample(IndexVerticle.class);
	}
	
	@Override
	public void start() throws Exception {
		Router router = Router.router(vertx);
		
		MetricsService service = MetricsService.create(vertx);

	    router.route().handler(BodyHandler.create());

	    final FreeMarkerTemplateEngine engine = FreeMarkerTemplateEngine.create();

	    router.get("/index").handler(ctx -> {
	      ctx.put("name", "Vert.x Web");

	      engine.render(ctx, "templates/index.ftl", res -> {
	        if (res.succeeded()) {
	          ctx.response().end(res.result());
	        } else {
	          ctx.fail(res.cause());
	        }
	      });
	    });
	    
	    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	    
	    
	    int metricsInterval = config().getInteger("monitor.metrics.interval", 5000);
	    
	    vertx.setPeriodic(metricsInterval, t -> {
	        JsonObject metrics = service.getMetricsSnapshot(vertx);
	        //System.out.println(metrics.toString());
	    });
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
	}
	
}
