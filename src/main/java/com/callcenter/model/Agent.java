package com.callcenter.model;

import com.callcenter.errors.exceptions.AgentRejectException;
import com.callcenter.model.enums.AgentLevel;
import com.callcenter.model.enums.CallRequestStatus;
import com.callcenter.model.enums.ExecCallHandlingStatus;
import com.callcenter.utils.JsonObjectMapper;
import java.util.concurrent.ArrayBlockingQueue;


public class Agent {

    // TODO Change Hierarchy to level
    private AgentLevel agentLevel;
    protected String agentName;
    private int escalationLimitInMins;
    private int maxNoOfCallsAllowed;



    // TODO check if this exists in this class!
    protected int[] mockDurationOfCallInMins;

    // TODO Change ExecCallHandlingStatus to availablility status
    ExecCallHandlingStatus callHandlingStatus;
    AgentMetrics metrics;

    public void printCallDurationMockData() {
        System.out.println("Agent " + agentName);
        for (int data : mockDurationOfCallInMins
                ) {
            System.out.println("mins : " + data);
        }
    }

    ArrayBlockingQueue<Call> callsInQueue;

    public Agent(){}
    Agent(String agentName, AgentLevel hierarchy, int maxCallsAllowed) {
        System.out.println("Adding agent with name " + agentName);
        this.agentName = agentName;
        this.agentLevel = hierarchy;
        this.maxNoOfCallsAllowed = maxCallsAllowed;
        this.metrics = new AgentMetrics(agentName);
        this.callHandlingStatus = ExecCallHandlingStatus.free;
        this.escalationLimitInMins = 0;
        callsInQueue = new ArrayBlockingQueue<Call>(maxCallsAllowed);
    }

    public float getAvgCallResolutionTime() {
        if (metrics.getTotalCallsResolved() > 0)
            return metrics.getTotalCallDurationInMins()/metrics.getTotalCallsResolved();
        else return 0.0f;
    }
    public ExecCallHandlingStatus getCallHandlingStatus() {
        return callHandlingStatus;
    }

    public void setCallDurationTimes(int[] durationOfCallsInMins) {
        mockDurationOfCallInMins = durationOfCallsInMins;
        printCallDurationMockData();
    }

    public AgentMetrics getMetrics() {
        return metrics;
    }


    public boolean isAvailableToAcceptCalls() {
        if (callsInQueue.remainingCapacity() > 0 && getCallHandlingStatus()!=ExecCallHandlingStatus.BusyLimitExceeded)
            return true;
        else
            return false;
    }

    public int getEscalationLimitInMins() {
        return escalationLimitInMins;
    }

    public void setEscalationLimitInMins(int escalationLimitInMins) {
        this.escalationLimitInMins = escalationLimitInMins;
    }

    private void setCallHandlingStatus(ExecCallHandlingStatus agentStatus) {
        this.callHandlingStatus = agentStatus;
    }

    public void setEscalationLimit(int escalationLimit) {
        this.escalationLimitInMins = escalationLimit;
    }

    public void setMaxNoOfCallsAllowed(int maxNoOfCallsAllowed) {
        this.maxNoOfCallsAllowed = maxNoOfCallsAllowed;
    }

    public CallRequestStatus serviceIncomingCall(Call callRequest) throws Exception {
        int index = metrics.getTotalCallsServiced()%mockDurationOfCallInMins.length;
        if (metrics.getTotalCallsServiced() < maxNoOfCallsAllowed) {
            callsInQueue.add(callRequest);
            int callDuration  = mockDurationOfCallInMins[index];
            metrics.addToTotalCallDurationInMins(callDuration);
            callRequest.updateCallStatus(CallRequestStatus.InProgress);
            setCallHandlingStatus(ExecCallHandlingStatus.inACall);
            metrics.incrementTotalCallsServiced();
            Thread.sleep(callDuration);
            System.out.println("Remaining capacity is " + callsInQueue.remainingCapacity());
            if (callDuration > getEscalationLimitInMins()) {
                System.out.println("Call duration of " + callDuration + " mins exceeds the "+
                        getEscalationLimitInMins() +", escalating" );
                callRequest.updateCallStatus(CallRequestStatus.Escalated);
                metrics.incrementTotalCallsEscalated();
                setCallHandlingStatus(ExecCallHandlingStatus.free);
                //callsInQueue.remove(callRequest);

            }
            else {
                callRequest.updateCallStatus(CallRequestStatus.Resolved);
                setCallHandlingStatus(ExecCallHandlingStatus.free);
                metrics.incrementTotalCallsResolved();
            }

        } else {
            callRequest.updateCallStatus(CallRequestStatus.Rejected);
            setCallHandlingStatus(ExecCallHandlingStatus.BusyLimitExceeded);
            throw new AgentRejectException(agentName + " Cannot accept any further calls, quota exceeded");
        }
        JsonObjectMapper.getObjectAsJsonString(metrics);
        return callRequest.getCallStatus();
    }

    public int getCallsServiced() {
        return metrics.getTotalCallsServiced();
    }

}
