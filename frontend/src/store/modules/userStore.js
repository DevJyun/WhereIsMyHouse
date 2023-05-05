import jwtDecode from "jwt-decode";
import router from "@/router";
import { kakaoLogin, naverLogin, logout, checkToken, tokenRegeneration } from "@/api/user";

const memberStore = {
  namespaced: true,
  state: {
    isLogin: false,
    isLoginError: false,
    userInfo: null,
    isValidToken: false,
    loginPlatform: null,  //1:카카오, 2:네이버
  },
  getters: {
    checkUserInfo: function (state) {
      return state.userInfo;
    },
    checkToken: function (state) {
      return state.isValidToken;
    },
    checkLoginPlatform: function (state) {
      return state.loginPlatform;
    },
  },
  mutations: {
    SET_IS_LOGIN: (state, isLogin) => {
      state.isLogin = isLogin;
    },
    SET_IS_LOGIN_ERROR: (state, isLoginError) => {
      state.isLoginError = isLoginError;
    },
    SET_IS_VALID_TOKEN: (state, isValidToken) => {
      state.isValidToken = isValidToken;
    },
    SET_USER_INFO: (state, userInfo) => {
      state.isLogin = true;
      state.userInfo = userInfo;
    },
    SET_LOGIN_PLATFORM: (state, loginPlatform) => {
      state.loginPlatform = loginPlatform;
    },
  },
  actions: {
    async userConfirmKakao({ commit }, codes) {
      await kakaoLogin(
        codes,
        ({ data }) => {
          if (data.message === "success") {
            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];
            let userInfo = data["userInfo"];
            console.log("login success token created!!!! >> ", accessToken, refreshToken);
            console.log("login userInfo!!!! >> ", userInfo);
            commit("SET_IS_LOGIN", true);
            commit("SET_IS_LOGIN_ERROR", false);
            commit("SET_IS_VALID_TOKEN", true);
            commit("SET_USER_INFO", userInfo);
            commit("SET_LOGIN_PLATFORM", 1);
            sessionStorage.setItem("access-token", accessToken);
            sessionStorage.setItem("refresh-token", refreshToken);
          } else {
            commit("SET_IS_LOGIN", false);
            commit("SET_IS_LOGIN_ERROR", true);
            commit("SET_IS_VALID_TOKEN", false);
            commit("SET_LOGIN_PLATFORM", null);
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    async userConfirmNaver({ commit }, codes) {
      await naverLogin(
        codes,
        ({ data }) => {
          if (data.message === "success") {
            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];
            let userInfo = data["userInfo"];
            console.log("login success token created!!!! >> ", accessToken, refreshToken);
            console.log("login userInfo!!!! >> ", userInfo);
            commit("SET_IS_LOGIN", true);
            commit("SET_IS_LOGIN_ERROR", false);
            commit("SET_IS_VALID_TOKEN", true);
            commit("SET_USER_INFO", userInfo);
            commit("SET_LOGIN_PLATFORM", 2);
            sessionStorage.setItem("access-token", accessToken);
            sessionStorage.setItem("refresh-token", refreshToken);
          } else {
            commit("SET_IS_LOGIN", false);
            commit("SET_IS_LOGIN_ERROR", true);
            commit("SET_IS_VALID_TOKEN", false);
            commit("SET_LOGIN_PLATFORM", null);
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    async userLogout({ commit }, userid) {
      await logout(
        userid,
        ({ data }) => {
          if (data.message === "success") {
            commit("SET_IS_LOGIN", false);
            commit("SET_USER_INFO", null);
            commit("SET_IS_VALID_TOKEN", false);
            commit("SET_LOGIN_PLATFORM", null);
          } else {
            console.log("유저 정보 없음!!!!");
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    async checkTokenAvail({ commit, dispatch }, token) {
      let decodeToken = jwtDecode(token);
      // console.log("2. getUserInfo() decodeToken :: ", decodeToken);
      await checkToken(
        decodeToken.userid,
        ({ data }) => {
          if (data.message === "success") {
            // commit("SET_USER_INFO", data.userInfo);
            // console.log("3. getUserInfo data >> ", data);
          } else {
            console.log("유저 정보 없음!!!!");
          }
        },
        async (error) => {
          console.log("checkToken() error code [토큰 만료되어 사용 불가능.] ::: ", error.response.status);
          commit("SET_IS_VALID_TOKEN", false);
          await dispatch("tokenRegeneration");
        }
      );
    },
    async tokenRegeneration({ commit, state }) {
      console.log("토큰 재발급 >> 기존 토큰 정보 : {}", sessionStorage.getItem("access-token"));
      await tokenRegeneration(
        state.userInfo.userId,
        ({ data }) => {
          if (data.message === "success") {
            let accessToken = data["access-token"];
            console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
            sessionStorage.setItem("access-token", accessToken);
            commit("SET_IS_VALID_TOKEN", true);
          }
        },
        async (error) => {
          // HttpStatus.UNAUTHORIZE(401) : RefreshToken 기간 만료 >> 다시 로그인!!!!
          if (error.response.status === 401) {
            console.log("갱신 실패");
            // 다시 로그인 전 DB에 저장된 RefreshToken 제거.
            await logout(
              state.userInfo.userId,
              ({ data }) => {
                if (data.message === "success") {
                  console.log("리프레시 토큰 제거 성공");
                } else {
                  console.log("리프레시 토큰 제거 실패");
                }
                alert("로그인 시간 만료!!! 다시 로그인해 주세요.");
                commit("SET_IS_LOGIN", false);
                commit("SET_USER_INFO", null);
                commit("SET_IS_VALID_TOKEN", false);
                commit("SET_LOGIN_PLATFORM", null);
                router.push({ name: "login" });
              },
              (error) => {
                console.log(error);
                commit("SET_IS_LOGIN", false);
                commit("SET_USER_INFO", null);
                commit("SET_LOGIN_PLATFORM", null);
              }
            );
          }
        }
      );
    },
  },
};

export default memberStore;
