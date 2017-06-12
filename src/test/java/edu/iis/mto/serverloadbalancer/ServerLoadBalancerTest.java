package edu.iis.mto.serverloadbalancer;


import static edu.iis.mto.serverloadbalancer.CurrentLoadPercentageMatcher.hasCurrentLoadOf;
import static edu.iis.mto.serverloadbalancer.ServerVmsCountMatcher.hasAVmsCountOf;
import static edu.iis.mto.serverloadbalancer.VmBuilder.vm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ServerLoadBalancerTest {
	@Test
	public void itCompiles() {
		assertThat(true, equalTo(true));
	}
	
	@Test
	public void balancingServerWithNoVms_serverStaysEmpty() {
		Server theServer = a(server().withCapacity(1));
		
		balancing(aServersListWith(theServer), anEmptyListOfVms());
		
		assertThat(theServer, hasCurrentLoadOf(0.0d));
	}
	
	@Test
	public void balancingOneServerWithOneSlotCapacity_andOneSlotVm_fillsServerWithTheVm() {
		Server theServer = a(server().withCapacity(1));
		Vm theVm = a(vm().ofSize(1));
		
		balancing(aServersListWith(theServer), aVmsListWith(theVm));
		
		assertThat(theServer, hasCurrentLoadOf(100.0d));
		assertThat("the server should contain the vm", theServer.contains(theVm));
	}
	
	@Test
	public void balancingOneServerWithTenSlotCapacity_andOneSlotVm_fillsServerWithTenPercent() {
		Server theServer = a(server().withCapacity(10));
		Vm theVm = a(vm().ofSize(1));
		
		balancing(aServersListWith(theServer), aVmsListWith(theVm));
		
		assertThat(theServer, hasCurrentLoadOf(10.0d));
		assertThat("the server should contain the vm", theServer.contains(theVm));
	}
	
	@Test
	public void balancingTheServerWithEnoughRoom_fillsServerWithAllVms() {
		Server theServer = a(server().withCapacity(100));
		Vm theFirstVm = a(vm().ofSize(1));
		Vm theSecondVm = a(vm().ofSize(1));
		
		balancing(aServersListWith(theServer), aVmsListWith(theFirstVm, theSecondVm));
		
		assertThat(theServer, hasAVmsCountOf(2));
		assertThat("the server should contain the vm", theServer.contains(theFirstVm));
		assertThat("the server should contain the vm", theServer.contains(theSecondVm));
	}

	private <T> T a(Builder<T> builder) {
		return builder.build();
	}

	private Vm[] aVmsListWith(Vm ... vms) {
		return vms;
	}


	private void balancing(Server[] servers, Vm[] vms) {
		new ServerLoadBalancer().balance(servers, vms);
	}

	private Vm[] anEmptyListOfVms() {
		return new Vm[0];
	}

	private Server[] aServersListWith(Server theServer) {
		return new Server[]{theServer};
	}

	private ServerBuilder server() {
		return new ServerBuilder();
	}

}
