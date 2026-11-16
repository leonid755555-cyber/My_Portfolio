import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import "./axios";
import {createRouter, createWebHashHistory} from "vue-router";
import tables from "@/data/tables";
import queries from "@/data/queries";
import HomePage from "@/components/HomePage";
import QueriesPage from "@/components/QueriesPage";
import RegistrationPage from "@/components/RegistrationPage";
import UserNamePage from "@/components/UserNamePage";

const app = createApp(App)

const router = createRouter({
    history: createWebHashHistory(),
    routes:
        tables.concat(
        queries.concat([
        {
            name: "Home Page",
            path: "/",
            component: HomePage
        },
        {
            name: "Queries Page",
            path: '/queries',
            component: QueriesPage
        },
        {
            name: "Registration Page",
            path: '/registration',
            component: RegistrationPage
        },
        {
            name: "Username Page",
            path: '/username',
            component: UserNamePage
        },
        {
             path: '/login',
             beforeEnter() {location.href = 'http://localhost:8081/login'}
        },
        {
             path: '/logout',
             beforeEnter() {location.href = 'http://localhost:8081/logout   '}
        }

    ]))
})

app.use(ElementPlus)
app.use(router)
app.mount('#app')