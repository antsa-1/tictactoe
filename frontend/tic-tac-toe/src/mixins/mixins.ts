import { IUser } from "@/interfaces";

export const ANONYM = "Anom:"
export const loginMixin = {
	data() {
		return {
			token: null
		};
	},
	computed: {
		authenticated() {
			let user: IUser = this.$store.getters.user 
			if(user && user.token && !user.token.startsWith("null") &&!user.token.startsWith("Anom:")){
				return true;
			}
			return false;
		},
		userName() {
			return this.$store.getters.user?.name
		},
		user(){
			return this.$store.getters.user
		}

	},
	created() {
		

	},
	methods: {
		getToken() {
			const user: IUser = this.$store.getters.user
			if (user) {
				return user.token
			}
			return null
		},
		logout() {
			return this.$store.dispatch("logout", this.$store.getters.user);			
		}
	}
};