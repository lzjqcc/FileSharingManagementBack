Vue.component('vue-gallery', {
    props: ['photos'],
    data: function () {
        return {
            activePhoto: null
        }
    },
    template:  `
    <div class="vueGallery">
    <div class="activePhoto" :style="'background-image: url('+photos[activePhoto]+');'">
      <button type="button" aria-label="Previous Photo" class="previous" @click="previousPhoto()">
        <i class="fas fa-chevron-circle-left"></i>
      </button>
      <button type="button" aria-label="Next Photo" class="next" @click="nextPhoto()">
        <i class="fas fa-chevron-circle-right"></i>
      </button>
    </div>
    <div class="thumbnails">
      <div
           v-for="(photo, index) in photos"
           :src="photo"
           :key="index"
           @click="changePhoto(index)"
           :class="{'active': activePhoto == index}" :style="'background-image: url('+photo+')'">
      </div>
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
            'https://www.moyublog.com/code/5d2eb3e108d3d/img/lordea-home-01-min.jpg',
            'https://www.moyublog.com/code/5d2eb3e108d3d/img/lordea-home-02-min.jpg',
            'https://www.moyublog.com/code/5d2eb3e108d3d/img/lordea-home-03-min.jpg',
            'https://www.moyublog.com/code/5d2eb3e108d3d/img/lordea-home-04-min.jpg',
            'https://www.moyublog.com/code/5d2eb3e108d3d/img/lordea-home-05-min.jpg',
            'https://www.moyublog.com/code/5d2eb3e108d3d/img/lordea-home-06-min.jpg',
            'https://www.moyublog.com/code/5d2eb3e108d3d/img/lordea-home-07-min.jpg',
            'https://www.moyublog.com/code/5d2eb3e108d3d/img/lordea-home-08-min.jpg'
        ]
    }
});