package com.callcenter.services;

import com.callcenter.model.*;
import com.callcenter.model.enums.AgentLevel;
import com.callcenter.model.enums.CallRequestStatus;
import com.callcenter.model.impl.CallHandlerImpl;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CallAllocationService {

    private CallCenter callCenter;   // TODO : Why does the Allocation service need the callcenter
    private int callsDispatched;

    private CallHandler rootHandler;

    public CallAllocationService(CallCenter callCenter) {
        this.callsDispatched = 0;
        this.callCenter = callCenter;
        rootHandler = new CallHandlerImpl(callCenter.getJuniorExecList());

        CallHandlerImpl seniorHandler = new CallHandlerImpl(callCenter.getSeniorExecList());
        CallHandlerImpl managerHandler = new CallHandlerImpl(callCenter.getMgrs());
        seniorHandler.setNextCallHandler(managerHandler);

        rootHandler.setNextCallHandler(seniorHandler);
    }

    public void dispatchCall(Call incomingCall) throws Exception {
        if (callsDispatched >= callCenter.getTotalNoOfCalls()) {
            throw new Exception("Max no of calls for the call center exceeded");
        }
        callsDispatched++;
        rootHandler.handleCall(incomingCall);

    }

}
