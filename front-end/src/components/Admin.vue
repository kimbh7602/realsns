<template v-slot:default>
    <div class="container-fluid photos">
        <div class="row justify-content-center">

            <div class="col-md-6 pt-4" data-aos="fade-up">
                <h2 class="text-white mb-4">AdminLog</h2>


                <div class="row">
                    <div class="col-md-12">
                        <p class="mb-5">Lorem ipsum dolor sit amet, consectetur <a href="#">adipisicing</a> log.</p>
                        <div class="row">
                            <div class="col-md-12">

                                <v-tabs fixed-tabs background-color="indigo" dark>
                                    <v-tab>
                                        Option
                                    </v-tab>
                                    <v-tab>
                                        Another Selection
                                    </v-tab>
                                    <v-tab>
                                        Items
                                    </v-tab>
                                    <v-tab>
                                        Another Screen
                                    </v-tab>
                                </v-tabs>
                            </div>

                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>
<style>
    /* #log{
    width:100%;
} */
</style>


<script>
    import http from "../http-common"
    import $ from "jquery"
    export default {
        data() {
            return {
                chain: [],
                trust: false,
            }
        },
        mounted() {
            $('html').scrollTop(0);
            var tmp;
            http
                .get("admin/info")
                .then(response => {
                    if (response.data['resmsg'] == "조회성공") {
                        tmp = response.data['resvalue'];
                        this.chain = tmp;

                    }
                })
            http
                .get("admin/trust")
                .then(response => {
                    if (response.data['resmsg'] == "신뢰") {
                        this.trust = true;
                    }
                })
        }
    }
</script>