<template>
  <div id="app" class="yscroll site-wrap">
    <!-- <Sidebar v-if="loginCheck" />
    <LoginSide v-if="!loginCheck" /> -->
    <Sideex />
      <!-- 공지 -->
    
    <main class="main-content">
      <div v-if="loginCheck" class="top-noti" style="padding-right:0px; margin-right:0px; width:101%; background-color:black; margin-bottom:3%;">
        <!-- <div class="text-white offset-4 col-1" style="text-align:right; display:inline-block;">
          <span class="icon-notifications_active neon"></span>
          <img src="../public/theme/images/notice.png" style="width:28px;height:28px;" />
        </div> -->
        <div class="all-scroll offset-4 col-4" style="display:inline-block;">
          <div class="swiper-scrollbar"></div>
          <div class="swiper-container oflow-visible" style="padding-top:10px; background-color:black; height:50px;"
            data-slide-effect="slide" data-autoheight="false" data-swiper-speed="5000" data-swiper-margin="25"
            data-swiper-slides-per-view="1" data-swiper-wheel-control="true" data-swiper-breakpoints="true"
            data-scrollbar="true" data-swiper-loop="true" data-swiper-direction="vertical" data-swiper-autoplay="true"
            data-swpr-responsive="[1, 2, 1, 2]">
            <div class="swiper-wrapper" style="height:10px;">
              <div class="elip text-white swiper-slide"
                style="font-size:1.5em;font-family: hand_mail; background-color:black;" @click="notimodal(no)" v-for="no in noti"
                :key="no.index">
                {{no}}
              </div>
            </div>
          </div>
        </div>
        <div v-if="$store.state.isadmin == 'true'" class="col-1">
          <div>today</div>
          <div>{{visit}}</div>
        </div>
      </div>

      <!-- writecontent에서 삭제 안되서 추가한부분 -->
      <div class="write-scroll all-scroll pos-relative mt-50" style="display:none;">
          <h5 class="mb-50"><b>Image</b></h5>                                            
          <div class="swiper-scrollbar"></div>
          <div class="write-swiper swiper-container oflow-visible" data-slide-effect="flip" data-autoheight="false" data-wheel-control="true" 
                                  data-swiper-speed="200" data-swiper-margin="25" data-swiper-slides-per-view="1"
                                  data-swiper-breakpoints="true" data-scrollbar="true" data-swiper-loop="false"
                                  data-swpr-responsive="[1, 2, 1, 2]">
              <div class="swiper-wrapper">
                  <div class="img-fluid swiper-slide">
                      <div style="text-align:right; background-color:black;">
                      </div>
                  </div>
              </div>
          </div>
      </div>

      <!-- 공지 2 -->
      <!-- <MARQUEE onmouseover=this.stop() style="WIDTH: 100%; HEIGHT: 10px" onmouseout=this.start() scrollAmount=1 scrollmount=10 direction=up>
        <div style="PADDING-RIGHT: 0px; OVERFLOW-Y: auto; PADDING-LEFT: 0px; OVERFLOW-X: hidden; PADDING-BOTTOM: 3px; WIDTH: 100%; PADDING-TOP: 3px">
          <div class="neon" style="display:inline-block; background-color:black;" v-for="no in noti" :key="no.index">
                {{no}}
          </div>
        </div>
      </MARQUEE> -->
      <router-view :key="$route.fullPath"></router-view>
      <Footer />
    </main>
    <!-- modal -->
    <p id="modalBtn" style="display:none;" data-toggle="modal" data-target="#myModal"></p>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-body py-4" style="text-align: center;">
            {{$store.state.modalText}}
          </div>
          <div class="modal-footer py-2">
            <button type="button" id="modalClose-btn" class="btn btn-secondary text-white py-1 px-3" data-dismiss="modal">닫기</button>
          </div>
        </div>
      </div>
    </div>
    <vue-snotify></vue-snotify>
  </div>
</template>

<script>
  // import Sidebar from "@/components/Sidebar.vue"
  // import LoginSide from "@/components/LoginSide.vue"
  import Sideex from "@/components/Sideex.vue"
  import Footer from "@/components/Footer.vue"
  import store from "@/store.js"
  import http from "@/http-common.js"
  // import io from 'socket.io-client';
  // import $ from "jquery"
  export default {
    name: 'app',
    data() {
      return {
        noti: [],
        noticeError: "",
        visit:"",
      }
    },
    components: {
      // LoginSide,
      // Sidebar,
      Sideex,
      Footer,
    },
    methods: {
      getNotice() {
        http
          .get('/notice/noticeNow')
          .then((res) => {
            if (res.data.resmsg === "조회성공") {
              this.noticeError = ""
              const noticeTitle = []
              res.data.resValue.forEach(function (el) {
                noticeTitle.push(el.title)
              })
              this.noti = noticeTitle
            } else {
              this.noticeError = "공지사항이 없습니다."
            }
          })
      },
      notimodal(noti){
        this.$store.commit('setModalText', noti);
        document.getElementById('modalBtn').click();
      }
    },
    computed: {
      loginCheck: () => {
        return store.state.islogin;
      }
    },
    created() {
      // this.getNotice();
    },
    mounted() {
      this.getNotice();

      http
        .get("admin/todayvisit")
        .then(response => {
          if (response.data['resmsg'] == "조회성공") {
            this.visit = response.data['resvalue'];
          }
        })
    },
    updated() {
      //modal 뜰 때 body에 padding 붙는거 없애주는거
      document.body.style.paddingRight = 0;
      document.querySelector('script[src$="script.js"]').remove()
      document.querySelector('script[src$="swiper.js"]').remove()
      let recaptchaScript = document.createElement('script')
      recaptchaScript.setAttribute('type', "text/javascript")
      recaptchaScript.setAttribute('src', "./theme/js/swiper.js")
      document.body.appendChild(recaptchaScript)
      let recaptchaScripta = document.createElement('script')
      recaptchaScripta.setAttribute('type', "text/javascript")
      recaptchaScripta.setAttribute('src', "./theme/js/script.js")
      document.body.appendChild(recaptchaScripta)
    }
  }
</script>
<style>
  .snotifyToast__title {
    font-size: 17px;
  }
  body{
    padding-right:0px;
  }

  .snotifyToast__body {
    font-size: 12px;
  }

  .elip {
    text-overflow: ellipsis;
    white-space: nowrap;
    word-wrap: normal;
    width: 100%;
    overflow: hidden;
  }

  #myModal {
    z-index: 99999;
    padding-right:0px;
  }

  /* @media (min-width: 992px) {
    #myModal {
      padding-right: 0px;
      position: absolute;
      top: 50px;
    }
  }

  @media (max-width: 991.98px) {
    #myModal {
      top: 255px;
    }
  } */
</style>