<template v-slot:default>
    <div class="container-fluid photos">
        <div class="row justify-content-center">

            <div class="col-md-6 pt-4" data-aos="fade-up">
                <h2 class="text-white mb-4">Admin</h2>


                <div class="row">
                    <div class="col-md-12">
                        <p class="mb-5">Lorem ipsum dolor sit amet, consectetur <a href="#">adipisicing</a> admin.</p>
                        <div class="row">
                        </div>
                    </div>
                    <div class="col-md-12">
                        <v-tabs fixed-tabs background-color="indigo" dark>
                            <v-tab v-on:click="tabclick('today')">
                                방문자수
                            </v-tab>
                            <v-tab v-on:click="tabclick('wordcloud')">
                                관심사
                            </v-tab>
                            <v-tab v-on:click="tabclick('calendar')">
                                캘린더
                            </v-tab>
                        </v-tabs>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid photos">
            <div class="row justify-content-center">
                <div class="col-md-6 pt-4" data-aos="fade-up">
                    <div class="col-md-12">
                        <div class="md-5" id="contents">
                            <div id="today" name="today" v-bind:style="today" >
                                <div>today</div>
                                <div>{{visit}}</div>
                            </div>
                            <div id="worldcloud" name="wordcloud" v-bind:style="wordcloud"
                                >
                                <wordcloud-vue></wordcloud-vue>
                            </div>
                        </div>
                            <div id="calendar" name="calendar" v-bind:style="calendar">
                                <mycalendar></mycalendar>
                            </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<script>
    import http from "../http-common"
    import $ from "jquery"
    import wordcloudVue from "./WordCloud.vue"
    import mycalendar from "./Calendar.vue"

    export default {
        components: {
            wordcloudVue,
            mycalendar
        },
        data() {
            return {
                visit: "",
                today: {
                    display: '',
                },
                wordcloud: {
                    display: 'none',
                },
                calendar: {
                    display: 'none',
                }
            }
        },
        mounted() {
            $('html').scrollTop(0);
            http
                .get("admin/todayvisit")
                .then(response => {
                    if (response.data['resmsg'] == "조회성공") {
                        this.visit = response.data['resvalue'];
                    }
                })

        },
        methods: {
            tabclick(e) {
                if (e === "today") {
                    this.today.display = "";
                    this.wordcloud.display = "none";
                    this.calendar.display = "none";
                } else if (e === "wordcloud") {
                    this.today.display = "none";
                    this.wordcloud.display = "";
                    this.calendar.display = "none";
                } else if (e === "calendar") {
                    this.today.display = "none";
                    this.wordcloud.display = "none";
                    this.calendar.display = "";
                }
            }

        }
    }
</script>