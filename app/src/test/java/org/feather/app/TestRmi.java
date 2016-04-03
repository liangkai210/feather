package org.feather.app;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestRmi {

	public static void main(String[] args) throws RemoteException, UnknownHostException {

		Registry registry = LocateRegistry.getRegistry();

		System.err.println(registry);

		System.out.println(getHost());
	}

	private static String getHost() throws UnknownHostException {
		System.out.println(java.net.InetAddress.getLocalHost());
		return java.net.InetAddress.getLocalHost().getHostAddress();
	}
}
