<template>
  <div>
    <h1>Register</h1>

    <div class="createForm">
      <el-form :model="form">
        <el-form-item label="User Name">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item label="Password">
          <el-input :type="showInput ? text : hidden" v-model="form.password" />
        </el-form-item>

        <el-form-item label="Confirm password">
          <el-input  :type="showInput ? text : hidden" v-model="form.passwordConfirm" />
        </el-form-item>

        <el-form-item label="Question">
          <el-input v-model="form.question" />
        </el-form-item>

        <el-form-item label="Answer">
          <el-input v-model="form.answer" />
        </el-form-item>

        <el-form-item>
          <el-button class="submit" type="success" round @click="register">Register</el-button>

        </el-form-item>
      </el-form>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import {reactive} from "vue";
import {ElMessage} from "element-plus";
export default {
  name: "RegistrationPage",
  mounted() {
    localStorage.setItem('role', '0');
  },
  data() {
    return {
      form: reactive({
        username: '',
        password: '',
        passwordConfirm: '',
        question: '',
        answer: ''
      })
    }
  },
  methods: {
    async register() {
      try {
      if(this.form.password === this.form.passwordConfirm)
      {
            await axios.post("/registration", this.form)
      }
        else
        {
        ElMessage({
            type: 'info',
            message: 'Passwords do not match',
         
        });
        }
      } catch (e) {
        console.log(e);
        ElMessage({
            type: 'info',
            message: 'unexpected error',
         
        });

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