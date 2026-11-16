<template>
  <div>
    <h1>{{ this.name }}</h1>
    <div class="all">

      <div class="selectDropdown">
        <el-select-v2
            v-model="this.params.partsListParam"
            :options="this.allPartsListOptions"
            placeholder="Select Parts"
        />
      </div>

      <div class="selectDropdown">
        <el-select-v2
            v-model="this.params.autoIdParam"
            :options="this.allAutoOptions"
            placeholder="Select Auto Id"
        />
      </div>

      <div class="selectDropdown">
        <el-select-v2
            v-model="this.params.typeNameParam"
            :options="this.allTypeNameOptions"
            placeholder="Select Type"
        />
      </div>

      <div class="date">
        <el-date-picker v-model="this.params.startDateParam"
                        type="date"
                        placeholder="Pick Start Date"
                        format="YYYY/MM/DD"
                        value-format="YYYY-MM-DD"/>
      </div>

      <div class="date">
        <el-date-picker v-model="this.params.endDateParam"
                        type="date"
                        placeholder="Pick End Date"
                        format="YYYY/MM/DD"
                        value-format="YYYY-MM-DD"/>
      </div>
      <el-button class="submit" type="success" round @click="this.updateData()">Submit</el-button>
    </div>
    <el-table :data="tableData.rows" style="width: 100%">
      <el-table-column v-for="column in this.tableData.columns"
                       v-bind:key="column.field" :prop="column.field" :label="column.headerName"/>
    </el-table>
  </div>
</template>

<script>
import {getTableRecords} from "@/getTableRecords";
import {autoIdParamOptions, typeNameParamOptions, partsListParamOptions} from "@/data/parameters";
import {ref} from "vue";

export default {
  name: "RepairCountAndCostQuery",
  props: {
    link: String,
    name: String
  },
  data() {
    return {
      params: {
        partsListParam: null,
        autoIdParam: null,
        typeNameParam: null,
        startDateParam: null,
        endDateParam: null
      },
      tableData: {}}
  },
  mounted() {
    this.updateData()
  },
  methods: {
    updateData() {
      console.log(this.params.startDateParam)
      console.log(this.params.endDateParam)
      getTableRecords(this.link, this.params).then((ret) => { this.tableData = ret})
    }
  },
  setup() {
    const allPartsListOptions = ref(partsListParamOptions)
    const allAutoOptions = ref(autoIdParamOptions)
    const allTypeNameOptions = ref(typeNameParamOptions)
    return {allPartsListOptions, allAutoOptions, allTypeNameOptions}
  }
}
</script>

<style scoped>
.submit {
  margin-left: 20px;
  margin-bottom: 30px;
}

.selectDropdown {
  margin-left: 20px;
  margin-right: 20px;
  margin-bottom: 30px;
}

.date {
  margin-left: 20px;
  margin-right: 20px;
  margin-bottom: 30px;
}

.all {
  display: inline-flex;
}

h1 {
  position: center;
  font-size: 50px;
  font-family: "Akshar";
}
</style>