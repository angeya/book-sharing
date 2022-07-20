<!--
  * @description:
  * @author: angeya
  * @date: 2021/8/13 9:29
 -->
<template>
    <div class="container">
        <el-upload
                name="file"
                :action="uploadUrl"
                :on-success="uploadSuccess"
                :auto-upload="false"
                :on-error="uploadError"
                :on-remove="removeFile"
                :on-change="fileStatusChange"
                ref="upload"
                :limit="1"
                class="choose-file-container"
                :data="bookParam">
            <el-button slot="trigger" size="medium" type="primary" class="file-choose-btn">
                <em class="el-icon-upload"></em>
                <span>选取文件</span>
            </el-button>
        </el-upload>
        <div class="classify-input-container">
            <span>书籍分类(必选)</span>
            <el-select v-model="selectedClassify" placeholder="请选择">
                <el-input placeholder="自定义选项，回车确定" v-model="userDefinedClassify"
                          @keyup.enter.native="addClassify">
                </el-input>
                <el-option
                        v-for="item in classifies"
                        :key="item"
                        :value="item">
                </el-option>
            </el-select>
        </div>
        <div class="label-input-container">
            <div>
                <span>添加书籍标签(可选)</span>
                <el-tooltip effect="light" content="填写概述标签，方便大家准确找书，也利于平台在后期上线推荐系统！"
                            placement="right">
                    <em class="el-icon-question"></em>
                    </el-tooltip>
            </div>
            <el-tag
                    :key="tag"
                    v-for="tag in tags"
                    closable
                    :disable-transitions="false"
                    @close="closeTag(tag)">
                {{tag}}
            </el-tag>
            <el-input
                    class="input-new-tag"
                    v-if="tagInputVisible"
                    v-model="tagValue"
                    ref="saveTagInput"
                    size="small"
                    @keyup.enter.native="addTag"
                    @blur="addTag">
            </el-input>
            <el-button v-else class="button-new-tag" size="small" @click="showInput">+ 新标签</el-button>
        </div>
        <div class="desc-input-container">
            <span>内容简介(可选)</span>
            <el-input type="textarea"
                      :autosize="{ minRows: 2, maxRows: 5}"
                      placeholder="请输入内容"
                      v-model="bookParam.desc">
            </el-input>
        </div>
        <el-button class="upload-btn" type="success" @click="submitUpload">开始上传</el-button>
    </div>
</template>

<script>
    import consts from '@/js/constant/consts.js'
    import tools from '@/js/tools.js'

    const MAX_TAG_NUMBER = 5
    export default {
        name: 'FIleUpload',
        data() {
            return {
                bookParam: {
                    classify: '',
                    labels: '',
                    desc: ''
                },
                uploadUrl: (process.env.NODE_ENV !== 'production' ? '/api' : '') + '/file/uploadFile',
                file: null,
                selectedClassify: '',
                isClassifyAdded: false,
                userDefinedClassify: '',
                tagInputVisible: false,
                tagValue: '',
                tags: [],
                classifies: [],
                isStorageAllow: false
            }
        },
        methods: {
            /**
             * 处理上传
             */
            submitUpload() {
                if (this.file === null) {
                    this.$notify.warningEx({
                        title: '未选择任何文件！'
                    })
                    return
                }
                if (!this.checkStorage()) {
                    return
                }
                if (this.selectedClassify === ''){
                    this.$notify.warningEx({
                        title: '未选择分类'
                    })
                    return
                }
                this.checkFileNameAndUpload()
            },
            /**
             * 关闭标签
             */
            closeTag(tag) {
                this.tags.splice(this.tags.indexOf(tag), 1);
            },
            /**
             * 显示标签输出框并聚焦
             */
            showInput() {
                this.tagInputVisible = true;
                this.$nextTick(() => {
                    this.$refs.saveTagInput.$refs.input.focus();
                });
            },
            uploadError(){
                this.$notify.errorEx({
                    title: '电子书上传失败！'
                })
            },
            /**
             * 上传成功
             */
            uploadSuccess(response) {
                if (response.code === consts.REQUEST_SUCCESS)
                    this.$notify.successEx({
                        title: '电子书上传成功！'
                    })
                    this.$refs.upload.clearFiles()
                    this.file = null
                    this.tags = []
                    this.em.fire(consts.UPDATE_STORAGE_INFO)
                },
            /**
             * 判断文件是否已经存在，不存在则上传
             */
            checkFileNameAndUpload() {
                const that = this
                this.axios({
                    url: '/file/isFileNameExist?name=' + this.file.name,
                    method: 'get'
                }).then((res) => {
                    if (res.data === true) {
                        that.$notify.warningEx({
                            title: '该书已存在！'
                        })
                    } else {
                        that.bookParam.classify = this.selectedClassify
                        that.bookParam.labels = that.tags.join(consts.LABELS_SEPARATOR)
                        that.$refs.upload.submit();
                    }
                })
            },
            /**
             * 添加新标签
             */
            addTag() {
                this.tagInputVisible = false;
                if (this.tagValue === '') {
                    return
                }
                if (this.tags.length >= MAX_TAG_NUMBER) {
                    this.$notify.warningEx({
                        title: "描述标签最大个数为：" + MAX_TAG_NUMBER
                    })
                    return
                }
                if (this.tags.indexOf(this.tagValue) !== -1) {
                    this.$notify.warningEx({
                        title: "该标签已经存在，无需重复添加"
                    })
                    return
                }
                let value = this.tagValue;
                if (value) {
                    this.tags.push(value);
                }
                this.tagValue = '';
            },
            /**
             * 移除文件时
             */
            removeFile(){
                this.file = null
            },
            /**
             * 检查存储空间
             */
            checkStorage() {
                const storageInfo = this.$store.getters.getStorageInfo
                const restSpace = storageInfo.totalSpace - storageInfo.usedSpace
                if (tools.kToM(this.file.size) > restSpace) {
                    this.$notify.warningEx({
                        title: '存储空间不足',
                        content: '鼠标指向用户名，可显示存储空间申请按钮。'
                    })
                    return false
                }
                return true
            },
            /**
             * 状态改变时钩子 如添加、上传文件
             */
            fileStatusChange(file){
                this.file = file
            },
            /**
             * 添加新分类选项
             */
            addClassify() {
                if (this.userDefinedClassify.length < 2 || this.userDefinedClassify.length > 10) {
                    this.$notify.errorEx({
                        title: "自定义选项长度应为2 - 10"
                    })
                    return
                }
                if (this.classifies.indexOf(this.userDefinedClassify) !== -1) {
                    this.selectedClassify = this.userDefinedClassify
                    this.$notify.successEx({
                        title: "选项" + this.userDefinedClassify + "已经存在，选择成功"
                    })
                    return
                }
                if (this.isClassifyAdded) {
                    this.classifies.splice(0, 1, this.userDefinedClassify)
                    this.selectedClassify = this.userDefinedClassify
                    return
                }
                this.classifies.unshift(this.userDefinedClassify)
                this.isClassifyAdded = true
                this.selectedClassify = this.userDefinedClassify
            },
            /**
             * 获取分类列表
             */
            queryClassifies() {
                const that = this
                this.axios({
                    url: '/file/getClassifyList'
                }).then((res) => {
                    if (res.data.code === consts.REQUEST_SUCCESS) {
                        // 不能使用简单赋值给数组，会覆盖掉原对象，导致组件显示为空
                        that.classifies.splice(0, this.classifies.length - 1, ...res.data.data)
                        if (that.classifies.length === 0) {
                            that.classifies.push('')
                        }
                    }
                })
            }
        },
        created() {
            this.queryClassifies()
        }
    }
</script>

<style scoped lang="stylus">
    .container
        background-color white
        border-radius 5px
        width 100%
        padding 20px 30px
        height calc(100% - 40px)
        .choose-file-container
            padding-bottom 20px
            width 300px
        .label-input-container
            margin-top 10px
            width 320px
        .desc-input-container
            width 320px
            margin-top 10px
        .upload-btn
            margin-top 20px
</style>
