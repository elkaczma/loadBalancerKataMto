package edu.iis.mto.serverloadbalancer;


import static edu.iis.mto.serverloadbalancer.CurrentLoadPercentageMatcher.hasCurrentLoadOf;
import static edu.iis.mto.serverloadbalancer.ServerBuilder.server;
import static edu.iis.mto.serverloadbalancer.ServerVmsCountMatcher.hasVmsCountOf;
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
	public void balancingServer_noVm_serverStaysEmpty() {
		Server theServer = a(server().withCapacity(10));
		
		balancing(aListOfServersWith(theServer), anEmptyListOfVms());
		
		assertThat(theServer, hasCurrentLoadOf(0.0d));
	}
	
	@Test
	public void balancingOneServerWithOneSlotCapacity_andOneSlotVm_fillsTheServerWithTheVm(){
		Server theServer = a(server().withCapacity(1));
		Vm theVm = a(vm().ofSize(1));
		balancing(aListOfServersWith(theServer), aListOfVmsWith(theVm));

		assertThat(theServer, hasCurrentLoadOf(100.0d));
		assertThat("the server should contain vm", theServer.contains(theVm));
	}

	@Test
	public void balancingOneServerWithTenSlotsCapacity_andOneSlotVm_fillTheServerWithTenPercent(){
		Server theServer = a(server().withCapacity(10));
		Vm theVm = a(vm().ofSize(1));
		balancing(aListOfServersWith(theServer), aListOfVmsWith(theVm));

		assertThat(theServer, hasCurrentLoadOf(10.0d));
		assertThat("the server should contain vm", theServer.contains(theVm));
	}
	
	@Test
	public void balancingAServerWithEnoughRoom_getsFilledWithAllVms(){
		Server theServer = a(server().withCapacity(100));
		Vm theFirstVm = a(vm().ofSize(1));
		Vm theSecondVm = a(vm().ofSize(1));
		balancing(aListOfServersWith(theServer), aListOfVmsWith(theFirstVm, theSecondVm));

		assertThat(theServer, hasVmsCountOf(2));
		assertThat("the server should contain vm", theServer.contains(theFirstVm));
		assertThat("the server should contain vm", theServer.contains(theSecondVm));

	}
	
	@Test 
	public void aVm_shouldBeBalanced_onLessLoadedServerFirst(){
		Server lessLoadedServer = a(server().withCapacity(100).withCurrentLoadOf(45.0d));
		Server moreLoadedServer = a(server().withCapacity(100).withCurrentLoadOf(50.0d));
		Vm theVm = a(vm().ofSize(10));
		
		balancing(aListOfServersWith(moreLoadedServer, lessLoadedServer), aListOfVmsWith(theVm));

		assertThat("the less loaded server should contain vm", lessLoadedServer.contains(theVm));
		assertThat("the more loaded server should not contain vm", !moreLoadedServer.contains(theVm));
	}
	
	@Test
	public void balanceAServerWithNotEnoughRoom_shouldNotBeFilledWithAVm(){
		Server theServer = a(server().withCapacity(10).withCurrentLoadOf(90.0d));
		Vm theVm = a(vm().ofSize(2));
		balancing(aListOfServersWith(theServer), aListOfVmsWith(theVm));

		assertThat("the server should contain vm", !theServer.contains(theVm));
	}
	
	@Test 
	public void balance_serversAndVms() {
		Server server1 = a(server().withCapacity(4));
        Server server2 = a(server().withCapacity(6));
        
        Vm vm1 = a(vm().ofSize(1));
        Vm vm2 = a(vm().ofSize(4));
        Vm vm3 = a(vm().ofSize(2));
        
        balancing(aListOfServersWith(server1, server2), aListOfVmsWith(vm1, vm2, vm3));
        
        assertThat("The server 1 should contain the vm 1", server1.contains(vm1));
        assertThat("The server 2 should contain the vm 2", server2.contains(vm2));
        assertThat("The server 1 should contain the vm 3", server1.contains(vm3));
        
        assertThat(server1, hasCurrentLoadOf(75.0d));
        assertThat(server2, hasCurrentLoadOf(66.66d));
	}
	
	private <T> T a(Builder<T> builder) {
		return builder.build();
	}

	private Vm[] aListOfVmsWith(Vm ...vms) {
		return vms;
	}

	private void balancing(Server[] servers, Vm[] vms) {
		new ServerLoadBalancer().balance(servers, vms);
	}

	private Vm[] anEmptyListOfVms() {
		return new Vm[0];
	}

	private Server[] aListOfServersWith(Server ... servers) {
		return servers;
	}


}
