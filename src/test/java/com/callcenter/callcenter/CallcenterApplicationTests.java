package com.callcenter.callcenter;

import com.callcenter.*;
import com.callcenter.errors.exceptions.NoAgentToHandleException;
import com.callcenter.model.*;
import com.callcenter.model.enums.AgentLevel;
import com.callcenter.model.enums.CallRequestStatus;
import com.callcenter.model.impl.CallHandlerImpl;
import com.callcenter.services.CallAllocationService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.callcenter.model.enums.AgentLevel.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CallcenterApplicationTests {

	private static CallCenter callCenterMock;

	@BeforeClass
	public static void setUp() {
		callCenterMock = mock(CallCenter.class);
	}


	@Test
	public void testCallResolutionByJE () throws Exception{

		ArrayList<Agent> agents = new ArrayList<Agent>();
		JuniorAgent je = new JuniorAgent("First", JuniorExec, 1);
		je.setCallDurationTimes(new int[] {1,2,3});
		agents.add(je);

		when(callCenterMock.getJuniorExecList()).thenReturn(agents);
		Call unassignedCall = new Call("Call1", CallRequestStatus.UnAssigned);

		CallHandler callHandler = new CallHandlerImpl(callCenterMock.getJuniorExecList());
		callHandler.handleCall(unassignedCall);
		Assert.assertEquals(CallRequestStatus.Resolved,unassignedCall.getCallStatus());
	}

	@Test
	public void testCallEscalationByJE () throws Exception{

		ArrayList<Agent> agents = new ArrayList<Agent>();
		JuniorAgent je = new JuniorAgent("First", JuniorExec, 1);
		je.setCallDurationTimes(new int[] {8,2,3});
		agents.add(je);


		when(callCenterMock.getJuniorExecList()).thenReturn(agents);
		Call unassignedCall = new Call("Call1", CallRequestStatus.UnAssigned);

		CallHandler callHandler = new CallHandlerImpl(callCenterMock.getJuniorExecList());
		callHandler.handleCall(unassignedCall);
		Assert.assertEquals(CallRequestStatus.Escalated,unassignedCall.getCallStatus());
	}

	@Test
	public void allJEsBusy () throws Exception{

		ArrayList<Agent> agents = new ArrayList<Agent>();
		JuniorAgent je = new JuniorAgent("First", JuniorExec, 1);
		je.setCallDurationTimes(new int[] {8,2,3});
		je.setMaxNoOfCallsAllowed(0);

		agents.add(je);
		SeniorAgent se = new SeniorAgent("FirstSE", SeniorExec, 1);
		se.setCallDurationTimes(new int[] {18,2,3});
		Manager mgr = new Manager("Mgr",Manager, 10);
		mgr.setCallDurationTimes(new int [] {30});
		when(callCenterMock.getJuniorExecList()).thenReturn(agents);
		when(callCenterMock.getSeniorExecList()).thenReturn(new ArrayList<>(Arrays.asList(se)));
		when(callCenterMock.getMgrs()).thenReturn(new ArrayList<>(Arrays.asList(mgr)));

		Call unassignedCall = new Call("Call1", CallRequestStatus.UnAssigned);
		when(callCenterMock.getTotalNoOfCalls()).thenReturn(1);
		CallAllocationService callHandler = new CallAllocationService(callCenterMock);
		callHandler.dispatchCall(unassignedCall);
		Assert.assertEquals(CallRequestStatus.Rejected,unassignedCall.getCallStatus());
	}

	@Test
	public void testCallEscalationByJESEMgr () throws Exception{

		ArrayList<Agent> agents = new ArrayList<Agent>();
		JuniorAgent je = new JuniorAgent("First", JuniorExec, 1);
		je.setCallDurationTimes(new int[] {8,2,3});
		agents.add(je);
		SeniorAgent se = new SeniorAgent("FirstSE", SeniorExec, 1);
		se.setCallDurationTimes(new int[] {18,2,3});
		Manager mgr = new Manager("Mgr",Manager, 10);
		mgr.setCallDurationTimes(new int [] {30});
		when(callCenterMock.getJuniorExecList()).thenReturn(agents);
		when(callCenterMock.getSeniorExecList()).thenReturn(new ArrayList<>(Arrays.asList(se)));
		when(callCenterMock.getMgrs()).thenReturn(new ArrayList<>(Arrays.asList(mgr)));
		Call unassignedCall = new Call("Call1", CallRequestStatus.UnAssigned);
		when(callCenterMock.getTotalNoOfCalls()).thenReturn(1);
		CallAllocationService callHandler = new CallAllocationService(callCenterMock);
		callHandler.dispatchCall(unassignedCall);
		Assert.assertEquals(CallRequestStatus.Escalated,unassignedCall.getCallStatus());
	}


	@Test
	public void testCallHandlingWithoutAgents() throws Exception {
		ArrayList<Agent> agents = null;
		when (callCenterMock.getJuniorExecList()).thenReturn(agents);

		Call unassignedCall = new Call("Call1", CallRequestStatus.UnAssigned);

		CallHandler callHandler = new CallHandlerImpl(callCenterMock.getJuniorExecList());
		callHandler.handleCall(unassignedCall);
		Assert.assertEquals(CallRequestStatus.UnAssigned,unassignedCall.getCallStatus());

	}

}
