import Vue from "vue";
import VueRouter from "vue-router";
import AppMain from "@/views/AppMain";

import FindAddress from "@/components/house/FindAddress";
import FindApartment from "@/components/house/FindApartment";
import FavoriteList from "@/components/house/FavoriteList";
import ShowHouseDetail from "@/components/house/ShowHouseDetail";

import QnAView from "@/components/QnA/QnAView";
import QnAListView from "@/components/QnA/QnAListView";
import QnARegistView from "@/components/QnA/QnARegistView";
import QnADetailView from "@/components/QnA/QnADetailView";
import QnAModifyView from "@/components/QnA/QnAModifyView";

import NoticeView from "@/components/Notice/NoticeView";
import NoticeListView from "@/components/Notice/NoticeListView";
import NoticeRegistView from "@/components/Notice/NoticeRegistView";
import NoticeDetailView from "@/components/Notice/NoticeDetailView";
import NoticeModifyView from "@/components/Notice/NoticeModifyView";

import store from "@/store";

Vue.use(VueRouter);

// https://v3.router.vuejs.org/kr/guide/advanced/navigation-guards.html
const onlyAuthUser = async (to, from, next) => {
  const checkUserInfo = store.getters["userStore/checkUserInfo"];
  const checkToken = store.getters["userStore/checkToken"];
  let token = sessionStorage.getItem("access-token");

  if (checkUserInfo != null && token) {
    console.log("토큰 유효성 체크하러 가자!!!!");
    await store.dispatch("userStore/checkTokenAvail", token);
  }
  if (!checkToken || checkUserInfo === null) {
    alert("로그인이 필요한 페이지입니다..");
    // next({ name: "login" });
    router.push({ name: "login" });
  } else {
    console.log("로그인 했다!!!!!!!!!!!!!.");
    next();
  }
};

const routes = [
  {
    path: "/",
    name: "main",
    component: AppMain,
  },
  {
    path: "/user/login",
    name: "login",
    component: () => import("@/components/user/UserLoginModified"),
  },
  {
    path: "/user/kakaoLogin",
    name: "kakaoLogin",
    component: () => import("@/components/user/UserKakaoLogin"),
  },
  {
    path: "/user/naverLogin",
    name: "naverLogin",
    component: () => import("@/components/user/UserNaverLogin"),
  },
  {
    path: "/user/logout",
    name: "logout",
    component: () => import("@/components/user/UserLogout"),
  },
  {
    path: "/findAddress",
    name: "findaddress",
    beforeEnter: onlyAuthUser,
    component: FindAddress,
  },
  {
    path: "/findApartment",
    name: "findapartment",
    beforeEnter: onlyAuthUser,
    component: FindApartment,
  },
  {
    path: "/qna",
    name: "QnAView",
    beforeEnter: onlyAuthUser,
    component: QnAView,
    redirect: "/qna/qnalist",
    children: [
      {
        path: "qnalist",
        name: "QnAListView",
        component: QnAListView,
      },
      {
        path: "qnaregist",
        name: "QnARegistView",
        component: QnARegistView,
      },
      {
        path: "qnadetail",
        name: "QnADetailView",
        component: QnADetailView,
      },
      {
        path: "qnamodify",
        name: "QnAModifyView",
        component: QnAModifyView,
      },
    ],
  },
  {
    path: "/notice",
    name: "NoticeView",
    component: NoticeView,
    redirect: "/notice/noticelist",
    children: [
      {
        path: "noticelist",
        name: "NoticeListView",
        component: NoticeListView,
      },
      {
        path: "noticeregist",
        name: "NoticeRegistView",
        component: NoticeRegistView,
      },
      {
        path: "noticedetail",
        name: "NoticeDetailView",
        component: NoticeDetailView,
      },
      {
        path: "noticemodify",
        name: "NoticeModifyView",
        component: NoticeModifyView,
      },
    ],
  },
  {
    path: "/favorite",
    name: "FavoriteList",
    beforeEnter: onlyAuthUser,
    component: FavoriteList,
  },
  {
    path: "/showHouseDetail",
    name: "ShowHouseDetail",
    component: ShowHouseDetail,
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
