<template>
  <container class="bv-example-row mt-3 text-center">
    <!-- 아파트 세부 정보 모달 -->
    <FavoriteDetailModal
      @close="closeModal"
      v-if="modal"
      :no="this.modalTargetNo"
    ></FavoriteDetailModal>

    <div class="px-5 py-5 input-group" style="width: 1000px; margin: 0 auto">
      <div class="input-group" style="display: flex; justify-content: center">
        <h2 class="fw-bolder" style="display: inline">🗺️ 시/구군/동 검색</h2>
      </div>
    </div>
    <div class="container">
      <div style="height: 2em"></div>
      <div id="form-findByDong">
        <div class="row col-md-12 justify-content-center mb-2">
          <div class="form-group col-md-3">
            <select
              class="form-select"
              id="sido"
              style="vertical-align: middle; text-align-last: center"
              @change="sidoChange($event)"
            >
              <option value="">시도선택</option>
            </select>
          </div>
          <div class="form-group col-md-3">
            <select
              class="form-select"
              id="gugun"
              style="vertical-align: middle; text-align-last: center"
              @change="gugunChange($event)"
            >
              <option value="">구군선택</option>
            </select>
          </div>
          <div class="form-group col-md-3">
            <select
              class="form-select"
              name="dongCode"
              id="dong"
              style="vertical-align: middle; text-align-last: center"
              @change="dongChange($event)"
            >
              <option value="">동선택</option>
            </select>
          </div>
          <button
            id="list-btn"
            class="btn btn-outline-primary form-group col-md-3"
            @click="searchDong(1)"
          >
            검색하기
          </button>
        </div>
      </div>
      <!-- <hr /> -->
      <!-- style="display: none" -->
      <hr/>
      <section class="py-5" id="contents" v-if="searched">
        <template v-if="houses.length > 0">
          <div style="display: flex; justify-content: center">
            <span>
              <!-- <h3 class="fw-bolder" style="display: inline; color: LightSlateGray"> -->
              <h3 class="fw-bolder" style="display: inline;">
                {{ addressName }}
              </h3>
            </span>
            &nbsp;&nbsp;
            <div v-if="isFav === 1">
              <a style="cursor: pointer" @click="removeFavorite"
                ><h3>❤️</h3></a>
            </div>
            <div v-else>
              <a style="cursor: pointer" @click="addFavorite"
                ><h3>🤍</h3></a>
            </div>
          </div>
        </template>

        <template v-if="houses.length > 0">
        <div class="container my-5">
          <div class="row gx-5">
          
          <div class="col-lg-7 mb-5 mb-lg-0">
            <div class="card" style="width: 100%; background-color: #f8f9fa; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
              <div class="card-body">
                <h3 class="card-title fw-bolder">📢 거래정보</h3>
                <br/>
                <p class="card-text">
                  <table class="table table-hover" style="text-align: center; vertical-align: middle">
                    <thead>
                      <tr>
                        <th>아파트이름</th>
                        <th>동 이름</th>
                        <th>거래 금액</th>
                        <th>면적</th>
                        <th>거래 일자</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="house in houses" :key="house.no">
                        <td
                          v-text="house.aptName"
                          @click="setModalNo(house.no)"
                          style="cursor: pointer; font-weight: bold"
                        >
                          <!-- <router-link :to="{ name: 'ShowHouseDetail', query: { no: house.no } }">{{
                            house.aptName
                          }}</router-link> -->
                        </td>
                        <td v-text="house.dongName"></td>
                        <td v-text="house.dealAmount"></td>
                        <td v-text="house.area"></td>
                        <td>{{ house.dealYear }}년 {{ house.dealMonth }}월</td>
                      </tr>
                    </tbody>
                  </table>
                </p>
                <ul class="pagination justify-content-center">
            <li v-if="currentPage != 1" class="page-item">
              <a class="page-link" style="cursor: pointer" @click="searchDong(1)">&lt;&lt;</a>
            </li>
            <li v-else class="page-item">
              <a class="page-link" style="cursor: pointer">&lt;&lt;</a>
            </li>

            <li v-if="currentPage != 1" class="page-item">
              <a class="page-link" style="cursor: pointer" @click="searchDong(currentPage - 1)"
                >&lt;</a
              >
            </li>
            <li v-else class="page-item">
              <a class="page-link" style="cursor: pointer">&lt;</a>
            </li>

            <template v-for="idx in pageNums">
              <li v-if="currentPage == idx" :key="idx" class="page-item active">
                <a class="page-link" style="cursor: pointer" @click="searchDong(idx)">{{ idx }}</a>
              </li>
              <li v-else class="page-item" :key="idx">
                <a class="page-link" style="cursor: pointer" @click="searchDong(idx)">{{ idx }}</a>
              </li>
            </template>

            <li v-if="currentPage < noOfPages" class="page-item">
              <a class="page-link" style="cursor: pointer" @click="searchDong(currentPage + 1)"
                >&gt;</a
              >
            </li>
            <li v-else class="page-item">
              <a class="page-link" style="cursor: pointer">&gt;</a>
            </li>

            <li v-if="currentPage != noOfPages" class="page-item">
              <a class="page-link" style="cursor: pointer" @click="searchDong(noOfPages)"
                >&gt;&gt;</a
              >
            </li>
            <li v-else class="page-item">
              <a class="page-link" style="cursor: pointer">&gt;&gt;</a>
            </li>
          </ul>
              </div>
            </div>
          </div>
          <div class="col-lg-5">
            <div class="card" style="width: 100%; background-color: #f8f9fa; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
              <div class="card-body">
                <h3 class="card-title fw-bolder">📰 부동산 뉴스</h3>
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
              </div>
            </div>
          </div>
          </div>
        </div>
        <!--  pagination 처리 -->
        <div class="col-lg-7 mb-5 mb-lg-0">
          <!-- <ul class="pagination justify-content-center">
            <li v-if="currentPage != 1" class="page-item">
              <a class="page-link" style="cursor: pointer" @click="searchDong(1)">&lt;&lt;</a>
            </li>
            <li v-else class="page-item">
              <a class="page-link" style="cursor: pointer">&lt;&lt;</a>
            </li>

            <li v-if="currentPage != 1" class="page-item">
              <a class="page-link" style="cursor: pointer" @click="searchDong(currentPage - 1)"
                >&lt;</a
              >
            </li>
            <li v-else class="page-item">
              <a class="page-link" style="cursor: pointer">&lt;</a>
            </li>

            <template v-for="idx in pageNums">
              <li v-if="currentPage == idx" :key="idx" class="page-item active">
                <a class="page-link" style="cursor: pointer" @click="searchDong(idx)">{{ idx }}</a>
              </li>
              <li v-else class="page-item" :key="idx">
                <a class="page-link" style="cursor: pointer" @click="searchDong(idx)">{{ idx }}</a>
              </li>
            </template>

            <li v-if="currentPage < noOfPages" class="page-item">
              <a class="page-link" style="cursor: pointer" @click="searchDong(currentPage + 1)"
                >&gt;</a
              >
            </li>
            <li v-else class="page-item">
              <a class="page-link" style="cursor: pointer">&gt;</a>
            </li>

            <li v-if="currentPage != noOfPages" class="page-item">
              <a class="page-link" style="cursor: pointer" @click="searchDong(noOfPages)"
                >&gt;&gt;</a
              >
            </li>
            <li v-else class="page-item">
              <a class="page-link" style="cursor: pointer">&gt;&gt;</a>
            </li>
          </ul> -->
        </div>
      </template>
      <template v-else>
        <div class="container my-5">
          <div style="display: flex; justify-content: center">
            <span
              ><h4 class="fw-bolder" style="display: inline; color: LightSlateGray">
                검색 결과가 없습니다.
              </h4></span
            >
          </div>
        </div>
      </template>
      </section>
    </div>
  </container>
</template>
<script>
// import HouseSearchBar from "@/components/house/HouseSearchBar.vue";
// import HouseList from "@/components/house/HouseList.vue";
// import HouseDetail from "@/components/house/HouseDetail.vue";
import http from '@/api/http';
import { mapState } from 'vuex';
import FavoriteDetailModal from '@/components/house/FavoriteDetailModal';

const userStore = 'userStore';

export default {
  name: 'FindAddress',
  components: {
    // HouseSearchBar,
    // HouseList,
    // HouseDetail,
    FavoriteDetailModal,
  },
  computed: {
    ...mapState(userStore, ['userInfo']),
  },
  mounted() {
    this.sendRequest('sido', '*00000000');
  },
  data: function () {
    return {
      dongCode: null,
      addressName: null,
      isFav: null,
      houses: [],
      currentPage: '',
      noOfPages: '',
      pageNums: [],

      // modal
      modal: false,
      modalTargetNo: 0,

      searched: false,

      newsList: [],
      newsText: '',
      newsNum: 5,
    };
  },
  methods: {
    // 프록시서버로 지역별 선택지 받아올 수 있음
    sendRequest: function (selid, regcode) {
      const url = 'https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes';
      let params = 'regcode_pattern=' + regcode + '&is_ignore_zero=true';
      fetch(url + '?' + params)
        .then((response) => response.json())
        .then((data) => this.addOption(selid, data));
    },
    addOption: function (selid, data) {
      let opt = ``;
      this.initOption(selid);
      switch (selid) {
        case 'sido': {
          opt += `<option value="">시도선택</option>`;
          data.regcodes.forEach(function (regcode) {
            opt += `<option value="` + regcode.code + `">` + regcode.name + `</option>`;
          });
          break;
        }
        case 'gugun': {
          opt += `<option value="">구군선택</option>`;
          for (let i = 0; i < data.regcodes.length; i++) {
            if (i != data.regcodes.length - 1) {
              if (
                data.regcodes[i].name.split(' ')[1] == data.regcodes[i + 1].name.split(' ')[1] &&
                data.regcodes[i].name.split(' ').length !=
                  data.regcodes[i + 1].name.split(' ').length
              ) {
                data.regcodes.splice(i, 1);
                i--;
              }
            }
          }
          let name = '';
          data.regcodes.forEach(function (regcode) {
            if (regcode.name.split(' ').length == 2) name = regcode.name.split(' ')[1];
            else name = regcode.name.split(' ')[1] + ' ' + regcode.name.split(' ')[2];
            opt += `<option value="` + regcode.code + `">` + name + `</option>`;
          });
          break;
        }
        case 'dong': {
          opt += `<option value="">동선택</option>`;
          let idx = 2;
          data.regcodes.forEach(function (regcode) {
            if (regcode.name.split(' ').length != 3) idx = 3;
            opt +=
              `<option value="` + regcode.code + `">` + regcode.name.split(' ')[idx] + `</option>`;
          });
        }
      }
      document.querySelector(`#` + selid).innerHTML = opt;
    },
    initOption: function (selid) {
      let options = document.querySelector(`#` + selid);
      options.length = 0;
    },
    initTable: function () {
      let tbody = document.querySelector('#aptlist');
      let len = tbody.rows.length;
      for (let i = len - 1; i >= 0; i--) {
        tbody.deleteRow(i);
      }
    },
    sidoChange: function (event) {
      if (event.target.value) {
        let regcode = event.target.value.substr(0, 2) + '*00000';
        this.sendRequest('gugun', regcode);
      } else {
        this.initOption('gugun');
        this.initOption('dong');
      }
    },
    gugunChange: function (event) {
      if (event.target.value) {
        let regcode = event.target.value.substr(0, 5) + '*';
        this.sendRequest('dong', regcode);
      } else {
        this.initOption('dong');
      }
    },
    dongChange: function (event) {
      if (event.target.value) {
        this.dongCode = event.target.value;
      }
    },
    searchDong(pageNo) {
      const url = `dongFind?dongCode=${this.dongCode}&id=${this.userInfo.userId}&pageNo=${pageNo}`;
      http.get(url).then(({ data }) => {
        console.log('success');
        console.log('응답 데이타', data);
        this.houses = data.houseList;
        this.currentPage = data.currentPage;
        this.noOfPages = data.noOfPages;
        this.isFav = data.isFav;
        this.addressName = data.addressName;
        this.setPageNums();
        this.searched = true;
        this.getNews();
      });
    },
    getNews(){
      console.log('getNews.......');
      const url = `news/${this.addressName}/${this.newsNum}`;
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
    removeFavorite() {
      const url = `removeFavorite?dongCode=${this.dongCode}&id=${this.userInfo.userId}`;
      http.get(url).then(({ data }) => {
        console.log('success');
        console.log('응답 데이타', data);
        this.searchDong(this.currentPage);
      });
    },
    addFavorite() {
      const url = `addFavorite?dongCode=${this.dongCode}&id=${this.userInfo.userId}`;
      http.get(url).then(({ data }) => {
        console.log('success');
        console.log('응답 데이타', data);
        this.searchDong(this.currentPage);
      });
    },

    // 모달 관련 함수
    setModalNo(targetNo) {
      this.modalTargetNo = targetNo;
      this.openModal();
    },
    openModal() {
      this.modal = true;
      console.log('modal is clicked');
    },
    closeModal() {
      this.modal = false;
    },
  },
};
</script>
<style scoped>
.underline-orange {
  display: inline-block;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(231, 149, 27, 0.3) 30%);
}
</style>
