package com.qiekang;

import com.qiekang.verticle.AlipayVerticle;
import com.qiekang.verticle.IndexVerticle;

import io.vertx.core.Vertx;

public class Application {

	public static void main(String[] args) {
		
		Vertx vertx = Vertx.vertx();
		//vertx.deployVerticle(new IndexVerticle());
		vertx.deployVerticle(new AlipayVerticle());
		
	}

}
