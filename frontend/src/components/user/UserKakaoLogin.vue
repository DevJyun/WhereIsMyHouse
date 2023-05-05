<template>
  <container class="bv-example-row mt-3" style="background-color:#fff7f7">
    <div style="height:700px; display: flex; justify-content: center; align-items: center;">
      <img src="../../assets/img/loading.gif" style="width:150px; height:150px;"/>
    </div>
  </container>
</template>

<script>
import { mapState, mapActions } from 'vuex';

const userStore = 'userStore';

export default {
  name: 'kakaoLogin',

  created() {
    this.create();
  },

  data() {
    return {
      codes: '',
    };
  },
  computed: {
    ...mapState(userStore, ['isLogin', 'isLoginError', 'userInfo']),
  },
  methods: {
    ...mapActions(userStore, ['userConfirmKakao']),
    create() {
      this.codes = this.$route.query.code;
      this.getToken();
    },
    async getToken() {
      //카카오 인가코드 서버에 넘겨서 정상 로그인 시 서버 토큰 발급받음
      await this.userConfirmKakao(this.codes);
      if (this.isLogin) {
        // await this.getUserInfo(token);
        // console.log('4. confirm() userInfo :: ', this.userInfo);
        this.$router.push({ name: 'main' });
      }
    },
  },
};
</script>

<style></style>
