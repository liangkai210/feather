package org.feather.app;

import org.feather.app.module.ModuleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Start {

	Logger logger = LoggerFactory.getLogger(Start.class);

	public abstract void registerModules();

	public abstract void addToStartModules();

	public void start() {
		ModuleFactory.registerModule(Server.class);
		ModuleFactory.addToStartModule(Server.class.getName());
		registerModules();
		addToStartModules();
		ModuleFactory.startModules();
		logger.info("App is running now.");
	}

}
