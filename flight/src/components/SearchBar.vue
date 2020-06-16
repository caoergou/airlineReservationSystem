<template>
  <div class="bar">
    <div class="searchbar">
      <el-input placeholder="请输入出发城市" v-model="depCity" class="input-with-select" style="line-height:50px;width:200px">
      </el-input>
      <el-input placeholder="请输入目的城市" v-model="arrCity" class="input-with-select" style="width:200px;">
      </el-input>
      <div class="block">
        <el-date-picker
                v-model="date"
                type="daterange"
                unlink-panels
                range-separator="至"
                start-placeholder="选择出发日期"
                :picker-options="pickerOptions">
        </el-date-picker>
        <el-button slot="append" @click="search()" icon="el-icon-search"></el-button>
      </div>
    </div>



    <div class="search-result">
      <el-table
              :header-cell-style="{background:'#d3d7d4'}"
              :data="tableData"
              style="width: 100%"
              ref="selectData"
              :row-class-name="tableRowClassName"
              :default-sort = "{prop: 'DepartureDate', order: 'descending'}">

        <el-table-column
                prop="flightID"
                label="航班编号"
                width="180">
        </el-table-column>
        <el-table-column
                prop="flightName"
                label="航空公司"
                width="180">
        </el-table-column>
        <el-table-column
                prop="departureCity"
                label="出发城市">
        </el-table-column>
        <el-table-column
                prop="departurePort"
                label="出发机场">
        </el-table-column>
        <el-table-column
                prop="destinationCity"
                label="目的城市">
        </el-table-column>
        <el-table-column
                prop="destinationPort"
                label="目的机场">
        </el-table-column>
        <el-table-column
                prop="DepartureDate"
                sortable
                label="出发日期">
        </el-table-column>
        <el-table-column
                sortable
                prop="fare"
                label="票价"
                class="el-icon-price-tag"
        >
        </el-table-column>
        <el-table-column
                sortable
                prop="remaining"
                label="剩余票量"
                :formatter="formatter">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="100">
          <template slot-scope="scope">
            <el-button
                    @click="open(scope.row)"
                    type="primary" size="small">预定</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
let moment = require("moment");
export default {

  created(){
    const _this=this;
   // http://localhost:8080/flightReservation获取所有航班接口
    axios.get('/airline').then(function (res){
      console.log(res);
      _this.tableData=res.data.info.list;
    });
//获取账户信息接口
    axios.get('/payment/login').then(function (res2){
      _this.balance=res2.data.balance;
      console.log(_this.balance);
    })
  },
  data () {
    return {
      selectData:[],
      searchTable: [],
      tableData: [],
      date: '',
      depCity: '',
      arrCity: '',
      value: '',
      balance :'',
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      }
      }
    },
  methods: {

    formatter(row, column) {
      return row.remaining;
    },
    tableRowClassName({row, rowIndex}) {
      if (rowIndex === 1) {

        return 'warning-row';
      } else if (rowIndex === 3) {
        return 'success-row';
      }
      return '';
    },
    open(row) {
     // const _selectData = this.$refs.tableData.selection;
             // .selection;
      console.log("row");
      console.log(row);
      const h = this.$createElement;
      this.$msgbox({
        title: '支付订单',
        message:
                h('p', null, [
                          h('span', null, '当前账户余额： '),
                          h('i', { style: 'color: teal' },this.balance ),
                          h('p',null,[
                            h('span', null, '本币订单金额： '),
                            h('i', { style: 'color: teal' },row.fare )
                          ])
                        ],

                ),
        showCancelButton: true,
        confirmButtonText: '支付',
        cancelButtonText: '取消',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = '执行中...';
            setTimeout(() => {
              done();
              setTimeout(() => {
                instance.confirmButtonLoading = false;
              }, 100);
            }, 500);
          } else {
            done();
          }
        }
      }).then(action=>{
        //提交表单接口
        axios.post('',row).then(function (response) {
          if(response.data=='success'){
            this.$message({
              message: '支付成功 '
            });
          }
        })
      });

    },
    search: function () {
     // dateA = moment(itemA.date, "MM-DD-YYYY");
      var date0    = moment(this.date[0],"YYYY-MM-DD");
      var date1    = moment(this.date[1],"YYYY-MM-DD");
      var arrCity = this.arrCity;
      var depCity = this.depCity;
      if(arrCity&&depCity){

        console.log(date0);
        this.searchTable = this.tableData.filter(function (product) {
          console.log("111")
          console.log(product.DepartureDate)
          return product.destinationCity == arrCity && product.departureCity == depCity&&moment(product.DepartureDate,"YYYY-MM-DD").isAfter(date0)&&moment(product.DepartureDate,"YYYY-MM-DD").isBefore(date1);
        })
        this.tableData=this.searchTable;
        return this.tableData;
      }

    }
    //  this.$router.push(`/flight/${depCity}/${arrCity}/${this.date}`)
  }
  }


</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
  .bar {
    padding: 10px;
  }
  .searchbar{
    margin: auto;
    width: 50%;
    height: 100px;
  }

</style>
