<template>
  <div id="app" style="background-color: #ffefe0">
    <the-header-navbar-modified></the-header-navbar-modified>
    <router-view></router-view>
    <the-footer></the-footer>
  </div>
</template>

<script>
import TheHeaderNavbarModified from '@/components/TheHeaderNavbarModified';
import TheFooter from '@/components/TheFooter';

export default {
  name: 'App',
  components: {
    TheHeaderNavbarModified,
    TheFooter,
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;

  min-height: 100%;
  position: relative;
  padding-bottom: 67px;
}

a.router-link-exact-active {
  color: #89bfef;
}

a.router-link-exact-active:hover {
  text-decoration: none;
}
</style>
