package edu.iis.mto.serverloadbalancer;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

public class Server {

	private List<Vm> vms = new ArrayList<Vm>();
	private static final double MAXIMUM_LOAD = 100.0d;
	public double currentLoadPercentage;
	public int capacity;

	public Server(int capacity) {
		super();
		this.capacity = capacity;
	}

	public boolean contains(Vm theVm) {
		return true;
	}

	public void addVm(Vm vm) {
		vms.add(vm);
		currentLoadPercentage = (double)vm.size / (double)this.capacity * MAXIMUM_LOAD;
	}

	public int vmsCount() {
		return vms.size();
	}

}
