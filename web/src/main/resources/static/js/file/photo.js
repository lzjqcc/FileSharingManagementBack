Vue.component('vue-gallery', {
    props: ['photos'],
    data: function () {
        return {
            activePhoto: null
        }
    },
    template:  `
    <div class="vueGallery">
    <div v-show="photos.length > 0" class="activePhoto" :style="'background-image: url('+photos[activePhoto].url+');'">
      <button type="button" aria-label="Previous Photo" class="previous" @click="previousPhoto()">
        <i class="fas fa-chevron-circle-left"></i>
      </button>
      <button type="button" aria-label="Next Photo" class="next" @click="nextPhoto()">
        <i class="fas fa-chevron-circle-right"></i>
      </button>
    </div>
    <div class="thumbnails" v-show="photos.length > 0">
      <img style="height: 100px"
           v-for="(photo, index) in photos"
           :key="index"
           @click="changePhoto(index)"
           :class="{'active': activePhoto == index}" :src="photo.url">
      </img>
      
    </div>
  </div>`,
    mounted() {
        this.changePhoto(0)
        document.addEventListener("keydown", (event) => {
            if(event.which == 37)
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
            var currentPosition,timer;
            var speed=1;
            var i=1;
            timer=setInterval(function(){
                i++;
                var img = document.getElementsByClassName('thumbnails')[0];
                console.log(img.wi)

                if(i <=img.width){
                    img.scrollLeft +=speed;
                }else{
                    clearInterval(timer);
                }
            },1);
        },
        previousPhoto() {
            this.changePhoto(this.activePhoto - 1 >= 0 ? this.activePhoto - 1 : this.photos.length - 1)
        }
    }
})
Vue.component("third", {

    template: ' <P>sdsdsd</P>'

})
new Vue({
    el: '#app',
    data: {
        photos: [

        ]
    },
    mounted: function(){
      this.queryFileTemplate();
    },
    methods:{
        queryFileTemplate: function () {
            var templateNum = this.getQueryVar("templateNum")
            var _this = this;
            axios.get("/template/fileTemplateDetails/" + templateNum).then(function (response) {
                _this.photos = response.data.body.images;
                console.log(_this.photos)
                //console.log(_this.items)
            }).catch(function (error) {
            })
        },
        getQueryVar: function (variable) {
            var query = window.location.search.substring(1);
            var vars = query.split("&");
            for (var i=0;i<vars.length;i++) {
                var pair = vars[i].split("=");
                if(pair[0] == variable){return pair[1];}
            }
            return(false);
        }
    }
});