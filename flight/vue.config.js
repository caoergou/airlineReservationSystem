
module.exports = {

  devServer: {
    host: 'localhost',
    port: 8083,
    proxy: {
      '/flightReservation': {
        target: 'http://120.24.33.16:8080/flightReservation',// 要跨域的域名
        changeOrigin: true, // 是否开启跨域
        pathRewrite: {
          '^/flightReservation': '' // 请求的时候 /flightReservation就会替换成 ''
        }
      },
      '/payment': {
        target: 'http://120.24.33.16:8082',// 要跨域的域名
        changeOrigin: true, // 是否开启跨域
      }
    }
  }
}
