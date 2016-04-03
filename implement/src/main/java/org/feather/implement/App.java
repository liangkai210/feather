package org.feather.implement;

import org.feather.app.Start;
import org.feather.app.module.ModuleFactory;

public class App extends Start {

	public static void main(String[] args) {
		new App().start();
	}

	@Override
	public void registerModules() {
		ModuleFactory.registerModule(IndexServer.class);
	}

	@Override
	public void addToStartModules() {
		ModuleFactory.addToStartModule(IndexServer.class.getName());
	}

}
