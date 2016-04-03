package org.feather.implement;

import org.feather.common.Module;
import org.feather.implement.craigslist.CarIndex;

public class IndexServer implements Module {

	private boolean alive = false;
	private CarIndex index = null;

	public void afterCreate(Object[] params) {
	}

	public boolean init(boolean isReload) {
		index = new CarIndex();
		return true;
	}

	public void start(boolean isReload) {
		alive = true;
	}

	public void stop() {
		alive = false;
		index.close();
	}

	public boolean isAlive() {
		return alive;
	}

}
