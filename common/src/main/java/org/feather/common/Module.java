package org.feather.common;

public interface Module {

	public void afterCreate(Object[] params);

	public boolean init(boolean isReload);

	public void start(boolean isReload);

	public void stop();

	public boolean isAlive();

}
