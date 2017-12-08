package com.callcenter.model;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PerformanceMetrics {
    //private CallCenter callCenterToAnalyse;
    protected HashMap<String, Integer> callCenterMetrics = new HashMap<String, Integer>();
    protected HashMap <String, List<AgentMetrics>> performanceMap = new HashMap<String, List<AgentMetrics>>();
    public PerformanceMetrics(CallCenter callCenterToAnalyse) {

        List<AgentMetrics> mgrMetrics = callCenterToAnalyse.getMgrs().stream()
                .map(agent->agent.getMetrics())
                .collect(Collectors.toList());
        List<AgentMetrics> jeMetrics = callCenterToAnalyse.getJuniorExecList().stream()
                .map(agent->agent.getMetrics())
                .collect(Collectors.toList());
        List<AgentMetrics> seMetrics = callCenterToAnalyse.getSeniorExecList().stream()
                .map(agent->agent.getMetrics())
                .collect(Collectors.toList());



        int totalCallsUnresolved = mgrMetrics.stream()
                .mapToInt(mgrAgent -> mgrAgent.getTotalCallsEscalated())
                .sum();

        performanceMap.putIfAbsent("manager",mgrMetrics);
        performanceMap.putIfAbsent("junior-executive",jeMetrics);
        performanceMap.putIfAbsent("senior-executive",seMetrics);
        callCenterMetrics.putIfAbsent("number_of_calls_allowed",callCenterToAnalyse.getTotalNoOfCalls());
        callCenterMetrics.putIfAbsent("number_of_calls_serviced",getTotalCallsServiced());
        callCenterMetrics.putIfAbsent("resolved",getTotalCallsResolved());
        callCenterMetrics.putIfAbsent("unresolved",getTotalCallsUnresolved());
        callCenterMetrics.putIfAbsent("totalTimeTakenInMinutes",getTotalCallDuration());

    }

    private int getTotalCallsServiced() {
        int totalCallsServiced = 0;

        totalCallsServiced += performanceMap.get("junior-executive").stream()
                .mapToInt(jemetrics->jemetrics.getTotalCallsServiced())
                .sum();
        totalCallsServiced += performanceMap.get("senior-executive").stream()
                .mapToInt(semetrics->semetrics.getTotalCallsServiced())
                .sum();
        totalCallsServiced += performanceMap.get("manager").stream()
                .mapToInt(mgrmetrics->mgrmetrics.getTotalCallsServiced())
                .sum();
        return totalCallsServiced;
    }

    private int getTotalCallsResolved() {
        int totalCallsResolved = 0;

        totalCallsResolved += performanceMap.get("junior-executive").stream()
                .mapToInt(jemetrics->jemetrics.getTotalCallsResolved())
                .sum();
        totalCallsResolved += performanceMap.get("senior-executive").stream()
                .mapToInt(semetrics->semetrics.getTotalCallsResolved())
                .sum();
        totalCallsResolved += performanceMap.get("manager").stream()
                .mapToInt(mgrmetrics->mgrmetrics.getTotalCallsResolved())
                .sum();
        return totalCallsResolved;
    }

    private int getTotalCallsUnresolved() {
        int totalCallsUnresolved = 0;

        totalCallsUnresolved += performanceMap.get("junior-executive").stream()
                .mapToInt(jemetrics->jemetrics.getTotalCallsEscalated())
                .sum();
        totalCallsUnresolved += performanceMap.get("senior-executive").stream()
                .mapToInt(semetrics->semetrics.getTotalCallsEscalated())
                .sum();
        totalCallsUnresolved += performanceMap.get("manager").stream()
                .mapToInt(mgrmetrics->mgrmetrics.getTotalCallsEscalated())
                .sum();
        return totalCallsUnresolved;
    }

    private int getTotalCallDuration() {
        int totalCallDurationInMins = 0;

        totalCallDurationInMins += performanceMap.get("junior-executive").stream()
                .mapToInt(jemetrics->jemetrics.getTotalCallDurationInMins())
                .sum();
        totalCallDurationInMins += performanceMap.get("senior-executive").stream()
                .mapToInt(semetrics->semetrics.getTotalCallDurationInMins())
                .sum();
        totalCallDurationInMins += performanceMap.get("manager").stream()
                .mapToInt(mgrmetrics->mgrmetrics.getTotalCallDurationInMins())
                .sum();
        return totalCallDurationInMins;
    }

}
