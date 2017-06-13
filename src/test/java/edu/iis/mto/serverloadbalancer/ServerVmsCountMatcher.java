package edu.iis.mto.serverloadbalancer;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ServerVmsCountMatcher extends TypeSafeMatcher<Server> {

	private int expectedCount;

	public ServerVmsCountMatcher(int expectedCount) {
		this.expectedCount = expectedCount;
	}

	public void describeTo(Description description) {
		description.appendText("a server with vms count of ").appendValue(expectedCount);
	}
	
	@Override
	protected void describeMismatchSafely(Server server, Description description) {
		description.appendText("a server with vms count of ").appendValue(server.vmsCount());
	}

	@Override
	protected boolean matchesSafely(Server server) {
		return expectedCount == server.vmsCount();
	}

	public static ServerVmsCountMatcher hasVmsCountOf(int expectedCount) {
		return new ServerVmsCountMatcher(expectedCount);
	}
}
