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
                      <td>
                        <input type="text" id="title" ref="title" v-model="notice.title" />
                      </td>
                    </tr>
                    <tr>
                      <td><b>작성자</b></td>
                      <td>
                        <!-- <input type="text" id="id" ref="id" v-model="notice.id" /> -->
                        {{ this.notice.name }}
                      </td>
                    </tr>
                    <tr>
                      <td><b>공지사항</b></td>
                      <td>
                        <textarea
                          id="content"
                          cols="46"
                          rows="10"
                          ref="content"
                          v-model="notice.content"
                        ></textarea>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </p>
            </div>
          </div>

          <div class="nav" style="padding-top: 1em;">
            <div class="nav-item">
              <button class="btn btn-primary" @click="updateHandler">수정</button>
            </div>
            &nbsp;
            <div class="nav-item">
              <router-link :to="{ name: 'NoticeListView' }">
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
      notice: Object,
    };
  },
  created() {
    //DOM객체와 연결되었고 Vue의 속성들도 초기화 된 상태
    this.no = this.$route.query.no;
    console.log('NoticeModifyView..................no]', this.no);
    http
      .get(`notice/${this.no}`)
      .then(({ data }) => {
        console.log('data......', data);
        this.notice = data;
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
      console.log('updateHandler.................', this.notice);
      !this.notice.no && ((msg = '글 번호를 입력해주세요'), (err = true), this.$refs.no.focus());
      !err & !this.notice.title &&
        ((msg = '제목을 입력해주세요'), (err = true), this.$refs.title.focus());
      !err & !this.notice.content &&
        ((msg = '공지사항을 입력해주세요'), (err = true), this.$refs.content.focus());
      if (err) {
        alert(msg);
      } else {
        //수정하기
        console.log(this.notice);
        http
          .put('notice', this.notice)
          .then(({ data }) => {
            if (data === 'success') {
              alert('수정 완료');
              this.$router.push({
                name: 'NoticeDetailView',
                query: { no: this.notice.no },
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
