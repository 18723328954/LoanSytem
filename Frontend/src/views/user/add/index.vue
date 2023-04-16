<template>
  <div class="app-container">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="编号" prop="id">
        <el-input v-model="ruleForm.id" prefix-icon="el-icon-edit" placeholder="请输入员工编号"></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="ruleForm.name" prefix-icon="el-icon-edit" placeholder="请输入员工姓名"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="ruleForm.password" prefix-icon="el-icon-edit" placeholder="请输入员工密码"></el-input>
      </el-form-item>
      <el-form-item label="角色职称" prop="role">
        <el-radio-group v-model="ruleForm.role">
          <el-radio label="销售代表"></el-radio>
          <el-radio label="销售经理"></el-radio>
          <el-radio label="战区总监"></el-radio>
          <el-radio label="金融经理"></el-radio>
          <el-radio label="金融专员"></el-radio>
          <el-radio label="会计"></el-radio>
          <el-radio label="总经理"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="战区" prop="militaryRegion" v-if="ruleForm.role === '销售代表' || ruleForm.role ==='销售经理' || ruleForm.role==='战区总监'">
        <el-radio-group v-model="ruleForm.militaryRegionId" v-if="ruleForm.role === '销售代表' || ruleForm.role ==='销售经理' || ruleForm.role ==='战区总监'">
          <el-radio label="战区1"></el-radio>
          <el-radio label="战区2"></el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="部门" prop="department" v-if="ruleForm.role === '销售代表' || ruleForm.role ==='销售经理'">
        <el-radio-group v-model="ruleForm.department" v-if="ruleForm.role === '销售代表' || ruleForm.role ==='销售经理'">
          <el-radio label="销售部1"></el-radio>
          <el-radio label="销售部2"></el-radio>
          <el-radio label="销售部3"></el-radio>
          <el-radio label="销售部4"></el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="ruleForm.sex">
          <el-radio label="男"></el-radio>
          <el-radio label="女"></el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="年龄" prop="age">
        <el-input v-model="ruleForm.age" prefix-icon="el-icon-edit" placeholder="请输入员工年龄"></el-input>
      </el-form-item>
      <el-form-item label="薪资" prop="salary">
        <el-input v-model="ruleForm.salary" prefix-icon="el-icon-edit" placeholder="请输入员工薪资"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">立即添加</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {deleteUserById, insertUser} from "@/api/user";

export default {
  data() {
    return {
      isSalesman: false,
      isSupervisor: true,
      roleDict: {
        系统管理员: 0,
        销售代表: 1,
        销售经理: 2,
        战区总监: 3,
        金融经理: 4,
        金融专员: 5,
        会计: 6,
        总经理: 7,
      },
      departmentDict: {
        销售部1: 0,
        销售部2: 1,
        销售部3: 2,
        销售部4: 3,
      },
      ruleForm: {
        id: '',
        name: '',
        password: '',
        role: '',
        department: '',
        sex: '',
        militaryRegionId: '',
        age: '',
        salary: '',
      },
      rules: {
        id: [
          {required: true, message: '请输入员工编号', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入员工姓名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入员工密码', trigger: 'blur'}
        ],
        role: [
          {required: true, message: '请选择员工职称', trigger: 'blur'}
        ],
        militaryRegion: [
          {required: true, message: '请输入员工战区', trigger: 'blur'}
        ],
        department: [
          {required: true, message: '请选择部门', trigger: 'blur'}
        ],
        sex: [
          {required: true, message: '请选择员工性别', trigger: 'blur'}
        ],
        age: [
          {required: true, message: '请输入年龄', trigger: 'blur'}
        ],
        salary: [
          {required: true, message: '请输入薪资', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    resetForm() {
      this.$refs.ruleForm.resetFields()
    },
    submitForm() {
      var user = {
        id: this.ruleForm.id,
        username: this.ruleForm.name,
        password: this.ruleForm.password,
        userRole: this.ruleForm.role,
        departmentId: this.ruleForm.militaryRegionId === "战区1" ? this.departmentDict[this.ruleForm.department] : this.departmentDict[this.ruleForm.department] + 4,
        roleId: this.roleDict[this.ruleForm.role],
        sex: this.ruleForm.sex === "男" ? false : true,
        militaryRegionId: this.ruleForm.militaryRegionId === "战区1" ? 1 : 2,
        age: this.ruleForm.age,
        salary: this.ruleForm.salary
      }
      this.$confirm("是否确定要添加员工？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        console.log(user)
        insertUser(user)
        this.$message({
          type: 'success',
          message: '添加成功!'
        })
        location.reload()
      }).catch(() => {
        this.$message({type: "info", message: "取消成功！"})
      })
    }
  }
}
</script>
