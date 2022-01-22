import { createApp } from 'vue'
import TicTacToe from './TicTacToe.vue'
import User from './components/User.vue'
import Lobby from './components/Lobby.vue'
import Home from './components/Home.vue'
import Table from './components/Table.vue'
import Info from './components/Info.vue'
import Feedback from './components/Feedback.vue'
import Registration from './components/Registration.vue'
import Error from './components/Error.vue'
import VueRouter, { RouteRecordRaw } from 'vue-router'
import { store } from './store'
import { IUser } from "./interfaces";
import { createWebHistory, createRouter } from "vue-router";

const routes: RouteRecordRaw[] = [
    {
        path: '/portal/index.html',
        redirect: { name: 'Home' }
    },
    {
        path: '/index.html',
        redirect: { name: 'Home' }
    },
    {
        path: '/',
        redirect: { name: 'Home' }
    },
    {
        path: '/portal',
        redirect: { name: 'Home' }
    },
    {
        path: '/portal/tictactoe',
        component: Home,
        name: "Home",
    },

    {
        path: '/portal/tictactoe/user',
        component: User,
        name: "User",
        props: true,
    },
    {
        path: '/portal/tictactoe/registration',
        component: Registration,
        name: "Registration",
        props: true,
    },
    {
        path: '/portal/tictactoe/lobby',
        component: Lobby,
        name: "Lobby",
        props: true,
        beforeEnter: (to, from, next) => {
            if (!store.state.user) {
                const userName = sessionStorage.getItem("userName")
                const token = sessionStorage.getItem("token")
                let user: IUser = { name: userName, token: token, webSocket: null }
                store.dispatch("setUser", user).then(() => {
                    next()
                })
            } else {
                next()
            }
        }
    },
    {
        path: '/portal/tictactoe/table/:watch?',
        component: Table,
        name: "Table",
        props: true,
        beforeEnter: (to, from, next) => {
            if (!store.state.user || !store.state.theTable) {
                next('/');
            } else {
                next()
            }
        }
    },
    {
        path: '/portal/tictactoe/terms',
        component: Info,
        name: "Info",
    },
    {
        path: '/portal/tictactoe/feedback',
        component: Feedback,
        name: "Feedback",
    },

    {
        path: "/portal/tictactoe/:catchAll(.*)",
        component: Error,
        name: "Error",
        props: true,
    }

]

const router = createRouter({
    history: createWebHistory(),
    routes,
});
const app = createApp(TicTacToe)

app.use(router).use(store).mount("#app");