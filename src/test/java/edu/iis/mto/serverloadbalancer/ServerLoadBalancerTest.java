package edu.iis.mto.serverloadbalancer;


import static edu.iis.mto.serverloadbalancer.CurrentLoadPercentageMatcher.hasCurrentLoadOf;
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
	public void balancingOneServerWithOneSlotCapacity_addOneSlotVm_fillsServerWithTheVm() {
		Server theServer = a(server().withCapacity(1));
		Vm theVm = a(vm().ofSize(1));
		
		balancing(aServersListWith(theServer), aVmsListWith(theVm));
		
		assertThat(theServer, hasCurrentLoadOf(100.0d));
		assertThat("the server should contain the vm", theServer.contains(theVm));
	}

	private void balancing(Server[] servers, Vm[] anEmptyListOfVms) {
		new ServerLoadBalancer().balance(servers, anEmptyListOfVms);
	}

	private Vm[] anEmptyListOfVms() {
		return new Vm[0];
	}

	private Server[] aServersListWith(Server theServer) {
		return new Server[]{theServer};
	}

	private Server a(ServerBuilder builder) {
		return builder.build();
	}

	private ServerBuilder server() {
		return new ServerBuilder();
	}

}
