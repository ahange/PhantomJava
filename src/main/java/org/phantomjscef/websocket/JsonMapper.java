package org.phantomjscef.websocket;

import java.io.IOException;

import org.phantomjscef.data.Data;
import org.phantomjscef.data.Result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {
	private static ObjectMapper mapper = new ObjectMapper();

	public static String getJsonString(Data data){
		try {
			String json = mapper.writeValueAsString(data);
			return json;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Result getResult(String json){
		try {
			Result result= mapper.readValue(json,Result.class);
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
