<template>
  <div>
    <h1>{{ this.name }}</h1>
    <div class="all">
      <div class="selectDropdown">
        <el-select-v2
            v-model="this.params.person_id"
            :options="this.allPersonOptions"
            placeholder="Select Person"
        />
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
import {personIdParamOptions} from "@/data/parameters";
import {ref} from "vue";

export default {
  name: "SubordinatesQuery",
  props: {
    link: String,
    name: String
  },
  data() {
    return {params: {person_id: null},tableData: {}}
  },
  mounted() {
    this.updateData()
  },
  methods: {
    updateData() {
      getTableRecords(this.link, this.params).then((ret) => { this.tableData = ret})
    }
  },
  setup() {
    const allPersonOptions = ref(personIdParamOptions)
    return {allPersonOptions}
  }
}
</script>

<style scoped>
.submit {
  margin-left: 20px;
  margin-bottom: 30px;
}

.selectDropdown {
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