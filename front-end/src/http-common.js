import axios from "axios";

export default axios.create({
    baseURL : "http://52.79.166.146:80/api",
    // baseURL : "http://192.168.100.60:8080/api",
    headers: {
        "Content-type": "application/json",
    }
})