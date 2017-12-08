package com.callcenter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ManagerMetrics extends AgentMetrics{
    public ManagerMetrics(String name) {
        super(name);
    }
    @JsonProperty("totalCallsUnresolved")
    public int getTotalCallsEscalated() {
        return super.getTotalCallsEscalated();
    }

}
