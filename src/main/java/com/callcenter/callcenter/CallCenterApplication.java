package com.callcenter.callcenter;

import com.callcenter.errors.exceptions.InvalidJSonConfigException;
import com.callcenter.model.Call;
import com.callcenter.model.CallCenter;
import com.callcenter.model.enums.CallRequestStatus;
import com.callcenter.services.CallAllocationService;
import com.callcenter.services.PerformanceAnalyser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class CallCenterApplication {

	CallCenter callCenter = null;
	CallAllocationService allocationService;
	int incomingCallCount = 0;

	@RequestMapping(value="/CallCenter", method = RequestMethod.POST)
	public void createCallCenter(@RequestBody String callCenterJSONStr) throws Exception {
		System.out.println("Rcvd request: " + callCenterJSONStr);
		if (callCenter!=null) {
			throw new Exception("A Call centre already exist, not creating one more");
		}
		try {
			callCenter = new CallCenter(callCenterJSONStr);
			callCenter.populateCallCentre();
			allocationService = new CallAllocationService(callCenter);
		} catch (Exception e) {
			System.out.println("JSON Config File Error: " + e.getMessage());
			throw new Exception("Failed to create Call center with the given config file");
		}
		return;
	}

	@RequestMapping(value="/Metrics", method = RequestMethod.GET)
	public String getPerformanceMetrics() throws Exception{
		if (callCenter==null) {
			throw new Exception("Create call center before requesting metrics");
		}
		PerformanceAnalyser metricAnalyser = new PerformanceAnalyser(callCenter);
		System.out.println("Getting performance metrics");
		String jsonOutput = metricAnalyser.getCallCenterMetrics();
		return jsonOutput;
	}

	@RequestMapping(value="/Call", method = RequestMethod.POST)
	public void handleCall() throws Exception{
		System.out.println("Rcvd Call request:");
		try {
			if (allocationService == null ) {
				throw new Exception("Create call center before placing a call");
			}

			String callID = String.format("%s%02d", "Call ID",incomingCallCount++ );
			Call newCall = new Call(callID, CallRequestStatus.UnAssigned);
			System.out.println("Dispatching call with callID " + callID);
			allocationService.dispatchCall(newCall);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

	public static void main(String[] args) {
		SpringApplication.run(CallCenterApplication.class, args);
	}
}
