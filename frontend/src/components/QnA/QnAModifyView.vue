<template>
  <div class="container">
    <div class="row col-md-12 justify-content-center mb-2"></div>

    <section id="contents">
      <div class="row">
        <div class="container col-9" style="float: left">
          <div class="card" style="width: 100%; background-color: #f8f9fa; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
            <div class="card-body">
              <p class="card-text">
                <table
                  class="table table-hover"
                  style="text-align: center; vertical-align: middle; height: 400px"
                >
                  <!-- <thead></thead> -->
                  <tbody>
                    <tr>
                      <td style="width: 100px"><b>제목</b></td>
                      <td v-if="this.addAnswer === 0">
                        <input type="text" id="title" ref="title" v-model="qna.title" />
                      </td>
                      <td v-else-if="this.addAnswer === 1">
                        {{ this.qna.title }}
                      </td>
                    </tr>
                    <tr>
                      <td><b>작성자</b></td>
                      <td>
                        <!-- <input type="text" id="id" ref="id" v-model="qna.id" /> -->
                        {{ this.qna.name }}
                      </td>
                    </tr>
                    <tr>
                      <td><b>문의사항</b></td>
                      <td v-if="this.addAnswer === 0">
                        <textarea
                          id="question"
                          cols="46"
                          rows="10"
                          ref="question"
                          v-model="qna.question"
                        ></textarea>
                      </td>
                      <td v-else-if="this.addAnswer === 1">
                        {{ this.qna.question }}
                      </td>
                    </tr>
                    <tr v-if="this.addAnswer === 1">
                      <td><b>답변</b></td>
                      <td>
                        <textarea
                          id="answer"
                          cols="46"
                          rows="10"
                          ref="question"
                          v-model="qna.answer"
                        ></textarea>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </p>
            </div>
          </div>

          <div class="nav" style="padding-top: 1em;">
            <div class="nav-item" v-if="this.addAnswer === 0">
              <button class="btn btn-primary" @click="updateHandler">수정</button>
            </div>
            <div class="nav-item" v-else-if="this.addAnswer === 1">
              <button class="btn btn-primary" @click="updateHandler">답변등록</button>
            </div>
            &nbsp;
            <div class="nav-item">
              <router-link :to="{ name: 'QnAListView' }">
                <button class="btn btn-primary">목록</button>
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import http from '@/api/http';
export default {
  data() {
    return {
      no: '',
      qna: Object,
    };
  },
  created() {
    //DOM객체와 연결되었고 Vue의 속성들도 초기화 된 상태
    this.no = this.$route.query.no;
    this.addAnswer = this.$route.query.addAnswer;
    console.log('Modify.html..................no]', this.no);
    http
      .get(`qna/${this.no}`)
      .then(({ data }) => {
        console.log('data......', data);
        this.qna = data;
      })
      .catch(({ data }) => {
        alert(data);
      });
  },
  methods: {
    updateHandler() {
      //데이타 검증
      let err = false;
      let msg = '';
      console.log('updateHandler.................', this.qna);
      !this.qna.no && ((msg = '글 번호를 입력해주세요'), (err = true), this.$refs.no.focus());
      !err & !this.qna.title &&
        ((msg = '제목을 입력해주세요'), (err = true), this.$refs.title.focus());
      !err & !this.qna.question &&
        ((msg = '문의사항을 입력해주세요'), (err = true), this.$refs.question.focus());
      !err & !this.qna.answer & !this.addAnswer == 1 &&
        ((msg = '답변내용을 입력해주세요'), (err = true), this.$refs.answer.focus());
      if (err) {
        alert(msg);
      } else {
        //수정하기
        console.log(this.qna);
        this.qna.completed = this.addAnswer; //처리상태
        http
          .put('qna', this.qna)
          .then(({ data }) => {
            if (data === 'success') {
              if (this.addAnswer === 0) {
                alert('수정 완료');
              } else if (this.addAnswer === 1) {
                alert('답변 완료');
              }
              this.$router.push({
                name: 'QnADetailView',
                query: { no: this.qna.no },
              });
            }
          })
          .catch(({ data }) => {
            alert(data);
          });
      }
    },
  },
};
</script>

<style></style>
