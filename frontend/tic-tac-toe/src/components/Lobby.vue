<template>
	<div class="row mt-3">
		<div class="col-xs-12 col-sm-4 mt-3 mt-md-0 pt-3">
			<span class="fw-bold ">
				Players
			</span>		
			<ul class="list-group ">						
				<li v-for="(user, index) in users" :key="user.name" class="list-group-item" :class="[index%2==0?'bg-success p-2 text-dark bg-opacity-25':'bg-success p-2 text-dark bg-opacity-10']">					
					<div class="float-start ">
						{{user.name}}
						
						<svg v-if="user.name===userName" xmlns="http://www.w3.org/2000/svg" width="23" height="23" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
  							<path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
						</svg>
					</div>
				</li>
			</ul>
		</div>
		<div class="col-xs-12 col-sm-4 mt-3 mt-md-0">
			<div class="row">
				<div class="col-xs-6">
					<span class="fw-bold ">
						Tables
					</span>
					<span class="float-end">
						<button v-if="isCreateTableButtonVisible" type="button" class="btn btn-primary w-30 float-start" data-bs-toggle="modal" data-bs-target="#createTableModal">
							Create table
						</button>
						<button v-if="isRemoveTableButtonVisible" @click="removeTable()" type="button" class="btn btn-primary bg-danger w-30 float-start" >
							Remove table
						</button>
					</span>
				</div>
				
			</div>			
			<div class="row">
				
				<div v-if="tablesExist" class="col">
				
					<ul class="list-group">
						<li v-for="(table, index) in tables" :key="table.id" class="list-group-item" :class="[index%2==0?'bg-info p-2 text-dark bg-opacity-25':'bg-info p-2 text-dark bg-opacity-10']">
							<svg v-if="isOwnTable(table)" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="green" class="bi bi-table float-start" viewBox="0 0 16 16">
								<path d="M0 2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V2zm15 2h-4v3h4V4zm0 4h-4v3h4V8zm0 4h-4v3h3a1 1 0 0 0 1-1v-2zm-5 3v-3H6v3h4zm-5 0v-3H1v2a1 1 0 0 0 1 1h3zm-4-4h4V8H1v3zm0-4h4V4H1v3zm5-3v3h4V4H6zm4 4H6v3h4V8z"/>
							</svg>	
							{{table?.playerA?.name}} - {{table?.playerB?.name}}
							<br> 
							({{table.x}}x{{table.y}})
							<div v-if="!isOwnTable(table)" class="row">
								<div class="col">
									<button v-if="playButtonVisible(table)"  :disabled="!createTableButtonVisible" @click="play(table)" type="button" class="btn btn-primary w-30 float-start">
										Play
									</button>									
									<button @click="watchTable(table)" :disabled="!createTableButtonVisible || !table.playerA || !table.playerB" type="button" class="btn btn-primary w-30 float-end">
										Watch
									</button>	
								</div>
							</div>
							<div v-else class="row">
								<div class="col">
									<span class="fw-bold">
										Your table waiting others to join the table...
									</span>
								</div>
							</div>
						</li>
					</ul>
				</div>
				<div v-else class="col fw-bold">
					<span class="float-start"> 
						No tables at the moment. 
					</span>
				</div>
			</div>
		</div>
	
	</div>
	<div class="modal fade" id="createTableModal" tabindex="-1" aria-labelledby="createTableModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="createTableModalLabel">Create table</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">
						<form>
							<div class="mb-3" id="v-model-select-dynamic">						
							<select v-model="selectedGameMode">
								<option :value="0" :key="0" >
									Select board size
								</option>
								<option v-for="gameMode in gameModes" :value="gameMode.id" :key="gameMode.id">
									{{gameMode.name}} connect {{gameMode.requiredConnections}}
								</option>
							</select>			
						</div>
						<div class="mb-3" id="computer_selection">
							<input class="form-check-input mr-2" type="checkbox" id="computerSelection" @change="computerSelectionOnChanged" v-model="playAgainstComputerChecked">
							<label class="form-check-label" for="computerSelection">Play against computer</label>
							
						</div>						
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary float-start" data-bs-dismiss="modal">Close</button>
					<button :disabled="modalCreateTableDisabled" @click="createTable" type="button" data-bs-dismiss="modal" class="btn btn-primary">
						Create table
					</button>
				</div>
			</div>
		</div>
	</div>


</template>

<script lang="ts">
import { defineComponent } from "vue";
import { IGameMode, IGameToken, ITable, IUser,ISquare, IChatMessage, IWinMessage,IWinTitle,IWin, IGameResult } from "../interfaces";
import { loginMixin } from "../mixins/mixins";
import { useRoute } from "vue-router";
import Chat from "./Chat.vue";
export default defineComponent({
	
	name: "Lobby",

	mixins: [loginMixin],

	data() {
		return {		
			createTableButtonVisible:true,
			watchTableButtonDisabled:false,
			removeTableButtonVisible:false,
			selectedGameMode:"0",
			playAgainstComputerChecked:false,
			computerLevel:"0"
		}
	},
	computed:{
	
		users() {
			return this.$store.getters.users
		},
		tables(){
			return this.$store.getters.tables
		},
		gameModes(){		
			return this.$store.getters.gameModes
		},
		modalCreateTableDisabled(){
			return this.selectedGameMode === "0"
			
		},
		tablesExist(){			
			return this.$store.getters.tables.length > 0
		},
		isCreateTableButtonVisible(){
			const firstTable:ITable = this.$store.getters.tables[0]
			if(firstTable && firstTable.playerA){
				return firstTable.playerA.name!==this.userName
			}
			return true
		},
		isRemoveTableButtonVisible(){
			const firstTable:ITable = this.$store.getters.tables[0]
			if(firstTable && firstTable.playerA){
				return firstTable.playerA.name===this.userName
			}
			return false
		},		
	},
	created() {
		
		if (this.user && !this.user.webSocket) {
			
			this.connect(this.user.token);
		}else{
			
		}
	},
	methods: {
		connect(token: string): void {		
			let websocket = new WebSocket(process.env.VUE_APP_WS_URL);

			websocket.onopen = event => {
				const title2 = "LOGIN";
				const obj = { title: title2, message: this.user.token };
				const myJSON = JSON.stringify(obj);
				const loginMessage = JSON.stringify(obj);
				websocket.send(loginMessage);
			};
			websocket.onmessage = event => {
				let data = JSON.parse(event.data);

				switch (data.title) {
					case "LOGIN":
						
						let user: IUser = {
							name: data.to,
							token: data.token,
							webSocket: websocket
						};
					 	this.$store.dispatch("setUser", user).then(() => {
							this.$store.dispatch("setUsers", data.players)
							this.$store.dispatch("setGameModes", data.gameModes)
							this.$store.dispatch("setTables", data.tables)
						})	
						break;
					case "CREATE_TABLE":
						
						this.$store.dispatch("addTable", data.table)						
						if (data.table.playerA.name===this.userName){
							
							this.createTableButtonVisible=false;
							this.removeTableButtonVisible =true
						}
						break;

					case "REMOVE_PLAYER":
						this.$store.dispatch("leaveTable", data.who.name).then(()=>{
							this.$store.dispatch("removePlayer", data.who)
							if(data.table){
							this.$store.dispatch("removeTable", data.table)
						}
						})
					
						break;
					case "REMOVE_TABLE":							
						this.$store.dispatch("removeTable", data.table)
						const tablesb:ITable=data.table
						if (tablesb.playerA && tablesb.playerA.name===this.userName){
							this.createTableButtonVisible=true;
							this.removeTableButtonVisible =false
						}
						break;
					case "NEW_PLAYER":
						
						this.$store.dispatch("addPlayer", data.who)						
						break;
					case "START_GAME":
						
						this.$store.dispatch("startGame", data.table)
						const tableC:ITable=data.table
						if (tableC.playerA.name===this.userName || tableC.playerB.name===this.userName ){
							tableC.playerA.wins=0
							tableC.playerB.wins=0
							this.createTableButtonVisible = false
							this.removeTableButtonVisible = false
							this.$store.dispatch("selectTable", data.table).then(() => {
								this.$router.push({ name: 'Table', id:data.table.id})
							})
						}						
						break;
					case "MOVE":
						const square :ISquare = {x: data.x, y: data.y, coordinates: data.x.toString().concat(data.y.toString()), token:data.message}
						
						this.$store.dispatch("move", square)
						this.$store.dispatch("changeTurn", data.table.playerInTurn.name)
						break;
					case "WATCH":						
						this.$store.dispatch("selectTable", data.table).then(() => {
							this.$router.push({ name: 'Table', params: { watch: "1" } })
						})		
						break;
					case "ADD_WATCHER":						
						this.$store.dispatch("addWatcher", data.who).then(() => {
							
						})		
						break;				
					case "CHAT":
						const message:IChatMessage={text: data.message,from:data.from}
						if(data.to==="COMMON"){
							this.$store.dispatch("updateCommonChat", message)
							return
						}
						this.$store.dispatch("chat", message)
						break;
					case "REMATCH":
						this.$store.dispatch("rematch", data.table).then(() => {
							//this.$router.push({ name: 'Table', id:data.table.id})
						})
						break;
					case "LEAVE_TABLE":		 				
						this.$store.dispatch("leaveTable", data.who.name)
						break;
					case "GAME_END":
							const lastSquare :ISquare = {x: data.x, y: data.y, coordinates: data.x.toString().concat(data.y.toString()), token:data.message}							
							this.$store.dispatch("move", lastSquare)							
							if(data.win.draw){
								const gameResult:IGameResult={table:data.table,win:data.win}
								
								this.$store.dispatch("setDraw",gameResult);
							}
							else
							{
								const updateScoreMessage:IWinMessage =
								{
									winner:data.win.player.name,
									reason: IWinTitle.GAME,
									winsA:data.table.playerA.wins,
									winsB:data.table.playerB.wins,
									from:"System"
								}									
								this.$store.dispatch("updateScore", updateScoreMessage);
								const win:IWin={
									fromX:data.win.fromX,
									fromY:data.win.fromY,
									toX:data.win.toX,
									toY:data.win.toY,
									winner:{name:data.win.player.name},	
								}							
								this.$store.dispatch("updateWinner", win);
							}		
						break;
					case "WINNER":
						const winMessage:IWinMessage={
							winner:data.who.name,
							reason:data.reason,
							winsA:data.table.playerA.wins,
							winsB:data.table.playerB.wins,
							from:data.from
						}
						this.$store.dispatch("updateScore", winMessage);
						const title: IWinTitle = data.message ==="R"?IWinTitle.RESIGNITION:IWinTitle.GAME
						let chatText = data.who.name.concat( title===IWinTitle.RESIGNITION? " won by resignation": " won")
               			const chatMessag:IChatMessage = {
							from:data.from,
							text:chatText
						}
						this.$store.dispatch("chat", chatMessag);
						break;
                }
			};
			websocket.onerror = event => {
				
			};
			websocket.onclose = event => {
				
				if(this.user){
					this.user.webSocket=null
				}
			//	this.logout();
			};
		},
		createTable(){
		 	
			const obj = { title: "CREATE_TABLE", message: this.selectedGameMode,"computer":this.playAgainstComputerChecked};			
			this.user.webSocket.send(JSON.stringify(obj));
			this.computerLevel=null;
			this.playAgainstComputerChecked=false;
		},
		computerSelectionOnChanged(){
			if(!this.playAgainstComputerChecked){
				
				this.computerLevel="0";
			}
		},
		removeTable(){
			
			const obj = { title: "REMOVE_TABLE",message:""};
			this.user.webSocket.send(JSON.stringify(obj));
		},
		play(table:ITable){
			
			const obj = { title: "JOIN_TABLE", message:table.id};
			this.user.webSocket.send(JSON.stringify(obj));
		},
		playButtonVisible(table:ITable){
			if(table.playerA && table.playerB){
				return false				
			}			
			return true
		},
		isOwnTable(table:ITable){
			return	table?.playerA?.name === this.userName
		},
		watchTable(table:ITable){
			
			const obj = { title: "WATCH",message:table.id};
			this.user.webSocket.send(JSON.stringify(obj));
		},
	}
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.players{
	background-color:#8EF0BF
}


</style>
