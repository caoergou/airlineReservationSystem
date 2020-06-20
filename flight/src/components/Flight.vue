<template>
  <div class="hello">

    <search-bar>
    </search-bar>



  </div>
</template>

<script>
import axios from 'axios'
const DatePicker = resolve => { require(['./DatePicker'], resolve) }
const SearchBar = resolve => { require(['./SearchBar'], resolve) }

export default {
  components: {
    SearchBar,
    DatePicker
  },
  data() {
    return {
      tableData: [],
      wk: ['日', '一', '二', '三', '四', '五', '六'],
      sort: 0,
      labTab: 0,
      showCalender: false,
      open: '',
      week: [],
      recommends: [],
      data: {},
      flights: [],
      companies: {},
      code: {},
      routes: {},
      departureCity: [],
      arrivalCity: [],
      departureDate: '',
      loading: false,
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .week {
    background: dodgerblue;
    margin: 0;
  }
  .filter {
    /*background: dodgerblue;*/
    width: 10%;
    padding: 10px;
    color:blue;
    display: flex;
    font-size: 14px;
    flex-wrap: inherit;

  }
  .part {
    margin: 2px 20px 2px 0;
    cursor: pointer;
  }
  .part:hover {
    color: #eeeeee;
  }
  .part-active {
    color: #43ff7d;
  }
  .part-active:hover {
    color: #c2ffc1;
  }
  .day {
    background: white;
    overflow: hidden;
    height: 40px;
    border-radius: 30px;
    margin: 5px;
    padding: 5px;
    text-align: center;
    cursor: pointer;
    color: black;
  }
  .day > i {
    width: 30px;
  }
  .day:hover {
    background: #eaffff;
  }
  .day-active {
    background: #333333;
    color: white;
  }
  .day-active:hover {
    background: #666666;
  }
  .date {
    font-size: 13px;
  }
  .price {
    font-size: 20px;
    color: dodgerblue;
  }
  .tax {
    font-size: 12px;
  }
  .notfound {
    margin-top: 100px;
    text-align: center;
    color: dodgerblue;
  }
  .depend {
    padding-right: 20px;
  }
  .title {
    font-size: 12px;
    margin-top: 10px;
    width: 100%;
    border-bottom: dashed 1px grey;
    margin-bottom: 10px;
    padding-bottom: 5px;
  }
</style>
