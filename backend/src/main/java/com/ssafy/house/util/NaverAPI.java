package com.ssafy.house.util;

import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NaverAPI {
	
	public String getAccessToken(String code, String state) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "4aJFLRGOXi0gGcBwJ9Vs");
        body.add("client_secret", "MwQVff0Xk6");
        body.add("code", code);
        body.add("state", state);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );
        
        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        System.out.println(responseBody);
        return jsonNode.get("access_token").asText();
    }
	
	public HashMap<String, Object> getUserInfo(String accessToken) throws JsonProcessingException {
		
		HashMap<String, Object> userInfo = new HashMap<String, Object>();
		
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverUserInfoRequest,
                String.class
        );

        // HTTP 응답 받아오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        System.out.println("resopnse body =" + responseBody);
        
        String userId = jsonNode.get("response").get("id").asText();
        String nickname  = jsonNode.get("response").get("nickname").asText();
		String email = jsonNode.get("response").get("email").asText();

		userInfo.put("userId", userId);
		userInfo.put("email", email);
		userInfo.put("name", nickname);
		
		return userInfo;
    }
}