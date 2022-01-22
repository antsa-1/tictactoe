<template>
<div class="row">
	<div class="col text-danger">
    	An error occured. Try navigating to
		 <router-link :to="{ name: 'Home' }">main page</router-link>
	</div>
</div>
</template>  

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
	name: "Error",
	methods: {
		toMainPage(){
			this.$router.push('/')
		},
	
	},
	created(){
		
	}
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
