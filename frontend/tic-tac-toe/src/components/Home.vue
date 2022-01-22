<template>
	<div class="row">
		<div class="col fw-bold">     		
				Hi and welcome to play tic-tac-toe
			
    	</div>		
	</div>
	<div class="row mt-4">
		<div class="col question">
			Play anonymous
			<svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-arrow-right" viewBox="0 0 25 25">
				<path fill-rule="evenodd" d="M1 8a.5.5 0 0 1 .5-.5h11.793l-3.147-3.146a.5.5 0 0 1 .708-.708l4 4a.5.5 0 0 1 0 .708l-4 4a.5.5 0 0 1-.708-.708L13.293 8.5H1.5A.5.5 0 0 1 1 8z"/>
			</svg>
		</div>
		<div class="col ">			
			<p class=" text-start">
				1. Click play on the top navigation bar HHHHHHHHH
				<br>
				2. Join table
				<br>
				3. Or create your own table with selected options
				<br>
				4. Invite your friend to play or wait for random player
			</p>
		</div>
	</div>
	<div class="row">
		<div class="col question">
					Play with nickname
					<svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-arrow-right" viewBox="0 0 25 25">
						<path fill-rule="evenodd" d="M1 8a.5.5 0 0 1 .5-.5h11.793l-3.147-3.146a.5.5 0 0 1 .708-.708l4 4a.5.5 0 0 1 0 .708l-4 4a.5.5 0 0 1-.708-.708L13.293 8.5H1.5A.5.5 0 0 1 1 8z"/>
					</svg>
		</div>
		<div class="col">			
			<p class=" text-start">
				1. Navigate to nickname registration page
				<br>
				2. Type nickname and password
				<br>
				3. After registration, login and click play
				<br>
				
			</p>
		</div>
	</div>

</template>

<script lang="ts">
import { defineComponent } from "vue";
import { loginMixin } from "../mixins/mixins";
export default defineComponent({
	name: "Home",
	
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
div.question{
	font-size:1.25rem;
}
</style>
