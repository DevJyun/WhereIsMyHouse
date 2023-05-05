<template>
  <body class="d-flex flex-column" style="background-color:#FFEFE0">
    <main class="flex-shrink-0">
      <section class="py-5">
        <div class="container px-5" style="height:600px; display: flex;justify-content: center;align-items: center;">
          <!-- Contact form-->
          <div class="card" style="width: 70%; height: 57%; background-color: #f8f9fa; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
            <div class="card-body">
              <div class="text-center mb-5">
                <h1>🔐</h1>
                <h1 class="fw-bolder">로그인</h1>
                <p class="lead fw-normal text-muted mb-0">간편하게 로그인하고<br>다양한 서비스를 이용하세요.</p>
              </div>
              <div class="row gx-5 justify-content-center">
                <div class="col-lg-8 col-xl-6">
                  <div class="row">
                    <div class="col-md-6">
                      <a style="cursor: pointer" :href="KAKAO_LOGIN_URL"
                        ><img src="../../assets/img/kakao_login.png"
                      /></a>
                    </div>
                    <div class="col-md-6">
                      <a style="cursor: pointer" :href="NAVER_LOGIN_URL"
                        ><img src="../../assets/img/naver_login.png"
                      /></a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </body>
</template>

<script>
export default {
  name: 'UserLoginModified',
  data() {
    return {
      //카카오
      KAKAO_LOGIN_URL: 'https://kauth.kakao.com/oauth/authorize?response_type=code',
      KAKAO_CLIENT_ID: '8705edb85db1a3d8342bb33ee262d130',
      KAKAO_REDIRECT_URI: 'http://localhost:9000/user/kakaoLogin',

      //네이버
      NAVER_LOGIN_URL: 'https://nid.naver.com/oauth2.0/authorize?response_type=code',
      NAVER_CLIENT_ID: '4aJFLRGOXi0gGcBwJ9Vs',
      NAVER_REDIRECT_URI: 'http://localhost:9000/user/naverLogin',
      NAVER_STATE: '123',
    };
  },
  created() {
    this.KAKAO_LOGIN_URL += '&client_id=' + this.KAKAO_CLIENT_ID;
    this.KAKAO_LOGIN_URL += '&redirect_uri=' + this.KAKAO_REDIRECT_URI;

    this.NAVER_LOGIN_URL += '&client_id=' + this.NAVER_CLIENT_ID;
    this.NAVER_LOGIN_URL += '&redirect_uri=' + this.NAVER_REDIRECT_URI;
    this.NAVER_LOGIN_URL += '&state=' + this.NAVER_STATE;
  },
  methods: {},
};
</script>

<style></style>
