<!--
  * @description:
  * @author: angeya
  * @date: 2021/8/13 16:56
 -->
<template>
    <div class="container">
        <el-table :data="tableData" class="book-table">
            <el-table-column type="index">
            </el-table-column>
            <el-table-column prop="path" label="书名" width="240">
            </el-table-column>
            <el-table-column prop="classify" label="分类" width="120">
            </el-table-column>
            <el-table-column prop="size" label="文件大小" width="120" :formatter="formatSize">
            </el-table-column>
            <el-table-column prop="label" :show-overflow-tooltip="true" label="标签" :formatter="splitLabels">
            </el-table-column>
            <el-table-column prop="desc" :show-overflow-tooltip="true" label="简介">
            </el-table-column>
            <el-table-column prop="createDate" label="上传日期" width="200" :formatter="formatDate">
            </el-table-column>
            <el-table-column label="操作" width="180">
                <template slot-scope="scope">
                    <el-button type="primary" size="medium" :disabled="true">编辑</el-button>
                    <el-button type="danger" @click="deleteFile(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="page-container">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pagingQueryParam.currentPage"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="10"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalNum">
            </el-pagination>
        </div>
    </div>

</template>

<script>
    import consts from '@/js/constant/consts.js'
    import tools from '@/js/tools.js'
    export default {
        name: "FileManagement",
        data() {
            return {
                tableData: [],
                totalNum: 0,
                pagingQueryParam: {
                    pageSize: 10,
                    currentPage: 1
                },
            }
        },
        methods: {
            formatSize(row, column, cellValue) {
                return tools.byteToMFormat(cellValue)
            },
            formatDate(row, column, cellValue) {
                return tools.formatDate(new Date(cellValue), consts.COMMON_DATE_FORMAT)
            },
            splitLabels(row, column, cellValue) {
                return tools.splitLabels(cellValue)
            },
            handleSizeChange(value) {
                this.pagingQueryParam.pageSize = value
                this.queryFileList()
            },
            handleCurrentChange(value) {
                this.pagingQueryParam.currentPage = value
                this.queryFileList()
            },
            queryFileList(){
                const that = this
                this.axios({
                    url: '/file/getPagingBook?pageSize=' + this.pagingQueryParam.pageSize
                        + '&currentPage=' + this.pagingQueryParam.currentPage,
                    method: 'get'
                }).then((res) => {
                    if (res.data.code === consts.REQUEST_SUCCESS) {
                        that.totalNum = res.data.totalNum
                        that.tableData = res.data.data
                    }
                })
            },
            deleteFile(file) {
                const that = this
                this.axios({
                    url: '/file/deleteFile?id=' + file.id
                }).then((res) => {
                    if (res.data.code === consts.REQUEST_SUCCESS) {
                        const index = that.tableData.indexOf(file)
                        if (index !== -1) {
                            that.tableData.splice(index, 1)
                            that.$notify.successEx({
                                title: '文件删除成功！'
                            })
                            that.em.fire(consts.UPDATE_STORAGE_INFO)
                            return
                        }
                    }
                    that.$notify.errorEx({
                        title: '文件删除失败！'
                    })
                })
            }
        },
        created(){
            this.queryFileList()
        }
    }
</script>

<style scoped lang="stylus">
    .container
        height 100%
        width 100%
        .book-table
            overflow-y scroll
            height calc(100% - 32px)
            width 100%
        .page-container
            display flex
            flex-direction row-reverse
</style>
