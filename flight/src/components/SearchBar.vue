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
                prop="flightId"
                label="航班编号"
                width="180">
        </el-table-column>
        <el-table-column
                prop="airlineName"
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
                prop="departureTime"
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
    axios.get('/flightReservation').then(function (res){
      console.log(res);
      _this.tableData=res.data;
      console.log("table")
      console.log(_this.tableData);
    });

  },
  data () {
    return {
      selectData:[],
      searchTable: [],
      tableData: [],
      orderData:[],
      date: '',
      depCity: '',
      arrCity: '',
      value: '',
      balance :'',
      token:'',
      orderId:'',
      userId:'',
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

      const _this=this;
     // const _selectData = this.$refs.tableData.selection;
             // .selection;
      console.log("row");
      console.log(row);
      //获取账户信息接口
      axios.post('/payment/login?username=admin&password=admin').then(function (res2){
        _this.balance=res2.data.balance;
        _this.token=res2.data.token;
        console.log(_this.token);
      })
      const h = this.$createElement;
      axios.post('/flightReservation/order',null,{
        params:{
          airlineId:row.airlineId,
          flightId:row.flightId
        }
      }).then(function (res){
        _this.orderId=res.data.orderId;
        _this.userId=res.data.userId;
        console.log("oderid");
        console.log(_this.orderId);
        console.log("_this.userId");
        console.log(_this.userId);
      });

      this.$msgbox({
          // message: '支付成功 '
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
          axios.post('/payment/pay',null,{
            params:{
              orderId:this.orderId,
            },
            headers:{
              token:this.token
            }
          }).then(function (response2) {
            console.log("/payment/pay");
            console.log(response2);
            axios.post('/flightReservation/payment',null,{
              params:{
                orderId:_this.orderId,
              }
            }).then(function (response3) {
              console.log("/flightReservation/payment");
              console.log(response3);
              if(response3.status==200)
              {
                _this.$message({
                  title:'success',
                  message: '支付成功 '
                });
              }
              else
              {
                _this.$message({
                  title:'err',
                  message: '支付失败 '
                });
              }
            })

          })
        });

        // this.$message({
        //   title:'success',
        //   message: '支付成功 '
        // });
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
          console.log(product.departureTime)
          return product.destinationCity == arrCity && product.departureCity == depCity&&moment(product.departureTime,"YYYY-MM-DD").isAfter(date0)&&moment(product.departureTime,"YYYY-MM-DD").isBefore(date1);
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
