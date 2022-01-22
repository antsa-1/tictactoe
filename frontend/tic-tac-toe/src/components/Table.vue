<template>
	<div class="row">
		<div class="col">
			<button v-if="resignButtonVisible" :disabled ="resignButtonDisabled" @click="resign" type="button" class="btn btn-primary w-30 float-xs-start float-sm-end">
				Resign
			</button>			
			<button v-if="rematchButtonEnabled" @click="rematch" type="button" class="btn btn-primary w-30 float-xs-start float-sm-end">
				Rematch
			</button>
		</div>
	</div>

	<div class="row">
		<div class="col-xs-12 col-sm-4">
			Connect {{theTable.gameMode.requiredConnections}} to win
				<br>
				<input class="form-check-input " type="checkbox" id="highlight" @change="highlightLastSquareIfSelected" v-model="showLastVal">
				<label class="form-check-label ms-1" for="highlight">Highlight last</label>
				<br>
				<input class="form-check-input " type="checkbox" id="soundOn" v-model="soundOn">
				<label class="form-check-label ms-1" for="soundOn">Notification sound</label>
				<br>				
				<span  v-if="theTable.playerA" class="fw-bold" :class="{'text-success':  theTable.playerInTurn?.name=== theTable?.playerA?.name}">
				 	{{theTable?.playerA?.gameToken}} = {{theTable?.playerA?.name}} 
				</span> 		
				<span class="text-primary"> [ w:{{ theTable?.playerA?.wins }} , d:{{ theTable?.playerA?.draws }} ] </span>		
				<br> vs.
				<br>
				 <span v-if="theTable.playerB" class="fw-bold"  :class="{'text-success': theTable.playerInTurn?.name===  theTable?.playerB?.name}">
					 {{theTable?.playerB?.gameToken}} =  {{theTable?.playerB?.name}}   
				</span>
				<span class="text-primary"> [ w:{{ theTable?.playerB?.wins }} , d:{{ theTable?.playerB?.draws }} ]  </span>
			<span v-if="theTable.playerInTurn?.name === userName" class="text-success"> It's your turn. </span>
			<span v-else-if="theTable?.playerInTurn === null" class="text-success"> Game ended </span>	
			<div v-else class="text-danger"> In turn: {{theTable.playerInTurn?.name}}</div>
			
			<div :class="{'hidden': theTable.playerInTurn?.name !== userName}">
				 <span class="text-danger"> Time left {{timeLeft}} </span>
			</div>
		</div>
		<div class="col-xs-12 col-sm-8">
			 <canvas :class="{'bg-secondary':theTable.playerInTurn ==null}" id="canvas" ></canvas>
    	</div>
		
	</div>
	<chat :id="theTable.id"> </chat>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import {IGameMode,IGameToken,	ITable,	IUser,ISquare, IWin} from "../interfaces";
import { loginMixin } from "../mixins/mixins";
import { useRoute } from "vue-router";
import Chat from "./Chat.vue";


export default defineComponent({
	components: { Chat },
	name: "Table",
	mixins: [loginMixin],
	props:["watch"],
	data(){
		return{
			horizontalGap: null,
			verticalGap: null,
			canvas:null,
			renderingContext:null,
			showLastVal:false,
			soundOn:false,
			secondsLeft:120,
		}
	},
	created() {
		
		this.unsubscribe = this.$store.subscribe((mutation, state) => {
			if (mutation.type === "move") {
				const board : ISquare[]= state.theTable.board
				this.removeLastSquareHighLightning()
				this.drawToken(this.theTable.board[this.theTable.board.length -1])
				this.playMoveNotification()
				this.highlightLastSquareIfSelected()
			}else if (mutation.type === "changeTurn") {
				if(state.theTable.playerInTurn.name === this.userName){
					this.addMouseListener()
					this.startReducer()
				}else{
					this.removeMouseListener()
					this.stopReducer()
				}
			}else if (mutation.type === "rematch" ){			
				this.initBoard()			
			}
			else if (mutation.type === "updateWinner" ){			
				const win:IWin = state.theTable.win
				this.stopReducer()
				this.drawWinningLine(win)
			}
    	})
		if(this.theTable.playerInTurn?.name===this.userName){
			this.startTime=120
			this.startReducer()
		}
	},
	computed: {
		timeLeft(){
			return this.secondsLeft
		},
		theTable() :ITable{
			
			return this.$store.getters.theTable;
		},
		iconColor(){
		 	const watchers=this.$store.getters.theTable.watchers
			 if(watchers && watchers.length>1){
				 return "green"
			 }

			 return "#000000"
		},
		watchers(){
			return this.$store.getters.theTable.watchers
		},
		resignButtonDisabled(){
			const table=this.$store.getters.theTable;
			if(this.watch){
			
				return true
			}else if(this.theTable && !this.theTable.playerInTurn){
					
				return true
			}
			else if(this.theTable.playerInTurn.name!==this.userName){
					
				return true
			}else if(this.theTable && this.theTable.board&& this.theTable.board.length<4){
					
				return true
			}
				
			return false
		},
		rematchButtonEnabled(){
				const table=this.$store.getters.theTable;
			 return !this.watch && table.playerInTurn ===null
		},
		resignButtonVisible(){
			return this.userName===this.theTable?.playerA?.name || this.userName===this.theTable?.playerB?.name
		}
	
	},
	mounted() {
		if(!this.$store.getters.theTable){
			
			this.$router.push('/error')
			return;
		}
		this.initBoard()
		
		if(this.watch==="1"){
			this.drawBoard()
		}
	},
	beforeUnmount() {
    	this.unsubscribe()
		this.leaveTable()
  	},
	methods: {
		startReducer(){			
			if(this.redurcerInterval){
				this.stopReducer()
			}
			this.secondsLeft = 120;
			this.redurcerInterval= setInterval(()=>{
				this.secondsLeft = this.secondsLeft-1
				if(this.secondsLeft <= 0){
					this.stopReducer()
					this.resign()
				}
			}, 1000)
		},
		
		stopReducer(){
			clearInterval(this.redurcerInterval)
			this.secondsLeft=null
		},
		highlightLastSquareIfSelected(){
		
			if(this.showLastVal && this.theTable.board.length>1){
				
				const board=this.theTable.board
				const lastSquare:ISquare=board[board.length-1]
				this.drawToken(lastSquare,"#FF0000")		
			}
		},
		playMoveNotification(){
			if(this.soundOn){
				const audioCtx = new (window.AudioContext )();

				const oscillator = audioCtx.createOscillator();

				oscillator.type = "sine";
				oscillator.frequency.setValueAtTime(446, audioCtx.currentTime); // value in hertz
				
				oscillator.connect(audioCtx.destination);
				oscillator.start();
				setTimeout(
					()=> {
					oscillator.stop();
					},
					250
				);
			}
		},
		removeLastSquareHighLightning(){
			if(this.theTable.board.length>1){
				
				const board=this.theTable.board
				const lastSquare:ISquare=board[board.length-2]
				this.drawToken(lastSquare,"#000000")	
			}
		},
		removeMouseListener(){
			this.canvas.removeEventListener("click", this.handleClick, false);
		},
		addMouseListener(){
			this.canvas.addEventListener("click", this.handleClick, false);
		},
		drawWinningLine(win:IWin){
			
			this.renderingContext.beginPath();
			this.renderingContext.moveTo(win.fromX*this.horizontalGap+this.horizontalGap/2-10, win.fromY*this.verticalGap+this.verticalGap/2);
			this.renderingContext.lineTo(win.toX*this.horizontalGap+this.horizontalGap/2+10, win.toY*this.verticalGap+this.verticalGap/2);
			this.renderingContext.strokeStyle = "#00FFFF";
			this.renderingContext.lineWidth = 5;
			this.renderingContext.stroke();
		},
		initBoard() {
			let table: ITable = this.theTable
			this.canvas = document.getElementById("canvas") 
			this.renderingContext = this.canvas.getContext("2d");
			let vpHeight = window.innerHeight;
			let vpWidth = window.innerWidth;
			
			this.canvas.height=400
			this.canvas.width=400
			if(table.x>=10 && table.x<=20){
				this.canvas.height=600
				this.canvas.width=600
			}else if(table.x>20){
				this.canvas.height=800
				this.canvas.width=800
			}	
			
			let verticalLineAmount= table.x - 1
			let horizontalLineAmount = table.y - 1
			this.verticalGap = this.canvas.height / table.x
		 	this.horizontalGap = this.canvas.width / table.y
			
			 let tempX = 0;
			let tempY = 0;
			//Vertical lines
			for (let i = 0; i < verticalLineAmount; i++) {
					tempX += this.horizontalGap;
					this.renderingContext.beginPath();
					this.renderingContext.moveTo(tempX, tempY);
					this.renderingContext.lineTo(tempX, this.canvas.height);
					this.renderingContext.stroke();
			}
			tempX = 0;
			tempY = 0;
			for (let i = 0; i < horizontalLineAmount; i++) {
					tempY += this.verticalGap;
					this.renderingContext.beginPath();
					this.renderingContext.moveTo(tempX, tempY);
					this.renderingContext.lineTo(this.canvas.width, tempY);
					this.renderingContext.stroke();
			}
			if(this.theTable.playerInTurn.name === this.userName){				
				this.canvas.addEventListener("click", this.handleClick, false);
			}
		},
		drawBoard(){
			
		 	let tokens:ISquare[]=this.theTable.board
			tokens.forEach(element => {
				this.drawToken(element)
			});
		},
	
		drawToken(square: ISquare,color:string) {
			
			const token:IGameToken = square.token
			const x=square.x
			const y=square.y
		
			if(token == IGameToken.X){
					const xStart=Math.floor(x * this.horizontalGap+ (this.horizontalGap * 0.1))
					const yStart=Math.floor(y * this.verticalGap +(this.verticalGap * 0.1))
					if(color){
						this.renderingContext.strokeStyle = color
					}
					this.renderingContext.beginPath()
					this.renderingContext.moveTo(xStart, yStart)
					this.renderingContext.lineTo(xStart+	this.horizontalGap- (this.horizontalGap*0.2), yStart+ this.verticalGap-(this.verticalGap*0.2))
					this.renderingContext.stroke()
					this.renderingContext.beginPath()
					this.renderingContext.moveTo(xStart, yStart+	this.verticalGap- (this.verticalGap*0.2))
					this.renderingContext.lineTo(xStart+	this.horizontalGap- (this.horizontalGap*0.2), yStart)
					this.renderingContext.stroke()
			}else{
					if(color){
						this.renderingContext.strokeStyle = color
					}
					const xCenter=	this.horizontalGap/2+ (x*this.horizontalGap)
					const yCenter=	this.verticalGap/2+ (y*	this.verticalGap)
					const r = this.verticalGap/2 *0.85
					this.renderingContext.beginPath();
					this.renderingContext.arc(xCenter, yCenter, r, 0, 2 * Math.PI);
					this.renderingContext.stroke(); 
			}				
		},
		handleClick(event: MouseEvent) {
			const x = Math.floor(event.offsetX/this.verticalGap);
			const y = Math.floor(event.offsetY/this.horizontalGap);		
			let clickedSquare: ISquare = {
				x: x,
				y: y,
				coordinates: x.toString().concat(y.toString())
			};
			
			const elementExist =this.theTable.board.find(square => square.coordinates===clickedSquare.coordinates)
			if(!elementExist){
				
				this.removeMouseListener()
				const obj = { title: "MOVE",x:clickedSquare.x,y:clickedSquare.y};
				this.user.webSocket.send(JSON.stringify(obj));
				this.removeMouseListener()		
			}
		},
		disableBoard(){
				this.renderingContext.font = "30px Arial";
				this.renderingContext.fillText("Game over", 10, this.canvas.height /2);
				return true
		},
		rematch(){
			
			const obj ={title:"REMATCH", message:""}
			this.user.webSocket.send(JSON.stringify(obj));	
		},
		resign(){
			
			const obj ={title:"RESIGN", message:this.tablea}
			this.user.webSocket.send(JSON.stringify(obj));	
		},
		leaveTable(){
			const obj ={title:"LEAVE_TABLE", message:this.theTable.id}
			this.user.webSocket.send(JSON.stringify(obj));
		}
	},
	
});
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.hidden{
	visibility:hidden
}
</style>
