package com.callcenter.model;

import com.callcenter.model.enums.CallRequestStatus;

public class Call {

    String callID;
    CallRequestStatus callStatus;

    public Call(String name, CallRequestStatus status) {
        this.callID = name;
        this.callStatus = status;
    }

    public CallRequestStatus getCallStatus() {
        return callStatus;
    }

    public void updateCallStatus(CallRequestStatus newStatus) {
        this.callStatus = newStatus;
    }
}
