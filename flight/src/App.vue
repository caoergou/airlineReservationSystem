<template>
  <div id="app">

    <div class="header">欢迎使用到哪儿航班预定系统</div>
    <router-view/>
  </div>

</template>

<script>

import axios from "axios";

export default {
  created() {
  }
}

</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.header {
  width: 40%;
  background: #409EFF;
  color: white;
  padding-left: 20px;
  height: 50px;
  line-height: 50px;
  font-size: 20px;
  display: flex;
  padding-right: 50px;
  margin: auto;
}
</style>
