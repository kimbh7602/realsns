<template>
    <div class="container-fluid photos">
      <div class="row justify-content-center">

        <div class="col-md-8 pt-4">
            <div class="mb-5 text-center" data-aos="fade-up">
                <h2 class="text-white mb-5">통계</h2>
                
                <div class="row" style="height:500px;">
                    <div class="col-md-4 text-center">
                        <p class="text-white">팔로우</p>
                        <div class="list-group">
                            <a v-for="(item, index) in followNoti" :key="`follow${index}`" href="#" class="list-group-item list-group-item-action">
                                <div>{{item.user_id}}님이 팔로우했습니다.</div>
                                <small>{{item.timestamp}}</small>
                            </a>
                        </div>
                    </div>
                    <div class="col-md-4 text-center">
                        <p class="text-white">좋아요</p>
                        <div class="list-group">
                            <a v-for="(item, index) in likeNoti" :key="`like${index}`" href="#" class="list-group-item list-group-item-action">
                                <div>💗 {{item.user_id}}님이 게시물을 좋아합니다.</div>
                                <small>{{item.timestamp}}</small>
                            </a>
                        </div>
                    </div>
                    <div class="col-md-4 text-center">
                        <p class="text-white">스크랩</p>
                        <div class="list-group">
                            <a v-for="(item, index) in scrapNoti" :key="`scrap${index}`" href="#" class="list-group-item list-group-item-action ">
                                <div>{{item.user_id}}님이 스크랩했습니다.</div>
                                <small>{{item.timestamp}}</small>
                            </a>
                        </div>
                    </div>       
                </div>

                <canvas id="myChart" class="col-xs-12 col-sm-12 col-md-12"></canvas>
            </div>
        </div>
      </div>
    </div>
</template>

<script>
import Chart from "chart.js";
import http from '../http-common.js';

export default {
  name: 'chart',
  data() {
    return {
      followNoti: [],
      likeNoti: [],
      scrapNoti: [],
      date: {
          'follow': {'01':0,'02':0,'03':0,'04':0,'05':0,'06':0,
                    '07':0,'08':0,'09':0,'10':0,'11':0,'12':0},
          'like': {'01':0,'02':0,'03':0,'04':0,'05':0,'06':0,
                    '07':0,'08':0,'09':0,'10':0,'11':0,'12':0},
          'scrap': {'01':0,'02':0,'03':0,'04':0,'05':0,'06':0,
                    '07':0,'08':0,'09':0,'10':0,'11':0,'12':0},
      },
      followDate: [],
      likeDate: [],
      scrapDate: []
    }
  },
  watch: {
    followDate: {
      deep: true,
      handler() {
        this.fetchChart()
      }
    },
    likeDate: {
      deep: true,
      handler() {
        this.fetchChart()
      }
    },
    scrapDate: {
      deep: true,
      handler() {
        this.fetchChart()
      }
    }
  },
  methods: {
      // weekUserChart(){
      //   http.get("/user/weekChart")
      //   .then((response) => {
      //       window.console.log(response);
      //   })
      //   .catch((error) => {
      //       window.console.log(error);
      //   })
      // },
      async fetchCategoryNoti(category){
        const temp = await http
          .get(`/notification/categoryList/${this.$store.state.user_id}/${category}`)
          .then(response => {
              // this.categoryNoti[category] = response.data.resvalue;
              // console.log(response.data.resvalue);
              if (category == 'follow') {
                this.followNoti = response.data.resvalue;
              }else if (category == 'like') {
                this.likeNoti = response.data.resvalue;
              }else {
                this.scrapNoti = response.data.resvalue;
              }
              this.response.data.resvalue.forEach(e => {
                  const month = e.timestamp.substring(5,7);
                  this.date[category][month] += 1;
              });
              return ['01','02','03','04','05','06','07','08','09','10','11','12'].map(item => {
                  return this.date[category][item];
              })
          })
          .catch(e => console.log(e))
          console.log(temp);

          if (category == 'follow') {
            this.followDate = temp;
          }else if (category == 'like') {
            this.likeDate = temp;
          }else {
            this.scrapDate = temp;
          }
      },
      fetchChart() {
          const myChart = new Chart('myChart', {
          type : 'line',
          data : {
              labels : [
                  '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'
              ],

              datasets : [
                  {
                      label : '팔로우',
                    //   backgroundColor : ['rgba(255, 255, 255, 1)'],
                      borderColor : 'white',
                      pointBackgroundColor : 'white',
                      pointBorderWidth : 2,
                      pointRadius : 6,
                      pointStyle : 'rectRot',
                      lineTension : 0.1,
                      data : this.followDate
                  },
                  {
                      label : '좋아요',
                    //   backgroundColor : 'red',
                      borderColor : 'red',
                      pointBackgroundColor : 'red',
                      pointBorderWidth : 2,
                      pointRadius : 6,
                      pointStyle : 'rectRot',
                      lineTension : 0.1,
                      data : this.likeDate
                  },
                  {
                      label : '스크랩',
                    //   backgroundColor : 'green',
                      borderColor : 'green',
                      pointBackgroundColor : 'green',
                      pointBorderWidth : 2,
                      pointRadius : 6,
                      pointStyle : 'rectRot',
                      lineTension : 0.1,
                      data : this.scrapDate
                  }
              ]
          },

          options : {
              maintainAspectRatio : true,

              scales : {
                  yAxes : [{
                      ticks : {
                          fontColor : 'white',
                          beginAtZero : true
                      }
                  }],
                  xAxes : [{
                      ticks : {
                          fontColor : 'white',
                        //   beginAtZero : true
                      }
                  }]
              },

              legend : {
                  labels : {
                      fontColor : 'white'
                  }
              }
          }
      })
      myChart;
      }
  },
  mounted() {
      this.fetchCategoryNoti('follow');
      this.fetchCategoryNoti('like');
      this.fetchCategoryNoti('scrap');
      this.fetchChart();
  }
}
</script>