<template>
  <div>
    <h1>{{ this.name }}</h1>
    <div class="createForm">
      <el-form :model="form">

        <el-form-item label="Auto Id">
          <div class="selectDropdown">
            <el-select-v2
                v-model="this.form.transportId"
                :options="this.allAutoIdOptions"
                placeholder="Select Auto Id"
            />
          </div>
        </el-form-item>

        <el-form-item label="Report Date">
          <div class="date">
            <el-date-picker v-model="this.form.report_date"
                          type="date"
                          placeholder="Pick Report Date"
                          format="YYYY/MM/DD"
                          value-format="YYYY-MM-DD"/>
          </div>
        </el-form-item>
        <el-form-item label="Tickets Count">
          <el-input v-model="form.tickets_count" />
        </el-form-item>

        <el-form-item label="Mileage">
          <el-input v-model="form.mileage" />
        </el-form-item>
        <el-form-item label="Cargo Transportation">
          <el-input v-model="form.cargo_transportation" />
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
          <el-button type="warning" round  @click="dialogFormVisible = true">Update</el-button>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogFormVisible" title="Update">
        
      <el-form :model="form">

        <el-form-item label="Id">
            <el-input v-model="form1.id" />
          </el-form-item>

        <el-form-item label="Auto Id">
          <div class="selectDropdown">
            <el-select-v2
                v-model="this.form1.transportId"
                :options="this.allAutoIdOptions"
                placeholder="Select Auto Id"
            />
          </div>
        </el-form-item>

        <el-form-item label="Report Date">
          <div class="date">
            <el-date-picker v-model="this.form1.report_date"
                          type="date"
                          placeholder="Pick Report Date"
                          format="YYYY/MM/DD"
                          value-format="YYYY-MM-DD"/>
          </div>
        </el-form-item>
        <el-form-item label="Tickets Count">
          <el-input v-model="form1.tickets_count" />
        </el-form-item>

        <el-form-item label="Mileage">
          <el-input v-model="form1.mileage" />
        </el-form-item>
        <el-form-item label="Cargo Transportation">
          <el-input v-model="form1.cargo_transportation" />
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
import {ref} from "vue";
import {autoIdParamOptions} from "@/data/parameters";
const dialogFormVisible = ref(false)

export default {
  name: "EmplTable",
  props: {
    link: String,
    name: String
  },
  data() {
    return {
      params: {offset: 0},
      tableData: {},
      form: reactive({
        transportId: '',
        report_date: '',
        tickets_count: null,
        mileage: null,
        cargo_transportation: null
      }),
      form1: reactive({
        id: '',
        transportId: '',
        report_date: '',
        tickets_count: null,
        mileage: null,
        cargo_transportation: null
      }),

      dialogFormVisible
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
    async updateEntity() {
        try {
          await axios.post(this.link, this.form1)
          this.getTable()
          ElMessage({
            type: 'success',
            message: 'Successfully updated',
          })
        } catch (e) {
          console.log(e);
          ElMessage({
            type: 'info',
            message: 'Unsuccessfully updated',
          });
        }
    },
    async createEntity() {
      try {
        await axios.post(this.link, this.form)
        this.getTable()
      } catch (e) {
        console.log(e);
      }
    }
  },
  setup() {
    const allAutoIdOptions = ref(autoIdParamOptions)
    return {allAutoIdOptions}
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