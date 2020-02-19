<template>
    <div class="container-fluid photos">
        <div class="row justify-content-center">
            <h2 class="text-white mb-4">Location List</h2>
            <div class="col-md-8 pt-4">
                <!-- 제목 -->
                <div class="row mb-5" data-aos="fade-up">
                    <div class="col-12">
                        <h2 class="text-white mb-4 text-center">게시물 찾기</h2>
                    </div>
                </div>
                <input id="mapModalBtn" data-toggle="modal" data-target="#mapModal" style="display:none;" />
                <!-- 친구 검색 바 -->
                <div v-if="isLocation&&!isLocationSelect" class="row form-group mb-5">
                    <div class="col-md-12">
                    <label class="text-white">Location Search</label> 
                    <input v-model="searchKeyword" type="text" class="form-control">
                    <br>
                    <input @click="searchLocation" type="button" value="Search" class="btn btn-outline-light btn-block">
                    </div>
                </div>
                <div v-if="!isLocation&&!isLocationSelect">
                    <input @click="isLocation=!isLocation" type="button" value="위치정보 등록" class="btn btn-outline-light btn-block">
                    <br/><br/><br/>
                </div>
                <div v-if="isLocationSelect" class="row form-group mb-5">
                    <div class="col-md-12">
                    <label class="text-white">Selected Location</label>
                    <input style="text-align:center;" v-model="selectedLocation.name" type="text" class="form-control"><br>
                    <input style="text-align:center;" v-model="distString" type="text" class="form-control"><br>
                    <div class="row form-group mb-5 justify-content-center">
                        <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Select Search Distance
                        </button>
                        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                            <a class="dropdown-item" href="javascript:void(0)" style="width:80%;" @click="dist=0.01; distString='1 Km'">1 Km</a>
                            <a class="dropdown-item" href="javascript:void(0)" style="width:80%;" @click="dist=0.005; distString='500 m'">500 m</a>
                            <a class="dropdown-item" href="javascript:void(0)" style="width:80%;" @click="dist=0.003; distString='300 m'">300 m</a>
                        </div>
                        <!-- <select v-model="dist" class="form-control" aria-placeholder="Select Search Distance" style="font-color:white; background-color:black; border:none; text-align:center;">
                            <option disabled value="" selected class="form-control" style="border:none;">Select Search Distance</option>
                            <option value="0.01" class="form-control">1 Km</option>
                            <option value="0.005" class="form-control">500 m</option>
                            <option value="0.003" class="form-control">300 m</option>
                        </select> -->
                        </div>
                    </div>
                    <input @click="isLocation = true; isLocationSelect=false; dist=0.01; distString='1 Km'" type="button" value="다시 위치검색" class="btn btn-outline-warning btn-block">
                    <input @click="findContentByLocation" type="button" value="게시물 검색" class="btn btn-outline-info btn-block">
                    <br/><br/><br/>
                    </div>
                </div>
                
                <div class="modal fade" id="mapModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="z-index:99999;">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-body" style="text-align:center;">
                                <gmap-map
                                :center="center"
                                :zoom="15"
                                style="width:100%;  height: 400px;"
                                >
                                <gmap-info-window :options="infoOptions" :position="infoWinPos" :opened="infoWinOpen" @closeclick="infoWinOpen=false">
                                    <br><h4 style="align:left">{{infoContent.title}}</h4>
                                    {{infoContent.text}}<br>
                                    <img width="150px" :src="infoContent.img" />
                                </gmap-info-window>

                                <gmap-marker
                                    :key="index"
                                    v-for="(m, index) in markers"
                                    :position="m.position"
                                    :title="m.title"
                                    :info="m.info"
                                    @click="placeClick(m, index)"
                                ></gmap-marker>
                                </gmap-map>
                            </div>
                            <div class="modal-footer d-flex justify-content-end py-2">
                                <div class="d-block">
                                    <button type="button" class="btn btn-danger mr-2 py-1 px-3" data-dismiss="modal" @click="selectLocation()">선택</button>
                                    <button type="button" class="btn btn-secondary py-1 px-3" data-dismiss="modal">닫기</button>
                                </div>
                            </div>

                            <!-- <div class="modal-footer">
                                <button type="button" class="btn btn-success text-white" @click="selectLocation" data-dismiss="modal">선택</button>
                                <button type="button" class="btn btn-danger text-white" data-dismiss="modal">닫기</button>
                            </div> -->
                        </div>
                    </div>
                </div>
                <div v-if="contents == null || contents.length == 0" style="text-align:center;">
                    검색 결과가 없습니다.
                </div>
                <div v-else class="container-fluid photos">
                    <div class="row align-items-stretch">
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
                            <div style="width:100%; height:100%" :class="con.images[0].filter">
                            <img :src="con.images[0].imageUrl" alt="Image" class="img-fluid pa blur m-0"/>
                            <div class="centertext">
                                <p class="text-white">신고 누적된 게시물입니다.</p>
                                <button class="btn btn-outline-light btn-sm py-0" @click="readReCon(con.contentId)">보기</button>
                            </div>
                            </div>
                        </div>
                        <div class="polaroid" v-show="reportMyList.includes(con.contentId) && !readContents.includes(con.contentId)">
                            <div style="width:100%; height:100%" :class="con.images[0].filter">
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
                                    <div class="mail-title offset-1 col-9 mt-2 ml-3" style="text-align:left;"><p class="mail-title-val">Dear {{ loginId }}</p></div>
                                    <div class="mail-message offset-2 pt-0 pb-0 col-8 ellipsis mail-message-val" v-html="con.contentValue">{{ con.contentValue }}</div>
                                    <div class="col-11 col-offset-1 pt-0 pr-0 mail-from-val">from {{ con.userId }}</div>
                                    </div>
                                    <!-- buttons -->
                                    <div class="mb-4 pb-2 d-flex justify-content-around size content_button" style="font-size: 1vw">
                                    <div @click="clickHeart(con.contentId)">
                                        <i class="icon-heart" v-if="con.likeButton"></i>
                                        <i class="icon-heart-o" v-else></i>
                                    </div>
                                    <div @click="clickFollow(con.userId)" v-if="con.userId !== loginId">
                                        <i class="icon-check" v-if="followList.includes(con.userId)">{{con.userId}}</i>
                                        <i class="icon-user-plus" v-else>{{con.userId}}</i>
                                    </div>
                                    <div @click="clickScrap(con.contentId, con.scrapButton)" v-if="con.userId !== loginId">
                                        <i class="icon-bookmark" v-if="scrapList.includes(con.contentId)"></i>
                                        <i class="icon-bookmark-o" v-else></i>
                                    </div>
                                    <div v-if="con.userId !== loginId">
                                        <i class="icon-bell" v-if="reportMyList.includes(con.contentId)" @click="cancel(con.contentId)"></i>
                                        <i class="icon-bell-o" v-else data-toggle="modal" data-target="#exampleModal" @click="sendInfo(con.contentId, con.timestamp)"></i>
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
                    <div class="modal fade mt-5" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" style="z-index: 99999;">
                        <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <!-- <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">신고하기</h5>
                            </div> -->
                            <div class="modal-body">
                            <div class="btn-group dropright">
                                <button class="btn btn-sm dropdown-toggle" style="font-size:13px;" type="button" id="dropdownMenu2" data-toggle="dropdown" data-display="static" aria-haspopup="true" aria-expanded="false">
                                신고 사항 선택
                                </button>
                                <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
                                <button class="dropdown-item" type="button" @click="sendReport(options[0].op1), reportOption(1)">{{ options[0].op1 }}</button>
                                <button class="dropdown-item" type="button" @click="sendReport(options[0].op2), reportOption(2)">{{ options[0].op2 }}</button>
                                <button class="dropdown-item" type="button" @click="sendReport(options[0].op3), reportOption(3)">{{ options[0].op3 }}</button>
                                <button class="dropdown-item" type="button" @click="sendReport(options[0].op4), reportOption(4)">{{ options[0].op4 }}</button>
                                <button class="dropdown-item" type="button" @click="sendReport(options[0].op5), reportOption(5)">{{ options[0].op5 }}</button>
                                <button class="dropdown-item" type="button" @click="sendReport(options[0].op6), reportOption(6)">{{ options[0].op6 }}</button>
                                </div>
                            </div>
                            <p class="mt-3 ml-2">신고 사항 : {{ whatoption }}</p>
                            <div class="card card-body" v-show="optionButton">
                                <input type="text" v-model="options[0].op7" placeholder="신고 내용">
                            </div>
                            </div>
                            <div class="modal-footer d-flex justify-content-end py-2">
                            <div class="d-block">
                                <button type="button" class="btn btn-danger mr-2 py-1 px-3" data-dismiss="modal" @click="clickBell(), opBtn()">신고</button>
                                <button type="button" class="btn btn-secondary py-1 px-3" data-dismiss="modal" @click="opBtn()">닫기</button>
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
                
            </div>

        </div>        
    </div>
</template>

<script>
import http from '../http-common';
import axios from 'axios'
import $ from "jquery"
import store from '../store'
export default {
    props:['location_name', 'lat', 'lng'],
    data() {
        return {
            Items: null,
            isLocation:false,
            isLocationSelect:false,
            searchKeyword:"",
            locationList:[],
            center: { lat: 45.508, lng: -73.587 },
            markers: [],
            places: [],
            res:[],
            infoWinPos : null,
            infoWinOpen : false,
            infoContent : {
                title : null,
                text : null,
                img : null
            },
            currentIdx : null,
            currentPlace: null,
            infoOptions: {
                pixelOffset: {
                width: 0,
                height: -35
                }
            },
            selectedLocation: null,
            dist:0.01,
            distString:"1 Km",


            contents: [{
                contentId: "",
                contentValue: "",
                timestamp: "",
                likeButton: false,
                userId: "",
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
            reportMyList: [],
            readContents: [],
            options: [{
                op1: "선정적인 게시물",
                op2: "폭력적인 게시물",
                op3: "혐오적인 게시물",
                op4: "허위 게시물",
                op5: "스팸 게시물",
                op6: "기타",
                op7: "",
            }],
            info: [],
            optionButton: false,
            whatoption: "",
        }
    },
    methods: {
        searchLocation(){
            let keyword = this.searchKeyword;
            keyword.replace(" ", "+");

            const url = "http://52.79.166.146:5000/location/"+keyword;
            
            axios.get(url)
                .then((response)=>{
                    this.locationList = response.data.results;
                    this.center.lat = parseFloat(this.locationList[0].geometry.location.lat);
                    this.center.lng = parseFloat(this.locationList[0].geometry.location.lng);

                    this.locationList.forEach(element => {
                        let addr = "";
                        axios.post("https://translation.googleapis.com/language/translate/v2?key=AIzaSyAcnkt6IBUt-bGIMw4u-VEIYpesgw4-2Lk",{
                          "q": [element.formatted_address],
                          "target": "ko"
                        })
                            .then((response)=>{
                                addr = response.data.data.translations[0].translatedText;
                                this.markers.push({
                                    position : {
                                        lat: parseFloat(element.geometry.location.lat),
                                        lng: parseFloat(element.geometry.location.lng)
                                    },
                                    title : element.name,
                                    info : {
                                        title: element.name,
                                        text: addr
                                    }
                                })
                            })
                            .catch((error)=>{
                              alert(error)
                            })
                    })

                    document.querySelector("#mapModalBtn").click();
                })
                .catch((error)=>{
                    alert(error)
                })
        },

        placeClick(marker, index){
          this.infoWinPos = marker.position;
          this.infoContent = marker.info;

          if(this.currentIdx == index){
              this.infoWinOpen = !this.infoWinOpen;
          }else{
              this.infoWinOpen = true;
              this.currentIdx = index;
              this.selectedLocation = {
                  position: marker.position,
                  name: marker.title,
                  addr: marker.info.text
              };
          }
      },

      selectLocation(){
          if(this.selectedLocation != null){
              this.isLocationSelect = true;
          }else{
              this.isLocation = false;
          }
      },

      findContentByLocation(){
          this.contents = [{
                contentId: "",
                contentValue: "",
                timestamp: "",
                likeButton: false,
                userId: "",
                imageLength: 0,
                images: [{
                imageUrl: "",
                filter: "",
                }],
                scrapButton: false,
                profileUrl: "",
                profileFilter: "",
            }]
          http.post("/content/findByLocation",{
              lat: this.selectedLocation.position.lat,
              lng: this.selectedLocation.position.lng,
              dist: this.dist
          })
          .then((res)=>{
              if (res.data.resValue.length > 0) {
                    this.contentErrorMsg = ""
                    for (var idx = 0; idx < res.data.resValue.length; idx++) {
                    for (var idx2 = 0; idx2 < this.userLikeList.length; idx2++) {
                        if (res.data.resValue[idx].content_id == this.userLikeList[idx2].contentId) {
                        res.data.resValue[idx].user_like = true
                        delete this.userLikeList[idx2].contentId
                        }
                        if (this.userLikeList[idx2].contentId && idx == res.data.resValue.length - 1) {
                        if (this.scrapList.includes(res.data.resValue[idx2].contentId)) {
                            this.contents.push({
                            contentId: this.userLikeList[idx2].contentId,
                            contentValue: this.userLikeList[idx2].contentValue.replace(/\n/g, "<br />"),
                            timestamp: this.userLikeList[idx2].timestamp,
                            likeButton: this.userLikeList[idx2].likeButton,
                            userId: this.userLikeList[idx2].userId,
                            imageLength: this.userLikeList[idx2].imageLength,
                            images: this.userLikeList[idx2].images,
                            scrapButton: true,
                            dislike: this.userLikeList[idx2].dislike,
                            profileUrl: this.userLikeList[idx2].profileUrl,
                            profileFilter: this.userLikeList[idx2].profileFilter,

                            })
                        } else {
                            this.contents.push({
                            contentId: this.userLikeList[idx2].contentId,
                            contentValue: this.userLikeList[idx2].contentValue.replace(/\n/g, "<br />"),
                            timestamp: this.userLikeList[idx2].timestamp,
                            likeButton: this.userLikeList[idx2].likeButton,
                            userId: this.userLikeList[idx2].userId,
                            imageLength: this.userLikeList[idx2].imageLength,
                            images: this.userLikeList[idx2].images,
                            scrapButton: false,
                            dislike: this.userLikeList[idx2].dislike,
                            profileUrl: this.userLikeList[idx2].profileUrl,
                            profileFilter: this.userLikeList[idx2].profileFilter,
                            })
                        }
                        }
                    }
                    if (this.scrapList.includes(res.data.resValue[idx].content_id)) {
                        this.contents.push({
                        contentId: res.data.resValue[idx].content_id,
                        contentValue: res.data.resValue[idx].content_val.replace(/\n/g, "<br />"),
                        timestamp: res.data.resValue[idx].timestamp,
                        likeButton: res.data.resValue[idx].user_like,
                        userId: res.data.resValue[idx].user_id,
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
                    } else {
                        this.contents.push({
                        contentId: res.data.resValue[idx].content_id,
                        contentValue: res.data.resValue[idx].content_val.replace(/\n/g, "<br />"),
                        timestamp: res.data.resValue[idx].timestamp,
                        likeButton: res.data.resValue[idx].user_like,
                        userId: res.data.resValue[idx].user_id,
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
                    this.sortList()
                    // this.getReport()

                    this.$nextTick(() => {
                        if(window.innerWidth <= 501){
                            setTimeout(() => {
                              window.addEventListener('scroll', this.scrollHandler)
                            }, 500);
                            }
                        })
                } else {
                    this.contentErrorMsg = "타임라인이 없습니다."
                }
          })
      },

      opBtn() {
      this.optionButton = false
    },
    reportOption(id) {
      if (id === 6) {
        this.optionButton = true
      } else {
        this.optionButton = false
      }
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
      this.whatoption = report_category
      this.info[0].report_category = report_category
      this.info[0].report_val = this.options[0].op7
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
    goDetail: function(con_id) {
      this.$router.push({
        name: 'bio',
        params: {
          cid: con_id
        }
      })
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
            if (this.contents[idx].userId !== this.loginId && this.followList.includes(this.contents[idx].userId) == false) {
              this.contents.splice(idx, 1)
            }
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
        const del = []
        for (var i = 0; i < this.contents.length; i++) {
          if (this.contents[i].userId == user && !this.contents[i].likeButton) {
            del.push(this.contents[i].contentId)
          }
        }
        for (var j =0; j < del.length; j++) {
          const index = this.contents.findIndex(function(item) {return item.contentId === del[j]})
          this.contents.splice(index, 1)
        }
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
        http
          .get('/content/contentUserList/' + user)
          .then((res) => {
            const li = []
            this.contents.forEach(i => {
              li.push(i.contentId)
            })
            res.data.resValue.forEach(idx => {
              if (!li.includes(idx.content_id)) {
                if (this.scrapList.includes(idx.content_id)) {
                  this.contents.push({
                    contentId: idx.content_id,
                    contentValue: idx.content_val.replace(/\n/g, "<br />"),
                    timestamp: idx.timestamp,
                    likeButton: idx.user_like,
                    userId: idx.user_id,
                    imageLength: idx.imageList.length,
                    images: [{
                      imageUrl: idx.imageList[0].image_url,
                      filter: idx.imageList[0].filter,
                    }],
                    scrapButton: true,
                    dislike: idx.dislike,
                    profileUrl: idx.profile_url,
                    profileFilter: idx.profile_filter

                  })
                } else {
                  this.contents.push({
                    contentId: idx.content_id,
                    contentValue: idx.content_val.replace(/\n/g, "<br />"),
                    timestamp: idx.timestamp,
                    likeButton: idx.user_like,
                    userId: idx.user_id,
                    imageLength: idx.imageList.length,
                    images: [{
                      imageUrl: idx.imageList[0].image_url,
                      filter: idx.imageList[0].filter,
                    }],
                    scrapButton: false,
                    dislike: idx.dislike,
                    profileUrl: idx.profile_url,
                    profileFilter: idx.profile_filter
                  })
                }
              }
              this.sortList()
            })
          })
        .catch(()=>{
          this.errored = true;
        })
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
              console.log(res.data.resmsg)
              // this.contents[idx].scrapButton = true
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
              console.log(res.data.resmsg)
              // this.contents[idx2].scrapButton = false
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
      } else if (this.info[0].report_category == "기타" && this.options[0].op7 == "") {
        this.$store.commit('setModalText', "기타를 선택하신 이유를 적어주십시오.");
        document.getElementById('modalBtn').click();
      } else {
        http
          .post('/userReport/report', {
            content_id: this.info[0].content_id,
            report_category: this.info[0].report_category,
            report_val: this.options[0].op7,
            timestamp: this.info[0].timestamp,
            user_id: this.loginId,
          })
          .then((res) => {
            this.reportMyList = []
            this.$socket.emit('notification', {
              user_id: res.data.resValue.user_id,
              target_user_id: res.data.resValue.target_user_id,
              category: res.data.resValue.category,
              flag: true,
            });
            if (res.data.resmsg == "신고 성공") {
              this.$store.commit('setModalText', "신고가 접수되었습니다.");
              document.getElementById('modalBtn').click();
            } else {
              console.log("신고 실패")
            }
            this.options[0].op7 = ""
            this.info = []
            this.whatoption = ""
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
            this.$socket.emit('notification', {
              user_id: res.data.resValue.user_id,
              target_user_id: res.data.resValue.target_user_id,
              category: res.data.resValue.category,
              flag: false,
            });
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
            this.sortList()
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
                if(value > -120 && value < 0){
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
    }


    },
    computed: {

    },
    watch: {
        
    },
    mounted(){
        this.getLike()
        this.getScrap()
        $('html').scrollTop(0);
        if(this.location_name != undefined){
            http.post("/content/findByLocation",{
              lat: this.lat,
              lng: this.lng,
              dist: this.dist
            })
            .then((res)=>{
                // this.Items = res.data.resValue;
                window.console.log(this.Items);
                this.searchKeyword = this.location_name;
                this.isLocation = true;
                this.isLocationSelect = true;
                this.selectedLocation = {
                    position:{
                        lat: this.lat,
                        lng: this.lng,

                    },
                    name: this.location_name
                }

                if (res.data.resValue.length > 0) {
                    this.contentErrorMsg = ""
                    for (var idx = 0; idx < res.data.resValue.length; idx++) {
                    for (var idx2 = 0; idx2 < this.userLikeList.length; idx2++) {
                        if (res.data.resValue[idx].content_id == this.userLikeList[idx2].contentId) {
                        res.data.resValue[idx].user_like = true
                        delete this.userLikeList[idx2].contentId
                        }
                        if (this.userLikeList[idx2].contentId && idx == res.data.resValue.length - 1) {
                        if (this.scrapList.includes(res.data.resValue[idx2].contentId)) {
                            this.contents.push({
                            contentId: this.userLikeList[idx2].contentId,
                            contentValue: this.userLikeList[idx2].contentValue.replace(/\n/g, "<br />"),
                            timestamp: this.userLikeList[idx2].timestamp,
                            likeButton: this.userLikeList[idx2].likeButton,
                            userId: this.userLikeList[idx2].userId,
                            imageLength: this.userLikeList[idx2].imageLength,
                            images: this.userLikeList[idx2].images,
                            scrapButton: true,
                            dislike: this.userLikeList[idx2].dislike,
                            profileUrl: this.userLikeList[idx2].profileUrl,
                            profileFilter: this.userLikeList[idx2].profileFilter,

                            })
                        } else {
                            this.contents.push({
                            contentId: this.userLikeList[idx2].contentId,
                            contentValue: this.userLikeList[idx2].contentValue.replace(/\n/g, "<br />"),
                            timestamp: this.userLikeList[idx2].timestamp,
                            likeButton: this.userLikeList[idx2].likeButton,
                            userId: this.userLikeList[idx2].userId,
                            imageLength: this.userLikeList[idx2].imageLength,
                            images: this.userLikeList[idx2].images,
                            scrapButton: false,
                            dislike: this.userLikeList[idx2].dislike,
                            profileUrl: this.userLikeList[idx2].profileUrl,
                            profileFilter: this.userLikeList[idx2].profileFilter,
                            })
                        }
                        }
                    }
                    if (this.scrapList.includes(res.data.resValue[idx].content_id)) {
                        this.contents.push({
                        contentId: res.data.resValue[idx].content_id,
                        contentValue: res.data.resValue[idx].content_val.replace(/\n/g, "<br />"),
                        timestamp: res.data.resValue[idx].timestamp,
                        likeButton: res.data.resValue[idx].user_like,
                        userId: res.data.resValue[idx].user_id,
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
                    } else {
                        this.contents.push({
                        contentId: res.data.resValue[idx].content_id,
                        contentValue: res.data.resValue[idx].content_val.replace(/\n/g, "<br />"),
                        timestamp: res.data.resValue[idx].timestamp,
                        likeButton: res.data.resValue[idx].user_like,
                        userId: res.data.resValue[idx].user_id,
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
                    this.sortList()
                    // this.getReport()

                    this.$nextTick(() => {
                        if(window.innerWidth <= 501){
                            setTimeout(() => {
                              window.addEventListener('scroll', this.scrollHandler)
                            }, 500);
                            }
                        })
                } else {
                    this.contentErrorMsg = "타임라인이 없습니다."
                }
            })
        }

        this.getFollow()
        this.sortList()
        this.getReport()
    },

    beforeDestroy(){
        window.removeEventListener('scroll', this.scrollHandler);
    }
}
</script>

<style>
  body{
    padding-right:0px;
  }
</style>