<template>
  <div>
    <h1>Enter your Username</h1>

    <div class="createForm">
      <el-form :model="form">
        <el-form-item label="User Name">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item>
          <el-button class="submit" type="success" round @click="tryUser(); dialogFormVisible = true;">Continue</el-button>

        </el-form-item>
      </el-form>
    </div>

<el-dialog v-model="dialogFormVisible"> 
   <h1> Question </h1>
   <h2>{{ this.new }}</h2>
        <el-form :model="form">
          <el-form-item label="Answer">
            <el-input v-model="form.answer" />
          </el-form-item>
          <el-form-item label="Password">
            <el-input v-model="form.password" />
          </el-form-item>
          <el-form-item label="PasswordConfirm">
            <el-input v-model="form.passwordConfirm" />
          </el-form-item>
      </el-form>
           <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">Cancel</el-button>
          <el-button type="primary" @click="updateEntity(); dialogFormVisible = false;">Confirm</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import axios from "axios";
import {reactive} from "vue";
import {ref} from "vue";
import {ElMessage} from "element-plus";
const dialogFormVisible = ref(false);
export default {
  name: "UserName",
  mounted() {
    localStorage.setItem('role', '0');
  },
  data() {
    return {
      form: reactive({
        username: '',
        password: '',
        passwordConfirm: ''
      }),
      dialogFormVisible,
      new:[]
    }
  },
  methods: {
    async updateEntity() {
      try {
      if(this.form.password === this.form.passwordConfirm)
      {
            await axios.post("/username/" + this.form.username, this.form)
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
            message: 'Wrong answer',
         
        })
      }},
    async tryUser() {
      try {
        const response = await axios.get("/username/" + this.form.username);
        this.new = response.data; 
      }
       catch (e) {
        console.log(e); 
        ElMessage({
            type: 'info',
            message: 'Wrong username',
         
        })
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