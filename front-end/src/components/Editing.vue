<template>
    <div class="offset-3 col-6" data-aos="fade-up">
        <div class="col-12 cropper-container" style="height:1000px;">
            <img ref="image" :src="imgs[imgs.length-1].base64" crossorigin style="height:auto;">
            <!-- <img ref="image" src="../../public/theme/images/temp.jpg" crossorigin> -->
        </div>
        <div class="col-12">
            <img :src="destination" class="img-preview">
        </div>
        <div class="col-12">
        <div @click="rotatep45">P rotate 45</div>
        <div @click="rotatem45">M rotate 45</div>
        <div @click="rotatep90">P rotate 90</div>
        <div @click="rotatem90">M rotate 90</div>
        <div @click="vertical_scale">vertical scale</div>
        <div @click="horizontal_scale">horizontal scale</div>
        </div>
        <!-- <div style="margin-top:1%; margin-left:5%;margin-right:5%; height:50px;">
          <div v-if="prevpage=='addimage'" class="col-4 col-md-4 col-lg-4" style="display:inline-block;">
            <input type="button" value="이전" @click="goPrev" class="btn btn-primary btn-md text-white">
          </div>
          <div v-else class="col-4 col-md-4 col-lg-4" style="display:inline-block;">
            <input type="button" value="이전" @click="goReg" class="btn btn-primary btn-md text-white">
          </div>
          <div v-if="prevpage=='addimage'" class="col-4 col-md-4 col-lg-4" style="display:inline-block; text-align:center;">
            <input type="button" value="추가" @click="goAddImage" class="btn btn-info btn-md text-white">
          </div>
          <div v-if="prevpage=='addimage'" class="col-4 col-md-4 col-lg-4" style="display:inline-block; text-align:right;">
            <input type="button" value="다음" @click="goNext" class="btn btn-success btn-md text-white">
          </div>
           <div v-else-if="prevpage=='useredit'" class="col-4 col-md-4 col-lg-4" style="display:inline-block; text-align:right;">
            <input type="button" value="다음" @click="goNextEdit" class="btn btn-success btn-md text-white">
          </div>
          <div v-else class="offset-4 col-4 col-md-4 col-lg-4" style="display:inline-block; text-align:right;">
            <input type="button" value="다음" @click="goNextReg" class="btn btn-success btn-md text-white">
          </div>
        </div> -->
    </div>
</template>

<script>
    // import 'cropperjs/dist/cropper.css';
    // import 'cropperjs/dist/cropper.min.css';
    import Cropper from "../../public/theme/js/cropper.min.js";
    export default {
        name: "editing",
        props: ["imgs","prevpage","oldpw"],
        data() {
            return {
                cropper: {},
                destination: {},
                image: {},
                src:"../../public/theme/images/ai.jpg",
                scale_vertical:false,
                scale_horizontal:false,
            }
        },
        // props: {
        //     src: String
        // },
        methods:{
            goPrev(){
                this.step = 1;
                this.$router.push("/addimage");
            },

            rotatep45(){
                this.cropper.rotate(45);
            },
            rotatem45(){
                this.cropper.rotate(-45);
            },
            rotatep90(){
                this.cropper.rotate(90);
            },
            rotatem90(){
                this.cropper.rotate(-90);
            },
            vertical_scale(){
                if(!this.scale_vertical){
                    this.scale_vertical = !this.scale_vertical;
                    this.cropper.scaleY(-1);
                }
                else{
                    this.scale_vertical = !this.scale_vertical;
                    this.cropper.scaleY(1);
                }
            },
            horizontal_scale(){
                if(!this.scale_horizontal){
                    this.scale_horizontal = !this.scale_horizontal;
                    this.cropper.scaleX(-1);
                }
                else{
                    this.scale_horizontal = !this.scale_horizontal;
                    this.cropper.scaleX(1);
                }
            },
        },
        mounted() {
            this.image = this.$refs.image;
            this.cropper = new Cropper(this.image, {
                zoomable: true,
                scalable: true,
                aspectRatio: 4 / 3,
                rotatable:true,
                cropBoxResizable:false,
                dragMode:'move',
                autoCropArea: 1,
                minContainerHeight:1000,
                crop: () => {
                    const canvas = this.cropper.getCroppedCanvas();
                    this.destination = canvas.toDataURL("image/png");
                }
            });
        }
    }
</script>

<style scoped>
    .img-container {
        width: auto;
        height: auto;
        float: left;
    }
    .img-preview {
        width: 200px;
        height: 200px;
        float: left;
        margin-left: 10px;
    }
</style>