<template>
  <div>
    <h1>{{this.name}}</h1>

    <div class="createForm">
      <el-form :model="text">
        <el-form-item>
          <el-input v-model="form.text" />
        </el-form-item>

        <el-form-item>
          <el-button class="submit" type="success" round @click="createEntity">Confirm</el-button>
        </el-form-item>
      </el-form>
    </div>



  </div>
</template>

<script>
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import axios from "axios";
export default {
  name: "EmplTable",
  props: {
    link: String,
    name: String
  },
  data() {
    return {
      tableData: {},
      form: reactive({
        text: ''}),
    }
  },
  methods: {
    async createEntity() { 
      const response = await axios.get(this.link, {params: this.form})
        if(response.status == 200){
            ElMessage({
            type: 'success',
            message: 'Successfully',
          })
        }
        if(response.status != 200){
            ElMessage({
            type: 'info',
            message: 'Unsuccessfully',
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