<template>
  <div>
    <div class="px-5 py-5 input-group" style="width:1000px; margin:0 auto;">
      <div class="input-group" style="display: flex; justify-content:center;">
        <h2 class="fw-bolder" style="display:inline">🤔 문의사항 관리</h2>
      </div>
    </div>
    <router-view></router-view>
  </div>
  <!-- <div>
    <h2 class="text-center">문의사항 관리</h2>
    <div class="nav">
      <div class="nav-item">
        <router-link class="nav-link" :to="{ name: 'QnAListView' }">문의사항 목록</router-link>
      </div>
      <div class="nav-item">
        <router-link class="nav-link" :to="{ name: 'QnARegistView' }">문의사항 등록</router-link>
      </div>
    </div>
  </div> -->
</template>

<script>
export default {};
</script>

<style></style>
