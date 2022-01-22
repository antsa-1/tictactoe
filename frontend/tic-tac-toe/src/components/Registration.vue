<template>
	<div v-if="!registerDone" class=row>
		<div class="col">
			<h3> Create nickname</h3>
		</div>
		<div class="row">
			<div class="col m-2"> 
				Nickname is optional to play the game. Nickname must be at least 2 chars and must not contain special characters. Password must be at minimum 8 characters.
			</div>
		</div>
	</div>
	<div v-if="!registerDone" class="row">
		<div class="col m-2">			
			*	Nickname
				<input v-model="usrNameTemp" class="p-3" id="username" type="text" name="username" pattern="\\S(.\\*\\S)\\?\\*" title="This field is required" size="40" maxlength="15" autocomplete="off" >				
		</div>
	</div>
	<div v-if="!registerDone" class="row">
		<div class="col">			
			*	Password
				<input v-model="usrPwTemp" class="p-3" id="pw" name="pw" type="password" minlength="8" size="40" maxlength="150" autocomplete="off"  >
		</div>
	</div>
	
	<div v-if="!registerDone" class="row">
		<div class="col">
			<button @click="registrate" class="btn btn-primary " :disabled='isRegistrateButtonDisabled' >
				Registrate
			</button>
			<span v-if="!registrationDataOk" class="text-danger"> Type (better) password</span>
		</div>
		<div v-if="errorInRegistration" class="d-inline-flex p-2 alert-danger" role="alert">
			Registration error occured! Nickname minimum 2 characters and password minimum 8 characters. Try another nickname.
		</div>
		<div v-if="errorInRegistrationDuplicate" class="d-inline-flex p-2 alert-danger" role="alert">Oops, seems like nickname is already taken, try another.</div>
	</div>
	<div v-if="registerDone" class="row" >
		<div class="col alert alert-success">
			Registration for nickname {{usrNameTemp}} is complete. You can now 
			<router-link :to="{ name: 'User' }">
                       login
            </router-link>
		</div>
	</div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { IUser } from "../interfaces";
export default defineComponent({
	name: "Registration",
	data() {
		return {			
			usrNameTemp: null,
			usrPwTemp: null,			
			registerError: null,
			registerErrorNickName: null,
			requestOnTheFly: false,
			registerDone:false
		};
	},
		computed: {		
			isRegistrateButtonDisabled() {
				if (this.requestOnTheFly) {
					return true;
				}
				return !this.registrationDataOk
			},
			errorInRegistration() {
				return this.registerError != null;
			},
			errorInRegistrationDuplicate(){
				return this.registerErrorNickName != null;
			},
			registrationDataOk(){
				if(!this.usrPwTemp || !this.usrNameTemp){
					return false
				}
				if(this.usrPwTemp===this.usrNameTemp){
					return false
				}
				if(this.usrPwTemp.startsWith("1234567")){
					return false
				}
				if(this.usrPwTemp.startsWith("qwerty")){
					return false
				}
				if(this.usrPwTemp.startsWith(" ") ||this.usrPwTemp.endsWith(" ")){
					return false
				}
				if(this.usrPwTemp.length < 8){
					return false
				}
				if(this.usrNameTemp.length <1){
					return false
				}
				return true
			}

	},

	created() {
		
	},

	methods: {
		showLogin() {
		
		},
		registrate() {
		
			this.registerError = null;
			this.registerErrorNickName =null;
			this.requestOnTheFly = true;
			const requestOptions = {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
					Authorization: null
				},
				body: JSON.stringify({
					userName: this.usrNameTemp?.trim(),
					password: this.usrPwTemp.trim(),
					
				})
			}
			const apiURL=process.env.VUE_APP_API_BASE_URL+"/portal/api/user/register"
			this.$store.dispatch("setLoadingStatus", true)
			fetch(apiURL, requestOptions)
				.then(response => {
					this.resetRegistrationData();
					if (response.status === 406) {
						return Promise.reject(406);
					}
					else if (response.status !== 200) {
						return Promise.reject("error in login");
					}
					return response.json();
				})
				.then(
					data => {						
						this.usrNameTemp = data.name
						this.registerDone = true;
					},
					(err) => {
						if(err===406){
							
							this.registerErrorNickName = true;
						}else{
							this.registerError = true;
						}						
					}
				)
				.finally(() => {
					this.requestOnTheFly = false;
					this.$store.dispatch("setLoadingStatus", false)
				});
		},
		resetRegistrationData() {
			this.usrNameTemp= null;
			this.usrPwTemp = null;
			
		}
	}

});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
