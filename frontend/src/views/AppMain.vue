<template>
  <body class="d-flex flex-column h-100" style="background-color:#fff7f7">
    <main class="flex-shrink-0">
      <!-- Header-->
      <header class="py-5" style="background-color:#7D7D7D">
        <div class="container px-5">
          <div class="row gx-5 align-items-center justify-content-center">
            <div class="col-lg-8 col-xl-7 col-xxl-6">
              <div class="my-5 text-center text-xl-start">
                <h2 class="display-6 fw-bolder text-white mb-2">100세 인생 내 집 마련하기🌠</h2>
                <p class="lead fw-normal text-white-50 mb-4">
                  이번 생에 서울에서 내 집 마련 가능할까요?
                </p>
                <div
                  class="d-grid gap-3 d-sm-flex justify-content-sm-center justify-content-xl-start"
                >
                  <!-- <a class="btn btn-primary btn-lg px-4 me-sm-3" href="#features">공지사항 보기</a> -->
                </div>
              </div>
            </div>
            <!-- <iframe width="1280" height="720" src="https://www.youtube.com/embed/p_XdZdg9oGc" title="[최초공개] LE SSERAFIM(르세라핌) - ANTIFRAGILE (4K) | LE SSERAFIM COMEBACKSHOW | Mnet 221017 방송" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe> -->
            <div class="col-xl-5 col-xxl-6 d-none d-xl-block text-center px-2">
              <!-- <iframe width="600" height="300" src="https://www.youtube.com/embed/p_XdZdg9oGc?autoplay=1&mute=1" title="[최초공개] LE SSERAFIM(르세라핌) - ANTIFRAGILE (4K) | LE SSERAFIM COMEBACKSHOW | Mnet 221017 방송" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen style="webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px"></iframe> -->
              <iframe width="600" height="300" src="https://www.youtube.com/embed/x9tYdmBQiSQ?autoplay=1&mute=1" title="집 없는 나도 알고 있어야 할 부동산 기초" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen style="webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px"></iframe>
              <!-- <img class="img-fluid rounded-3 my-5" src="../../src/assets/img/img3.jpg" alt="..." /> -->
            </div>
          </div>
        </div>
      </header>
      <!-- Features section-->
      
      <section class="py-2" id="features" style="background-color:#FFEFE0">
        <div class="container px-4 my-5">
          <div class="row gx-5">
            <div class="col-lg-7 mb-5 mb-lg-0">
              <div class="card" style="width: 100%; background-color: #f8f9fa; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
                <div class="card-body"
                  style="">
                  <h3 class="card-title fw-bolder">📢 공지사항</h3>
                  <p class="card-text">
                    <section class="" id="contents">
                    <div v-if="notices.length > 0" class="">
                      <table class="table table-hover" style="text-align: center; vertical-align: middle">
                        <thead>
                          <tr>
                            <!-- <th style="width: 10%">글 번호</th> -->
                            <th style="width: 35%">제목</th>
                            <th style="width: 20%">작성자</th>
                            <th style="width: 25%">작성일자</th>
                            <th style="width: 20%">조회수</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr v-for="notice in notices" :key="notice.no">
                            <!-- <td>{{ notice.no }}</td> -->
                            <td>
                              <b style="color: #000">
                                <router-link :to="{ name: 'NoticeDetailView', query: { no: notice.no } }">{{
                                  notice.title
                                }}</router-link>
                              </b>
                            </td>
                            <td v-text="notice.name"></td>
                            <td v-text="notice.regtime.split(' ')[0]"></td>
                            <td v-text="notice.hit"></td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <div v-else>등록된 공지사항이 없습니다.</div>
                  </section>
                  </p>
                </div>
              </div>

              <!-- <h3 class="fw-bolder mb-0">📢 공지사항</h3>
              <br/>
              <section class="" id="contents">
                <div v-if="notices.length > 0" class="">
                  <table class="table table-hover" style="text-align: center; vertical-align: middle">
                    <thead>
                      <tr>
                        <th style="width: 10%">글 번호</th>
                        <th style="width: 35%">제목</th>
                        <th style="width: 20%">작성자</th>
                        <th style="width: 25%">작성일자</th>
                        <th style="width: 20%">조회수</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="notice in notices" :key="notice.no">
                        <td>{{ notice.no }}</td>
                        <td>
                          <b style="color: #5587ed">
                            <router-link :to="{ name: 'NoticeDetailView', query: { no: notice.no } }">{{
                              notice.title
                            }}</router-link>
                          </b>
                        </td>
                        <td v-text="notice.name"></td>
                        <td v-text="notice.regtime.split(' ')[0]"></td>
                        <td v-text="notice.hit"></td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div v-else>등록된 공지사항이 없습니다.</div>
              </section> -->
            </div>
            <div class="col-lg-5">
              <div class="card" style="width: 100%; background-color: #f8f9fa; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
                <div class="card-body">
                  <h3 class="card-title fw-bolder">📰 부동산 뉴스</h3>
                  <!-- <h7 class="card-subtitle mb-2 text-muted">내집 마련 가즈아~</h7> -->
                  <p class="card-text">
                    <section class="" id="contents">
                      <div v-if="newsList.length > 0" class="">
                        <table class="table table-hover" style="text-align: center; vertical-align: middle">
                          <thead>
                            <tr>
                              <br/>
                              <!-- <th style="width: 10%">글 번호</th> -->
                              <!-- <th style="width: 35%">제목</th>
                              <th style="width: 20%">작성자</th>
                              <th style="width: 25%">작성일자</th>
                              <th style="width: 20%">조회수</th> -->
                            </tr>
                          </thead>
                          <tbody>
                            <tr v-for="(news, index) in newsList" :key="index">
                              <!-- <td v-if="index = newsList.length-1" v-html="news.title" @click="moveNewsLink(news.link)" style="cursor: pointer; height: 50% !important; border-style: none;"></td>
                              <td v-else v-html="news.title" @click="moveNewsLink(news.link)" style="cursor: pointer; height: 50% !important;"></td> -->
                              <td v-html="news.title" @click="moveNewsLink(news.link)" style="cursor: pointer;">
                                
                                <!-- <b style="color: #5587ed">
                                  <a href="#">link</a>
                                  <router-link :to="{ name: 'NoticeDetailView', query: { no: notice.no } }">{{
                                    notice.title
                                  }}</router-link>
                                </b> -->
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                      <div v-else>등록된 뉴스가 없습니다.</div>
                    </section>
                  </p>
                  <!-- <a href="#" class="card-link">Card link</a>
                  <a href="#" class="card-link">Another link</a> -->
                </div>
              </div>
              <!-- <div class="row gx-5 row-cols-1 row-cols-md-2">
                <div class="col mb-5 h-100">
                  <div class="feature bg-primary bg-gradient text-white rounded-3 mb-3">
                    <i class="bi bi-collection"></i>
                  </div>
                  <h2 class="h5">2022-09-07 리뉴얼 기념</h2>
                  <p class="mb-0">지금 가입하시면 혜택이 빵빵!</p>
                </div>
                <div class="col mb-5 h-100">
                  <div class="feature bg-primary bg-gradient text-white rounded-3 mb-3">
                    <i class="bi bi-building"></i>
                  </div>
                  <h2 class="h5">2022-09-06 기능 추가</h2>
                  <p class="mb-0">관심지역 기능 업데이트.</p>
                </div>
              </div> -->
            </div>
          </div>
        </div>
      </section>
      <!-- Testimonial section-->

      <!-- Blog preview section-->
    </main>
    <!-- Footer-->
    <!-- <footer class="bg-dark py-4 mt-auto">
      <div class="container px-5">
        <div class="row align-items-center justify-content-between flex-column flex-sm-row">
          <div class="col-auto">
            <div class="small m-0 text-white">Copyright &copy; SSAFY 2022</div>
          </div>
          <div class="col-auto">
            <a class="link-light small" href="#!">Privacy</a>
            <span class="text-white mx-1">&middot;</span>
            <a class="link-light small" href="#!">Terms</a>
            <span class="text-white mx-1">&middot;</span>
            <a class="link-light small" href="#!">Contact</a>
          </div>
        </div>
      </div>
    </footer> -->
    <!-- Bootstrap core JS-->
    <!-- <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script> -->
    <!-- Core theme JS-->
    <!-- <script src="js/scripts.js"></script> -->
  </body>
</template>

<script>
import http from '@/api/http';

export default {
  name: "AppMain",
  props: {
    msg: String,
  },
  data() {
    return {
      notices: [],
      setNoticeNum: 5,
      newsList: [],
      newsText: '부동산',
      newsNum: 5,
    }
  },
  created() {
    this.searchNotice(1);
    this.getNews();
  },
  methods:{
    searchNotice(pageNo) {
      console.log('searchNotice.......');
      const url = `notice?pageNo=${pageNo}`;
      http.get(url).then(({ data }) => {
        console.log('응답 데이타', data);
        this.notices = data.noticeList.slice(0, this.setNoticeNum);
        // this.currentPage = data.currentPage;
        // this.noOfPages = data.noOfPages;
        // this.setPageNums();
      });
    },
    getNews(){
      console.log('getNews.......');
      const url = `news/${this.newsText}/${this.newsNum}`;
      http.get(url).then(({data}) => {
        this.newsList = data.items;
        console.log('newsList : ',this.newsList);
        console.log('type : ', this.newsList);
        console.log("length : ", this.newsList.length);
      })
    },
    moveNewsLink(link){
      window.open(link, "_blank");  
    },
  }
};
</script>

<style scoped>
.underline-steelblue {
  display: inline-block;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(72, 190, 233, 0.3) 30%);
}
/* .th {
  height: 3em;
} */
</style>
