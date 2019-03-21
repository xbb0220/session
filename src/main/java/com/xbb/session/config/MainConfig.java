package com.xbb.session.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.template.Engine;
import com.xbb.session.web.HelloController;

public class MainConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {

	}

	@Override
	public void configRoute(Routes me) {
		me.add("hello", HelloController.class);
	}

	@Override
	public void configEngine(Engine me) {

	}

	@Override
	public void configPlugin(Plugins me) {
		PropKit.use("config.properties");
		RedisPlugin bbsRedis = new RedisPlugin("main", PropKit.get("redis.host"), PropKit.getInt("redis.port")); 
		me.add(bbsRedis);
	}

	@Override
	public void configInterceptor(Interceptors me) {

	}

	@Override
	public void configHandler(Handlers me) {

	}

}
