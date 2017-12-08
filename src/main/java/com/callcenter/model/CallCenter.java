package com.callcenter.model;

import com.callcenter.errors.exceptions.InvalidJSonConfigException;
import com.callcenter.model.enums.AgentLevel;
import com.callcenter.repository.CallCenterRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class CallCenter {
    private CallCenterRepo callAgentsRepo;
    private int noOfJuniorAgents;
    private int noOfSeniorAgents;
    private int totalNoOfCalls;
    private HashMap<AgentLevel, List<Agent>> agents;

    public int getTotalNoOfCalls() {
        return totalNoOfCalls;
    }

    public CallCenter(String JSONString) throws Exception {
        callAgentsRepo = new CallCenterRepo();
        callAgentsRepo.readFromJSONFile(JSONString);
        totalNoOfCalls = callAgentsRepo.getTotaNoOfCalls();
        noOfJuniorAgents = callAgentsRepo.getNoOfJuniorExecs();
        noOfSeniorAgents = callAgentsRepo.getNoOfSeniorExecs();
        agents = new HashMap<>();
    }

    public void populateCallCentre() {
        int maxNoOfCallsPerExec = (totalNoOfCalls/(noOfSeniorAgents + noOfJuniorAgents))+1;
        System.out.println("Max Calls per exec"+ maxNoOfCallsPerExec);

        List<Agent> juniorAgentsPool = new ArrayList<Agent>(noOfJuniorAgents);
        List<Agent> seniorAgentsPool = new ArrayList<Agent>(noOfSeniorAgents);
        List<Agent> managers = new ArrayList<Agent>();
        Manager callCenterManager;

        for (int i = 0; i < callAgentsRepo.getNoOfJuniorExecs(); i++)  {
            String juniorAgentName = String.format("%s%02d", "je", i);
            juniorAgentsPool.add(new JuniorAgent(juniorAgentName, AgentLevel.JuniorExec,maxNoOfCallsPerExec));
            juniorAgentsPool.get(i).setCallDurationTimes(callAgentsRepo.getJETestCallDurationInMins(i));

        }
        for (int i = 0; i < callAgentsRepo.getNoOfSeniorExecs(); i++)  {
            String seniorAgentName = String.format("%s%02d", "se", i);
            seniorAgentsPool.add(new SeniorAgent(seniorAgentName, AgentLevel.SeniorExec,maxNoOfCallsPerExec));
            seniorAgentsPool.get(i).setCallDurationTimes(callAgentsRepo.getSETestCallDurationInMins(i));

        }
        callCenterManager = new Manager("mgr", AgentLevel.Manager,totalNoOfCalls);
        callCenterManager.setCallDurationTimes(callAgentsRepo.getMgrTestCallDurationInMins(0));

        managers.add (callCenterManager);

        agents.put (AgentLevel.JuniorExec, juniorAgentsPool);
        agents.put (AgentLevel.SeniorExec, seniorAgentsPool);
        agents.put (AgentLevel.Manager, managers);
    }

    public List<Agent> getJuniorExecList() {
        return agents.get(AgentLevel.JuniorExec);
    }

    public List<Agent> getSeniorExecList() {
        return agents.get(AgentLevel.SeniorExec);
    }

    public List<Agent> getMgrs() {
        return agents.get(AgentLevel.Manager);
    }

}
