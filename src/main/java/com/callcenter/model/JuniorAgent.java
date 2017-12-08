package com.callcenter.model;

import com.callcenter.model.enums.AgentLevel;

public class JuniorAgent extends Agent {
    final int MAX_TIMETO_RESOLVE_JE_IN_MINS=7;
    public JuniorAgent(String staffName, AgentLevel hierarchy, int maxCallsAllowed){
        super(staffName, hierarchy, maxCallsAllowed);
        setEscalationLimit(MAX_TIMETO_RESOLVE_JE_IN_MINS);
    }

}
