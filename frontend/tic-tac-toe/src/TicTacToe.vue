<template>
	<div class="container">
		<header>
			<h2>TicTacToe with custom board sizes </h2>
		</header>
		<NavbarTop />

		<router-view></router-view>
		<footer> </footer>
	</div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import NavbarTop from "./components/NavbarTop.vue";
import Lobby from "./components/Lobby.vue";
import {IUser} from "./interfaces";
import { loginMixin } from "./mixins/mixins";
export default defineComponent({
	name: "TicTacToe",
	components: {
		NavbarTop
	},
	mixins: [loginMixin],
	data() {
		return {
			title: ""
		};
	},
	async created() {
		
		if(!this.user){
			const userName=sessionStorage.getItem("userName")
            const token=sessionStorage.getItem("token")
			
			let user: IUser = { name: userName, token: token, webSocket: null }	
			if(userName && !userName.startsWith("Anon")){
				let user: IUser = { name: userName, token: token, webSocket: null }				
			}
			await this.$store.dispatch("setUser", user)
		}
	},
	methods: {}
});
</script>

<style>
#app {
	font-family: Avenir, Helvetica, Arial, sans-serif;
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
	text-align: center;
	color: #2c3e50;
	margin-top: 60px;	
}
</style>
