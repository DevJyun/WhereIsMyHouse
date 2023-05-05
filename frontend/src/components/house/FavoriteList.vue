<template>
  <main class="flex-shrink-0" style="padding-bottom: 3em;">
    <!-- <button @click="openModal">모달창 열기</button> -->
    <FavoriteDetailModal @close="closeModal" v-if="modal" :no="this.modalTargetNo"></FavoriteDetailModal>

    <div class="px-5 py-5 input-group" style="width: 1000px; margin: 0 auto">
      <div class="input-group" style="display: flex; justify-content: center">
        <h2 class="fw-bolder" style="display: inline">😆 관심지역 조회</h2>
      </div>
    </div>

    <div class="container">
      <section class="py-3" id="contents">
        <!-- <form id="houseDetail" action="${root}/house/showHouseDetail">
				<input type="hidden" id="no" name="no" value="" />
			</form> -->
        <div class="row">
          <div class="container col-4" style="float: left;">
            <div class="card" style="width: 100%; background-color: #f8f9fa; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
              <div class="card-body">
                <!-- <h3 class="card-title fw-bolder">🏠 부동산 뉴스</h3> -->
                <!-- <h7 class="card-subtitle mb-2 text-muted">내집 마련 가즈아~</h7> -->
                <p class="card-text">
                  <table class="table table-hover" style="text-align: center; vertical-align: middle">
                    <thead>
                      <tr>
                        <th><h5 class="fw-bolder" style="display: inline">❤️ 관심지역 목록</h5></th>
                      </tr>
                    </thead>
                    <tbody>
                      <template v-for="favorite in favorites">
                        <tr
                          v-if="favorite.dongCode == dongCode"
                          :key="favorite.dongCode"
                          style="
                            --bs-table-accent-bg: var(--bs-table-hover-bg);
                            color: var(--bs-table-hover-color);
                          "
                        >
                          <td style="cursor: pointer" @click="updateMap(favorite.id, favorite.dongCode)">
                            <b
                              >{{ favorite.sidoName }}&nbsp;{{ favorite.gugunName }}&nbsp;{{
                                favorite.dongName
                              }}</b
                            >
                          </td>
                        </tr>
                        <tr v-else :key="favorite.dongCode">
                          <td style="cursor: pointer" @click="updateMap(id, favorite.dongCode)">
                            {{ favorite.sidoName }}&nbsp;{{ favorite.gugunName }}&nbsp;{{
                              favorite.dongName
                            }}
                            <!-- </router-link> -->
                          </td>
                        </tr>
                      </template>
                    </tbody>
                  </table>
                  <div>
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
                          <a class="page-link" style="cursor: pointer" @click="searchDong(idx)">{{
                            idx
                          }}</a>
                        </li>
                        <li v-else class="page-item" :key="idx">
                          <a class="page-link" style="cursor: pointer" @click="searchDong(idx)">{{
                            idx
                          }}</a>
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
                <!-- </p> -->
              </div>
            </div>
            
          </div>

          <div class="col-8">
            <div class="map_wrap">
              <div
                id="map"
                style="width: 100%; height: 100%; position: relative; overflow: hidden; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;"
              ></div>

              <ul id="category">
                <li id="BK9" data-order="0" @click="onClickCategory('BK9')">
                  <span class="category_bg bank"></span> 은행
                </li>
                <li id="MT1" data-order="1" @click="onClickCategory('MT1')">
                  <span class="category_bg mart"></span> 마트
                </li>
                <li id="PM9" data-order="2" @click="onClickCategory('PM9')">
                  <span class="category_bg pharmacy"></span> 약국
                </li>
                <li id="OL7" data-order="3" @click="onClickCategory('OL7')">
                  <span class="category_bg oil"></span> 주유소
                </li>
                <li id="CE7" data-order="4" @click="onClickCategory('CE7')">
                  <span class="category_bg cafe"></span> 카페
                </li>
                <li id="CS2" data-order="5" @click="onClickCategory('CS2')">
                  <span class="category_bg store"></span> 편의점
                </li>
              </ul>
            </div>
          </div>
        </div>
      </section>
    </div>
  </main>
</template>

<script>
import http from '@/api/http';
import { mapState } from 'vuex';
import FavoriteDetailModal from "@/components/house/FavoriteDetailModal";

const userStore = 'userStore';

export default {
  components: {
    FavoriteDetailModal
  },
  data() {
    return {
      id: 'asd',
      favorites: Object,
      noOfPages: 0,
      currentPage: 0,
      dongCode: '',
      mapResult: [],
      pageNums: [],

      // kakao map variables
      houseMarkers: [],
      housePositions: [],
      dealNo: [],
      dealAmount: [],
      len: 0,
      infowindowList: [],

      // category
      ps: Object,
      currCategory: '',
      placeOverlay: Object,
      markers: [],
      contentNode: Object,

      // modal
      modal: false,
      modalTargetNo: 0,
    };
  },
  computed: {
    ...mapState(userStore, ['userInfo']),
  },
  mounted() {
    // if (window.kakao && window.kakao.maps) {
    //   this.setMap();
    // } else {
    const script = document.createElement('script');
    /* global kakao */
    script.onload = () => kakao.maps.load(this.setMap);
    script.src =
      '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=8705edb85db1a3d8342bb33ee262d130&libraries=services';
    document.head.appendChild(script);
    // this.setMap();
    // }

    this.setFavoriteList();
  },
  created() {},
  methods: {
    setModalNo(targetNo){
      this.modalTargetNo = targetNo;
      this.openModal();
    },
    openModal(){
      this.modal = true;
      console.log("modal is clicked");
    },
    closeModal(){
      this.modal = false;
    },

    setFavoriteList: function () {
      http.get(`favorite/${this.userInfo.userId}/1`).then(({ data }) => {
        console.log('favorite : ', data.favorites);
        this.favorites = data.favorites;
        this.noOfPages = data.noOfPages;
        this.currentPage = data.currentPage;
        this.mapResult = data.mapResult;
        this.setPageNums();
        
      });
    },
    searchDong: function (pageNo) {
      const url = `favorite/${this.userInfo.userId}/${pageNo}`;
      http.get(url).then(({ data }) => {
        this.favorites = data.favorites;
        this.noOfPages = data.noOfPages;
        this.currentPage = data.currentPage;
        this.setPageNums();
      });
    },
    setPageNums: function () {
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

    ////////// KAKAO MAP //////////
    setMap() {
      this.setMapInfo();
      const mapContainer = document.getElementById('map'); // 지도를 표시할 div
      const mapOption = {
        center: this.housePositions[0], // 지도의 중심좌표
        level: 5, // 지도의 확대 레벨
      };
      this.map = new kakao.maps.Map(mapContainer, mapOption);

      this.updateMap(this.userInfo.userId, this.favorites[0].dongCode);

      this.categoryFunction();
    },

    setMapInfo() {
      // 지도에 필요한 정보 설정
      // dealNo - PK, dealAmount - 가격, housePositions - 위경도

      // 초기화
      this.dealNo = [];
      this.dealAmount = [];
      this.housePositions = [];

      for (var idx = 0; idx < this.mapResult.length; idx++) {
        this.dealNo[idx] = this.mapResult[idx].no;
        // console.log('here :', this.dealNo[idx]);
        this.dealAmount[idx] = this.mapResult[idx].dealAmount;
        this.housePositions[idx] = new kakao.maps.LatLng(
          this.mapResult[idx].lat,
          this.mapResult[idx].lng
        );
        // console.log('Here ', this.housePositions[idx]);
      }

      // 이전 마크를 지우고 새로운 마커를 생성하고 지도위에 표시합니다
      for (var k = 0; k < this.infowindowList.length; k++) {
        // this.infowindowList[k].close();
        this.infowindowList[k].setMap(null);
        this.houseMarkers[k].setMap(null);
      }

      this.infowindowList = [];
      this.houseMarkers = [];

      for (var i = 0, len = this.housePositions.length; i < len; i++) {
        this.addHouseMarker(i, this.housePositions[i]);
      }

      for (var j = 0; j < this.infowindowList.length; j++) {
        // this.infowindowList[j].open(this.map, this.houseMarkers[j]);
        this.infowindowList[j].setMap(this.map);
      }

      // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
      if (this.map != null) {
        this.map.panTo(this.housePositions[0]);
      }
    },

    // 마커를 생성하고 지도위에 표시하는 함수입니다
    addHouseMarker: function (index, housePosition) {
      // 마커를 생성합니다
      var marker = new kakao.maps.Marker({
        position: housePosition,
      });

      // 마커가 지도 위에 표시되도록 설정합니다
      marker.setMap(this.map);

      // 생성된 마커를 배열에 추가합니다
      this.houseMarkers.push(marker);

      const iwPosition = housePosition; // 인포윈도우 표시 위치입니다

      // console.log("statat ", this.mapResult[index].aptName);

      // 인포윈도우를 생성합니다
      var infowindow = new kakao.maps.CustomOverlay({
        position: iwPosition,
        content:
          '<span class="info-title" style="display: block;background: #50627F;color: #fff;text-align: center;height: 24px;line-height:22px;border-radius:4px;padding:0px 10px; width:100%;">' +
          this.dealAmount[index] +
          '</span>',
      });

      // 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
      this.infowindowList[this.infowindowList.length] = infowindow;
      // infowindow.open(this.map, marker);

      // 마커에 click 이벤트를 등록합니다
      var self = this;
      var targetNo = this.dealNo[index];
      kakao.maps.event.addListener(marker, 'click', function () {
        // let form = document.getElementById("houseDetail");
        // document.getElementById("no").setAttribute('value', this.dealNo[index]);
        // form.submit();
        console.log('Marker no :', targetNo);

        // modal로 바꾸기
        // self.$router.push({ name: 'ShowHouseDetail', query: { no: targetNo } });
        // this.showFavoriteDetailModal(targetNo);
        self.setModalNo(targetNo);
      });
    },

    updateMap(newId, newDongCode) {
      console.log('updateMap here');
      http.get(`showMap/${newDongCode}/${newId}`).then(({ data }) => {
        // console.log('updateMap here');
        this.mapResult = data.mapResult;
        if (this.mapResult == null) {
          alert('해당 지역의 매매 정보가 없습니다.');
        } else {
          // console.log("mapResult 길이 : ", this.mapResult.length);
          // console.log(this.mapResult[0]);
          this.dongCode = newDongCode;
          console.log('newDongCode : ', newDongCode);
          this.setMapInfo();
        }
      });
    },

    ////////// 카테고리 //////////

    categoryFunction() {
      console.log('categoryFunction() is called');

      // 마커를 클릭했을 때 해당 장소의 상세정보를 보여줄 커스텀오버레이입니다
      this.placeOverlay = new kakao.maps.CustomOverlay({ zIndex: 1 });
      this.contentNode = document.createElement('div'); // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
      this.markers = []; // 마커를 담을 배열입니다

      this.currCategory = ''; // 현재 선택된 카테고리를 가지고 있을 변수입니다

      console.log(this.placeOverlay, this.contentNode, this.markers, this.currCategory);

      // 장소 검색 객체를 생성합니다
      this.ps = new kakao.maps.services.Places(this.map);

      console.log("ps : ",this.ps);

      // 지도에 idle 이벤트를 등록합니다
      kakao.maps.event.addListener(this.map, 'idle', this.searchPlaces);

      // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
      this.contentNode.className = 'placeinfo_wrap';

      // 커스텀 오버레이의 컨텐츠 노드에 mousedown, touchstart 이벤트가 발생했을때
      // 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록합니다
      this.addEventHandle(this.contentNode, 'mousedown', kakao.maps.event.preventMap);
      this.addEventHandle(this.contentNode, 'touchstart', kakao.maps.event.preventMap);

      // 커스텀 오버레이 컨텐츠를 설정합니다
      this.placeOverlay.setContent(this.contentNode);

      // 각 카테고리에 클릭 이벤트를 등록합니다
      // this.addCategoryClickEvent();
    },

    // 엘리먼트에 이벤트 핸들러를 등록하는 함수입니다
    addEventHandle(target, type, callback) {
      if (target.addEventListener) {
        target.addEventListener(type, callback);
      } else {
        target.attachEvent('on' + type, callback);
      }
    },

    // 카테고리 검색을 요청하는 함수입니다
    searchPlaces() {
      if (!this.currCategory) {
        return;
      }

      // 커스텀 오버레이를 숨깁니다
      this.placeOverlay.setMap(null);

      // 지도에 표시되고 있는 마커를 제거합니다
      this.removeMarker();

      console.log('this.currCategory : ', this.currCategory);
      this.ps.categorySearch(this.currCategory, this.placesSearchCB, { useMapBounds: true });
    },

    // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
    placesSearchCB(data, status, pagination) {
      if (status === kakao.maps.services.Status.OK) {
        console.log('장소검색 완료');
        // 정상적으로 검색이 완료됐으면 지도에 마커를 표출합니다
        this.displayPlaces(data);
      } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        // 검색결과가 없는경우 해야할 처리가 있다면 이곳에 작성해 주세요
        console.log('장소검색 없는경우');
      } else if (status === kakao.maps.services.Status.ERROR) {
        // 에러로 인해 검색결과가 나오지 않은 경우 해야할 처리가 있다면 이곳에 작성해 주세요
      }
      pagination;
    },

    // 지도에 마커를 표출하는 함수입니다
    displayPlaces(places) {
      // 몇번째 카테고리가 선택되어 있는지 얻어옵니다
      // 이 순서는 스프라이트 이미지에서의 위치를 계산하는데 사용됩니다
      var order = document.getElementById(this.currCategory).getAttribute('data-order');

      var self = this;
      for (var i = 0; i < places.length; i++) {
        // 마커를 생성하고 지도에 표시합니다
        var marker = this.addMarker(new kakao.maps.LatLng(places[i].y, places[i].x), order);

        // 마커와 검색결과 항목을 클릭 했을 때
        // 장소정보를 표출하도록 클릭 이벤트를 등록합니다
        (function (marker, place) {
          kakao.maps.event.addListener(marker, 'click', function () {
            self.displayPlaceInfo(place);
          });
        })(marker, places[i]);
      }
    },

    // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
    addMarker(position, order) {
      var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(27, 28), // 마커 이미지의 크기
        imgOptions = {
          spriteSize: new kakao.maps.Size(72, 208), // 스프라이트 이미지의 크기
          spriteOrigin: new kakao.maps.Point(46, order * 36), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
          offset: new kakao.maps.Point(11, 28), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
        marker = new kakao.maps.Marker({
          position: position, // 마커의 위치
          image: markerImage,
        });

      // console.log('this.map : ',this.map);
      marker.setMap(this.map); // 지도 위에 마커를 표출합니다
      this.markers.push(marker); // 배열에 생성된 마커를 추가합니다

      return marker;
    },

    // 지도 위에 표시되고 있는 마커를 모두 제거합니다
    removeMarker() {
      for (var i = 0; i < this.markers.length; i++) {
        this.markers[i].setMap(null);
      }
      this.markers = [];
    },

    // 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
    displayPlaceInfo(place) {
      var content =
        '<div class="placeinfo">' +
        '   <a class="title" href="' +
        place.place_url +
        '" target="_blank" title="' +
        place.place_name +
        '">' +
        place.place_name +
        '</a>';

      if (place.road_address_name) {
        content +=
          '    <span title="' +
          place.road_address_name +
          '">' +
          place.road_address_name +
          '</span>' +
          '  <span class="jibun" title="' +
          place.address_name +
          '">(지번 : ' +
          place.address_name +
          ')</span>';
      } else {
        content += '    <span title="' + place.address_name + '">' + place.address_name + '</span>';
      }

      content +=
        '    <span class="tel">' + place.phone + '</span>' + '</div>' + '<div class="after"></div>';

      this.contentNode.innerHTML = content;
      this.placeOverlay.setPosition(new kakao.maps.LatLng(place.y, place.x));
      this.placeOverlay.setMap(this.map);
    },

    // 카테고리를 클릭했을 때 호출되는 함수입니다
    onClickCategory(catrgoryId) {
      var id = catrgoryId,
        className = document.getElementById(id).className;

      this.placeOverlay.setMap(null);

      if (className === 'on') {
        this.currCategory = '';
        this.changeCategoryClass();
        this.removeMarker();
      } else {
        this.currCategory = id;
        this.changeCategoryClass(id);
        this.searchPlaces();
      }
    },

    // 클릭된 카테고리에만 클릭된 스타일을 적용하는 함수입니다
    changeCategoryClass(categoryId) {
      var category = document.getElementById('category'),
        children = category.children,
        i;

      for (i = 0; i < children.length; i++) {
        children[i].className = '';
      }

      if (categoryId) {
        for (var j = 0; j < children.length; j++) {
          if (children[j].getAttribute('id') === categoryId) {
            children[j].className = 'on';
          }
        }
      }
    },
  },
};
</script>

<style>
/* @import '../../../src/assets/css/styles.css'; */

a {
  color: inherit;
  text-decoration: none;
}

.map_wrap,
.map_wrap * {
  margin: 0;
  padding: 0;
  font-family: 'Malgun Gothic', dotum, '돋움', sans-serif;
  font-size: 12px;
}

.map_wrap {
  position: relative;
  width: 100%;
  height: 500px;
}

#category {
  position: absolute;
  top: 10px;
  left: 10px;
  border-radius: 5px;
  border: 1px solid #909090;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.4);
  background: #fff;
  overflow: hidden;
  z-index: 2;
}

#category li {
  float: left;
  list-style: none;
  width: 50px;
  border-right: 1px solid #acacac;
  padding: 6px 0;
  text-align: center;
  cursor: pointer;
}

#category li.on {
  background: #eee;
}

#category li:hover {
  background: #ffe6e6;
  border-left: 1px solid #acacac;
  margin-left: -1px;
}

#category li:last-child {
  margin-right: 0;
  border-right: 0;
}

#category li span {
  display: block;
  margin: 0 auto 3px;
  width: 27px;
  height: 28px;
}

#category li .category_bg {
  background: url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png)
    no-repeat;
}

#category li .bank {
  background-position: -10px 0;
}

#category li .mart {
  background-position: -10px -36px;
}

#category li .pharmacy {
  background-position: -10px -72px;
}

#category li .oil {
  background-position: -10px -108px;
}

#category li .cafe {
  background-position: -10px -144px;
}

#category li .store {
  background-position: -10px -180px;
}

#category li.on .category_bg {
  background-position-x: -46px;
}

.placeinfo_wrap {
  position: absolute;
  bottom: 28px;
  left: -150px;
  width: 300px;
}

.placeinfo {
  position: relative;
  width: 100%;
  border-radius: 6px;
  border: 1px solid #ccc;
  border-bottom: 2px solid #ddd;
  padding-bottom: 10px;
  background: #fff;
}

.placeinfo:nth-of-type(n) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}

.placeinfo_wrap .after {
  content: '';
  position: relative;
  margin-left: -12px;
  left: 50%;
  width: 22px;
  height: 12px;
  background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png');
}

.placeinfo a,
.placeinfo a:hover,
.placeinfo a:active {
  color: #fff;
  text-decoration: none;
}

.placeinfo a,
.placeinfo span {
  display: block;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.placeinfo span {
  margin: 5px 5px 0 5px;
  cursor: default;
  font-size: 13px;
}

.placeinfo .title {
  font-weight: bold;
  font-size: 14px;
  border-radius: 6px 6px 0 0;
  margin: -1px -1px 0 -1px;
  padding: 10px;
  color: #fff;
  background: #d95050;
  background: #d95050 url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/arrow_white.png)
    no-repeat right 14px center;
}

.placeinfo .tel {
  color: #0f7833;
}

.placeinfo .jibun {
  color: #999;
  font-size: 11px;
  margin-top: 0;
}


</style>
