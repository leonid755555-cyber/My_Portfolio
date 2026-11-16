<template>

  <div class="navbar"/>
  <el-menu
      class="el-menu-demo"
      mode="horizontal"
      background-color="#22a2e3"
      text-color="#ffffff"
      active-text-color="#ffffff"
  >
    <el-menu-item index="1"
                  @click="this.handle('Home Page')">
      Home
    </el-menu-item>

    <div v-if="!check0()">
    <el-sub-menu index="2">
      <template #title>Tables</template>
      <el-menu-item v-for="tbl in this.allTables"
                    v-bind:key="tbl.name"
                    :index="'2-'+tbl.id"
                    @click="this.secondHandle(tbl.name, tbl.path)">
                    
        {{ tbl.name }}
      </el-menu-item>
    </el-sub-menu>
      </div>

    <div v-if="!check0()">
    <el-sub-menu index="3">
      <template #title>Queries</template>
      <el-menu-item v-for="qrs in this.allQueries"
                    v-bind:key="qrs.name"
                    :index="'3-'+qrs.id"
                    @click="this.secondHandle(qrs.name, qrs.path)">
        {{ qrs.name }}
      </el-menu-item>
    </el-sub-menu>
      </div>
      <div v-if="check0()">
      <el-menu-item index="5"
                  @click="this.$router.push('/login');login();">
      Login
    </el-menu-item>
      </div>
      <div v-if="check0()">
      <el-menu-item index="6"
                  @click="this.handle('Registration Page')">
      Registration
    </el-menu-item>
      </div>
      <div v-if="!check0()">
      <el-menu-item index="7" @click="this.$router.push('/logout');logout();">
      Logout
    </el-menu-item>
      </div>
  </el-menu>
</template>

<script>
import tables from "@/data/tables";
import queries from "@/data/queries"
import {ref} from "vue";    

export default {
  name: "NavbarMenu",
  methods: {
    handle(name) {
      this.$router.push({name: name})
    },
    async logout() {
      localStorage.setItem('role', '0');
    },
    async login() {
      localStorage.setItem('role', '1');
    },
    check0() {
      if(localStorage.getItem('role')==='0'||localStorage.getItem('role')===null)
      {
      return true;
      }
      else
      {
      return false;
      }
    },
    secondHandle(name, path) {
      this.$router.push({
        name: name,
        params: {
          link: path,
          name: name
        }})
    }
  },
  setup() {
    const allTables = ref(tables)
    const allQueries = ref(queries)
    return {allTables, allQueries}
  }
}
</script>

<style scoped>

</style>