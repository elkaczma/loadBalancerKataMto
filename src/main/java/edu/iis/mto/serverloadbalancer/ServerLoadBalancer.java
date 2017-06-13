package edu.iis.mto.serverloadbalancer;

import java.util.ArrayList;
import java.util.List;

public class ServerLoadBalancer {

	private static final double MAXIMUM_LOAD = 100.0d;

	public void balance(Server[] servers, Vm[] vms) {
		for (Vm vm : vms) {
			List<Server> capableServers = findServersWithEnoughCapacity(servers, vm);
			Server lessLoaded = extractLessLoadedServer(capableServers);
			addToCapableLessLoadedServer(vm, lessLoaded);
		}
	}

	private void addToCapableLessLoadedServer(Vm vm, Server lessLoaded) {
		if (lessLoaded!=null) {
			lessLoaded.addVm(vm);
		}
	}

	private List<Server> findServersWithEnoughCapacity(Server[] servers, Vm vm) {
		List<Server> capableServers = new ArrayList<Server>();
		for (Server server : servers) {
			if (server.canFit(vm)) {
				capableServers.add(server);
			}
		}
		return capableServers;
	}

	private Server extractLessLoadedServer(List<Server> capableServers) {
		Server lessLoaded = null;
		for (Server server : capableServers) {
			if (lessLoaded == null || lessLoaded.getCurrentLoadPercentage() > server.getCurrentLoadPercentage()) {
				lessLoaded = server;
			}
		}
		return lessLoaded;
	}

}
