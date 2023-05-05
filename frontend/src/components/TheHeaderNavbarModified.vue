<template>
  <nav class="navbar navbar-expand-lg navbar-dark" style="background-color: #7d7d7d">
    <div class="container px-5">
      <router-link :to="{ name: 'main' }" class="navbar-brand"
        ><b>🏠 Where Is My House</b></router-link
      >
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav ms-auto mb-2 mb-lg-0" v-if="userInfo">
          <li class="nav-item">
            <router-link :to="{ name: 'QnAView' }" class="nav-link">QnA</router-link>
          </li>
          <li class="nav-item">
            <router-link :to="{ name: 'NoticeView' }" class="nav-link">공지사항</router-link>
          </li>
          <li class="nav-item">
            <router-link :to="{ name: 'findaddress' }" class="nav-link">동검색</router-link>
          </li>
          <li class="nav-item">
            <router-link :to="{ name: 'findapartment' }" class="nav-link">아파트검색</router-link>
          </li>
          <li class="nav-item">
            <a class="nav-link" style="cursor: pointer" @click="checkFavorite">관심지역</a>
          </li>
          <li class="nav-item">
            <a class="nav-link"> &nbsp;&nbsp;|&nbsp; </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" v-if="userInfo.isAdmin == 0"> 😊 {{userInfo.name}} 님 </a>
            <a class="nav-link" v-else> 👑 {{userInfo.name}} 님 </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" style="cursor: pointer; color: #5587ed" @click="doLogout"
              ><b>로그아웃</b></a
            >
          </li>
        </ul>
        <ul class="navbar-nav ms-auto mb-2 mb-lg-0" v-else>
          <li class="nav-item">
            <router-link :to="{ name: 'NoticeView' }" class="nav-link">공지사항</router-link>
          </li>
          <li class="nav-item">
            <router-link :to="{ name: 'login' }" class="nav-link" style="color: #5587ed"
              ><b>로그인</b></router-link
            >
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import store from '@/store';
import { mapState, mapGetters, mapActions } from 'vuex';
import http from '@/api/http';

const userStore = 'userStore';

export default {
  name: 'TheHeaderNavbar',
  data() {
    return {};
  },
  computed: {
    ...mapState(userStore, ['isLogin', 'userInfo', 'login']),
    ...mapGetters(['checkUserInfo']),
  },
  methods: {
    ...mapActions(userStore, ['userLogout']),
    doLogout() {
      const loginPlatform = store.getters['userStore/checkLoginPlatform'];
      if (loginPlatform === 1) {
        const url =
          'https://kauth.kakao.com/oauth/logout?client_id=' +
          '8705edb85db1a3d8342bb33ee262d130' +
          '&logout_redirect_uri=' +
          'http://localhost:9000/user/logout' +
          '&response_type=code';
        window.location.replace(url);
      } else if (loginPlatform === 2) {
        this.userLogout(this.userInfo.userId);
        this.$router.push({ name: 'main' });
      }
    },
    checkFavorite() {
      http.get(`favorite/${this.userInfo.userId}/1`).then(({ data }) => {
        if (!data) {
          alert('등록된 관심지역이 없습니다');
        } else {
          this.$router.push({ name: 'FavoriteList' });
        }
      });
    },
  },
};
</script>

<style>
@import 'https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css';
@import '../../src/assets/css/styles.css';

#logo {
  width: 120px;
}

.link {
  text-decoration: none;
}

/* 상단 nav 스크롤에 따라 상단 고정 */
/* nav {
  position: sticky !important;
  top: 0 !important;
} */
</style>
