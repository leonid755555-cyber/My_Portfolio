<template>
  <div>
    <h1>{{ this.name }}</h1>
    <div class="createForm">
      <el-form :model="form">

        <el-form-item label="Auto Id">
          <el-select-v2
                class="selectDropdown"
                v-model="form.transportId"
                :options="this.allAutoIdOptions"
                placeholder="Select Auto Id"
            />
        </el-form-item>

        <el-form-item label="Employe FIO">
          <div class="selectDropdown">
            <el-select-v2
                v-model="this.form.emplId"
                :options="this.allEmplIdOptions"
                placeholder="Select FIO"
            />
          </div>
        </el-form-item>


        <el-form-item label="Date Of Receipt">
          <div class="date">
            <el-date-picker v-model="this.form.date_of_receipt"
                          type="date"
                          placeholder="Pick Date Of Receipt"
                          format="YYYY/MM/DD"
                          value-format="YYYY-MM-DD"/>
          </div>
        </el-form-item>

        <el-form-item label="Completion Date">
          <div class="date">
            <el-date-picker v-model="this.form.completion_date"
                          type="date"
                          placeholder="Pick Completion Date"
                          format="YYYY/MM/DD"
                          value-format="YYYY-MM-DD"/>
          </div>
        </el-form-item>

        <el-form-item label="Cost">
          <el-input v-model="form.cost" />
        </el-form-item>

        <el-form-item label="Work Type">
          <el-input v-model="form.work_type" />
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
          <el-select-v2
                class="selectDropdown"
                v-model="form1.transportId"
                :options="this.allAutoIdOptions"
                placeholder="Select Auto Id"
            />
        </el-form-item>

        <el-form-item label="Employe FIO">
          <div class="selectDropdown">
            <el-select-v2
                v-model="this.form1.emplId"
                :options="this.allEmplIdOptions"
                placeholder="Select FIO"
            />
          </div>
        </el-form-item>


        <el-form-item label="Date Of Receipt">
          <div class="date">
            <el-date-picker v-model="this.form1.date_of_receipt"
                          type="date"
                          placeholder="Pick Date Of Receipt"
                          format="YYYY/MM/DD"
                          value-format="YYYY-MM-DD"/>
          </div>
        </el-form-item>

        <el-form-item label="Completion Date">
          <div class="date">
            <el-date-picker v-model="this.form1.completion_date"
                          type="date"
                          placeholder="Pick Completion Date"
                          format="YYYY/MM/DD"
                          value-format="YYYY-MM-DD"/>
          </div>
        </el-form-item>

        <el-form-item label="Cost">
          <el-input v-model="form1.cost" />
        </el-form-item>

        <el-form-item label="Work Type">
          <el-input v-model="form1.work_type" />
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
import {autoIdParamOptions, personIdParamOptions} from "@/data/parameters";
const dialogFormVisible = ref(false)

export default {
  name: "Repair_reportsTable",
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
        emplId: '',
        date_of_receipt: '',
        completion_date: '',
        cost: null,
        work_type: '' 
      }),
      form1: reactive({
        id: '',
        transportId: '',
        emplId: '',
        date_of_receipt: '',
        completion_date: '',
        cost: null,
        work_type: '' 
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
    async createEntity() {
      try {
        await axios.post(this.link, this.form)
        this.getTable()
      } catch (e) {
        console.log(e);
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
  setup() {
    const allAutoIdOptions = ref(autoIdParamOptions)
    const allEmplIdOptions = ref(personIdParamOptions)
    return {allAutoIdOptions, allEmplIdOptions}
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