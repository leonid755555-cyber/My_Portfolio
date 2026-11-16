<template>
  <div class="home">
      <div>
        <img src="@/assets/original.jpg" alt="home">
      </div>
      <h1>Welcome Home</h1>
      <pre>
        This is database study project called "car company database".
        
      </pre>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "HomePage",
  mounted() {
    this.getRole();
  },
  methods: {
    async getRole() {
          try{
          await axios.get("/admin");
          localStorage.setItem('role', '2');
          }
          catch (error) {
          console.log(error);
          if(error.status === '302')
          {
            localStorage.setItem('role', '0');
          }

          else if(error.status === '403')
          {
            localStorage.setItem('role', '1');         
          }
          }
        }
      }
    }

</script>

<style scoped>

home {
  margin: 20px;
  text-align: center;
}

h1 {
  margin-top: 30px;
  margin-right: 100px;
  font-size: 55px;
  font-family: "Akshar";
}

img {
  float: left;
  height: 840px;
  margin-left: 10px;
  margin-right: 180px;
  width: 650px;
}
pre {
  text-align: justify;
  margin-right: 120px;
  margin-top: 200px;
  font-size: 35px;
  font-family: "Akshar";
}
</style>