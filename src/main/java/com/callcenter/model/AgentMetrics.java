package com.callcenter.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

public class AgentMetrics {

    private String agentName;
    private int totalCallDurationInMins;
    private int totalCallsServiced;
    private int totalCallsEscalated;
    private int totalCallsResolved;

   public void incrementTotalCallsResolved() {
        this.totalCallsResolved++;
    }

    public AgentMetrics() {

    }
    public AgentMetrics(String agentName) {
        this.agentName = agentName;
        this.totalCallDurationInMins = 0;
        this.totalCallsServiced = 0;
        this.totalCallsEscalated = 0;
        this.totalCallsResolved = 0;
    }
    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getTotalCallsResolved() {
        return totalCallsResolved;
    }

    public int getTotalCallDurationInMins() {
        return totalCallDurationInMins;
    }

    public void addToTotalCallDurationInMins(int addlnCallDurationInMins) {
        this.totalCallDurationInMins += addlnCallDurationInMins;
    }

    public int getTotalCallsServiced() {
        return totalCallsServiced;
    }

    public void incrementTotalCallsServiced() {

        this.totalCallsServiced++;
    }

    public int getTotalCallsEscalated() {
        return totalCallsEscalated;
    }

    public void incrementTotalCallsEscalated() {

        this.totalCallsEscalated++;

    }

    public String toJSONString() throws Exception {
        ObjectMapper agentObjectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        String jsonString = agentObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        System.out.println(jsonString);
        return jsonString;

    }

}
