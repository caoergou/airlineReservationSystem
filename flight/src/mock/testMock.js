const Mock=require('mockjs')
let id=Mock.mock('@id')
let obj=Mock.mock({
    flightID: /[A-Z][A-Z]\d{4,6}/,
    flightName: '@cname',
    departurePort: "@cname",
    departureCity: "@city",
    destinationPort: "@city",
    destinationCity: "@city",
    airline: /[0-9]/,
    DepartureDate: '@date',
    fare: 1699.99,
    remaining: 99
    }
)
console.log(obj)
