package edu.iis.mto.serverloadbalancer;

public class ServerLoadBalancer {

	private static final double MAXIMUM_LOAD = 100.0d;

	public void balance(Server[] servers, Vm[] vms) {
		for (Vm vm : vms) {
			Server lessLoaded = null;
			for (Server server : servers) {
				if (lessLoaded == null || lessLoaded.currentLoadPercentage > server.currentLoadPercentage) {
					lessLoaded = server;
				}
			}
			lessLoaded.addVm(vm);
		}
	}

}
