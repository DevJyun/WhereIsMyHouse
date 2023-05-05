<template>
  <div v-if="showModal">
    <transition name="modal" appear>
      <div class="modal-mask">
        <div class="modal-wrapper">
          <div class="modal-dialog" role="document">
            <div class="modal-content" style="width: 1000px; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
              <div class="modal-header" style="justify-content : center;">
                <h4 class="modal-title"><b>🏢 아파트 세부 정보</b></h4>
                <!-- <button type="button" class="btn btn-warning" @click.self="$emit('close')">
                  &times;
                </button> -->
              </div>
              <div class="modal-body" style="padding-right:3em; padding-left:0em;">
                <div class="row">
                <!-- <div class="col-2"></div> -->
                <div class="container col-4" style="float: left;">
                  <div class="card" style="width: 100%; background-color: #f8f9fa; webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
                    <div class="card-body"
                      style="">
                      <h3 class="card-title fw-bolder">🏡 {{house.aptName}}</h3>
                      <p class="card-text">
                        <table class="table table-hover" style="text-align:center; vertical-align : middle; height:400px">
                          <thead>
                            <tr>
                              <!-- <br/> -->
                              <!-- <th colspan="2"><h4>{{house.aptName}}</h4></th> -->
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td><b>동 이름</b></td>
                              <td>{{house.dongName}}</td>
                            </tr>
                            <tr>
                              <td><b>거래 금액</b></td>
                              <td>{{house.dealAmount}}</td>
                            </tr>
                            <tr>
                              <td><b>면적</b></td>
                              <td>{{house.area}}</td>
                            </tr>
                            <tr>
                              <td><b>층수</b></td>
                                <td>{{house.floor}}</td>
                            </tr>
                            <tr>
                              <td><b>지번</b></td>
                              <td>{{house.jibun}}</td>
                            </tr>
                            <tr>
                              <td><b>건축년도</b></td>
                              <td>{{house.buildYear}}</td>
                            </tr>
                            <tr>
                              <td><b>거래 일자</b></td>
                              <td>{{house.dealYear}}/{{house.dealMonth}}/{{house.dealDay}}</td>
                            </tr>
                          </tbody>
                        </table>
                      </p>
                    </div>
                  </div>
                </div>
                
                &nbsp;

                <div id="map" class="col-7" style="webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px;">
                  
                </div>
                <!-- <div class="col-2"></div> -->
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" @click.self="$emit('close')">닫기</button>
                <!-- <button type="button" class="btn btn-primary">Save changes</button> -->
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import http from '@/api/http';

export default {
  props: ['no'],
  data() {
    return {
      house: Object,
      showModal: true,
    }
  },
  mounted() {
    // if (window.kakao && window.kakao.maps) {
    //   this.setMap();
    // } else {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.setMap);
      script.src = "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=8705edb85db1a3d8342bb33ee262d130&libraries=services";
      document.head.appendChild(script);
      // this.setMap();
    // }
  },
  created() {
    console.log('ShowHouseDetail.......... no :', this.no);
    http.get(`showHouseDetail/${this.no}`)
        .then(({data}) => {
          this.house = data;
          console.log("house :", this.house);
        })
  },
  methods: {
    setMap() {
      var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
        mapOption = { 
            center: new kakao.maps.LatLng(this.house.lat, this.house.lng), // 지도의 중심좌표
            level: 2 // 지도의 확대 레벨
        };
  
      // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
      var map = new kakao.maps.Map(mapContainer, mapOption); 
      
      // 마커가 표시될 위치입니다 
      var markerPosition  = new kakao.maps.LatLng(this.house.lat, this.house.lng); 
  
      // 마커를 생성합니다
      var marker = new kakao.maps.Marker({
        position: markerPosition
      });
  
      // 마커가 지도 위에 표시되도록 설정합니다
      marker.setMap(map);
      
      // var iwContent = '<div style="padding:5px; text-align:center">거래 금액<br>${house.dealAmount}</div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
      var iwPosition = new kakao.maps.LatLng(this.house.lat, this.house.lng); //인포윈도우 표시 위치입니다
  
      // 인포윈도우를 생성합니다
      var infowindow = new kakao.maps.InfoWindow({
        position : iwPosition, 
        content : '<span class="info-title" style="display: block;background: #50627F;color: #fff;text-align: center;height: 24px;line-height:22px;border-radius:4px;padding:0px 10px;">'+this.house.dealAmount+'</span>'
      });
  
      // 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
      infowindow.open(map, marker); 
      
      var infoTitle = document.querySelectorAll('.info-title');
      infoTitle.forEach(function(e) {
        var w = e.offsetWidth + 10;
        var ml = w/2;
        e.parentElement.style.top = "82px";
        e.parentElement.style.left = "50%";
        e.parentElement.style.marginLeft = -ml+"px";
        e.parentElement.style.width = w+"px";
        e.parentElement.previousSibling.style.display = "none";
        e.parentElement.parentElement.style.border = "0px";
        e.parentElement.parentElement.style.background = "unset";
      });
    }
  },
}
</script>

<style>
  .modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .3s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

</style>