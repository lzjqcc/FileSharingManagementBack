
var parent = new Vue({
    el:"#parentTitle",
    vuetify: new Vuetify(),
    data: {
        aa:10,
        target:{
            targetAmount:100,
            targetReturnRate: 0.07,
            targetYear:7
        },
        accountFunds:[
            {edit: false,childAccountFunds:[
                {name:'余额宝',totalCash:50,totalAmount:55,totalInterest:5,fundType:"现金"},
                {name:'基金帐号',totalCash:50,totalAmount:55,totalInterest:5, fundType:'理财'},

            ],name:'支付宝',totalCash:100,totalAmount:110,totalInterest:10}

        ],
        parentAccountFundParam:{
            name:'',totalAmount:0,totalCash:0,totalInterest:0
        },
        childAccountFund: {
            name:'',totalAmount:0,totalCash:0, totalInterest:0, parentId:0,fundType:''
        },
        childFundType:{name:'',remarks:''},
        childFundTypes:[{name:'',remarks:''}],
        childFundTypeNames:[],
        parentAccountFundDialog:false,
        childAccountFundDialog: false,
        currentParentAccountFundId:0,
        currentEditChildAccountFundId:-1,
        errorMessage:'',
        showErrorMessage:false
    },
    mounted: function () {
        this.initTargetCharts();
        this.getAccountInfo();
        this.getAccountFundTypes();
    },
    methods: {
        buildOption: function (title = {text:'折线图'}, legend = {data: ['总资产', '现金贡献', '收益贡献']},
                               series =  [
                                   {
                                       name: '总资产',
                                       type: 'line',
                                       stack: '总量',
                                       data: [120, 132, 101, 134, 90, 230, 210]
                                   },
                                   {
                                       name: '现金贡献',
                                       type: 'line',
                                       stack: '总量',
                                       data: [220, 182, 191, 234, 290, 330, 310]
                                   },
                                   {
                                       name: '收益贡献',
                                       type: 'line',
                                       stack: '总量',
                                       data: [150, 232, 201, 154, 190, 330, 410]
                                   }
                               ]) {
           return option = {
                title: title,
                tooltip: {
                    trigger: 'axis'
                },
                legend: legend,
                grid: {
                    left: '3%',
                    right: '4%',
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
                yAxis: {
                    type: 'value'
                },
                series: series
            };
        },
        clickEditTable: function (childAccountFund, event) {
            if (this.currentEditChildAccountFundId != childAccountFund.id) {
                event.target.innerText= '保存';
                this.currentEditChildAccountFundId = childAccountFund.id;

            }else {
                event.target.innerText = '编辑';
                this.currentEditChildAccountFundId = -1;
            }
        },

        initTargetCharts: function () {
            var that = this;
            postForm('/asserts/login', {'email': '1161889163@qq.com', 'password': '11111111a'}, function (successData) {
                getJSON('/asserts/getTargetInfo', null, function (successData) {
                    var targetInfos = successData.body;
                    var option = that.buildOption();
                    option.xAxis.data = [];
                    option.series[0].data = [];
                    option.series[1].data = [];
                    option.series[2].data = [];
                    for (var data of targetInfos) {
                        var date = new Date(data.targetDate);
                        option.xAxis.data.push(date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate())
                        option.series[0].data.push(data.currentAmount);
                        option.series[1].data.push(data.currentCash);
                        option.series[2].data.push(data.currentInterest);
                    }
                    var myChart = echarts.init(document.getElementById('targetChart'));
                    myChart.setOption(option);
                })
            })
        },
        getAccountInfo: function() {
            var that =this;
            getJSON('/asserts/accountInfo', null, function (successData) {
                that.target = successData.body.targetAccountVO;
                that.accountFunds = successData.body.currentAccountVOS;
                for (var accountFund of that.accountFunds) {
                    accountFund.edit = false;
                }
            })
        },
        newParentAccountFund:function () {
            this.parentAccountFundDialog = false;
            var that = this;
            postJson('/asserts/createAccountFund', this.parentAccountFundParam, function (successData) {
                that.getAccountInfo();
            })
        },
        newChildAccountFund:function () {
            this.childAccountFund.parentId = this.currentParentAccountFundId;
            var that = this;

            postJson('/asserts/createAccountFund', this.childAccountFund, function (successDate) {
                that.getAccountInfo();
                that.childAccountFundDialog = false;
            }, function (errorData) {
                that.errorMessage = errorData.responseJSON.body.errorMsg;
                that.showErrorMessage = true;
                that.childAccountFundDialog = true;

            })
        },
        newChildFundType:function () {
            var that =this;
            postJson('/asserts/createAccountFundType', this.childFundType, function (successData) {
                that.getAccountFundTypes();
            })
        },
        getAccountFundTypes:function () {
            var that = this;
            getJSON('/asserts/fundTypes',null, function (successDate) {
                that.childFundTypes = successDate.body;
                for (var childFundType of successDate.body) {
                    that.childFundTypeNames.push(childFundType.name);
                }
            })
        },
        childAccountFundDialogShow:function (parentAccountFund) {
            this.childAccountFundDialog = true;
            this.currentParentAccountFundId = parentAccountFund.id;
        },
        deleteAccountFund:function (accountFundId) {
            var that = this;
            getJSON('/asserts/deleteAccountFund', {accountFundId:accountFundId}, function (successDate) {
                that.getAccountInfo();
            })
        }
    }
})