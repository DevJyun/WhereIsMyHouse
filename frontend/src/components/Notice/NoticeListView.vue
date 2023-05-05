<template>
  <div class="container">
    <div class="nav">
      <!-- <div class="col-1"></div> -->
      <div class="nav-item">
        <router-link :to="{ name: 'NoticeListView' }">
          <button class="btn btn-outline-primary">공지사항 목록</button>
        </router-link>
      </div>
      &nbsp;
      <div class="nav-item" v-if="this.userInfo && this.userInfo.isAdmin === 1">
        <router-link :to="{ name: 'NoticeRegistView' }">
          <button class="btn btn-outline-primary">공지사항 등록</button>
        </router-link>
      </div>
    </div>
    <br />

    <div class="row col-md-12 justify-content-center mb-2">
      <div class="col-md-4">
        <select
          class="form-control"
          v-model="key"
          style="vertical-align: middle; text-align-last: center"
        >
          <option value="all">--선택하세요--</option>
          <option v-for="(v, k) in keys" :key="k" :value="k">
            {{ v }}
          </option>
        </select>
      </div>
      <div class="col-md-6">
        <input type="text" class="form-control" v-model="word" />
      </div>
      <div class="col-md-2">
        <button class="btn btn-outline-primary col-12" @click="searchNotice(1)">검색</button>
      </div>
    </div>
    <section class="" id="contents">
      <div v-if="notices.length > 0" class="container my-5" style="padding-left: 0;">
        <div class="card" style="width: 100%; background-color: #f8f9fa; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
          <div class="card-body">
            <!-- <h3 class="card-title fw-bolder">🏠 부동산 뉴스</h3> -->
            <!-- <h7 class="card-subtitle mb-2 text-muted">내집 마련 가즈아~</h7> -->
            <p class="card-text">
              <table class="table table-hover" style="text-align: center; vertical-align: middle">
                <thead>
                  <tr>
                    <th style="width: 10%">글 번호</th>
                    <th style="width: 35%">공지사항</th>
                    <th style="width: 20%">작성자</th>
                    <th style="width: 25%">작성일자</th>
                    <th style="width: 10%">조회수</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="notice in notices" :key="notice.no">
                    <td>{{ notice.no }}</td>
                    <td>
                      <b style="color: #000">
                        <router-link :to="{ name: 'NoticeDetailView', query: { no: notice.no } }">{{
                          notice.title
                        }}</router-link>
                      </b>
                    </td>
                    <td v-text="notice.name"></td>
                    <td v-text="notice.regtime"></td>
                    <td v-text="notice.hit"></td>
                  </tr>
                </tbody>
              </table>
              <!--  pagination 처리 -->
              <div>
                <ul class="pagination justify-content-center">
                  <li v-if="currentPage != 1" class="page-item">
                    <a class="page-link" style="cursor: pointer" @click="searchNotice(1)">&lt;&lt;</a>
                  </li>
                  <li v-else class="page-item">
                    <a class="page-link" style="cursor: pointer">&lt;&lt;</a>
                  </li>

                  <li v-if="currentPage != 1" class="page-item">
                    <a class="page-link" style="cursor: pointer" @click="searchNotice(currentPage - 1)"
                      >&lt;</a
                    >
                  </li>
                  <li v-else class="page-item">
                    <a class="page-link" style="cursor: pointer">&lt;</a>
                  </li>

                  <template v-for="idx in pageNums">
                    <li v-if="currentPage == idx" :key="idx" class="page-item active">
                      <a class="page-link" style="cursor: pointer" @click="searchNotice(idx)">{{
                        idx
                      }}</a>
                    </li>
                    <li v-else class="page-item" :key="idx">
                      <a class="page-link" style="cursor: pointer" @click="searchNotice(idx)">{{
                        idx
                      }}</a>
                    </li>
                  </template>

                  <li v-if="currentPage < noOfPages" class="page-item">
                    <a class="page-link" style="cursor: pointer" @click="searchNotice(currentPage + 1)"
                      >&gt;</a
                    >
                  </li>
                  <li v-else class="page-item">
                    <a class="page-link" style="cursor: pointer">&gt;</a>
                  </li>

                  <li v-if="currentPage != noOfPages" class="page-item">
                    <a class="page-link" style="cursor: pointer" @click="searchNotice(noOfPages)"
                      >&gt;&gt;</a
                    >
                  </li>
                  <li v-else class="page-item">
                    <a class="page-link" style="cursor: pointer">&gt;&gt;</a>
                  </li>
                </ul>
              </div>
            <!-- </p> -->
          </div>
        </div>
      </div>
      <div v-else>등록된 공지사항이 없습니다.</div>
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
      notices: [],
      word: '',
      keys: { no: '글번호', title: '제목', content: '내용' },
      key: 'all',
      currentPage: '',
      noOfPages: '',
      pageNums: [],
    };
  },
  computed: {
    ...mapState(userStore, ['userInfo']),
  },
  created() {
    this.searchNotice(1);
  },
  methods: {
    searchNotice(pageNo) {
      console.log('key.......', this.key);
      const url = `notice?key=${this.key}&word=${this.word}&pageNo=${pageNo}`;
      http.get(url).then(({ data }) => {
        console.log('응답 데이타', data);
        this.notices = data.noticeList;
        this.currentPage = data.currentPage;
        this.noOfPages = data.noOfPages;
        this.setPageNums();
      });
    },
    setPageNums() {
      this.pageNums.splice(0);
      let begin = ((this.currentPage - 1 - ((this.currentPage - 1) % 5)) / 5) * 5 + 1;
      let end =
        ((this.currentPage - 1 - ((this.currentPage - 1) % 5)) / 5) * 5 + 5 < this.noOfPages
          ? ((this.currentPage - 1 - ((this.currentPage - 1) % 5)) / 5) * 5 + 5
          : this.noOfPages;
      for (var i = begin; i <= end; i++) {
        this.pageNums.push(i);
      }
    },
  },
};
</script>

<style scoped>
a {
  color: inherit;
  text-decoration: none;
}
</style>
