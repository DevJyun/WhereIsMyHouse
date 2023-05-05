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
                      <th>제목</th>
                      <td>
                        <input type="text" id="title" ref="title" v-model="notice.title" />
                      </td>
                    </tr>
                    <tr>
                      <th>작성자</th>
                      <td>
                        <!-- <input type="text" id="id" ref="id" v-model="notice.id" disabled /> -->
                        {{ this.userInfo.name }}
                      </td>
                    </tr>
                    <tr>
                      <th colspan="2">공지사항</th>
                    </tr>
                    <tr>
                      <td colspan="2">
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
              <button class="btn btn-primary" @click="createHandler">등록</button>
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
import { mapState } from 'vuex';

const userStore = 'userStore';

export default {
  data() {
    return {
      notice: {
        title: '',
        content: '',
      },
    };
  },
  computed: {
    ...mapState(userStore, ['userInfo']),
  },
  methods: {
    moveHandler() {
      this.$router.push({ name: 'NoticeListView' });
    },
    createHandler() {
      //데이타 검증
      let err = false;
      let msg = '';

      if (!err && this.notice.title == '') {
        err = true;
        msg = '제목을 입력해 주세요';
        this.$refs.title.focus();
      }

      if (!err && this.notice.content == '') {
        err = true;
        msg = '문의내용을 입력해 주세요';
        this.$refs.content.focus();
      }

      if (err) {
        alert(msg);
      } else {
        //등록 처리
        //localStorage에 이미 저장된 책 목록이 있는지 검사

        let newNotice = {
          id: this.userInfo.userId,
          name: this.userInfo.name,
          title: this.notice.title,
          content: this.notice.content,
        };

        http.post('notice', newNotice).then(({ data }) => {
          if (data === 'success') {
            alert('등록 완료');
            this.moveHandler();
          }
        });
      }
    },
  },
};
</script>

<style></style>
