import http from '@/api/http';

async function kakaoLogin(codes, success, fail) {
  await http.post('kakaoLogin', codes).then(success).catch(fail);
}

async function naverLogin(codes, success, fail) {
  await http.post('naverLogin', codes).then(success).catch(fail);
}

async function checkToken(userid, success, fail) {
  http.defaults.headers["access-token"] = sessionStorage.getItem("access-token");
  await http.get(`checkToken/${userid}`).then(success).catch(fail);
}

async function tokenRegeneration(userid, success, fail) {
  http.defaults.headers["refresh-token"] = sessionStorage.getItem("refresh-token"); //axios header에 refresh-token 셋팅
  await http.post('refresh', userid).then(success).catch(fail);
}

async function logout(userid, success, fail) {
  await http.get(`logout/${userid}`).then(success).catch(fail);
}

export { kakaoLogin, naverLogin, tokenRegeneration, logout, checkToken };
