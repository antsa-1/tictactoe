<template>	
	<div class="row">
		<div class="col overflow-auto" style="max-height:500px !important"> 
			<ul> 
				
				<li v-for="(message, index) in messagesd" :key="index" class="list-group-item rounded-pill text-start w-75 text-break mb-1" :class="[index%2==0?'bg-success p-2 text-dark bg-opacity-25':'bg-success p-2 text-dark bg-opacity-10']">					
				
					&nbsp;{{message.from}}:&nbsp; {{message.text}}
				</li>
			</ul>		
		</div>		
	</div>
	
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { loginMixin } from "../mixins/mixins";
import { IChat } from "../interfaces";
export default defineComponent({
	name: "Chat",
	mixins: [loginMixin],
	props:["id","common"],
	data():IChat {
		return {			
			messages:null,
			users:null,
			message:{text:""},
		}
	},
	computed:{
	
		messageExist() {
			return this.message.text.length > 0
		},
		messagesd() {	
			if(this.common){
				
				return this.$store.getters.commonChatMessages
			}
			return this.$store.getters.chatMessages
		},
	},
	created(){
		
	},
	methods: {
		sendMessage(){
			let message:string=this.message.text.trim()

			if(message.length<1 || message.length >75){
				this.message.text=""
				return
			}
			const tos=this.common? "COMMON":this.id
			const obj = { title: "CHAT", message:message, to:tos}
			this.user.webSocket.send(JSON.stringify(obj));
			this.message.text="";
		}	
	},
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
