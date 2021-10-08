package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

import com.serverless.dal.Mutant;

public class MutantHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

      try {
          // get the 'body' from input
          JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

          // create the mutant object for post
          Mutant mutant = new Mutant();
          // mutant.setId(body.get("id").asText());

          String[] dna = body.get("adn").asText().replace("[","").replace("]", "").split(",");  
          System.out.println("adn: "+ body.get("adn").asText());
          System.out.println("dna: "+ Arrays.toString(dna));
          //String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
          String[][] math = fillMat(dna);
          if(isMutant(math)){
				mutant.setMutant("1");
				// send the response back
				mutant.setAdn(body.get("adn").asText());
          		mutant.save(mutant);
      		return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(mutant)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();
          }else{
          		mutant.setMutant("0");
          		mutant.setAdn(body.get("adn").asText());
         		mutant.save(mutant);
          		return ApiGatewayResponse.builder()
      				.setStatusCode(403)
      				.setObjectBody(mutant)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();
          }
          
          

          

      } catch (Exception ex) {
          logger.error("Error in saving mutant: " + ex);

          // send the error response back
    			Response responseBody = new Response("Error in saving mutant: ", input);
    			return ApiGatewayResponse.builder()
    					.setStatusCode(500)
    					.setObjectBody(responseBody)
    					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
    					.build();
      }
	}

	private static Boolean isMutant(String[][] dna) {
		if(verticalMutant(dna)) {
			System.out.println("ES MUTANT VERTICAL!!!");
			return true;
		}else{
			if(horizontalMutant(dna)) {
				System.out.println("ES MUTANT HORIZONTAL!!!");
				return true;
			}else {
				return false;
			}
		}	
	}

	private static String[][] fillMat(String[]dna){
		if(dna[0]!=null && !dna[0].isEmpty()) {
			String[][]mat = new String[dna[0].length()][dna[0].length()];
			
			for (int i = 0; i < dna.length; i++) {
				if(dna[i].length()!=dna[0].length()) {
					System.out.println("Todas las columnas deben ser del mismo tamano");
			         return null;
				}
				for (int j = 0; j < dna[i].length(); j++) {
					Pattern pat = Pattern.compile("(A|T|C|G|')+");
				     Matcher matches = pat.matcher(dna[i]);
				     System.out.println("dna[i]" + dna[i]);                                                                           
				     //if (!matches.matches()) {
				         //System.out.println("NO PUEDE CONTENER CARACTERES DIFERENTES A ATCG");
				         //return null;
				     //} 

					mat[i][j]= String.valueOf(dna[i].charAt(j)); 
				} 
			}
			
			//printMath(mat);
			return mat;
			
		}else {
			System.out.println("ingresar un valor");
		}
		
		return  null;
	}

		private static Boolean verticalMutant(String[][]dna) {
		Integer cont = 1 ;
		for (int i = 0; i < dna.length; i++) {
			String aux = dna[0][i];
            // Loop through all elements of current row
            for (int j = 0; j < dna[i].length-1; j++) {
                if(dna[j+1][i].equals(aux)) {
                	cont = cont + 1;
                }else {
                	if(cont >=4) {
                		break;
                	}else {
                		 aux = dna[j+1][i];
                		 cont =1;
                	}
                }
            }
            if(cont >=4) {
            	return true;
            }else {
            	cont =1;
            }
		}
		return false;
	}
	
		private static Boolean horizontalMutant(String[][]dna) {
		Integer cont = 1 ;
		for (int i = 0; i < dna.length; i++) {
			String aux = dna[i][0];
            // Loop through all elements of current row
            for (int j = 0; j < dna[i].length-1; j++) {
                if(dna[i][j+1].equals(aux)) {
                	cont = cont + 1;
                }else {
                	if(cont >=4) {
                		break;
                	}else {
                		 aux = dna[i][j+1];
                		 cont =1;
                	}
                }
            }
            if(cont >=4) {
            	return true;
            }else {
            	cont =1;
            }
		}
		return false;
	}
}
