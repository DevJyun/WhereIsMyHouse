<template>
  <b-container class="bv-example-row mt-3 text-center">
    <!-- 아파트 세부 정보 모달 -->
    <FavoriteDetailModal
      @close="closeModal"
      v-if="modal"
      :no="this.modalTargetNo"
    ></FavoriteDetailModal>

    <div class="px-5 py-5 input-group" style="width: 1000px; margin: 0 auto">
      <div class="input-group" style="display: flex; justify-content: center">
        <h2 class="fw-bolder" style="display: inline">🏢 아파트명으로 검색</h2>
      </div>
      <div id="aptFindForm" class="px-4 py-4 input-group">
        <input
          type="search"
          class="form-control rounded"
          id="aptName"
          name="aptName"
          placeholder="아파트를 검색하세요"
          aria-label="Search"
          aria-describedby="search-addon"
          v-model="aptName"
        />
        <button id="apt-btn" class="btn btn-outline-primary" @click="searchApt(0)">검색하기</button>
      </div>
    </div>

    <section id="contents" v-if="apts" style="display: flex; justify-content: center">
      <div class="container my-3">
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
                    <tr v-for="apt in apts" :key="apt.no">
                      <td
                        v-text="apt.aptName"
                        @click="setModalNo(apt.no)"
                        style="cursor: pointer; font-weight: bold"
                      >
                        <!-- <router-link :to="{ name: 'ShowHouseDetail', query: { no: apt.no } }">{{
                          apt.aptName
                        }}</router-link> -->
                      </td>
                      <td v-text="apt.dongName"></td>
                      <td v-text="apt.dealAmount"></td>
                      <td v-text="apt.area"></td>
                      <td>{{ apt.dealYear }}년 {{ apt.dealMonth }}월</td>
                    </tr>
                  </tbody>
                </table>
              </p>
              <!--  pagination 처리 -->
              <ul class="pagination justify-content-center">
                <li v-if="currentPage != 1" class="page-item">
                  <a class="page-link" style="cursor: pointer" @click="searchApt(1)">&lt;&lt;</a>
                </li>
                <li v-else class="page-item">
                  <a class="page-link" style="cursor: pointer">&lt;&lt;</a>
                </li>

                <li v-if="currentPage != 1" class="page-item">
                  <a class="page-link" style="cursor: pointer" @click="searchApt(currentPage - 1)"
                    >&lt;</a
                  >
                </li>
                <li v-else class="page-item">
                  <a class="page-link" style="cursor: pointer">&lt;</a>
                </li>

                <template v-for="idx in pageNums">
                  <li v-if="currentPage == idx" :key="idx" class="page-item active">
                    <a class="page-link" style="cursor: pointer" @click="searchApt(idx)">{{ idx }}</a>
                  </li>
                  <li v-else class="page-item" :key="idx">
                    <a class="page-link" style="cursor: pointer" @click="searchApt(idx)">{{ idx }}</a>
                  </li>
                </template>

                <li v-if="currentPage < noOfPages" class="page-item">
                  <a class="page-link" style="cursor: pointer" @click="searchApt(currentPage + 1)"
                    >&gt;</a
                  >
                </li>
                <li v-else class="page-item">
                  <a class="page-link" style="cursor: pointer">&gt;</a>
                </li>

                <li v-if="currentPage != noOfPages" class="page-item">
                  <a class="page-link" style="cursor: pointer" @click="searchApt(noOfPages)"
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
              <h3 class="card-title fw-bolder">🏠 월별 거래량</h3>
              <p class="card-text">
                <apartment-chart :dealCount="dealCount"></apartment-chart>
              </p>
            </div>
          </div>
        </div>
      </div>
      </div>
    </section>
    <section v-else>
      <div class="container">
          <div style="display: flex; justify-content: center">
            <span
              ><h4 class="fw-bolder" style="display: inline; color: LightSlateGray">
                검색 결과가 없습니다.
              </h4></span
            >
          </div>
        </div>
    </section>
  </b-container>
</template>
<script>
// import HouseSearchBar from "@/components/house/HouseSearchBar.vue";
// import HouseList from "@/components/house/HouseList.vue";
// import HouseDetail from "@/components/house/HouseDetail.vue";
import http from '@/api/http';
import FavoriteDetailModal from '@/components/house/FavoriteDetailModal';
import ApartmentChart from '@/components/house/ApartmentChart';

import { mapState, mapActions } from 'vuex';

const houseStore = 'houseStore';

export default {
  name: 'FindAddress',
  components: {
    // HouseSearchBar,
    // HouseList,
    // HouseDetail,
    FavoriteDetailModal,
    ApartmentChart,
  },
  mounted() {},
  data: function () {
    return {
      aptName: '',
      apts: null,
      currentPage: '',
      noOfPages: '',
      pageNums: [],

      // modal
      modal: false,
      modalTargetNo: 0,
    };
  },
  computed: {
    ...mapState(houseStore, ['dealCount']),
  },
  methods: {
    ...mapActions(houseStore, ['getDealCount']),
    searchApt(pageNo) {
      console.log(pageNo);
      let graphUpdate = false;
      if (pageNo == 0) {
        graphUpdate = true;
        pageNo = 1;
      }

      const aptFindurl = `aptFind?aptName=${this.aptName}&pageNo=${pageNo}`;
      http.get(aptFindurl).then(({ data }) => {
        console.log('success');
        console.log('응답 데이타', data);
        this.apts = data.aptList;
        this.currentPage = data.currentPage;
        this.noOfPages = data.noOfPages;
        console.log(this.currentPage);
        console.log(this.noOfPages);
        this.setPageNums();
      });

      if (graphUpdate == true) {
        this.getDealCount(this.aptName);
      }
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
