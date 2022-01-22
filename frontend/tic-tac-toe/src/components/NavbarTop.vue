<template>
  <nav  class="navbar navbar-expand-lg navbar-light rounded" style="background-color: #BC8F8F;">
    <div v-if="!loading" class="container-fluid">
      <button  class="navbar-toggler" type="button"  data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
        aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon" />
      </button>
      <div id="navbarSupportedContent" class="collapse navbar-collapse">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">     
           <li class="nav-item">
            <router-link class="nav-link"  :to="{ name: 'Home' }">Home</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link"  :to="{ name: 'Lobby' }">Play</router-link>
          </li>
          <li v-if="!authenticated" class="nav-item">
            <router-link class="nav-link"  :to="{ name: 'Registration' }">Registration</router-link>
          </li>
          <li class="nav-item dropdown">
         
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                <i class="bi bi-person-fill"></i>
              User 
            </a>            
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                 <li v-if="authenticated">                    
                    <router-link  class="dropdown-item" :to="{ name: 'User' }">Show profile</router-link>
                </li>
                <li v-if="authenticated">                    
                    <router-link @click="logout" class="dropdown-item" :to="{ name: 'User' }">Logout</router-link>
                </li>
                  <li v-if="!authenticated">               
                     <router-link class="dropdown-item" :to="{ name: 'User' }">
                       Login
                    </router-link>
                </li>                         
            </ul>
          </li>
          <li class="nav-item">
              <router-link  class="nav-link"  :to="{ name: 'Info' }">
                    Terms and conditions
              </router-link>
          </li>
          <li class="nav-item">
              <router-link  class="nav-link"  :to="{ name: 'Feedback' }">
                Feedback
              </router-link>
          </li> 
        </ul>
      </div>
    </div>
    <div v-if="loading" class="container-fluid">
      <div class="spinner-border" role="status">
        <span class="sr-only"></span>
      </div>
    </div>
  </nav>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { loginMixin } from "../mixins/mixins";
import { watch} from "vue";
export default defineComponent({
	name: "NavbarTop",
  mixins: [loginMixin],	
	props: {
		msg: String
	},
  data() {
    return {

    }
	},
  

  created(){
    
  },
	computed: {
    loading(){
        return this.$store.getters.loadingStatus
    }
	},
	methods: {
 
  }
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
