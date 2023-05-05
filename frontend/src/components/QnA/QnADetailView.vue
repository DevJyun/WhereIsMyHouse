<template>
  <div class="container">
    <div class="row col-md-12 justify-content-center mb-2"></div>

    <section id="contents">
      <div class="row">
        <div class="container col-9" style="float: left">
          <div class="card" style="width: 100%; background-color: #f8f9fa; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
            <div class="card-body">
              <p class="card-text">
                <table class="table table-hover" style="text-align: center; vertical-align: middle; height: 400px">
                  <!-- <thead></thead> -->
                  <tbody>
                    <tr>
                      <td style="width: 200px"><b>제목</b></td>
                      <td style="height: 40px; font-weight: bold" v-text="qna.title"></td>
                    </tr>
                    <tr>
                      <td><b>글 번호</b></td>
                      <td style="height: 40px" v-text="qna.no"></td>
                    </tr>
                    <tr>
                      <td><b>작성자</b></td>
                      <td style="height: 40px" v-text="qna.name"></td>
                    </tr>
                    <tr>
                      <td><b>작성일자</b></td>
                      <td style="height: 40px" v-text="qna.regtime"></td>
                    </tr>
                    <tr>
                      <td><b>문의사항</b></td>
                      <td style="height: 40px" v-text="qna.question"></td>
                    </tr>
                    <tr>
                      <td><b>답변</b></td>
                      <td v-text="qna.answer"></td>
                    </tr>
                  </tbody>
                </table>
              </p>
            </div>
          </div>
          <!-- <h4 style="color: #204969">
            <b>{{ this.qna.title }}</b>
          </h4>
          <br /> -->

          <div class="nav" style="padding-top: 1em;">
            <div class="nav-item">
              <button class="btn btn-outline-primary" @click="moveHandler">목록</button>
            </div>
            &nbsp;
            <div class="nav-item" v-if="this.userInfo && this.userInfo.userId === this.qna.id">
              <router-link :to="{ name: 'QnAModifyView', query: { no: qna.no, addAnswer: 0 } }">
                <button class="btn btn-outline-primary">수정</button>
              </router-link>
            </div>
            <div class="nav-item" v-else-if="this.userInfo && this.userInfo.isAdmin === 1">
              <router-link :to="{ name: 'QnAModifyView', query: { no: qna.no, addAnswer: 1 } }">
                <button class="btn btn-outline-primary">답변</button>
              </router-link>
            </div>
            &nbsp;
            <div
              class="nav-item"
              v-if="
                this.userInfo &&
                (this.userInfo.userId === this.qna.id || this.userInfo.isAdmin === 1)
              "
            >
              <button class="btn btn-outline-primary" @click="removeHandler">삭제</button>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import http from '@/api/http';
import { mapState } from 'vuex';

const userStore = 'userStore';

export default {
  data() {
    return {
      no: '',
      qna: {},
    };
  },
  computed: {
    ...mapState(userStore, ['userInfo']),
  },
  created() {
    //DOM객체와 연결되었고 Vue의 속성들도 초기화 된 상태
    this.no = this.$route.query.no;
    console.log('view.html..................no]', this.no);
    http
      .get(`qna/${this.no}`)
      .then(({ data }) => {
        this.qna = data;
      })
      .catch(({ data }) => {
        alert(data);
      });
  },
  methods: {
    moveHandler() {
      this.$router.push({ name: 'QnAListView' });
    },
    removeHandler() {
      console.log('view.html..................삭제]', this.no);
      http
        .delete(`qna/${this.no}`)
        .then(({ data }) => {
          if (data == 'success') {
            alert('삭제 성공');
            this.moveHandler();
          }
        })
        .catch(({ data }) => {
          alert(data);
        });
    },
  },
};
</script>

<style></style>
