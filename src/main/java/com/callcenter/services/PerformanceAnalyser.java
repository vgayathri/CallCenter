package com.callcenter.services;

import com.callcenter.model.Agent;
import com.callcenter.model.AgentMetrics;
import com.callcenter.model.CallCenter;
import com.callcenter.model.PerformanceMetrics;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.javafx.font.Metrics;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sun.misc.Perf;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerformanceAnalyser {

    CallCenter callCenterToAnalyse;
    public PerformanceAnalyser(CallCenter callcenter) {
        this.callCenterToAnalyse = callcenter;
    }

    public String getCallCenterMetrics() throws Exception{

        PerformanceMetrics performanceMetrics = new PerformanceMetrics(callCenterToAnalyse);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, performanceMetrics );
        System.out.println(stringWriter);
        return stringWriter.toString();

    }
}
