package com.callcenter.model.impl;

import com.callcenter.errors.exceptions.AgentRejectException;
import com.callcenter.errors.exceptions.NoAgentToHandleException;
import com.callcenter.model.AgentComparator;
import com.callcenter.model.Call;
import com.callcenter.model.CallHandler;
import com.callcenter.model.Agent;
import com.callcenter.model.enums.CallRequestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CallHandlerImpl implements CallHandler {

    int totalCallsServiced;
    int totalCallsUnServiced;
    CallHandler nextHandler;
    List<Agent> poolOfAgents;

    public CallHandlerImpl(List<Agent> listOfAgents) {
        totalCallsUnServiced = 0;
        totalCallsServiced= 0;
        nextHandler = null;
        poolOfAgents = listOfAgents;
    }


    @Override
    public void setNextCallHandler(CallHandlerImpl nextLevelHandler) {
        if (nextHandler == null) {
            nextHandler = nextLevelHandler;
        } else {
            nextHandler.setNextCallHandler(nextLevelHandler);
        }
    }


    private List<Agent> getFreeAgents () {
        if (poolOfAgents != null) {
            return poolOfAgents.stream()
                    .sorted(new AgentComparator())
                    .filter(agent -> agent.isAvailableToAcceptCalls())
                    .collect(Collectors.toList());
        } else {
            return null;
        }

    }

    public void handleCall(Call incomingCall)  {
        Agent agentAssigned = null;
        // Get the agents at a level that are free to take calls.
        List<Agent> avlAgents = new ArrayList<>();
        try {

            avlAgents = getFreeAgents();
            if (null == avlAgents || avlAgents.isEmpty()) {
                throw new NoAgentToHandleException("No agents available to service calls, cannot service calls");
            }
            agentAssigned = avlAgents.get(0);
            System.out.println("Routing the call to the agent " + agentAssigned);
            totalCallsServiced++;
            CallRequestStatus servicedStatus = agentAssigned.serviceIncomingCall(incomingCall);
            switch (servicedStatus) {
                case Resolved:
                    System.out.println("Call resolved by agent " + agentAssigned );
                    break;

                case Escalated:
                    if (this.nextHandler != null) {
                        nextHandler.handleCall(incomingCall);
                    } else {
                        System.out.println("No Agent to escalate the call to, marking call as unresolved");
                        totalCallsUnServiced++;
                    }
                    System.out.println("Call escalated by " + agentAssigned + "to the next Level");
                    break;
                case Rejected:
                    System.out.println("Call rejected by " + agentAssigned );
                    totalCallsUnServiced++;
                    break;
                default:
                    System.out.println("Undefined state of call");
            }
        } catch (AgentRejectException e)
        {
            System.out.println(e.getMessage());
            System.out.println(agentAssigned + "Too busy to handle calls, hence rejecting");
            totalCallsUnServiced++;
        } catch (NoAgentToHandleException e) {
            System.out.println(e.getMessage());
            totalCallsUnServiced++;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
