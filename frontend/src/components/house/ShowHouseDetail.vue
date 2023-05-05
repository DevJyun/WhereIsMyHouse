<template>
  <main class="flex-shrink-0"> 
	<div class="px-5 py-5 input-group" style="width:1000px; margin:0 auto;">
		<div class="input-group" style="display: flex; justify-content:center;">
			<h2 class="fw-bolder" style="display:inline">거래 정보 보기</h2>
		</div>
	</div>

	<section id="contents">

		<!--  form 설정하기 -->
		<!-- 
		<form id="searchByaddress" method="get" action="">
		-->
		<div class="row">
		<div class="col-2"></div>
		<div class="container my-5 col-3" style="float: left">
			<table class="table table-hover" style="text-align:center; vertical-align : middle; height:400px">
				<thead>
					<tr>
						<th colspan="2"><h4>{{house.aptName}}</h4></th>
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
		</div>
		<div id="map" class="col-5">
			
		</div>
		<div class="col-2"></div>
		</div>
	</section>
	</main>
</template>

<script>
import http from '@/api/http';

export default {
  data() {
    return {
      no: '',
      house: Object,
    };
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
    this.no = this.$route.query.no;
    console.log('ShowHouseDetail.......... no :', this.no);
    http.get(`showHouseDetail/${this.no}`)
        .then(({data}) => {
          this.house = data;
          console.log("house :", this.house);
        })
  },
  methods:{
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
        content : '<span class="info-title" style="display: block;background: #50627F;color: #fff;text-align: center;height: 24px;line-height:22px;border-radius:4px;padding:0px 10px;">'+this.house.aptName+'</span>'
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

</style>