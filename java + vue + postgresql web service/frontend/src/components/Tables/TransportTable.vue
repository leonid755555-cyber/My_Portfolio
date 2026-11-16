<template>
  <div>
    <h1>{{ this.name }}</h1>
    <div class="createForm">
      <el-form :model="form">

        <el-form-item label="Type name">
          <el-select-v2
                class="selectDropdown"
                v-model="form.typeId"
                :options="this.allTypeNameOptions"
                placeholder="Select Type"
          />
        </el-form-item>

        <el-form-item label="Place name">
          <el-select-v2
              class="selectDropdown"
              v-model="form.placeId"
              :options="this.allplaceIdOptions"
              placeholder="Select Place"
          />
        </el-form-item>

        <el-form-item label="Date Of Purchase">
          <div class="date">
            <el-date-picker v-model="this.form.dt1"
                          type="date"
                          placeholder="Pick Date Of Purchase"
                          format="YYYY/MM/DD"
                          value-format="YYYY-MM-DD"/>
          </div>
        </el-form-item>

        <el-form-item label="End Of Life">
          <div class="date">
            <el-date-picker v-model="this.form.working_life"
                          type="date"
                          placeholder="Pick End-Of-Life Date"
                          format="YYYY/MM/DD"
                          value-format="YYYY-MM-DD"/>
          </div>
        </el-form-item>
        
        <el-form-item v-if="form.typeId === 1" label="Capacity">
          <el-input v-model="formPropValue1.value" placeholder="Pick Capacity"/>
        </el-form-item>
        
        <el-form-item v-if="form.typeId === 1" label="High Speed">
          <el-input v-model="formPropValue2.value" placeholder="Pick High Speed"/>
        </el-form-item>
        
        <el-form-item v-if="form.typeId === 2" label="Capacity">
          <el-input v-model="formPropValue3.value" placeholder="Pick Capacity"/>
        </el-form-item>

        <el-form-item v-if="form.typeId === 2" label="Load Capacity">
          <el-input v-model="formPropValue4.value" placeholder="Pick Load Capacity"/>
        </el-form-item>

        <el-form-item v-if="form.typeId === 2" label="High Speed">
          <el-input v-model="formPropValue5.value" placeholder="Pick High Speed"/>
        </el-form-item>

        <el-form-item v-if="form.typeId === 3" label="Load Capacity">
          <el-input v-model="formPropValue6.value" placeholder="Pick Load Capacity"/>
        </el-form-item>
        
        <el-form-item v-if="form.typeId === 3" label="Capacity">
          <el-input v-model="formPropValue7.value" placeholder="Pick Capacity"/>
        </el-form-item>

        <el-form-item v-if="form.typeId === 3" label="High Speed">
          <el-input v-model="formPropValue8.value" placeholder="Pick High Speed"/>
        </el-form-item>
        
        <el-form-item v-if="form.typeId === 4" label="Capacity">
          <el-input v-model="formPropValue9.value" placeholder="Pick Capacity"/>
        </el-form-item>

        <el-form-item v-if="form.typeId === 4" label="High Speed">
          <el-input v-model="formPropValue10.value" placeholder="Pick High Speed"/>
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

        <el-form-item label="Type name">
          <el-select-v2
                class="selectDropdown"
                v-model="form1.typeId"
                :options="this.allTypeNameOptions"
                placeholder="Select Type"
          />
        </el-form-item>

        <el-form-item label="Place name">
          <el-select-v2
              class="selectDropdown"
              v-model="form1.placeId"
              :options="this.allplaceIdOptions"
              placeholder="Select Place"
          />
        </el-form-item>

        <el-form-item label="Date Of Purchase">
          <div class="date">
            <el-date-picker v-model="this.form1.dt1"
                          type="date"
                          placeholder="Pick Date Of Purchase"
                          format="YYYY/MM/DD"
                          value-format="YYYY-MM-DD"/>
          </div>
        </el-form-item>

        <el-form-item label="End Of Life">
          <div class="date">
            <el-date-picker v-model="this.form1.working_life"
                          type="date"
                          placeholder="Pick End-Of-Life Date"
                          format="YYYY/MM/DD"
                          value-format="YYYY-MM-DD"/>
          </div>
        </el-form-item>
        
        <el-form-item v-if="form1.typeId === 1" label="Capacity">
          <el-input v-model="formPropValue1.value" placeholder="Pick Capacity"/>
        </el-form-item>
        
        <el-form-item v-if="form1.typeId === 1" label="High Speed">
          <el-input v-model="formPropValue2.value" placeholder="Pick High Speed"/>
        </el-form-item>
        
        <el-form-item v-if="form1.typeId === 2" label="Capacity">
          <el-input v-model="formPropValue3.value" placeholder="Pick Capacity"/>
        </el-form-item>

        <el-form-item v-if="form1.typeId === 2" label="Load Capacity">
          <el-input v-model="formPropValue4.value" placeholder="Pick Load Capacity"/>
        </el-form-item>

        <el-form-item v-if="form1.typeId === 2" label="High Speed">
          <el-input v-model="formPropValue5.value" placeholder="Pick High Speed"/>
        </el-form-item>

        <el-form-item v-if="form1.typeId === 3" label="Load Capacity">
          <el-input v-model="formPropValue6.value" placeholder="Pick Load Capacity"/>
        </el-form-item>
        
        <el-form-item v-if="form1.typeId === 3" label="Capacity">
          <el-input v-model="formPropValue7.value" placeholder="Pick Capacity"/>
        </el-form-item>

        <el-form-item v-if="form1.typeId === 3" label="High Speed">
          <el-input v-model="formPropValue8.value" placeholder="Pick High Speed"/>
        </el-form-item>
        
        <el-form-item v-if="form1.typeId === 4" label="Capacity">
          <el-input v-model="formPropValue9.value" placeholder="Pick Capacity"/>
        </el-form-item>

        <el-form-item v-if="form1.typeId === 4" label="High Speed">
          <el-input v-model="formPropValue10.value" placeholder="Pick High Speed"/>
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
import {placeIdParamOptions, typeIdParamOptions} from "@/data/parameters";
import {ref} from "vue";
const dialogFormVisible = ref(false)


export default {
  name: "TransportTable",
  props: {
    link: String,
    name: String
  },
  data() {
    return {
      params: {offset: 0},
      tableData: {},
      form: reactive({
        typeId: '',
        placeId: '',
        dt1: '',  
        working_life: ''     
      }),
      form1: reactive({
        id: '',
        typeId: '',
        placeId: '',
        dt1: '',  
        working_life: ''     
      }),
      formPropValue1: reactive({
        transportId: '',
        propId: '',
        value: null
      }),
      formPropValue2: reactive({
        transportId: '',
        propId: '',
        value: null
      }),
      formPropValue3: reactive({
        transportId: '',
        propId: '',
        value: null
      }),
      formPropValue4: reactive({
        transportId: '',
        propId: '',
        value: null
      }),
      formPropValue5: reactive({
        transportId: '',
        propId: '',
        value: null
      }),
      formPropValue6: reactive({
        transportId: '',
        propId: '',
        value: null
      }),
      formPropValue7: reactive({
        transportId: '',
        propId: '',
        value: null
      }),
      formPropValue8: reactive({
        transportId: '',
        propId: '',
        value: null
      }),
      formPropValue9: reactive({
        transportId: '',
        propId: '',
        value: null
      }),
      formPropValue10: reactive({
        transportId: '',
        propId: '',
        value: null
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
        const response = await axios.post(this.link, this.form)
        this.getTable()

        if (this.typeId === 1) {
          this.formPropValue1.transportId = response.data.id
          this.formPropValue1.propId = 1
          await this.createPropEntity1()

          this.formPropValue2.transportId = response.data.id
          this.formPropValue2.propId = 2
          await this.createPropEntity2()
        }

        if (this.typeId === 2) {
          this.formPropValue3.transportId = response.data.id
          this.formPropValue3.propId = 3
          await this.createPropEntity3()

          this.formPropValue4.transportId = response.data.id
          this.formPropValue4.propId = 4
          await this.createPropEntity4()

          this.formPropValue5.transportId = response.data.id
          this.formPropValue5.propId = 5
          await this.createPropEntity5()
        }

        if (this.typeId === 3) {
          this.formPropValue6.transportId = response.data.id
          this.formPropValue6.propId = 6
          await this.createPropEntity6()

          this.formPropValue7.transportId = response.data.id
          this.formPropValue7.propId = 7
          await this.createPropEntity7()

          this.formPropValue8.transportId = response.data.id
          this.formPropValue8.propId = 8
          await this.createPropEntity8()
        }

        if (this.typeId === 4) {
          this.formPropValue9.transportId = response.data.id
          this.formPropValue9.propId = 9
          await this.createPropEntity9()

          this.formPropValue10.transportId = response.data.id
          this.formPropValue10.propId = 10
          await this.createPropEntity10()
        }

      } catch (e) {
        console.log(e);
      }
    },
    async createPropEntity1() {
      try {
        await axios.post("/type_value", this.formPropValue1)
      } catch (e) {
        console.log(e);
      }
    },
    async createPropEntity2() {
      try {
        await axios.post("/type_value", this.formPropValue2)
      } catch (e) {
        console.log(e);
      }
    },
    async createPropEntity3() {
      try {
        await axios.post("/type_value", this.formPropValue3)
      } catch (e) {
        console.log(e);
      }
    },
    async createPropEntity4() {
      try {
        await axios.post("/type_value", this.formPropValue4)
      } catch (e) {
        console.log(e);
      }
    },
    async createPropEntity5() {
      try {
        await axios.post("/type_value", this.formPropValue5)
      } catch (e) {
        console.log(e);
      }
    },
    async createPropEntity6() {
      try {
        await axios.post("/type_value", this.formPropValue6)
      } catch (e) {
        console.log(e);
      }
    },
    async createPropEntity7() {
      try {
        await axios.post("/type_value", this.formPropValue7)
      } catch (e) {
        console.log(e);
      }
    },
    async createPropEntity8() {
      try {
        await axios.post("/type_value", this.formPropValue8)
      } catch (e) {
        console.log(e);
      }
    },
    async createPropEntity9() {
      try {
        await axios.post("/type_value", this.formPropValue9)
      } catch (e) {
        console.log(e);
      }
    },
    async createPropEntity10() {
      try {
        await axios.post("/type_value", this.formPropValue10)
      } catch (e) {
        console.log(e);
      }
    },
  },
  setup() {
    const allTypeNameOptions = ref(typeIdParamOptions)
    const allplaceIdOptions = ref(placeIdParamOptions)
    return {allTypeNameOptions, allplaceIdOptions}
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