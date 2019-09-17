//$emit('enlarge-text')  必须是 enlarge-text

Vue.component("third", {
    props: ['post'],

    template: ' <li  class="nav-item">\n' +
    '    <a  class="nav-link"  href="#" v-on:click="$emit(\'enlarge-text\',post.id)" >{{post.name}}</a>\n' +
    '    </li>'

})
// 要在父组件上面
Vue.component("file-template", {
    props: ['post'],
    template: `
    <div style="margin-right: 2%;margin-top: 2%" class="shadow p-3 mb-5 bg-white rounded"  v-on:click="$emit('enlarge-text',post.id)" > 
    <div class="card" style="width: 18rem;">
      <img  v-if=post.images v-bind:src="post.images[0].url" class="card-img-top" alt="..."> 
      <div class="card-body"> 
        <p class="card-text">{{post.name}}</p> 
        <div style="display: flex;justify-content: space-around">
        <div style="display: flex;justify-content: center">
        <img src="../../img/download.png" style="width: 30%">
        <span style="font-size: x-small">{{post.downloadNums}}</span>
        </div>
        <div style="display: flex;justify-content: center">
        <img src="../../img/thumbsUp.png" style="width: 30%">
        <span>{{post.thumbsUp}}</span>
        </div>
        <div style="display: flex;justify-content: center">
        <img src="../../img/view.png" style="width: 30%">
        <span>{{post.viewNums}}</span>
        </div>
        </div>
      </div> 
        </div> 
    </div>
    `
})

Vue.component('paginate', VuejsPaginate);


var parentTitle = new Vue({
    el: "#parentTitle",
    data: {
        items: [{
            name: "PPT", id: 3
        }, {name: "Word", id: 4}],
        childItems: [
            {name: 'PPT模版', id: 5},
            {name: "PPT图表", id: 2},

        ],

        currentThirdItem: null,
        thirdShowNum: 10,
        ThirdShowMoreText: "更多",
        thirdChildItems: [
            {name: '节日模版', id: 10}
        ],
        fileTemplates: [
            {
                name: "简笔画创意自我介绍PPT",
                description: "小",
                images: [{url: "http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]
            },
            {
                name: "创意自我介绍PPT",
                description: "小dfd",
                images: [{url: "http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]
            }
        ],
        paginate: ["fileTemplates"],
        defaultPageSize: 20,
        totalElements: 20,
        totalPages: 1,
        currentClassifyId: 1,
        currentParentItem: null,
        searchText: null,
        showTemplateDetails: null,
        currentChildItem: null
    },
    mounted: function () {
        this.queryParentTitle();
        this.queryChildTitle(5)
        this.queryThirdTitle(this.currentClassifyId)
        this.findAllByPage(this.currentClassifyId, 0, this.defaultPageSize)
        //this.findAllByPage()
        //this.queryChildTitle()
    },
    computed: {},
    methods: {
        fileTemplateClick: function (template) {
            let thirdName = this.currentThirdItem == null ?  '全部' : this.currentThirdItem.name ;
            let location = this.currentParentItem.name + '->' + this.currentChildItem.name + '->' + thirdName;
            let storeInfo = {};
            storeInfo.currentParentItem = this.currentParentItem;
            storeInfo.parentItems = this.items;
            storeInfo.location = location;
            localStorage.setItem("title", JSON.stringify(storeInfo))
            window.open(window.location.origin + "/file/details.html?templateNum=" + template.fileNum);


        },
        parentTitleClick: function (item) {
            this.queryChildTitle(item.id, 2)
            this.currentParentItem = item;
        },
        thirdShowMoreClick: function () {
            if (this.ThirdShowMoreText == '更多') {
                this.thirdShowNum = 1000;
                this.ThirdShowMoreText = "收起"
                console.log('dd')
            } else if (this.ThirdShowMoreText == '收起') {
                this.thirdShowNum = 10;
                this.ThirdShowMoreText = "更多"
            }


        },
        childTitleClick: function (item) {
            this.queryThirdTitle(item.id)
            this.findAllByPage(item.id, 0, this.defaultPageSize)
            this.currentClassifyId = item.id;
            this.currentChildItem = item;
        },
        thirdTitleClick: function (item) {
            this.queryContents(item.id, 0, this.defaultPageSize);
            this.currentThirdItem = item;
        },
        showAllClick: function () {
            this.findAllByPage(this.currentClassifyId, 0, this.defaultPageSize);
            this.currentThirdItem == null;
        },
        queryParentTitle: function () {
            var _this = this;
            axios.get("/template/parentGroups").then(function (response) {
                _this.items = response.data.body;
                _this.currentParentItem = response.data.body[0];

            }).catch(function (error) {

            })
        },
        queryChildTitle: function (parentId) {
            var _this = this;
            axios.get("/template/childGroups/" + parentId).then(function (response) {
                _this.childItems = response.data.body;
                _this.currentChildItem = _this.childItems[0];
                //console.log(_this.items)
            }).catch(function (error) {
                _this.childItems = null;
            })
        },
        queryThirdTitle: function (parentId) {
            var _this = this;
            axios.get("/template/childGroups/" + parentId).then(function (response) {
                _this.thirdChildItems = response.data.body;
            }).catch(function (error) {
                _this.childItems = null;
            })
        },
        queryContents: function (childGroupId, page, size) {
            var _this = this;
            axios.get("/template/fileTemplatesByPage/" + childGroupId + "?page=" + page + "&" + "size=" + size + "&sort=insertTime,desc").then(function (response) {
                _this.fileTemplates = response.data.body.content;
                _this.totalPages = response.data.body.totalPages;

                //_this.count = response.data.body.
                //console.log(_this.items)
            }).catch(function (error) {
                _this.fileTemplates = [{
                    name: "简笔",
                    "description": "小sdsd",
                    "images": [{"url": "http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]
                }];
            })
        },
        findAllByPage: function (parentId, page, size) {
            var _this = this;
            axios.get("/template/findAllByPage/" + parentId + "?page=" + page + "&" + "size=" + size + "&sort=insertTime,desc").then(function (response) {
                _this.fileTemplates = response.data.body.content;
                _this.totalPages = response.data.body.totalPages;
                console.log(_this.totalPages)
                //console.log(_this.items)
            }).catch(function (error) {
                _this.fileTemplates = [{
                    name: "简笔",
                    "description": "小sdsd",
                    "images": [{"url": "http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]
                }];
            });
            this.currentThirdItem = null;
        },
        pageClick: function (pageNum) {
            if (this.searchText != null && this.searchText != '') {
                this.textQuery(pageNum - 1);
                return;
            }
            if (this.currentThirdItem == null) {
                this.findAllByPage(this.currentClassifyId, pageNum - 1, this.defaultPageSize);
            } else if (this.currentThirdItem != null) {
                this.queryContents(this.currentThirdItem.id, pageNum - 1, this.defaultPageSize);
            }
        },
        textQuery: function (page) {
            if (this.searchText == null || this.searchText == '') {
                if (this.currentThirdItem != null) {
                    this.queryContents(this.currentThirdItem.id, 0, this.defaultPageSize);
                } else {
                    this.findAllByPage(this.currentClassifyId, 0, this.defaultPageSize);
                }
                return;
            }
            var _this = this;
            var parentId = null;
            if (this.currentThirdItem == null) {
                parentId = this.currentClassifyId;
            }else {
                parentId = this.currentThirdItem.id;
            }
            axios.get("/template/textQuery/" + parentId + "?page=" + page + "&" + "size=" + this.defaultPageSize + "&text=" + this.searchText + "&sort=insertTime,desc").then(function (response) {
                _this.fileTemplates = response.data.body.content;
                _this.totalPages = response.data.body.totalPages;
            }).catch(function (error) {
                _this.fileTemplates = [{
                    name: "简笔",
                    "description": "小sdsd",
                    "images": [{"url": "http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]
                }];
            });

        }

    }
})
