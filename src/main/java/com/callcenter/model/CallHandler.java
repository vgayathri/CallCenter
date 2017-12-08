package com.callcenter.model;

import com.callcenter.model.impl.CallHandlerImpl;

public interface CallHandler {

    public void handleCall (Call call) throws Exception;

    public void setNextCallHandler(CallHandlerImpl nextLevelHandler);

}
