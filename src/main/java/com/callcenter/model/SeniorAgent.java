package com.callcenter.model;

import com.callcenter.model.enums.AgentLevel;

public class SeniorAgent extends Agent {
    final int MAX_TIMETO_RESOLVE_SE_IN_MINS=10;
    public SeniorAgent(String staffName, AgentLevel hierarchy, int maxCallsAllowed){
        super(staffName, hierarchy, maxCallsAllowed);
        setEscalationLimitInMins(MAX_TIMETO_RESOLVE_SE_IN_MINS);
    }

}
