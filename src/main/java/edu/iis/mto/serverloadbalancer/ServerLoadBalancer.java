package edu.iis.mto.serverloadbalancer;

import java.util.ArrayList;
import java.util.List;

public class ServerLoadBalancer {

	private static final double MAXIMUM_LOAD = 100.0d;

	public void balance(Server[] servers, Vm[] vms) {
		for (Vm vm : vms) {
			List<Server> capableServers = new ArrayList<Server>();
			for (Server server : servers) {
				if (server.canFit(vm)) {
					capableServers.add(server);
				}
			}
			Server lessLoaded = extractLessLoadedServer(capableServers);
			if (lessLoaded!=null) {
				lessLoaded.addVm(vm);
			}
		}
	}

	private Server extractLessLoadedServer(List<Server> capableServers) {
		Server lessLoaded = null;
		for (Server server : capableServers) {
			if (lessLoaded == null || lessLoaded.currentLoadPercentage > server.currentLoadPercentage) {
				lessLoaded = server;
			}
		}
		return lessLoaded;
	}

}
