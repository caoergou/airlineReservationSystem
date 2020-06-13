<template>
    <div class="search-result">
        <el-table
                :header-cell-style="{background:'#d3d7d4'}"
                :data="tableData"
                style="width: 100%"
                :row-class-name="tableRowClassName">
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
                    label="出发日期">
            </el-table-column>
            <el-table-column
                    prop="fare"
                    label="票价"
                    class="el-icon-price-tag"
            >
            </el-table-column>
            <el-table-column
                    prop="remaining"
                    label="剩余票量">
            </el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    width="100">
                <template slot-scope="scope">
                    <el-button
                            @click="open"
                            type="primary" size="small">预定</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>

</template>

<style>
    .el-table .warning-row {
        /*background:#a1a3a6;*/
    }

    .el-table .success-row {
        background: #f0f9eb;
    }
</style>

<script>
    import axios from 'axios'
    export default {
        computed: {
            changeButton: function(){
                let buttonStatus = false;
                if (this.data.all > 0){
                    buttonStatus = true
                }else {
                    buttonStatus = false
                }
                return buttonStatus;
            }
        },
        methods: {
            tableRowClassName({row, rowIndex}) {
                if (rowIndex === 1) {

                    return 'warning-row';
                } else if (rowIndex === 3) {
                    return 'success-row';
                }
                return '';
            },handleClick(row) {
                console.log(row);
            },
            open() {
                console.log(this.tableData)
                //   2000

                //  ￥ 1999
                const h = this.$createElement;
                this.$msgbox({
                    title: '支付订单',
                    message:
                        h('p', null, [
                        h('span', null, '当前账户余额： '),
                        h('i', { style: 'color: teal' },2000 ),
                                h('p',null,[
                                    h('span', null, '本币订单金额： '),
                                    h('i', { style: 'color: teal' },1699 )
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
                                }, 300);
                            }, 3000);
                        } else {
                            done();
                        }
                    }
                });

            },

        },
        created(){
            const _this=this;
            axios.get('/user/userInfo').then(function (res){
                console.log(res);
                _this.tableData=res.data.info;
            })
        },
        data() {
            return {
                tableData: [{
                    // "flightID": 121,
                    "flightID": "MU9227",
                    "flightName": "东方航空",
                    "departurePort": "虹桥国际机场",
                    "departureCity": "上海",
                    "destinationPort": "白云国际机场",
                    "destinationCity": "广州",
                    "airline": 1,
                    "DepartureDate": "2020/06/08 07:05",
                    "fare": 1699.99,
                    "remaining": 99
                }, {
                    "id": 121,
                    "flightID": "CZ6718",
                    "flightName": "南方航空",
                    "departurePort": "黄花国际机场",
                    "departureCity": "长沙",
                    "destinationPort": "浦东国际机场",
                    "destinationCity": "上海",
                    "airline": 1,
                    "DepartureDate": "2020/06/08 07:05",
                    "fare": 1699.99,
                    "remaining": 0
                },{
                    "id": 121,
                    "flightID": "MU9227",
                    "flightName": "东方航空",
                    "departurePort": "虹桥国际机场",
                    "departureCity": "上海",
                    "destinationPort": "白云国际机场",
                    "destinationCity": "广州",
                    "airline": 1,
                    "DepartureDate": "2020/06/08 07:05",
                    "fare": 1699.99,
                    "remaining": 99
                }]
            }
        },
        // mounted:function() {
        //     axios.get('/user/userInfo').then(res=>{
        //         console.log(res);
        //     })
        //
        //     .catch(err=>{
        //         console.log(err);
        //     })
        // }
    }
</script>
