<template>
  <div class="offset-3 col-6" data-aos="fade-up">
    <div class="row mb-5" data-aos="fade-up">
        <div class="col-12">
          <h2 class="text-white mb-4">Add Image</h2>
        </div>
    </div>
    <div class="col-12">
    <div class="selected-image"  @click="$refs.fileInput.click()" style="margin-bottom:0px; border:1px solid white;" @dragover.prevent @dragenter.prevent @drop.prevent="dragupload" v-on:change="fileUpload">
      <!-- <div style="height:35%"></div> -->
      <!-- <div style="margin:auto; width:20%; height:35%; background-size:contain; background-repeat:no-repeat; background-image:url('./theme/images/plus.png')"> -->
      <div class="text-center text-primary" style="background-repeat:no-repeat; position: relative; top: 45%;">
      이 곳을 클릭하여 사진을 추가하세요.
      </div>
      <!-- <span>이미지를 drag&drop하거나 +를 클릭하여 추가해주세요.</span> -->
    </div>
    <div style="margin-top:3%;margin-left:5%;margin-right:5%; height:50px;">
    </div>
      <div class="container col-md-12 px-0">
        <div class="btn-group col-12 px-0" role="group" aria-label="Basic example">
          <input type="button" class="btn btn-outline-light col-sm btnprev p-2" value="취소" @click="goPrev">
          <button type="button" class="btn btn-outline-light col-sm btnnext p-2" v-if="prevpage == 'addimage'" @click="goNext">다음</button>
          <button type="button" class="btn btn-outline-light col-sm btnprev p-2" v-else-if="prevpage=='contentupdate'" @click="goNextUpdate">다음</button>
        </div>
      </div>
    <input ref="fileInput" type="file" accept="image/*" style="display:none;" name="file" id="file" class="inputfile" @dragover.prevent @dragenter.prevent @drop.prevent="dragupload" v-on:change="fileUpload"/>
    </div>
  </div>
</template>

<script>
import $ from "jquery"
export default {
  name: "App",
  props:["fimgs","prevpage","items", "ftags"],
  data() {
    return {
      step: 1,
      filterType: "",
      image: "",
      imginfo:{
        base64:"",
        filter:""
      },
      imgs:[],
      tags:[],
      caption: "",
      first:true,
      defaultImag:{base64:"",filter:"normal"},
      prev:"",
    };
  },
  created() {
    // EventBus.$on("selectFilter", evt => {
    //   this.filterType = evt.filter;
    // });
  },
  methods: {
    fileUpload(e) {
      const files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.image = files[0];
      this.createImage();
    },
    dragupload(e){
      const files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.image = files[0];
      this.createImage();
    },
    createImage() {
      const reader = new FileReader();
      reader.readAsDataURL(this.image);
      reader.onload = e => {
        this.image = e.target.result;
        this.imginfo.base64 = this.image;
        this.imgs.push(this.imginfo);
        this.tags.push([]);
        this.step = 2;
        // EventBus.$emit("imglink", { image: this.image });
        if(this.prevpage==undefined){
          this.prev = "addimage";
          this.$router.push({
            name: 'editing',
            params:{
              imgs: this.imgs,
              tags: this.tags,
              step: this.step+1,
              prevpage: this.prev,
            }
          })
        }else{
          this.prev = this.prevpage;
          this.$router.push({
            name: 'editing',
            params:{
              imgs: this.imgs,
              tags: this.tags,
              step: this.step+1,
              prevpage: this.prev,
              items: this.items,
            }
          })
        }
        // this.$router.push({
        //   name: 'imagefilter', 
        //   params: {
        //     imgs: this.imgs, 
        //     step: this.step+1,
        //     prevpage: "addimage",
        //   }
        // })
      };
    },
    goToHome() {
      this.image = "";
      this.caption = "";
      this.filterType = "normal";
      this.step = 1;
    },
    goPrev() {
      this.$router.go(-1);
    },
    goNext() {
      if(this.imgs.length==0){
        this.imgs.push(this.defaultImag);
        this.tags.push([]);
      }
      this.$router.push({
          name: 'writecontent', 
          params: {
            imgs: this.imgs, 
            tags: this.tags,
          }
        })
    },
    goNextUpdate() {
      window.console.log(this.prevpage)
      this.imgs[this.imgs.length-1].filter = this.filterType;
      this.$router.push({
        name: 'updatecontent', 
        params: {
          uimgs: this.imgs, 
          items: this.items,
          prevpage: this.prevpage,
        }
      });
    },
  },
  mounted(){
    if(this.fimgs!=undefined){
      this.imgs = this.fimgs;
      if(this.ftags!=undefined){
        this.tags = this.ftags;
      }
      this.first = false;
    }
    $('html').scrollTop(0);
  }
};
</script>

<style scoped>
.selected-image{
    height:30vw;
    background-size: cover;
}
</style>