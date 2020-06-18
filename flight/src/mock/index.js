const fs=require('fs')
const path=require('path')
const jsoN5=require('json5')
const Mock=require('mockjs')

function getJsonFile(filepath) {
    var js=fs.readFileSync(filepath,"utf-8");
    return jsoN5.parse(js)
}
module.exports=function(app){
    //监听http请求
    app.get('/user/flightInfo',function (rep,res) {
//每次相应请求读取flightInfo的json5文件
        //getJsonFile定义了如何读取json文件并解析成对象
        var json = getJsonFile('./flightInfo.json5');
        //将Json传入mock方法中生成数据
        res.json(Mock.mock(json));
    })
}
////*************************************////
Mock.mock('/airline', 'get', {
        info: {
            "list|5":
                [{
                    "flightID": "@id()",
                    "flightName": "@cname()",
                    "departurePort|1": ["虹桥机场","首都机场","双流机场","白云机场","保安机场"],
                    "departureCity|1": ["上海","北京","成都","广州","深圳"],
                    "destinationPort|1": ["虹桥机场","首都机场","双流机场","白云机场","保安机场"],
                    "destinationCity|1": ["上海","北京","成都","广州","深圳"],
                    "airline": "/[0-9]/",
                    "DepartureDate": "@date()",
                    "fare": "@integer(1000,2000)",
                    "remaining": "@integer(100,200)"
                }
                ]
        }
    },
    // Mock.mock('/payment/login','get',{
    //     "id": '123456789',
    //     "username": "string",
    //     "password": "123456789",
    //     "balance": "@integer(2000,3000)"
    // }),

    Mock.mock('/payment/new','post',function(option){
        //请求相关的参数
        console.log(option)
        //模拟假数据需要重新写Mock
        return Mock.mock({
            data:'success',
            status:200,
            message:'@cword(4,9)'
        })
    })

);
