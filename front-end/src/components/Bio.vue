<template>
    <div class="container-fluid photos">
      <div class="row justify-content-center">
        <div class="col-10" data-aos="fade-up">
          <div class="d-block photo-item">
            <div class="postcard" style="height:auto;">
              <div class="content" style="height:auto;">
                <!-- 우표 -->
                <div>
                  <div class="stamp-cover" style="background:black; height:52px; width:52px; z-index:3;">
                    <div class="stamp" style=" margin:1px; float:right; background-color:white; height:50px; width:50px;">
                    </div>
                  </div>
                  <img :src="items.profile_url" :class="items.profile_filter" style="width:37px;height:37px; z-index:4;" class="stamp-img"/>
                  <img src="../../public/theme/images/stamp1.png" style="width:45px;height:45px; z-index:5;" alt="Postage mark" class="postmark">
                  <!-- 끝 -->
                  <div class="col-12 col-lg-6" style="display:inline-block; vertical-align:middle; padding-top:0px; margin-top:0px; padding-bottom:50px">
                    <div class="mail-title offset-1 col-9" style="text-align:left;"><p style="color:black; font-size:2em; font-family: loveson;">Dear {{uid}}</p></div>
                    <div class="mail-message offset-2 col-8" style="color:black; font-family: loveson; word-break:break-all;text-align:left;">{{items.content_val}}</div>
                    <div class="col-11 col-offset-1" style="color:black; font-family: loveson; word-break:break-all; text-align:right;">from {{items.user_id}}</div>
                  </div>
                  <div class="col-12 col-lg-6" style="display:inline-block; vertical-align:middle; z-index:0; padding-top:15px; padding-bottom:15px;">
                    <div class="all-scroll pos-relative mt-50">
                      <div class="swiper-scrollbar"></div>
                        <div class="swiper-container oflow-visible" data-slide-effect="slide" data-autoheight="false"  data-swiper-wheel-control="true"
                                            data-swiper-speed="8000" data-swiper-margin="25" data-swiper-slides-per-view="1"
                                            data-swiper-breakpoints="true" data-swiper-autoplay="true" data-scrollbar="true"
                                            data-swiper-loop="true" data-swpr-responsive="[1, 2, 1, 2]">
                          <div class="swiper-wrapper">
                              <div class="swiper-slide" style="" v-for="img in items.imageList" :key="img.index">
                                <a :href="img.image_url" :class="img.filter" data-fancybox="gallery">
                                  <div :class="img.filter" style="width:100%; height:100%;">
                                      <img :src="img.image_url" style="width:100%; height:100%; margin-bottom:0px" alt="Image"/>
                                  </div>
                                </a>
                              </div>
                          </div>
                        </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
import $ from "jquery"
import http from '../http-common';
import store from '../store'
export default {
  props: ['cid'],
  data () {
    return {
      bell: this.iconbell,
      contentId: "",
      items: {},
      uid:"",
    }
  },
  methods: {
    clickBell() {
      if (this.bell === true) {
        this.bell = false;
      } else {
        this.bell = true;
        this.$store.commit('setModalText', '신고가 접수되었습니다.');
        document.getElementById('modalBtn').click();
      }
    },
    getData() {
      this.contentId = this.cid
      http
        .get('/content/detail/' + this.cid)
        .then((res) => {
          if (res.data.resmsg == "게시물 출력 성공") {
            this.items = res.data.resValue;
            if(this.items.profile_filter==null){
              this.items.profile_filter="normal";
            }
          }
        })
    },
  },
  created() {
    this.getData()
  },
  mounted() {
    $('html').scrollTop(0);
    this.uid = store.state.user_id
    
  },
  updated(){
    let recaptchaScripta = document.createElement('script')
    recaptchaScripta.setAttribute('type',"text/javascript")
    recaptchaScripta.setAttribute('src', "./theme/js/script.js")
    document.body.appendChild(recaptchaScripta)
    let recaptchaScriptb = document.createElement('script')
    recaptchaScriptb.setAttribute('type',"text/javascript")
    recaptchaScriptb.setAttribute('src', "./theme/js/swiper.js")
    document.body.appendChild(recaptchaScriptb)
  }
}
</script>

<style>
  .size {
    font-size: 2em;
  }
</style>

<style>
  .fancy-box-mask {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: #333;
    z-index: 22;
  }
  .fancy-box-image-wrapper {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    z-index: 33;
  }
  .fancy-box-image-wrapper div {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    transform-origin: left top 0px;
    transition: transform .3s cubic-bezier(0.4, 0, 0.22, 1);
  }
  .fancy-box-image-wrapper img {
    max-width: 100%;
    max-height: 100%;
  }
  .fancy-enter, .fancy-leave-active{
    opacity: 0;
  }
  .fancy-enter-active, .fancy-leave-active{
    transition: .2s;
  }

  .mail-message {
    margin-top: 70px;
    margin-bottom: 70px;
    font-size: 23px;
  }

  @media screen and (max-width: 991.98px) {
    .mail-message {
      margin-top: 30px;
      margin-bottom: 30px;
      /* font-size: 20; */
    }
  }
</style>