package com.callcenter.model;

import java.util.Comparator;

public class AgentComparator implements Comparator<Agent> {

    private Agent agent1;
    private Agent agent2;

    public int compareAvgDurationTimes(float duration1 , float duration2) {

        if(duration1 == duration2)
            return 0;
        else if(duration1 > duration2)
            return 1;
        else
            return -1;
    }
    @Override
    public int compare(Agent agent1, Agent agent2) {

        if(agent1.getCallsServiced()==agent2.getCallsServiced()) {
            compareAvgDurationTimes(agent1.getAvgCallResolutionTime(), agent2.getAvgCallResolutionTime());
        }
        else if(agent1.getCallsServiced() > agent2.getCallsServiced())
            return 1;
        else
            return -1;
        return 0;
    }
}
