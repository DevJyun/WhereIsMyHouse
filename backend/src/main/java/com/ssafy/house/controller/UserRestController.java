package com.ssafy.house.controller;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.house.model.dto.House;
import com.ssafy.house.model.dto.User;
import com.ssafy.house.model.service.FavoriteService;
import com.ssafy.house.model.service.FavoriteServiceImpl;
import com.ssafy.house.model.service.UserService;
import com.ssafy.house.util.KakaoAPI;
import com.ssafy.house.util.NaverAPI;
import com.ssafy.house.model.service.JwtServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class UserRestController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	FavoriteService favService;
	
	@Autowired
	private JwtServiceImpl jwtService;
	
	KakaoAPI kakaoApi = new KakaoAPI();
	NaverAPI naverApi = new NaverAPI();
	
	Logger logger = LoggerFactory.getLogger(UserRestController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	private static final int KAKAO = 1;
	private static final int NAVER = 2;
	
	@PostMapping("/kakaoLogin")
//	public ResponseEntity<?> aptFind(@RequestBody HashMap<String, Object> reqMap){
	public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestBody String code){
		logger.debug("kakaoCallback..........");
//		System.out.println(code);
		
		//인가코드로 카카오 액세스 토큰 받아오기
		String access_token = kakaoApi.getAccessToken(code);
//		System.out.println(access_token);
		
		//카카오 토큰으로 회원 정보 가져오기
		HashMap<String, Object> userInfo = kakaoApi.getUserInfo(access_token);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		
		if (userInfo != null) {
			String id = (String) userInfo.get("userId");
			String email = (String) userInfo.get("email");
			String name = (String) userInfo.get("name");
			
			//회원 DB에 해당 카카오 id 정보가 아직 없으면 생성
			User result = userService.search(new User(id, null, null, null, null, KAKAO, 0));
			if (result == null) {
				userService.insert(new User(id, null, name, email, null, KAKAO, 0));
				userInfo.put("isAdmin", 0);
			} else {
				userInfo.put("isAdmin", result.getIsAdmin());
			}
			
			//우리 서버 토큰 발행
			String accessToken = jwtService.createAccessToken("userid", id);// key, data
			String refreshToken = jwtService.createRefreshToken("userid", id);// key, data
			userService.saveRefreshToken(id, refreshToken);
			logger.debug("로그인 accessToken 정보 : {}", accessToken);
			logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
			
			resultMap.put("access-token", accessToken);
			resultMap.put("refresh-token", refreshToken);
			resultMap.put("userInfo", userInfo);
			resultMap.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} else {
			resultMap.put("message", FAIL);
			status = HttpStatus.ACCEPTED;
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PostMapping("/naverLogin")
//	public ResponseEntity<?> aptFind(@RequestBody HashMap<String, Object> reqMap){
	public ResponseEntity<Map<String, Object>> naverLogin(@RequestBody String code) throws IOException, ParseException{
		logger.debug("naverCallback..........");
		code = code.replaceAll("\\\"","");	//code 감싸고 있는 큰 따옴표 제거
		
		//인가코드로 네이버 액세스 토큰 받아오기
		String access_token = naverApi.getAccessToken(code, "123");
//		System.out.println(access_token);
		
		//네이버 토큰으로 회원 정보 가져오기
		HashMap<String, Object> userInfo = naverApi.getUserInfo(access_token);

		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		
		if (userInfo != null) {
			String id = (String) userInfo.get("userId");
			String email = (String) userInfo.get("email");
			String name = (String) userInfo.get("name");
			
			//회원 DB에 해당 네이버 id 정보가 아직 없으면 생성
			User result = userService.search(new User(id, null, null, null, null, NAVER, 0));
			if (result == null) {
				userService.insert(new User(id, null, name, email, null, NAVER, 0));
				userInfo.put("isAdmin", 0);
			} else {
				userInfo.put("isAdmin", result.getIsAdmin());
			}
			
			//우리 서버 토큰 발행
			String accessToken = jwtService.createAccessToken("userid", id);// key, data
			String refreshToken = jwtService.createRefreshToken("userid", id);// key, data
			userService.saveRefreshToken(id, refreshToken);
			logger.debug("로그인 accessToken 정보 : {}", accessToken);
			logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
			
			resultMap.put("access-token", accessToken);
			resultMap.put("refresh-token", refreshToken);
			resultMap.put("userInfo", userInfo);
			resultMap.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} else {
			resultMap.put("message", FAIL);
			status = HttpStatus.ACCEPTED;
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/logout/{userid}")
	public ResponseEntity<?> removeToken(@PathVariable("userid") String userid) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			userService.deleteRefreshToken(userid);
			resultMap.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);

	}
	
	@GetMapping("/checkToken/{userid}")
	public ResponseEntity<Map<String, Object>> checkToken(
			@PathVariable("userid") String userid,
			HttpServletRequest request) {
//		logger.debug("userid : {} ", userid);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		if (jwtService.checkToken(request.getHeader("access-token"))) {
			logger.info("사용 가능한 토큰!!!");
			try {
//				로그인 사용자 정보.
				User user = userService.idSearch(userid);
				if (user != null) {
					resultMap.put("message", SUCCESS);
					status = HttpStatus.ACCEPTED;
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				logger.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			logger.error("사용 불가능 토큰!!!");
			resultMap.put("message", FAIL);
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody String userid, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refresh-token");
		logger.debug("token : {}, userid : {}", token, userid);
		if (jwtService.checkToken(token)) {
			if (token.equals(userService.getRefreshToken(userid.replaceAll("\\\"","")))) {
				String accessToken = jwtService.createAccessToken("userid", userid);
				logger.debug("token : {}", accessToken);
				logger.debug("정상적으로 액세스토큰 재발급!!!");
				resultMap.put("access-token", accessToken);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			}
		} else {
			logger.debug("리프레쉬토큰도 사용불가!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/addFavorite")
	public ResponseEntity<?> addFavorite(String dongCode, String id) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("dongCode", dongCode);
		
		favService.insertFavorite(map);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping("/removeFavorite")
	public ResponseEntity<?> removeFavorite(String dongCode, String id) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("dongCode", dongCode);
		
		favService.removeFavorite(map);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
//	private static String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
//	private static String RSA_INSTANCE = "RSA"; // rsa transformation
//	
//	@GetMapping("/login")
//	public String loginForm(HttpSession session, Model model) throws IOException {
//		initRsa(session, model);
//		return "user/login";
//	}
//	
//	@PostMapping("/login")
//	public String login(HttpSession session,
//						@RequestParam(value = "id_rsa") String id_rsa, 
//						@RequestParam(value = "password_rsa") String password_rsa, 
//						Model model) throws Exception {
//		PrivateKey privateKey = (PrivateKey) session.getAttribute(RSA_WEB_KEY);
//		
//		// 복호화
//		String id = decryptRsa(privateKey, id_rsa);
//		String password = decryptRsa(privateKey, password_rsa);
//		
//		// 개인키 삭제
//		session.removeAttribute(RSA_WEB_KEY);
//		
//		User user = userService.search(new User(id, password, null, null));
//		
//		if (user != null) {
//			session.setAttribute("userInfo", user);
//			return "redirect:/";
//		} else {
//			model.addAttribute("msg", "아이디 또는 비밀번호가 잘못 되었습니다!");
//			initRsa(session, model);
//			return "user/login";
//		}
//	}
//	
//	@GetMapping("/logout")
//	public String logout(HttpSession session) {
//		session.invalidate();
//		return "redirect:/";
//	}
//	
//	@GetMapping("/signup")
//	public String signupForm(HttpSession session, Model model) {
//		initRsa(session, model);
//		return "user/signup";
//	}
//	
//	@PostMapping("/signup")
//	public String signup(HttpSession session,
//						@RequestParam(value = "id_rsa") String id_rsa, 
//						@RequestParam(value = "password_rsa") String password_rsa,
//						Model model,
//						User user) throws Exception {
//		
//		if (userService.idSearch(user.getId()) != null) {
//			model.addAttribute("msg", "이미 존재하는 아이디입니다!");
//			initRsa(session, model);
//			return "user/signup";
//		}
//		
//		PrivateKey privateKey = (PrivateKey) session.getAttribute(RSA_WEB_KEY);
//
//		String id = decryptRsa(privateKey, id_rsa);
//		String password = decryptRsa(privateKey, password_rsa);
//		
//		if(id.equals(user.getId()) && password.equals(user.getPassword())) {
//			userService.insert(user);
//			session.setAttribute("userInfo", user);
//			session.removeAttribute(RSA_WEB_KEY);
//		}
//		return "/index";
//	}
//	
//	@GetMapping("/userinfo")
//	public String userinfo(HttpSession session, Model model) {
//		if (!checkUserSession(session, model)) return "user/login";
//		return "user/userInfo";
//	}
//	
//	@GetMapping("/userUpdateForm")
//	public String userUpdateForm(HttpSession session, Model model) {
//		if (!checkUserSession(session, model)) return "user/login";
//		initRsa(session, model);
//		return "user/userUpdate";
//	}
//	
//	@PostMapping("/userUpdate")
//    public String userUpdate(HttpSession session,
//					        @RequestParam(value = "id_rsa") String id_rsa, 
//					        @RequestParam(value = "password_rsa") String password_rsa, 
//					        Model model, 
//					        User user) throws Exception {
//        
//        PrivateKey privateKey = (PrivateKey) session.getAttribute(RSA_WEB_KEY);
//        
//        // 복호화
//        String id = decryptRsa(privateKey, id_rsa);
//        String password = decryptRsa(privateKey, password_rsa);
//        String name = user.getName();
//        String email = user.getEmail();
//        User updateUser = new User(id, password, name, email);
//
//        // 개인키 삭제
//        session.removeAttribute(RSA_WEB_KEY);
//       
//        userService.update(updateUser);
//        session.setAttribute("userInfo", updateUser);
//        
//        return "redirect:/userinfo";
//    }
//	
//	@PostMapping("/userCancel")
//	public String userCancel() {
//		return "redirect:/userinfo";
//	}
//	
//	@PostMapping("/findPass/form")
//	public String findPassword() {
//		return "user/findPassword";
//	}
//	
//	@PostMapping("/findPassword")
//	public String findPassword(HttpSession session, Model model, User user) {
//		String password = userService.findPassword(user);
//		
//		if (password != null) {
//			model.addAttribute("msg", String.format("회원님의 비밀번호는 %s 입니다!", password));
//			initRsa(session, model);
//			return "user/login";
//		} else {
//			model.addAttribute("msg", "일치하는 회원 정보가 없습니다!");
//			return "user/findPassword";
//		}
//	}
//	
//	@GetMapping("/userRemove")
//	public String userRemove(HttpSession session, Model model) {
//		User user = (User) session.getAttribute("userInfo");
//		userService.remove(user.getId());
//		model.addAttribute("msg", "로그인이 필요합니다!");
//		session.invalidate();
//		return "redirect:/";
//	}
//	
//	////////////////////////////////// RSA ////////////////////////////////////////
//	/**
//	 * 복호화
//	 */
//	private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
//		Cipher cipher = Cipher.getInstance(RSA_INSTANCE);
//		byte[] encryptedBytes = hexToByteArray(securedValue);
//		cipher.init(Cipher.DECRYPT_MODE, privateKey);
//		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
//		String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
//		return decryptedValue;
//	}
//	
//	/**
//	* 16진 문자열을 byte 배열로 변환한다.     
//	*/
//	public static byte[] hexToByteArray(String hex) {
//		if (hex == null || hex.length() % 2 != 0) { return new byte[] {}; }
//		byte[] bytes = new byte[hex.length() / 2];
//		for (int i = 0; i < hex.length(); i += 2) {
//			byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
//			bytes[(int) Math.floor(i / 2)] = value;
//		}
//		return bytes;
//	}
//	
//	/**
//	 * rsa 공개키, 개인키 생성
//	 */
//	public void initRsa(HttpSession session, Model model) {
//		KeyPairGenerator generator;
//		try {
//			generator = KeyPairGenerator.getInstance(RSA_INSTANCE);
//			generator.initialize(1024);
//			
//			KeyPair keyPair = generator.genKeyPair();
//			KeyFactory keyFactory = KeyFactory.getInstance(RSA_INSTANCE);
//			PublicKey publicKey = keyPair.getPublic();
//			PrivateKey privateKey = keyPair.getPrivate();
//			
//			session.setAttribute(RSA_WEB_KEY, privateKey); // session에 RSA 개인키를 세션에 저장
//			
//			RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
//			String publicKeyModulus = publicSpec.getModulus().toString(16);
//			String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
//			
//			model.addAttribute("RSAModulus", publicKeyModulus); // rsa modulus 를 request 에 추가
//			model.addAttribute("RSAExponent", publicKeyExponent); // rsa exponent 를 request 에 추가
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	////////////////////////////////// Session에 유저정보 있는지 확인 ////////////////////////////////////////
//	public boolean checkUserSession(HttpSession session, Model model) {
//		if (session.getAttribute("userInfo") == null) {
//			model.addAttribute("msg", "로그인이 필요합니다!");
//			initRsa(session, model);
//			return false;
//		}
//		return true;
//	}
}
