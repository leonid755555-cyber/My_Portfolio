<template>
  <div>
    <h1>{{ this.name }}</h1>

    <el-table :data="tableData.rows" style="width: 100%">
      <el-table-column v-for="column in this.tableData.columns"
                       v-bind:key="column.field" :prop="column.field" :label="column.headerName"/>
    </el-table>
  </div>
    <div>
    <el-button v-if="this.params.offset > 0" @click="prevPage">Previous</el-button>
    <el-button v-if="this.params.offset < this.tableData.totalPages - 1" @click="nextPage">Next</el-button>
  </div>
</template>

<script>
import {getTableRecords1} from "@/getTableRecords";

export default {
  name: "Transport_emplTable",
  props: {
    link: String,
    name: String
  },
    data() {
    return {
      params: {offset: 0},
      tableData: {}
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
    }
  }
}
</script>

<style scoped>

</style>