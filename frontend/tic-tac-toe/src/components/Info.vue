<template>
	<div class="row mb-3 mt-2">
		<div class="col ">
			<span class="fw-bold">
				<h3>Terms and conditions *date here*</h3>
			</span>			
			
			<p class="">
				<span class=" fw-bold">Browsers</span>
				<br>
				 Recommended browsers are latest versions of Firefox, Chrome and Edge.
				 <br>
			</p>
			<p class="">
				<span class=" fw-bold">Intention of the site</span>
				<br>
				To play tic-tac-toe with variable board sizes with variable amount of connecting points.
			</p>			
		</div>
	</div>		
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { IChat } from "../interfaces";
export default defineComponent({
	name: "Info",

	data(){
		return {			
			messages:null,
			message:{text:""},
		}
	},
	computed:{
	
		messageExist() {
			return this.message.text.length > 0
		},
		
	},
	created(){
		
	},
	methods: {
		sendMessage(){
			let message:string=this.message.text.trim()
			if(message.length<1 || message.length >200){				
				return
			}
			
		}	
	},
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
