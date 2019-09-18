

Vue.component('vue-gallery', {
    props: ['photo'],
    template: `
            <img v-if="photo != null" style="width:50%;margin: 5px" class="shadow"
                :src="photo.url">
            </img>
        
    `,
    mounted() {
        this.changePhoto(0)
        document.addEventListener("keydown", (event) => {
            if (event.which == 37)
                this.previousPhoto()
            if (event.which == 39)
                this.nextPhoto()
        })
    },
    methods: {
        changePhoto(index) {
            this.activePhoto = index

        },
        nextPhoto() {
            this.changePhoto(this.activePhoto + 1 < this.photos.length ? this.activePhoto + 1 : 0)
            var div = document.getElementsByClassName("thumbnails")[0]
            if (this.activePhoto == 0) {
                this.scroll(-20, div.scrollLeft);
            } else {
                var lastPhoto = this.activePhoto == 0 ? this.photos.length - 1 : this.activePhoto - 1;
                var img = document.getElementById(lastPhoto + "");
                this.scroll(1, img.offsetWidth)

            }
        },
        previousPhoto() {
            this.changePhoto(this.activePhoto - 1 >= 0 ? this.activePhoto - 1 : this.photos.length - 1)

            if (this.activePhoto == this.photos.length - 1) {
                var div = document.getElementsByClassName("thumbnails")[0]
                this.scroll(20, div.scrollWidth);

            } else {
                var lastPhoto = this.activePhoto == 0 ? this.photos.length - 1 : this.activePhoto - 1;
                this.scroll(-1, document.getElementById(lastPhoto + "").offsetWidth)
            }

        },
        scroll: function (speed, offset) {
            var timer;
            var i = Math.abs(speed);
            var _this = this;
            var div = document.getElementsByClassName("thumbnails")[0];
            timer = setInterval(function () {
                if (i <= offset) {
                    div.scrollLeft += speed;
                } else {
                    clearInterval(timer);
                }
                i += Math.abs(speed);

            }, 1);
        }
    }
})
Vue.component("third", {

    template: ' <P>sdsdsd</P>'

})
new Vue({
    el: '#parent',
    data: {
        photos: [],
        template: null,
        currentParentItem: null,
        parentItems: null,
        currentLocation: null,
    },
    mounted: function () {
        this.queryFileTemplate();
        this.initParentTitle();
    },
    methods: {
        initParentTitle: function () {
            var title = localStorage.getItem("title");
            title = JSON.parse(title);
            this.currentParentItem = title.currentParentItem;
            this.parentItems = title.parentItems;
            this.currentLocation = title.location;
        },
        queryFileTemplate: function () {
            var templateNum = this.getQueryVar("templateNum")
            var _this = this;
            axios.get("/template/fileTemplateDetails/" + templateNum).then(function (response) {
                _this.photos = response.data.body.images;
                _this.template = response.data.body;
            }).catch(function (error) {
            })
        },
        getQueryVar: function (variable) {
            var query = window.location.search.substring(1);
            var vars = query.split("&");
            for (var i = 0; i < vars.length; i++) {
                var pair = vars[i].split("=");
                if (pair[0] == variable) {
                    return pair[1];
                }
            }
            return (false);
        },
        thumbsUpClick: function (el) {
            if (!el.target.disabled) {
                if (!this.template.thumbsUp) {
                    this.template.thumbsUp = 0;
                }
                this.template.thumbsUp +=1;
                el.target.disabled = true;
                el.target.backgroundColor = '#d7d7d7'
                let param = {};
                param.thumbsUp = this.template.thumbsUp;
                param.fileNum = this.template.fileNum;
                this.update(param);
            }

        },
        downloadClick: function () {
            if (!this.template.downloadNums) {
                this.template.downloadNums = 0;
            }
            this.template.downloadNums +=1;
            var param = {};
            param.fileNum = this.template.fileNum;
            param.downloadNums = this.template.downloadNums;
            this.update(param);
        },
        update: function (paramObject) {
            var requestParam = "";
            for (param in paramObject) {
                requestParam = requestParam + param + "=" + paramObject[param] + "&";
            }
            requestParam = requestParam.substr(0, requestParam.length - 1);
            console.log(requestParam)
            axios.get("/template/update?" + requestParam);
        }
    }
});