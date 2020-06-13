<template>
  <div class="hello">

    <search-bar>
    </search-bar>

    <div class="filter">
      <div class="part" :class="sort===1?'part-active':''" @click="sortByPrice()">价格</div>
      <div class="part" :class="sort===3?'part-active':''" @click="sortByTime()">时间</div>
    </div>
<search-result></search-result>

  </div>
</template>

<script>
import axios from 'axios'
import SearchResult from "./searchResult";
const DatePicker = resolve => { require(['./DatePicker'], resolve) }
const SearchBar = resolve => { require(['./SearchBar'], resolve) }

export default {
  components: {
    SearchResult,
    SearchBar,
    DatePicker
  },
  data () {
    return {
      columns: [
        { title: '姓名', key: 'name', sortable: true },
        { title: '所属部门', key: 'email', editable: true },
        { title: '职位', key: 'position' ,editable: true},
        { title: '角色', key: 'role',editable: true },
        {
          title: '处理',
          key: 'handle',
          options: ['delete'],
          button: [
            (h, params, vm) => {
              return h('Poptip', {
                props: {
                  confirm: true,
                  title: '你确定要删除吗?'
                },
                on: {
                  'on-ok': () => {
                    vm.$emit('on-delete', params)
                    vm.$emit('input', params.tableData.filter((item, index) => index !== params.row.initRowIndex))
                  }
                }
              }, [
                h('Button', '自定义删除')
              ])
            }
          ]
        }
      ],
      tableData: [



      ],
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
  },
  watch: {
    '$route' (to, from) {
      this.search()
    }
  },
  mounted () {
    this.search()
  },
  methods: {
    sortByPrice () {
      if (this.sort === 1) {
        this.flights.reverse()
        return
      }
      this.sort = 1
      this.flights.sort((e, f) => e.minPrice > f.minPrice)
    },
    sortByTime () {
      if (this.sort === 3) {
        this.flights.reverse()
        return
      }
      this.sort = 3
      this.flights.sort((e, f) => (e.binfo ? e.binfo.depTime : e.binfo1.depTime) > (f.binfo ? f.binfo.depTime : f.binfo1.depTime))
    },
    async search () {
      console.log('search')
      this.departureCity = this.$route.params.dep ? this.$route.params.dep.split(',') : []
      this.arrivalCity = this.$route.params.arr ? this.$route.params.arr.split(',') : []
      this.departureDate = this.$route.params.date
      this.data = {}
      this.flights = []
      this.companies = []
      this.recommends = []
      this.departureCity.forEach(async dep => {
        await this.arrivalCity.forEach(async arr => {
          await this.loadFlight(dep, arr, this.departureDate)
          await this.loadNearby(dep, arr, this.departureDate)
        })
      })
      await this.loadCalenders()
    },
    loadCalenders (date = null) {
      this.week = []
      this.departureCity.forEach(async dep => {
        await this.arrivalCity.forEach(async arr => {
          await this.loadCalender(dep, arr, date || this.departureDate)
        })
      })
    },
    loadNearby (departureCity = '无锡', arrivalCity = '贵阳', departureDate = '2018-03-17') {
      let that = this
      axios.get(`http://dustark.cn:7001/nearby?from=${departureCity}&to=${arrivalCity}&start_date=${departureDate}`)
        .then(function (response) {
          console.log(response)
          if (response.data.records && Array.isArray(response.data.records)) {
            that.recommends = that.recommends.concat(response.data.records)
          }
        })
        .catch(function (response) {
          console.log(response)
        })
    },
    async loadCalender (departureCity = '无锡', arrivalCity = '贵阳', departureDate = '2018-03-17', month = 0) {
      let that = this
      await axios.get(`http://dustark.cn:7001/ca?dep=${departureCity}&arr=${arrivalCity}&dep_date=${departureDate}&adultCount=1&month_lp=${month}`)
        .then(function (response) {
          console.log(response)
          if (response.data.ret) {
            if (that.week.length) {
              for (let i = 0; i < that.week.length; i++) {
                if (that.week[i].price > response.data.data.banner[i].price) {
                  that.week[i].price = response.data.data.banner[i].price
                  that.week[i].direct = response.data.data.banner[i].direct
                }
              }
            } else {
              that.week = response.data.data.banner || []
            }
          }
        })
        .catch(function (response) {
          console.log(response)
        })
    },
    async loadFlight (departureCity = '无锡', arrivalCity = '贵阳', departureDate = '2018-03-17') {
      this.loading = true
      let that = this
      await axios.get(`http://dustark.cn:7001/fl?departureCity=${departureCity}&arrivalCity=${arrivalCity}&departureDate=${departureDate}`)
        .then(function (response) {
          console.log(response)
          let data = response.data.data
          if (response.data.code === 0) {
            data.flights.forEach(e => {
              e.departureCity = departureCity
              e.arrivalCity = arrivalCity
              if (e.binfo && !that.companies.some(f => f === e.binfo.name)) {
                that.companies.push(e.binfo.name)
              }
              if (e.binfo1 && !that.companies.some(f => f === e.binfo1.name)) {
                that.companies.push(e.binfo1.name)
              }
              if (e.binfo2 && !that.companies.some(f => f === e.binfo2.name)) {
                that.companies.push(e.binfo2.name)
              }
            })
            if (that.data.total) {
              that.data.total += data.total
              that.data.flights = that.data.flights.concat(data.flights)
              that.flights = that.flights.concat(data.flights)
              that.data.geographyInfo.push(data.geographyInfo)
              if (that.data.min_flight.all.price > data.min_flight.all.price) {
                that.data.min_flight = data.min_flight
              }
            } else {
              data.geographyInfo = [data.geographyInfo]
              that.data = data
              that.flights = data.flights
            }
          }
          that.loading = false
        })
        .catch(function (response) {
          console.log(response)
          that.loading = false
        })
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
