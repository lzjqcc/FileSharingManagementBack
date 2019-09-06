
//$emit('enlarge-text')  必须是 enlarge-text

Vue.component("third", {
    props: ['post'],
    template: '<button type="button" class="btn btn-outline-secondary" v-on:click="$emit(\'enlarge-text\',post.id)" v-bind:value="post.id">{{post.name}}</button>'

})
// 要在父组件上面
Vue.component("file-template", {
    props: ['post'],
    template: '' +
    '<div style="margin-right: 2%;margin-top: 2%" >' +
    '<div class="card" style="width: 18rem;">\n' +
    '  <img  v-if=\'post.images\' v-bind:src="post.images[0].url" class="card-img-top" alt="...">\n' +
    '  <div class="card-body">\n' +
    '    <p class="card-text">{{post.name}}{{post.description}}</p>\n' +
    '  </div>\n' +
        '</div>' +
    '</div>'
})
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
        thirdChildItems:[
            {name: '节日模版', id: 10}
        ],
        fileTemplates:[
            {name:"简笔画创意自我介绍PPT",description:"小",images:[{url:"http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]},
            {name:"创意自我介绍PPT",description:"小dfd",images:[{url:"http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]}
        ]
    },
    mounted: function () {
        this.queryParentTitle();
        this.queryChildTitle(5)
        this.queryThirdTitle(1)
        //this.findAllByPage()
        //this.queryChildTitle()
    },
    computed: {

    },
    methods: {
        parentTitleClick: function (item) {
            this.queryChildTitle(item.id, 2)
        },
        childTitleClick: function (item) {
            this.queryThirdTitle(item.id)
            this.findAllByPage(item.id, 1, 30)
        },
        thirdTitleClick: function (item) {
            this.queryContents(item.id, 1, 30)
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
                //console.log(_this.items)
            }).catch(function (error) {
                _this.fileTemplates =  [{name:"简笔","description":"小sdsd","images":[{"url":"http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]}];
            })
        },
        findAllByPage: function (parentId, page, size) {
            var _this = this;
            axios.get("/template/findAllByPage/" + parentId + "?page=" + page + "&" + "size=" + size + "&sort=insertTime,desc").then(function (response) {
                _this.fileTemplates = response.data.body.content;
                //console.log(_this.items)
            }).catch(function (error) {
                _this.fileTemplates =  [{name:"简笔","description":"小sdsd","images":[{"url":"http://www.ypppt.com/uploads/allimg/181212/1-1Q2120T203.jpg"}]}];
            })
        }

    }
})



