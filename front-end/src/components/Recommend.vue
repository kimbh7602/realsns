<template>
    <div class="container-fluid photos">
      <div class="row mb-5">
          <div class="col-12">
            <h2 class="text-white mb-4">Recommend List</h2>
          </div>
      </div>
      <div v-if="!isLoading&&(contents == null || contents.length == 0)" style="text-align:center;">
                    검색 결과가 없습니다.
                </div>
                <div style="text-align:center;" v-if="isLoading&&(contents == null || contents.length == 0)">
                    <img class="col-md-12" src="/theme/images/loading10.gif" />
                </div>
      <div v-else class="row align-items-stretch">
        <!-- 게시물 하나 -->
        <div class="col-6 col-md-6 col-lg-4" data-aos="fade-up" style="padding: 10px 10px" v-for="con in contents" :key="con.id">
          <div class="d-block photo-item content-div">
            <!-- 이미지 처리 -->
            <div class="polaroid" v-if="con.dislike < 5 && !reportMyList.includes(con.contentId) || readContents.includes(con.contentId)">
              <div :class="con.images[0].filter" style="width:100%; height:100%">
                <img :src="con.images[0].imageUrl" alt="Image" class="img-fluid pa m-0" style="box-shadow: 3px 3px 3px;"/>
              </div>
            </div>
            <div class="polaroid" v-show="con.dislike > 4 && !readContents.includes(con.contentId) && !reportMyList.includes(con.contentId)">
              <div style="width:100%; height:100%">
                <img :src="con.images[0].imageUrl" alt="Image" class="img-fluid pa blur m-0" style="box-shadow: 3px 3px 3px;"/>
                <p class="centertext text-white">신고가 누적된 게시물입니다.</p>
                <button class="centerbutton btn btn-primary btn-sm" @click="readReCon(con.contentId)">보기</button>
              </div>
            </div>
            <div class="polaroid" v-show="reportMyList.includes(con.contentId) && !readContents.includes(con.contentId)">
              <div style="width:100%; height:100%">
                <img :src="con.images[0].imageUrl" alt="Image" class="img-fluid pa blur m-0"/>
                <div class="centertext">
                  <p class="text-white">내가 신고한 게시물입니다.</p>
                  <button class="btn btn-outline-light btn-sm py-0" @click="readReCon(con.contentId)">보기</button>
                </div>
              </div>
            </div>
            <!-- 마우스 오버 했을 때 -->
            <div class="photo-text-more" v-if="con.dislike < 5 && !reportMyList.includes(con.contentId) || readContents.includes(con.contentId)">
              <div class="">
                <div class="d-block photo-item">
                  <div class="postcard">
                    <div class="content">
                      <!-- 누르면 상세 페이지로 -->
                      <div v-on:click="goDetail(con.contentId)">
                        <!-- 우표 -->
                        <div class="stamp-cover">
                          <div class="stamp" style=" margin:1px; float:right; background-color:white; height:50px; width:50px;">
                          </div>
                        </div>
                        <div class="stamp-img" style="top:25px;right:25px;height:43px;width:43px;background-color:white;"></div>
                        <div style="width:37px;height:37px" class="stamp-img" :class="con.profileFilter">
                          <img :src="con.profileUrl" style="width:37px;height:37px; background: none;" />
                        </div>
                        <img src="../../public/theme/images/stamp1.png" style="width:45px;height:45px;" alt="Postage mark" class="postmark">
                        <!-- 우편 내용 -->
                        <div class="mail-title offset-1 col-9 mt-2 ml-3 pb-" style="text-align:left;"><p class="mail-title-val">Dear {{ loginId }}</p></div>
                        <div class="mail-message offset-2 pt-0 pb-0 col-8 ellipsis mail-message-val" v-html="con.contentValue">{{ con.contentValue }}</div>
                        <div class="col-11 col-offset-1 pt-0 pr-0 mail-from-val">from {{ con.userId }}</div>
                      </div>
                      <!-- buttons -->
                      <div class="mb-4 pb-2 d-flex justify-content-around size content-button" style="font-size:1vw">
                        <div @click="clickHeart(con.contentId)">
                          <i class="icon-heart" v-if="con.likeButton"></i>
                          <i class="icon-heart-o" v-else></i>
                        </div>
                        <div @click="clickFollow(con.userId)" v-if="con.userId !== loginId">
                          <i class="icon-check" v-if="followList.includes(con.userId)">{{con.userId}}</i>
                          <i class="icon-user-plus" v-else>{{con.userId}}</i>
                        </div>
                        <div @click="clickScrap(con.contentId, con.scrapButton)" v-if="con.userId !== loginId">
                          <i class="icon-bookmark" v-if="con.scrapButton"></i>
                          <i class="icon-bookmark-o" v-else></i>
                        </div>
                        <div v-if="con.userId !== loginId">
                          <i class="icon-bell" v-if="reportMyList.includes(con.contentId)" @click="cancel(con.contentId)"></i>
                          <i class="icon-bell-o" v-else id="reportId" data-toggle="modal" data-target="#exampleModal" @click="sendInfo(con.contentId, con.timestamp)"></i>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>

        <!-- Modal -->
        <div class="modal fade mt-5" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" style="z-index:99999;">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">신고하기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <div class="btn-group dropright">
                  <button class="btn btn-whatever btn-sm dropdown-toggle" style="font-size:13px;" type="button" id="dropdownMenu2" data-toggle="dropdown" data-display="static" aria-haspopup="true" aria-expanded="false">
                    신고 사항 선택
                  </button>
                  <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
                    <button class="dropdown-item" type="button" @click="sendReport(options[0].op1)">{{ options[0].op1 }}</button>
                    <button class="dropdown-item" type="button" @click="sendReport(options[0].op2)">{{ options[0].op2 }}</button>
                    <button class="dropdown-item" type="button" @click="sendReport(options[0].op3)" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">{{ options[0].op3 }}</button>
                  </div>
                </div>

                <div class="collapse" id="collapseExample">
                  <div class="card card-body">
                    <input type="text" v-model="options[0].op4" placeholder="기타를 선택하신 분은 신고 내용을 입력해주세요.">
                  </div>
                  <input class="btn btn-outline-info" type="button" value="입력" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample" @click="sendReport2(options[0].op4)">
                </div>
              </div>
              <div class="modal-footer d-flex justify-content-end py-2">
                <div class="d-block">
                  <button type="button" class="btn btn-danger mr-2 py-1 px-3" data-dismiss="modal" @click="clickBell()">신고</button>
                  <button type="button" class="btn btn-secondary py-1 px-3" data-dismiss="modal">닫기</button>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>

      <div class="text-white text-center" v-if="this.contentErrorMsg">
        <h5>{{this.contentErrorMsg}}</h5>
      </div>
    </div>
</template>

<script>
import $ from "jquery"
import http from '../http-common';
import store from '../store'
import axios from 'axios'
export default {
  // props:["userId"],
  data() {
    return {
      userId:"",
      myInterest:[],
      Interests:[],
      contents: [{
        contentId: "",
        contentValue: "",
        timestamp: "",
        likeButton: false,
        userId: "",
        hashtag:"",
        imageLength: 0,
        images: [{
          imageUrl: "",
          filter: "",
        }],
        scrapButton: false,
        profileUrl: "",
        profileFilter: "",
      }],
      loginId: store.state.user_id,
      contentErrorMsg: "",
      errored: false,
      userLikeList: [],
      followList: [],
      scrapList: [],
      myContentList:[],
      reportMyList: [],
      readContents: [],
      options: [{
        op1: "부적절한 게시물",
        op2: "광고 게시물",
        op3: "기타",
        op4: "",
      }],
      info: [],
    }
  },
  methods: {
    getData() {
      this.isLoading = true;
      http
        .get(`/user/myInterest/${this.userId}`)
        .then((res) => {
          res.data.resValue.forEach(element => {
            this.myInterest.push(element);
            this.Interests.push(element);
          })
          axios.post("https://translation.googleapis.com/language/translate/v2?key=AIzaSyAcnkt6IBUt-bGIMw4u-VEIYpesgw4-2Lk",{
                  "q": this.myInterest,
                  "target": "en"
                })
                .then((res) => {
                  res.data.data.translations.forEach(element => {
                    const keyword = element.translatedText;
                    axios
                      .get("http://52.79.166.146:5000/searchByKeyword/" + keyword)
                      .then((res) => {
                        axios.post("https://translation.googleapis.com/language/translate/v2?key=AIzaSyAcnkt6IBUt-bGIMw4u-VEIYpesgw4-2Lk",{
                                "q": res.data,
                                "target": "ko"
                              })
                              .then((res) => {
                                let num = 0;
                                res.data.data.translations.some( element => {
                                  if(element.translatedText!=""&&!element.translatedText.includes(" ")&&!this.Interests.includes(element.translatedText)){
                                    this.Interests.push(element.translatedText);
                                    num++;
                                  }
                                  return(num>=3);
                                });

                                http
                                .get(`/user/allInterestList`)
                                .then((res) => {
                                  res.data.resValue.forEach( element => {
                                    if(element != "" && !this.Interests.includes(element)){
                                      this.Interests.push(element);
                                    }
                                  })

                                  if (this.contents[0].contentId == "") {
                                    this.contents = []
                                  }
                                  http
                                    .post(`/content/contentListHashtagList`, this.Interests)
                                    .then((res) => {
                                      this.isLoading = false;

                                      if (res.data.resValue.length > 0) {
                                        this.contentErrorMsg = ""
                                        for (var idx = 0; idx < res.data.resValue.length; idx++) {
                                          for (var idx2 = 0; idx2 < this.userLikeList.length; idx2++) {
                                            if (res.data.resValue[idx].content_id == this.userLikeList[idx2].contentId) {
                                              res.data.resValue[idx].user_like = true
                                            }
                                          }
                                          if(!this.myContentList.includes(res.data.resValue[idx].content_id)){
                                            if (this.scrapList.includes(res.data.resValue[idx].content_id)) {
                                              this.contents.push({
                                                contentId: res.data.resValue[idx].content_id,
                                                contentValue: res.data.resValue[idx].content_val.replace(/\n/g, "<br />"),
                                                timestamp: res.data.resValue[idx].timestamp,
                                                likeButton: res.data.resValue[idx].user_like,
                                                userId: res.data.resValue[idx].user_id,
                                                hashtag: res.data.resValue[idx].hashtag,
                                                imageLength: res.data.resValue[idx].imageList.length,
                                                images: [{
                                                  imageUrl: res.data.resValue[idx].imageList[0].image_url,
                                                  filter: res.data.resValue[idx].imageList[0].filter,
                                                }],
                                                scrapButton: true,
                                                dislike: res.data.resValue[idx].dislike,
                                                profileUrl: res.data.resValue[idx].profile_url,
                                                profileFilter: res.data.resValue[idx].profile_filter,
                                              })
                                            }
                                            else{
                                              this.contents.push({
                                                contentId: res.data.resValue[idx].content_id,
                                                contentValue: res.data.resValue[idx].content_val.replace(/\n/g, "<br />"),
                                                timestamp: res.data.resValue[idx].timestamp,
                                                likeButton: res.data.resValue[idx].user_like,
                                                userId: res.data.resValue[idx].user_id,
                                                hashtag: res.data.resValue[idx].hashtag,
                                                imageLength: res.data.resValue[idx].imageList.length,
                                                images: [{
                                                  imageUrl: res.data.resValue[idx].imageList[0].image_url,
                                                  filter: res.data.resValue[idx].imageList[0].filter,
                                                }],
                                                scrapButton: false,
                                                dislike: res.data.resValue[idx].dislike,
                                                profileUrl: res.data.resValue[idx].profile_url,
                                                profileFilter: res.data.resValue[idx].profile_filter,
                                              })
                                            }
                                          }
                                        }
                                        // this.sortList()
                                        // this.getReport()
                                      } else {
                                        this.contentErrorMsg = "추천 게시물이 없습니다."
                                      }

                                      // this.Items = res.data.resValue;
                                      // this.Items.forEach(element => {
                                      this.contents.forEach(element => {
                                        element.value = 0;
                                        this.myInterest.forEach(interest => {
                                          if(element.hashtag.includes(interest)){
                                            element.value++;
                                          }
                                        })
                                      });
                                      this.contents.sort(function(a, b){
                                        return a.value > b.value ? -1 : a.value < b.value ? 1 : 0;
                                      });
                                      if(window.innerWidth <= 501){
                                        setTimeout(() => {
                                          window.removeEventListener('scroll', this.scrollHandler);
                                          window.addEventListener('scroll', this.scrollHandler)
                                        }, 500);
                                      }
                                    })
                                })
                              })
                      })
                  });

                  
                })

                
        })
        .finally(() => {
          
        })
    },

    getMypage() {
      http
      .get('content/contentUserList/'+this.userId)
      .then((res)=>{
        if (res.data.resValue.length > 0) {
          this.contentErrorMsg = ""
          if (res.data.resmsg == "개인 게시물 리스트 출력 성공") {
            this.myContentList = res.data.resValue;
          }
        } else {
          this.contentErrorMsg = "게시물이 없습니다."
        }
      })
      .catch(()=>{
        this.errored = true;
      })
      .catch(e => console.log(e))
    },
    sendInfo(cid, time) {
      this.info = []
      this.info.push({
        content_id: cid,
        user_id: this.loginId,
        report_category: "",
        report_val: "",
        timestamp: time,
      })
    },
    sendReport(report_category) {
      this.info[0].report_category = report_category
    },
    sendReport2(report_val) {
      this.info[0].report_val = report_val
    },
    readReCon(cid) {
      this.readContents.push(cid)
    },
    getReport() {
      this.reportMyList = []
      http
        .get('/userReport/myReportList/' + this.loginId)
        .then((res) => {
          if (res.data.resvalue.length > 0) {
            for (var i = 0; i < res.data.resvalue.length; i++) {
              this.reportMyList.push(res.data.resvalue[i].content_id)
            }
          }
        })
        .catch(() => {
          this.errored = true
        })
    },
    getLike() {
      http
        .get('/userLike/userLikeList/' + this.loginId)
        .then((res) => {
          for (var i = 0; i < res.data.resvalue.length; i++) {
            res.data.resvalue[i].user_like = true
            this.userLikeList.push({
              contentId: res.data.resvalue[i].content_id,
              contentValue: res.data.resvalue[i].content_val.replace(/\n/g, "<br />"),
              timestamp: res.data.resvalue[i].timestamp,
              likeButton: res.data.resvalue[i].user_like,
              userId: res.data.resvalue[i].user_id,
              imageLength: res.data.resvalue[i].imageList.length,
              images: [{
                imageUrl: res.data.resvalue[i].imageList[0].image_url,
                filter: res.data.resvalue[i].imageList[0].filter,
              }],
              dislike: res.data.resvalue[i].dislike,
              profileUrl: res.data.resvalue[i].profile_url,
              profileFilter: res.data.resvalue[i].profile_filter,
            })
          }
        })
        .catch(()=>{
          this.errored = true;
        })
    },
    getScrap() {
      http
        .get('/scrap/scrapList/' + this.loginId)
        .then((res) => {
          for (var i = 0; i < res.data.resvalue.length; i++) {
            if (!this.scrapList.includes(res.data.resvalue[i].content_id)) {
              this.scrapList.push(res.data.resvalue[i].content_id)
            }
          }
        })
        .catch(()=>{
          this.errored = true;
        })
    },
    getFollow() {
      http
        .get('/follow/followList/' + this.loginId)
        .then((res) => {
          for (var i = 0; i < res.data.resvalue.length; i++) {
            this.followList.push(res.data.resvalue[i].user_id)
          }
        })
        .catch(()=>{
          this.errored = true;
        })
    },
    sortList() {
      this.contents.sort(function(a, b) {
        return (a.timestamp < b.timestamp) ? - 1 : (a.timestamp > b.timestamp) ? 1 : 0;
      })
      this.contents.reverse()
    },
    clickHeart(num) {
      for (var idx = 0; idx < this.contents.length; idx++) {
        if (this.contents[idx].contentId == num) {
          if (this.contents[idx].likeButton == false) {
            this.contents[idx].likeButton = true
            http
              .post('/userLike/like', {
                content_id: this.contents[idx].contentId,
                timestamp: this.contents[idx].timestamp,
                user_id: this.loginId
              })
              .then((response) =>{
                this.$socket.emit('notification', {
                  user_id: response.data.resValue.user_id,
                  target_user_id: response.data.resValue.target_user_id,
                  category: response.data.resValue.category,
                  flag: true,
                });
              })
              .catch(()=>{
                this.errored = true;
              })
          } else {
            this.contents[idx].likeButton = false       
            http
              .delete('/userLike/dislike', {
                data: {
                  content_id: this.contents[idx].contentId,
                  timestamp: this.contents[idx].timestamp,
                  user_id: this.loginId
                }
              })
              .then((response) =>{
                this.$socket.emit('notification', {
                  user_id: response.data.resValue.user_id,
                  target_user_id: response.data.resValue.target_user_id,
                  category: response.data.resValue.category,
                  flag: false,
                });
              })
              .catch(()=>{
                this.errored = true;
              })
          }
        }
      }
    },
    clickFollow(user) {
      this.scrapList = []
      this.getScrap()
      if (this.followList.includes(user) == true) {
        http
          .delete('/follow/deleteFollow', {
            data: {
              follow_id: user,
              follower_id: this.loginId
            }
          })
          .then(response => {
            this.$socket.emit('notification', {
              user_id: response.data.resValue.user_id,
              target_user_id: response.data.resValue.target_user_id,
              category: response.data.resValue.category,
              flag: false
            });
          })
          .catch(()=>{
            this.errored = true;
          })
        const idx = this.followList.indexOf(user)
        if (idx > -1) {
          this.followList.splice(idx, 1)
        }
      } else {
        http
          .post('/follow/insertFollow', {
            follow_id: user,
            follower_id: this.loginId
          })
          .then(response => {
            this.$socket.emit('notification', {
              user_id: response.data.resValue.user_id,
              target_user_id: response.data.resValue.target_user_id,
              category: response.data.resValue.category,
              flag: true
            });
          })
          .catch(()=>{
            this.errored = true;
          })
        const idx = this.followList.indexOf(user)
        if (idx == -1) {
          this.followList.push(user)
        }
      }
    },
    clickScrap(id, button) {
      if (button == false) {
        http
          .post('/scrap/insertScrap', {
            content_id: id,
            user_id : this.loginId,
          })
          .then((res) => {
            const idx = this.contents.findIndex(function(item) {
              return item.contentId === id
            })
            if (res.data.resmsg === "스크랩성공") {
              this.contents[idx].scrapButton = true
              this.$socket.emit('notification', {
                  user_id: res.data.resValue.user_id,
                  target_user_id: res.data.resValue.target_user_id,
                  category: res.data.resValue.category,
                  flag:true
                });
            } else {
              this.contents[idx].scrapButton = true
            }
          })
          .catch(()=>{
            this.errored = true;
          })
      } else {
        http
          .delete('/scrap/deleteScrap', {
            data: {
              content_id: id,
              user_id : this.loginId,
            }
          })
          .then((res) => {
            const idx2 = this.contents.findIndex(function(item) {
              return item.contentId === id
            })
            if (res.data.resmsg === "스크랩취소성공") {
              this.contents[idx2].scrapButton = false;
              this.$socket.emit('notification', {
                  user_id: res.data.resValue.user_id,
                  target_user_id: res.data.resValue.target_user_id,
                  category: res.data.resValue.category,
                  flag:false
                });
            } else {
              this.contents[idx2].scrapButton = false
            }
          })
          .catch(()=>{
            this.errored = true;
          })
      }
    },
    clickBell() {
      if (this.info[0].report_category == "") {
        this.$store.commit('setModalText', "신고 사항을 선택해주십시오.");
        document.getElementById('modalBtn').click();
      } else if (this.info[0].report_category == "기타" && this.info[0].report_val == "") {
        this.$store.commit('setModalText', "기타를 선택하신 이유를 적어주십시오.");
        document.getElementById('modalBtn').click();
      } else {
        http
          .post('/userReport/report', {
            content_id: this.info[0].content_id,
            report_category: this.info[0].report_category,
            report_val: this.info[0].report_val,
            timestamp: this.info[0].timestamp,
            user_id: this.loginId,
          })
          .then((res) => {
            this.reportMyList = []
            if (res.data.resmsg == "신고 성공") {
              this.$store.commit('setModalText', "신고가 접수되었습니다.");
              document.getElementById('modalBtn').click();
            }
            this.options[0].op4 = ""
            this.info = []
            this.getReport()
          })
          .catch(()=>{
            this.errored = true;
          })
      }
    },    
    cancel(cid) {
      if (this.reportMyList.includes(cid)) {
        http
          .delete('/userReport/deleteReport', {
            data: {
              content_id: cid,
              report_category: null,
              report_val: null,
              timestamp: null,
              user_id: this.loginId,
            }
          })
          .then((res) => {
            if (res.data.resmsg == "신고취소성공") {
              const idx = this.reportMyList.findIndex(function(item) {
                return item === res.data.resValue.target_event_id
              })
              this.reportMyList.splice(idx, 1)
              this.$store.commit('setModalText', "신고가 취소되었습니다.");
              document.getElementById('modalBtn').click();            
            } else {
              this.$store.commit('setModalText', "신고가 취소되지 않았습니다.");
              document.getElementById('modalBtn').click();            
            }
            // this.sortList()
          })
          .catch(()=>{
            this.errored = true;
          })
      }
    },
    scrollHandler(){
      const contentDivs = document.querySelectorAll(".content-div");
              contentDivs.forEach(div => {
                const parent = div.offsetParent;
                var value = $(window).scrollTop() - parent.offsetTop;
                if(value > -130 && value < 10){
                  const nodeList = div.childNodes;
                  if(nodeList.length >= 4){
                    if(nodeList[3].className != undefined && nodeList[3].className == "photo-text-more"){
                      nodeList[3].style.opacity = 1;
                      nodeList[3].style.visibility = "visible";
                    }
                  }
                }else{
                  const nodeList = div.childNodes;
                  if(nodeList.length >= 4){
                    if(nodeList[3].className != undefined && nodeList[3].className == "photo-text-more"){
                      nodeList[3].style.opacity = 0;
                      nodeList[3].style.visibility = "hidden";
                    }
                  }
                }
              })
    },
  },
  created() {
    this.userId = this.$store.state.user_id;
    this.getLike()
    this.getScrap()
    this.getData()
    this.getFollow()
    this.sortList()
    this.getReport()
  },
  mounted() {
    $('html').scrollTop(0);
    this.$nextTick(() => {
      
    })
  },
  updated(){
    document.querySelector('script[src$="script.js"]').remove()
    document.querySelector('script[src$="swiper.js"]').remove()
    let recaptchaScripta = document.createElement('script')
    recaptchaScripta.setAttribute('type',"text/javascript")
    recaptchaScripta.setAttribute('src', "./theme/js/script.js")
    document.body.appendChild(recaptchaScripta)
    let recaptchaScriptb = document.createElement('script')
    recaptchaScriptb.setAttribute('type',"text/javascript")
    recaptchaScriptb.setAttribute('src', "./theme/js/swiper.js")
    document.body.appendChild(recaptchaScriptb)
  },

  beforeDestroy(){
    window.removeEventListener('scroll', this.scrollHandler);
  }
}
</script>

<style>
  #textarea {
    width: 100%;
  }
  #bg {
    background-color: rgba(255, 255, 255, 0.2);
  }
  #text-color {
    color: white;
  }
  .ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    word-wrap: break-word;
    line-height: 2;
    height: 6rem;
  }
  .size {
    font-size: 1em;
  }
  .snotifyToast__title {
  font-size: 17px;
  }
  .snotifyToast__body {
    font-size: 12px;
  }
</style>