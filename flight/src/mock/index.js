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
Mock.mock('/user/userInfo', 'get', require('./testJson.json'));
