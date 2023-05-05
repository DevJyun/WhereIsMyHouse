<template>
  <container class="bv-example-row mt-3">
    <b-row>
      <b-col>
        <alert variant="secondary" show><h3>로그아웃 중</h3></alert>
      </b-col>
    </b-row>
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
    return {};
  },
  computed: {
    ...mapState(userStore, ['isLogin', 'isLoginError', 'userInfo']),
  },
  methods: {
    ...mapActions(userStore, ['userLogout']),
    create() {
      console.log(this.userInfo.userId);
      this.userLogout(this.userInfo.userId);
      sessionStorage.removeItem('access-token'); //저장된 토큰 없애기
      sessionStorage.removeItem('refresh-token'); //저장된 토큰 없애기
      if (this.$route.path != '/') this.$router.push({ name: 'main' });
    },
  },
};
</script>

<style></style>
