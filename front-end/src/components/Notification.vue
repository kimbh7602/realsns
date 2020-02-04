<template>
   <div class="container-fluid photos">
      <div class="row justify-content-center">

        <div class="col-md-8 pt-4">
            <div class="mb-5 text-center text-light" data-aos="fade-up">
                <h2 class="mb-5">읽지 않은 알림</h2>

                <ul class="nav nav-tabs" id="myTab" role="tablist">
                  <li class="nav-item">
                    <a class="nav-link" :class="{'active': check=='all'}" @click="check='all'">전체</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" :class="{'active': check=='follow'}" @click="check='follow'">팔로우</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" :class="{'active': check=='like'}" @click="check='like'">좋아요</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" :class="{'active': check=='scrap'}" @click="check='scrap'">스크랩</a>
                  </li>
                </ul>
              
                <table class="table">
                  <tbody v-if="check=='all'">
                    <tr v-for="(item, index) in allNoti" :key="`follow${index}`">
                      <td>
                        <div>{{item.user_id}}님이 팔로우했습니다.</div>
                        <small>{{item.timestamp}}</small>
                      </td>
                    </tr>
                  </tbody>
                  <tbody v-if="check=='follow'">
                    <tr v-for="(item, index) in followNoti" :key="`follow${index}`">
                      <td>
                        <div>{{item.user_id}}님이 팔로우했습니다.</div>
                        <small>{{item.timestamp}}</small>
                      </td>
                    </tr>
                  </tbody>
                  <tbody v-if="check=='like'">
                    <tr v-for="(item, index) in likeNoti" :key="`follow${index}`">
                      <td>
                        <div>💗 {{item.user_id}}님이 게시물을 좋아합니다.</div>
                        <small>{{item.timestamp}}</small>
                      </td>
                    </tr>
                  </tbody>
                  <tbody v-if="check=='scrap'">
                    <tr v-for="(item, index) in scrapNoti" :key="`follow${index}`">
                      <td>
                        <div>{{item.user_id}}님이 스크랩했습니다.</div>
                        <small>{{item.timestamp}}</small>
                      </td>
                    </tr>
                  </tbody>
                </table>
            </div>
        </div>
      </div>
    </div>

</template>

<script>
import http from '../http-common.js';

export default {
  data() {
    return {
      allNoti: [],
      followNoti: [],
      likeNoti: [],
      scrapNoti: [],
      check: 'all',
    }
  },
  methods: {
    fetchNoti() {
       http
        .get(`/notification/uncheckedList/${this.$store.state.user_id}`)
        .then(response => {
            this.allNoti = response.data.resvalue;
            this.allNoti.reverse();
        })
        .catch(e => console.log(e))
    },
    fetchCategoryNoti(category){
      http
        .get(`/notification/categoryUncheckedList/${this.$store.state.user_id}/${category}`)
        .then(response => {
            if (category == 'follow') {
              this.followNoti = response.data.resvalue;
              this.followNoti.reverse().slice(0,10);
            }else if (category == 'like') {
              this.likeNoti = response.data.resvalue;
              this.likeNoti.reverse().slice(0,10);
            }else {
              this.scrapNoti = response.data.resvalue;
              this.scrapNoti.reverse().slice(0,10);
            }
        })
        .catch(e => console.log(e))
    },
  },
  mounted() {
    this.fetchNoti();
    this.fetchCategoryNoti('follow');
    this.fetchCategoryNoti('like');
    this.fetchCategoryNoti('scrap');
  }
}
</script>

<style>

</style>