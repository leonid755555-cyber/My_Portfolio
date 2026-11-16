<template>
  <div>
    <h1>Enter your Username</h1>

    <div class="createForm">
      <el-form :model="form">
        <el-form-item label="User Name">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item>
          <el-button class="submit" type="success" round @click="login">Continue</el-button>

        </el-form-item>
      </el-form>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import {reactive} from "vue";
export default {
  name: "UserName",
  data() {
    return {
      form: reactive({
        username: ''
      })
    }
  },
  methods: {
    async answer() {
      try {
        await axios.post(this.link, this.form)
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