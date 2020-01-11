Vue.component('v-chart', VueECharts)

var parent = new Vue({
    el: "#parentTitle",
    vuetify: new Vuetify(),
    data: {
        aa: 10,
        target: {
            targetAmount: 0,
            targetReturnRate: 0,
            targetYear: 0,
            currentCash:0,
            currentInterest:0
        },
        targetMonth: {
            currentAmount: 0,
            addCash: 0,
            addInterest: 0
        },

        accountFunds: [
            {
                edit: false, childAccountFunds: [
                {name: '余额宝', totalCash: 50, totalAmount: 55, totalInterest: 5, fundType: "现金"},
                {name: '基金帐号', totalCash: 50, totalAmount: 55, totalInterest: 5, fundType: '理财'},

            ], name: '支付宝', totalCash: 100, totalAmount: 110, totalInterest: 10
            }

        ],
        parentAccountFundParam: {
            name: '', totalAmount: 0, totalCash: 0, totalInterest: 0
        },
        childAccountFund: {
            name: '', totalAmount: 0, totalCash: 0, totalInterest: 0, parentId: 0, fundType: ''
        },
        childFundType: {name: '娜娜', remarks: ''},
        childFundTypes: [{name: '', remarks: ''}],
        childFundTypeNames: [],
        parentAccountFundDialog: false,
        childAccountFundDialog: false,
        currentParentAccountFundId: 0,
        currentEditChildAccountFundId: -1,
        errorMessage: '',
        showErrorMessage: false,
        targetChartOption: {},
        parentAccountFundDetailsOptions: [],
        realChartOption: {},
        realMonthAddAmount: 0,
        realTotalParentAccountFund: {},
        targetDialog: false,
        loginPage: true,
        loginInfo: {},
        codeUrl: url + "/asserts/code"
    },
    mounted: function () {
        this.resizeChart();
        this.isLogin()
    },
    methods: {
        isLogin: function () {
            var that = this;
            getJSON('/asserts/isLogin', null, function (successData) {
                that.loginPage = false;
                that.initTargetCharts()
                that.getAccountInfo();
                that.getAccountFundTypes();
                that.getAllParentAccountFundAndDetails();
                that.initRealCharts();
            }, function (errorData) {
                that.loginPage = true;
            })
        },
        resizeChart: function () {
            const self = this;//因为箭头函数会改变this指向，指向windows。所以先把this保存
            // setTimeout(() => {
            //     window.onresize = function() {
            //         self.$refs.targetChart.resize();
            //     }
            // },500)
        },
        backgroundImageCode: function () {
            var that = this;
            this.codeUrl = url+'/asserts/code?ranom = ' + Math.random() ;
        },
        buildOption: function (title = {text: '折线图'}, legend = {data: ['总资产', '现金贡献', '收益贡献']},
                               series = [
                                   {
                                       name: '总资产',
                                       type: 'line',
                                       data: []
                                   },
                                   {
                                       name: '现金贡献',
                                       type: 'line',
                                       data: []
                                   },
                                   {
                                       name: '收益贡献',
                                       type: 'line',
                                       data: []
                                   }
                               ], startValue) {
            return option = {
                title: title,
                tooltip: {
                    trigger: 'axis'
                },
                legend: legend,
                grid: {

                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: []
                },
                dataZoom: [{
                    startValue: startValue
                }, {
                    type: 'inside'
                }],
                yAxis: {
                    type: 'value'
                },
                series: series
            };
        },
        clickEditTable: function (childAccountFund) {

            if (this.currentEditChildAccountFundId != childAccountFund.id) {
                this.currentEditChildAccountFundId = childAccountFund.id;
            } else {
                var self = this;
                this.currentEditChildAccountFundId = -1;
                getJSON('/asserts/addFundInfo/' + childAccountFund.id, childAccountFund, function (data) {
                    self.getAccountInfo();
                })
            }
        },

        initTargetCharts: function () {
            var that = this;
            getJSON('/asserts/getTargetInfo', null, function (successData) {
                var targetInfos = successData.body;
                var option = that.buildOption();
                option.xAxis.data = [];
                option.title = {text: "目标"}
                option.series[0].data = [];
                option.series[1].data = [];
                option.series[2].data = [];
                for (var data of targetInfos) {
                    var date = new Date(data.targetDate);
                    var now = new Date();
                    if (date.getFullYear() == now.getFullYear() && date.getMonth() == now.getMonth()) {
                        that.targetMonth.currentAmount = data.currentAmount;
                        that.targetMonth.addCash = data.addCash;
                        that.targetMonth.addInterest = data.addInterest;
                        that.target.currentInterest = data.currentInterest == undefined ? 0 : data.currentInterest;
                        that.target.currentCash = data.currentCash == undefined ? 0 : data.currentCash;
                    }

                    option.xAxis.data.push(date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate())
                    option.series[0].data.push(data.currentAmount);
                    option.series[1].data.push(data.currentCash);
                    option.series[2].data.push(data.currentInterest);


                }
                that.targetChartOption = option;
            })
        },
        loginAction: function () {
            var that = this;
            postForm('/asserts/login', this.loginInfo, function (successData) {
                that.loginPage = false;
                that.initTargetCharts()
                that.getAccountInfo();
                that.getAccountFundTypes();
                that.getAllParentAccountFundAndDetails();
                that.initRealCharts();

            }, function (fail) {
                that.errorMessage = fail.responseJSON.body.errorMsg;
                that.showErrorMessage = true;
            })
        },
        initRealCharts: function () {
            var that = this;
            getJSON('/asserts/totalParentAccountDetails', null, function (successDate) {
                var realInfos = successDate.body;
                if (realInfos.length <= 0) {
                    return;
                }
                var option = that.buildOption();
                option.xAxis.data = [];
                option.title = {text: "实际"}
                option.series[0].data = [];
                option.series[1].data = [];
                option.series[2].data = [];
                that.realTotalParentAccountFund = {addCash: 0, addInterest: 0, currentAmount: 0, currentInterest:0,currentCash:0};
                var now = new Date();
                var createDate = new Date(realInfos[0].createDate);
                that.realTotalParentAccountFund.month = (now.getFullYear()  - createDate.getFullYear() ) * 12 + now.getMonth() - createDate.getMonth() + 1;


                for (var data of realInfos) {
                    var date = new Date(data.createDate);
                    if (date.getFullYear() == now.getFullYear() && date.getMonth() == now.getMonth()) {
                        that.realTotalParentAccountFund.addCash = that.realTotalParentAccountFund.addCash + data.addCash;
                        that.realTotalParentAccountFund.addInterest = that.realTotalParentAccountFund.addInterest + data.addInterest;
                        that.realTotalParentAccountFund.currentAmount = data.currentAmount;
                    }
                    option.xAxis.data.push(date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate())
                    option.series[2].data.push(data.currentInterest);
                    option.series[1].data.push(data.currentCash);
                    option.series[0].data.push(data.currentAmount);
                }
                var lastData = realInfos[realInfos.length -1];
                that.realTotalParentAccountFund.currentInterest = lastData.currentInterest;
                that.realTotalParentAccountFund.currentCash = lastData.currentCash;
                var currentCash = that.realTotalParentAccountFund.currentCash == 0 ? 1 : that.realTotalParentAccountFund.currentCash;
                that.realTotalParentAccountFund.returnRate = Math.round(that.realTotalParentAccountFund.currentInterest / currentCash * 10000)/ 10000;
                that.realChartOption = option;
            })
        },
        getAccountInfo: function () {
            var that = this;
            getJSON('/asserts/accountInfo', null, function (successData) {
                var targetVo = successData.body.targetAccountVO;
                that.target.targetYear = targetVo.targetYear;
                that.target.targetReturnRate = targetVo.targetReturnRate;
                that.target.targetAmount = targetVo.targetAmount;
                that.accountFunds = successData.body.currentAccountVOS;
            })
        },
        newParentAccountFund: function () {
            this.parentAccountFundDialog = false;
            var that = this;
            postJson('/asserts/createAccountFund', this.parentAccountFundParam, function (successData) {
                that.getAccountInfo();
            })
        },
        newChildAccountFund: function () {
            this.childAccountFund.parentId = this.currentParentAccountFundId;
            var that = this;
            postJson('/asserts/createAccountFund', this.childAccountFund, function (successDate) {
                that.getAccountInfo();
                that.initRealCharts();
                that.getAllParentAccountFundAndDetails()
                that.childAccountFundDialog = false;
            }, function (errorData) {
                that.errorMessage = errorData.responseJSON.body.errorMsg;
                that.showErrorMessage = true;
                that.childAccountFundDialog = true;

            })
        },
        newChildFundType: function () {
            var that = this;
            postJson('/asserts/createAccountFundType', this.childFundType, function (successData) {
                that.getAccountFundTypes();
            })
        },
        getAccountFundTypes: function () {
            var that = this;
            getJSON('/asserts/fundTypes', null, function (successDate) {
                that.childFundTypes = successDate.body;
                for (var childFundType of successDate.body) {
                    that.childFundTypeNames.push(childFundType.name);
                }
            })
        },
        childAccountFundDialogShow: function (parentAccountFund) {
            this.childAccountFundDialog = true;
            this.currentParentAccountFundId = parentAccountFund.id;
        },
        deleteAccountFund: function (accountFund) {
            var that = this;
            getJSON('/asserts/deleteAccountFund', {accountFundId: accountFund.id}, function (successDate) {
                that.getAccountInfo();
                that.getAllParentAccountFundAndDetails();
                that.initRealCharts();
            })
        },
        getAllParentAccountFundAndDetails: function () {
            var that = this;
            getJSON('/asserts/getAllParentAcountFundAndDetails', null, function (successData) {
                that.parentAccountFundDetailsOptions =[];
                for (var key in successData.body) {
                    var option = that.buildOption({text: key});
                    option.xAxis.data = [];
                    option.series[0].data = [];
                    option.series[1].data = [];
                    option.series[2].data = [];
                    for (var data of successData.body[key]) {
                        var date = new Date(data.createDate);
                        option.xAxis.data.push(date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate())
                        option.series[0].data.push(data.currentAmount);
                        option.series[1].data.push(data.currentCash);
                        option.series[2].data.push(data.currentInterest);
                    }
                        that.parentAccountFundDetailsOptions.push(option);
                }

            })
        },
        saveTargetInfo: function () {
            this.targetDialog = false;
            var that = this;
            postForm('/asserts/initTargetAmount', this.target, function (date) {
                that.initTargetCharts()
            })
        }
    }
})