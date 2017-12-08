package com.callcenter.model.enums;

public enum AgentLevel {
    JuniorExec,
    SeniorExec,
    Manager,
    None;

    static AgentLevel nextLevel(AgentLevel currentLevel) {
        switch (currentLevel) {
            case JuniorExec:
                return SeniorExec;
            case SeniorExec:
                return Manager;
            case Manager:
            default:
                return None;
        }
    }
}
