<template>
  <div class="container">
    <div class="nav">
      <!-- <div class="col-1"></div> -->
      <div class="nav-item">
        <router-link :to="{ name: 'QnAListView' }">
          <button class="btn btn-outline-primary">문의사항 목록</button>
        </router-link>
      </div>
      &nbsp;
      <div class="nav-item" v-if="this.userInfo && this.userInfo.isAdmin === 0">
        <router-link :to="{ name: 'QnARegistView' }">
          <button class="btn btn-outline-primary">문의사항 등록</button>
        </router-link>
      </div>
    </div>
    <br />

    <div class="row col-md-12 justify-content-center mb-2">
      <!-- <div class="col-1"></div> -->

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
        <button class="btn btn-outline-primary col-12" @click="searchQnA(1)">검색</button>
      </div>

      <!-- <div class="col-1"></div> -->
      <!-- <div class="col-2"></div> -->
    </div>
    <section class="" id="contents">
      <div v-if="qnas.length > 0" class="container my-5" style="padding-left: 0;">
        <div class="card" style="width: 100%; background-color: #f8f9fa; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
            <div class="card-body">
              <!-- <h3 class="card-title fw-bolder">🏠 부동산 뉴스</h3> -->
              <!-- <h7 class="card-subtitle mb-2 text-muted">내집 마련 가즈아~</h7> -->
              <p class="card-text">
                <table class="table table-hover" style="text-align: center; vertical-align: middle">
                  <!-- <colgroup>
                    <col width="5%" />
                    <col width="45%" />
                    <col width="15%" />
                    <col width="25%" />
                    <col width="10%" />
                  </colgroup> -->
                  <thead>
                    <tr>
                      <th style="width: 10%">글 번호</th>
                      <th style="width: 35%">문의사항</th>
                      <th style="width: 15%">작성자</th>
                      <th style="width: 25%">작성일자</th>
                      <th style="width: 15%">처리상태</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="qna in qnas" :key="qna.no">
                      <td>{{ qna.no }}</td>
                      <td>
                        <b style="color: #000">
                          <router-link :to="{ name: 'QnADetailView', query: { no: qna.no } }">{{
                            qna.title
                          }}</router-link>
                        </b>
                      </td>
                      <td v-text="qna.name"></td>
                      <td v-text="qna.regtime"></td>
                      <td v-if="qna.completed == 1"><b style="color: #1d8b15">답변완료</b></td>
                      <td v-else><b style="color: #bc2424">답변예정</b></td>
                    </tr>
                  </tbody>
                </table>
                <div>
                  <ul class="pagination justify-content-center">
                    <li v-if="currentPage != 1" class="page-item">
                      <a class="page-link" style="cursor: pointer" @click="searchQnA(1)">&lt;&lt;</a>
                    </li>
                    <li v-else class="page-item">
                      <a class="page-link" style="cursor: pointer">&lt;&lt;</a>
                    </li>

                    <li v-if="currentPage != 1" class="page-item">
                      <a class="page-link" style="cursor: pointer" @click="searchQnA(currentPage - 1)"
                        >&lt;</a
                      >
                    </li>
                    <li v-else class="page-item">
                      <a class="page-link" style="cursor: pointer">&lt;</a>
                    </li>

                    <template v-for="idx in pageNums">
                      <li v-if="currentPage == idx" :key="idx" class="page-item active">
                        <a class="page-link" style="cursor: pointer" @click="searchQnA(idx)">{{ idx }}</a>
                      </li>
                      <li v-else class="page-item" :key="idx">
                        <a class="page-link" style="cursor: pointer" @click="searchQnA(idx)">{{ idx }}</a>
                      </li>
                    </template>

                    <li v-if="currentPage < noOfPages" class="page-item">
                      <a class="page-link" style="cursor: pointer" @click="searchQnA(currentPage + 1)"
                        >&gt;</a
                      >
                    </li>
                    <li v-else class="page-item">
                      <a class="page-link" style="cursor: pointer">&gt;</a>
                    </li>

                    <li v-if="currentPage != noOfPages" class="page-item">
                      <a class="page-link" style="cursor: pointer" @click="searchQnA(noOfPages)"
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
        
        <!--  pagination 처리 -->
        
      </div>
      <div v-else>등록된 문의사항이 없습니다.</div>
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
      qnas: [],
      word: '',
      keys: { no: '글번호', title: '제목', id: '작성자' },
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
    this.searchQnA(1);
  },
  methods: {
    searchQnA(pageNo) {
      console.log('key.......', this.key);
      const url = `qna?key=${this.key}&word=${this.word}&pageNo=${pageNo}`;
      http.get(url).then(({ data }) => {
        console.log('응답 데이타', data);
        this.qnas = data.qnaList;
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
