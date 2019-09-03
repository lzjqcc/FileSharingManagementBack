Vue.component("PPT", {
    template: "<div>Home component</div>"
})
var parentTitle = new Vue({
    el: "#parentTitle",
    data: {
        currentTab:"PPT",
        items: [{
            name: "PPT", id: ""
        },{name: "Word",id:""}]
    },
    mounted: function () {
        this.queryParentTitle();

    },
    computed: {
        currentTabComponent: function () {
            console.log(this.currentTab)
            return  this.currentTab
        }


    },
    methods: {
        queryParentTitle: function () {
            var _this = this;
            axios.get("/template/parentGroups").then(function (response) {
                _this.items = response.data.body;
                //console.log(_this.items)
            }).catch(function (error) {

            })
        }

    }
})

