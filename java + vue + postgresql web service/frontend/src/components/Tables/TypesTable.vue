<template>
  <div>
    <h1>{{ this.name }}</h1>
    
    <div class="createForm">
      <el-form :model="form">
        <el-form-item label="name">
          <el-input v-model="form.name"/>
        </el-form-item>

        <el-form-item>
          <el-button class="submit" type="success" round @click="createEntity">Create</el-button>

        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData.rows" style="width: 100%">
      <el-table-column v-for="column in this.tableData.columns"
                       v-bind:key="column.field" :prop="column.field" :label="column.headerName"/>
      <el-table-column label="Delete Record">
        <template #default="scope">
          <el-button type="danger" round  v-on:click="deleteEntity(scope.$index)">Delete</el-button>
        </template>
      </el-table-column>

      <el-table-column label="Update Record">
        <template #default="scope">
          <el-button type="warning" round  @click="updateEntity(scope.$index)">Update</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div>
    <el-button v-if="this.params.offset > 0" @click="prevPage">Previous</el-button>
    <el-button v-if="this.params.offset < this.tableData.totalPages - 1" @click="nextPage">Next</el-button>
  </div>
</template>

<script>
import {getTableRecords1} from "@/getTableRecords";
import {ElMessage, ElMessageBox} from "element-plus";
import axios from "axios";
import {reactive} from "vue";

export default {
  name: "TypesTable",
  props: {
    link: String,
    name: String
  },
  data() {
    return {
      params: {offset: 0},
      tableData: {},
      form: reactive({
          name: ''
      }),
      form1: reactive({
          name: ''
      })
    }
  },
  mounted() {
    this.getTable()
  },
  methods: {
    getTable() {
      getTableRecords1(this.link, this.params).then((ret) => { this.tableData = ret})
    },
    nextPage(){
       this.params.offset++;
        getTableRecords1(this.link, this.params).then((ret) => { this.tableData = ret})
    },
    prevPage(){
      this.params.offset--;
      getTableRecords1(this.link, this.params).then((ret) => { this.tableData = ret})
    },
    async deleteEntity(idx) {
      let id = this.tableData.rows[idx].id
      if (await ElMessageBox.confirm("Do you really want to delete this record?",
          'Warning',
          {
            confirmButtonText: 'OK',
            cancelButtonText: 'Cancel',
            type: 'warning',
          })) {
        try {
          await axios.delete(this.link + "/" + id)
          this.tableData.rows.splice(idx, 1)
          ElMessage({
            type: 'success',
            message: 'Successfully deleted',
          })
        } catch (e) {
          console.log(e);
          ElMessage({
            type: 'info',
            message: 'Unsuccessfully deleted',
          });
        }
      }
    },
    async updateEntity(idx) {
      let id = this.tableData.rows[idx].id
      ElMessageBox.prompt('name', 'Tip', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      inputErrorMessage: 'Invalid Email',
    })
      .then( async ({ value }) => {
      this.form1.id = id
      this.form1.name = value
      try {

        const response = await axios.post(this.link, this.form1)
        this.getTable()
        this.allpersonIdOptions.push({
          value: response.data.id,
          label: response.data.FIO,
        })
        } catch (e) {
      console.log(e);
      }
    })
  },
     async createEntity() {
    try {
      const response = await axios.post(this.link, this.form)
      this.getTable()
      this.allpersonIdOptions.push({
        value: response.data.id,
        label: response.data.FIO,
      })
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