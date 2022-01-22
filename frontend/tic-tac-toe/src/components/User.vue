<template>

	<div v-if="authenticated" class="row"  >
		
		<div class="row ">
			<div class="col-md-6 offset-md-3 alert alert-info">
				Hi {{userName}}
				<br>
				
			</div>
		</div>
	</div>
	<div v-if="!authenticated" class="row">
		<div class="col m-2">
			<h3>Login </h3>
			<div>
				UserName:
				<input v-model="usrNameTemp" class="p-3" type="text" >
				<br>
				
			</div>
		</div>		
	</div>
	<div v-if="!authenticated" class="row">
			<div class="col m-2">
			Password:
				<input v-model="usrPwTemp" class="p-3" type="password" >
				<div>
					<button @click="login" :disabled='isLoginButtonDisabled' >Login</button>
				</div>
			</div>			
		</div>
	<div v-if="errorFromLogin" class="row d-inline-flex p-2 alert-danger" role="alert">
		Login error occured!
	</div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { loginMixin } from "../mixins/mixins";
import { IUser } from "../interfaces";
export default defineComponent({
	name: "User",
	mixins: [loginMixin],
	props: {
		token: String
	},
	data() {
		return {
			loginToken: null,
			usrNameTemp: null,
			usrPwTemp: null,
			loginError: null,
			loginRequestOnTheFly: false
		};
	},
	computed: {
		
		isLoginButtonDisabled() {
			if (this.loginRequestOnTheFly) {
				return true;
			} else if (this.usrNameTemp && this.usrPwTemp) {
				return this.usrNameTemp.length < 1 || this.usrPwTemp.length < 1;
			}
			return true;
		},
		errorFromLogin() {
			return this.loginError != null;
		}
	},

	created() {
		
	},

	methods: {
		
		login() {
			this.logout().then(()=>{
				
				this.loginError = null;
				this.loginRequestOnTheFly = true;
				const requestOptions = {
					method: "POST",
					headers: {
						"Content-Type": "application/json",
						Authorization: null
					},
					body: JSON.stringify({
						userName: this.usrNameTemp,
						password: this.usrPwTemp
					})
				}
				const apiURL=process.env.VUE_APP_API_BASE_URL+"/portal/api/user/login"
				this.$store.dispatch("setLoadingStatus", true)
				fetch(apiURL, requestOptions)
					.then(response => {
						this.resetLoginData();
						if (response.status !== 200) {
							return Promise.reject("error in login");
						}
						return response.json();
					})
					.then(
						data => {
							//	this.setUser(data.name , data.activeLoginId)
							let user: IUser = { name: data.name, token: data.activeLoginId };
							this.$store.dispatch("setUser", user);
						},
						() => {
							this.loginError = true;
						}
					)
					.finally(() => {
						this.loginRequestOnTheFly = false;
						this.$store.dispatch("setLoadingStatus", false)
					});
				})
			
		},
		resetLoginData() {
			this.usrName = null;
			this.pw = null;
		}
	}
});
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
