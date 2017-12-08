package com.callcenter.repository;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.callcenter.errors.exceptions.InvalidJSonConfigException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;


public class CallCenterRepo {

    int totaNoOfCalls= 0 ;
    int noOfJuniorExecs = 0;
    int noOfSeniorExecs = 0;
    /*Currently the system supports only 1 manager*/
    final int noOfManagers = 1;

    JSONObject jsonObject;
    JSONArray juniorExecList, seniorExecList;
    String managerList;

    public void readFromJSONFile(String jsonString) throws Exception {

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonString);

        jsonObject = (JSONObject) obj;

        String name = (String) jsonObject.get("number_of_calls");
        juniorExecList = (JSONArray) jsonObject.get("je");
        seniorExecList = (JSONArray) jsonObject.get("se");
        managerList = (String) jsonObject.get("mgr");
        if (null == juniorExecList || juniorExecList.size() < 1) {
            throw new InvalidJSonConfigException ("Call center needs at least one Junior agent to handle call");
        }
        if (null == seniorExecList || seniorExecList.size() < 1) {
            throw new InvalidJSonConfigException ("Call center needs at least one Senior agent to handle escalations");
        }
        if (null == managerList || noOfManagers != 1) {
            throw new InvalidJSonConfigException ("Call center needs a manager");
        }
        totaNoOfCalls = Integer.parseInt(name);
        if  (totaNoOfCalls <=0) {
            throw new InvalidJSonConfigException ("Call center max no of calls needs to be greater than 0");
        }
        System.out.println("Number of calls the call center supports: " + totaNoOfCalls);
        noOfJuniorExecs = juniorExecList.size();
        noOfSeniorExecs = seniorExecList.size();
        if (noOfSeniorExecs >= noOfJuniorExecs) {
            throw new InvalidJSonConfigException("No of Junior Execs should be more than no of Senior Executives");
        }

    }

    public int getNoOfJuniorExecs() {
        return noOfJuniorExecs;
    }

    public int getNoOfSeniorExecs() {
        return noOfSeniorExecs;
    }

    public int getTotaNoOfCalls() {
        return totaNoOfCalls;
    }

    public int[] getJETestCallDurationInMins(int index) {
        return Arrays.stream(juniorExecList.get(index).toString().split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }
    public int[] getSETestCallDurationInMins(int index) {
       return Arrays.stream(seniorExecList.get(index).toString().split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

    }
    public int[] getMgrTestCallDurationInMins(int index) {
        return Arrays.stream(managerList.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}

