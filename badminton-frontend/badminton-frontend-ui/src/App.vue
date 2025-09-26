<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
  <RouterView />
</template>

<style>
/* 新拟态风格弹窗全局样式 */
.neumorphism-dialog .el-dialog {
  background: #f0f2f5;
  border-radius: 20px;
  box-shadow: 8px 8px 16px #c8c9cb, -8px -8px 16px #ffffff;
  border: none;
}

.neumorphism-dialog .el-dialog__header {
  text-align: center;
  padding-top: 25px;
}

.neumorphism-dialog .el-dialog__title {
  font-size: 20px;
  font-weight: 600;
  color: #555;
}

.neumorphism-dialog .el-dialog__body {
  padding: 20px 40px;
}

.neumorphism-dialog .el-dialog__footer {
  padding: 20px 40px;
}

/* 输入框凹陷效果 */
.neumorphism-dialog .el-input__wrapper,
.neumorphism-dialog .el-input-number,
.neumorphism-dialog .el-select__wrapper { /* 新增对 el-select 的支持 */
  background-color: #f0f2f5;
  border-radius: 10px;
  box-shadow: inset 5px 5px 10px #c8c9cb, inset -5px -5px 10px #ffffff !important;
  border: none !important;
  transition: all 0.2s ease-in-out;
}

.neumorphism-dialog .el-input__wrapper.is-focus {
  box-shadow: inset 2px 2px 4px #c8c9cb, inset -2px -2px 4px #ffffff !important;
}

/* 按钮凸起效果 */
.neumorphism-dialog .el-button {
  border-radius: 10px;
  border: none;
  background: #f0f2f5;
  color: #606266;
  box-shadow: 4px 4px 8px #c8c9cb, -4px -4px 8px #ffffff;
  transition: all 0.2s ease-in-out;
}

.neumorphism-dialog .el-button:hover {
  transform: translateY(-1px);
  box-shadow: 6px 6px 12px #c8c9cb, -6px -6px 12px #ffffff;
}

.neumorphism-dialog .el-button:active {
  transform: translateY(1px);
  box-shadow: inset 2px 2px 4px #c8c9cb, inset -2px -2px 4px #ffffff;
}

.neumorphism-dialog .el-button--primary {
  background: linear-gradient(145deg, #4da9ff, #409eff);
  color: white;
  box-shadow: 4px 4px 8px #c8c9cb, -4px -4px 8px #ffffff;
}

/* 表单标签样式 */
.neumorphism-dialog .el-form--label-top .el-form-item__label {
  color: #606266;
  font-weight: 500;
  padding-bottom: 5px;
}
</style>
