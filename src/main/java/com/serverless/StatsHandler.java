package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import org.json.simple.JSONObject;

import com.serverless.dal.Mutant;

public class StatsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
    try {
        // get mutants
       Integer nMutant = new Mutant().countMutant();
       System.out.println("nMutant "+ nMutant);
       Integer nHumans = new Mutant().countHuman();
 	   System.out.println("nHumans "+ nHumans);
       JSONObject obj = new JSONObject();

		obj.put("count_mutant_dna", nMutant);
		obj.put("count_human_dna", nHumans);
		obj.put("ratio", nMutant/nHumans);

        // send the response back
        return ApiGatewayResponse.builder()
    				.setStatusCode(200)
    				.setObjectBody(obj)
    				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
    				.build();
    } catch (Exception ex) {
        logger.error("Error in listing products: " + ex);

        // send the error response back
  			Response responseBody = new Response("Error in listing products: ", input);
  			return ApiGatewayResponse.builder()
  					.setStatusCode(500)
  					.setObjectBody(responseBody)
  					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
  					.build();
    }
	}
}
