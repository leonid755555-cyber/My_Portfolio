<template>
  <div>
    <h1>Login</h1>

    <div class="createForm">
      <el-form :model="params">
        <el-form-item label="User Name">
          <el-input v-model="params.username" />
        </el-form-item>

        <el-form-item label="Password">
          <el-input v-model="params.password" />
        </el-form-item>

        <el-form-item>
          <el-button class="submit" type="success" round @click="login">Login</el-button>
          <el-button class="submit" round @click="this.handle('Username Page')">Don't remember a password?</el-button>

        </el-form-item>
      </el-form>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import {reactive} from "vue";
export default {
  name: "LoginPage",
  data() {
    return {
      params: reactive({
        username: '',
        password: '',
        passwordConfirm: '123',
        question: '123',
        answer: '123'
      })
    }
  },
  methods: {
    handle(name) {
      this.$router.push({name: name})
    },
    async login() {
      try {
        await axios.post("/login", {params: this.params})
      } catch (e) {
        console.log(e);
      }
    }
  }
}
</script>

<style scoped>
.createForm {
  width: 520px;
  margin-left: 35%;
  border: 2px solid #22a2e3;
  border-radius: 20px;
  padding: 30px 30px 15px;
  margin-bottom: 30px;
}
</style>