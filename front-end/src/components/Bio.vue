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
                    <div class="detail-mail-message mail-message offset-2 col-8" style="color:black; font-family: loveson; word-break:break-all;text-align:left;">{{items.content_val}}</div>
                    <div class="col-11 col-offset-1" style="color:black; font-family: loveson; word-break:break-all; vertical-align:bottom; text-align:right;">from {{items.user_id}}</div>
                    <div class="col-12 row" style="margin:0; color:black; vertical-align:bottom; text-align:left;">
                      <div style="font-size:1.5em; font-family: loveson; word-break:break-all; " class="col-3">PS .</div>
                      <div class="col-6"><input type="text" class="text-black form-control" v-model="comment_val" /></div>
                      <div class="col-3"><input type="button" @click="insertComment" class="btn btn-block btn-outline-info" value="Send" /></div>
                    </div>
                    <div v-if="!isComment">
                      <div class="row" style="text-align:left;" v-for="(comment, index) in tempComments" :key="index">
                        <div class="col-1"></div>
                        <div class="col-3" style="display:inline-block; font-weight:bold; font-size:1.2em; font-family: loveson; word-break:break-all; vertical-align:top;">{{comment.user_id}}</div>
                        <!-- <div class="col-1"></div> -->
                        <div class="comment-val col-7" style="display:inline-block; padding-left:5px; font-size:1.2em; font-family: loveson; word-break:break-all; vertical-align:bottom;">{{comment.comment_val}}</div>
                      </div>
                      <div style="padding-top:20px; text-align:center;">
                        <svg @click="isComment = true;" style="cursor:pointer;" xmlns="http://www.w3.org/2000/svg" height="25" viewBox="0 0 512 512" width="25" class=""><g><path d="m437.019531 74.980469c-48.351562-48.351563-112.640625-74.980469-181.019531-74.980469s-132.667969 26.628906-181.019531 74.980469c-48.351563 48.351562-74.980469 112.640625-74.980469 181.019531s26.628906 132.667969 74.980469 181.019531c48.351562 48.351563 112.640625 74.980469 181.019531 74.980469s132.667969-26.628906 181.019531-74.980469c48.351563-48.351562 74.980469-112.640625 74.980469-181.019531s-26.628906-132.667969-74.980469-181.019531zm-181.019531 397.019531c-119.101562 0-216-96.898438-216-216s96.898438-216 216-216 216 96.898438 216 216-96.898438 216-216 216zm20-236.019531h90v40h-90v90h-40v-90h-90v-40h90v-90h40zm0 0" data-original="#000000" class="active-path" data-old_color="#000000" fill="#5EB2FD"/></g> </svg>
                      </div>
                    </div>
                    <div v-if="isComment">
                      <div class="row" style="text-align:left;" v-for="(comment, index) in comments" :key="index">
                        <div class="col-2"></div>
                        <div class="col-2" style="display:inline-block; font-weight:bold; font-size:1.2em; font-family: loveson; word-break:break-all; vertical-align:top;">{{comment.user_id}}</div>
                        <!-- <div class="col-1"></div> -->
                        <div class="comment-val col-7" style="display:inline-block; padding-left:5px; font-size:1.2em; font-family: loveson; word-break:break-all; vertical-align:bottom;">{{comment.comment_val}}</div>
                      </div>
                    </div>
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
      isComment:false,
      comment_val:"",
      comments:[
        {
          user_id:'kimbh',
          comment_val:'testtttttttttttttttttttttttttttttttttttttttt'
        },
        {
          user_id:'kimbh',
          comment_val:'test2'
        },
        {
          user_id:'kimbh',
          comment_val:'test3'
        },
        {
          user_id:'kimbh',
          comment_val:'test3'
        },
        {
          user_id:'kimbh',
          comment_val:'test3'
        },
        {
          user_id:'kimbh',
          comment_val:'test3'
        },
        {
          user_id:'kimbh',
          comment_val:'test3'
        },
        {
          user_id:'kimbh',
          comment_val:'test3'
        },
        {
          user_id:'kimbh',
          comment_val:'test3'
        },
      ],
      tempComments:[],
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
        });
      
      if(this.comments.length != 0) {
        this.comments.some(comment => {
          if(this.tempComments.length >= 2){
            return true;
          }
          this.tempComments.push(comment);
        })
      }
    },
    fancy(){
      $.fancybox.open([
          {
              href : 'http://fancyapps.com/fancybox/demo/1_b.jpg',                
              title : '1st title'
          },
          {
              href : 'http://fancyapps.com/fancybox/demo/2_b.jpg',                
              title : '2nd title'
          },
          {
              href : 'http://fancyapps.com/fancybox/demo/3_b.jpg',                
              title : '3rd title'
          }
      ], {
          padding : 0
      });
      
      return false;
    },
    insertComment(){
      window.console.log(this.comment_val);
    }
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

  .detail-mail-message {
    margin-top: 70px;
    margin-bottom: 70px;
    font-size: 23px;
  }

  @media screen and (max-width: 991.98px) {
    .detail-mail-message {
      margin-top: 30px;
      margin-bottom: 30px;
      /* font-size: 20; */
    }
  }

  /* .comment-val {
    line-height: 1.3em;
  } */
</style>