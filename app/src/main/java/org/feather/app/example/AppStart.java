package org.feather.app.example;

import org.feather.app.Start;

public class AppStart extends Start {

	@Override
	public void registerModules() {
	}

	@Override
	public void addToStartModules() {
	}

	public static void main(String[] args) {
		Start start = new AppStart();
		start.start();
	}

}
