const fs = require('fs');
const async = require('async');
const superagent = require('superagent');

const workDetail = '张震，1976年10月14日出生于台湾省台北市，祖籍浙江余姚，中国台湾影视男演员、歌手';

let readAccount = function () {
    let account = fs.readFileSync('./account.json');
    return JSON.parse(account.toString());
};

let httpRequest = function (params, cb) {
    superagent
        .get('http://192.168.1.134:8080/api/user/commitWork')
        .query(params)
        .end(function (err, res) {
            if (err) {
                console.error(err)
            } else {
                console.log(JSON.stringify(res.body));
                cb(null)
            }
        })
};

let pressTest = function (courseCode, benchmark) {
    let beginTime = new Date().getMilliseconds();
    let account = readAccount();
    let params = [];
    for (let i in account) {
        let param = {
            'courseCode': courseCode,
            'workDetail': workDetail,
            'email': account[i],
            'userName': i,
            'userType': 'student'
        };
        params.push(param);
    }
    async.eachLimit(
        params, benchmark, function (param, callback) {
            httpRequest(param, callback)
        }, function (err) {
            if (err) {
                console.log('*********************request error');
            } else {
                console.log('=====================request success');
                console.log('=====================request consume time=' + (new Date().getMilliseconds() - beginTime));
            }
        }
    )
};

pressTest('1572', 10);