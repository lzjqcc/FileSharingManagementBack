
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
    template: '' +
    '<div style="margin-right: 2%;margin-top: 2%" class="shadow p-3 mb-5 bg-white rounded" >' +
    '<div class="card" style="width: 18rem;">\n' +
    '  <img  v-if=\'post.images\' v-bind:src="post.images[0].url" class="card-img-top" alt="...">\n' +
    '  <div class="card-body">\n' +
    '    <p class="card-text">{{post.name}}{{post.description}}</p>\n' +
    '  </div>\n' +
        '</div>' +
    '</div>'
})

Vue.component('paginate', VuejsPaginate);

var parentTitle = new Vue({
    el: "#parentTitle",
    data: {
        currentTab: "PPT",
        items: [{
            name: "PPT", id: 3
        }, {name: "Word", id: 4}],
        childItems: [
            {name: 'PPT模版', id: 5},
            {name: "PPT图表", id: 2},

        ],

        currentThirdItem:null,
        thirdShowNum: 10,
        ThirdShowMoreText: "更多",
        thirdChildItems:[
            {name: '节日模版', id: 10}
        ],
        fileTemplates:[
            {name:"简笔画创意自我介绍PPT",description:"小",images:[{url:"http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]},
            {name:"创意自我介绍PPT",description:"小dfd",images:[{url:"http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]}
        ],
        paginate:["fileTemplates"],
        defaultPageSize:20,
        totalElements: 20,
        totalPages:1,
        currentClassifyId:1,
        currentParentId:5,
        currentThirdItemId: -1,
        searchText:null

    },
    mounted: function () {
        this.queryParentTitle();
        this.queryChildTitle(5)
        this.queryThirdTitle(this.currentClassifyId)
        this.findAllByPage(this.currentClassifyId,0, this.defaultPageSize)
        //this.findAllByPage()
        //this.queryChildTitle()
    },
    computed: {

    },
    methods: {

        parentTitleClick: function (item) {
            this.queryChildTitle(item.id, 2)
            this.currentParentId = item.id;
        },
        thirdShowMoreClick: function () {
            if (this.ThirdShowMoreText == '更多') {
                this.thirdShowNum = 1000;
                this.ThirdShowMoreText = "收起"
                console.log('dd')
            }else if(this.ThirdShowMoreText == '收起') {
                this.thirdShowNum = 10;
                this.ThirdShowMoreText = "更多"
            }


        },
        childTitleClick: function (item) {
            this.queryThirdTitle(item.id)
            this.findAllByPage(item.id, 0, this.defaultPageSize)
            this.currentClassifyId = item.id;
        },
        thirdTitleClick: function (item) {
            this.queryContents(item.id, 0, this.defaultPageSize);
            this.currentThirdItem = item;
            this.currentThirdItemId = item.id;
        },
        showAllClick: function () {
          this.findAllByPage(this.currentClassifyId, 0, this.defaultPageSize);
          this.currentThirdItem == null;
          this.currentThirdItemId = -1;
        },
        queryParentTitle: function () {
            var _this = this;
            axios.get("/template/parentGroups").then(function (response) {
                _this.items = response.data.body;
                //console.log(_this.items)
            }).catch(function (error) {

            })
        },
        queryChildTitle: function (parentId) {
            var _this = this;
            axios.get("/template/childGroups/" + parentId).then(function (response) {
                _this.childItems = response.data.body;
                //console.log(_this.items)
            }).catch(function (error) {
                _this.childItems = null;
            })
        },
        queryThirdTitle: function (parentId) {
            var _this = this;
            axios.get("/template/childGroups/" + parentId).then(function (response) {
                _this.thirdChildItems = response.data.body;
                //console.log(_this.items)
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
                _this.fileTemplates =  [{name:"简笔","description":"小sdsd","images":[{"url":"http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]}];
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
                _this.fileTemplates =  [{name:"简笔","description":"小sdsd","images":[{"url":"http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]}];
            });
            this.currentThirdItem = null;
            this.currentThirdItemId = -1
        },
        pageClick: function (pageNum) {
            if (this.searchText != null && this.searchText != '') {
                this.textQuery(pageNum-1);
                return;
            }
            if (this.currentThirdItem == null) {
                this.findAllByPage(this.currentClassifyId, pageNum-1, this.defaultPageSize);
            }else if (this.currentThirdItem != null){
                this.queryContents(this.currentThirdItem.id, pageNum - 1, this.defaultPageSize);
            }
        },
        textQuery: function (page) {
            if (this.searchText == null || this.searchText == '') {
                if (this.currentThirdItemId != -1) {
                    this.queryContents(this.currentThirdItemId, 0, this.defaultPageSize);
                }else {
                    this.findAllByPage(this.currentClassifyId, 0, this.defaultPageSize);
                }
                return;
            }
            var _this = this;
            var parentId = this.currentThirdItemId;
            if (parentId == -1) {
                parentId = this.currentClassifyId;
            }
            console.log(this.currentThirdItemId)
            axios.get("/template/textQuery/" + parentId + "?page=" + page + "&" + "size=" + this.defaultPageSize + "&text="+this.searchText+"&sort=insertTime,desc").then(function (response) {
                _this.fileTemplates = response.data.body.content;
                _this.totalPages = response.data.body.totalPages;
                console.log(_this.totalPages)
                //console.log(_this.items)
            }).catch(function (error) {
                _this.fileTemplates =  [{name:"简笔","description":"小sdsd","images":[{"url":"http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]}];
            });

        }

    }
})
