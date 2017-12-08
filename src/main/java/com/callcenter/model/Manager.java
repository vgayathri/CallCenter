package com.callcenter.model;

import com.callcenter.model.enums.AgentLevel;

public class Manager extends Agent {
    final int MAX_TIMETO_RESOLVE_MGR_IN_MINS=15;

    public Manager(String staffName, AgentLevel hierarchy, int maxCallsAllowed){
        super(staffName, hierarchy, maxCallsAllowed);
        this.metrics = new ManagerMetrics(staffName);
        setEscalationLimitInMins(MAX_TIMETO_RESOLVE_MGR_IN_MINS);
    }

    public AgentMetrics getMetrics() {
        return this.metrics;
    }

}
