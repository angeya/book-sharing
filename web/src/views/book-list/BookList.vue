<!--
  * @description:
  * @author: angeya
  * @date: 2021/8/11 16:03
 -->
<template>
    <div class="container">
        <section class="book-classify-container">
            <h4>分类列表</h4>
            <ul v-for="(item, key) in classifies" :key="key" class="book-classify-list">
                <li style="cursor: pointer" @click="showFileNodeInTable(item)"
                    class="classify-item"
                    :class="{'active-classify': item === classify}">
                    {{item}}
                </li>
            </ul>
        </section>
        <section class="book-table-container">
            <el-table :data="tableData"
                      :stripe="true"
                      class="book-table">
                <el-table-column prop="path" label="书名" width="240">
                </el-table-column>
                <el-table-column prop="size" label="文件大小" width="120" :formatter="formatSize">
                </el-table-column>
                <el-table-column prop="label" :show-overflow-tooltip="true" label="标签" :formatter="splitLabels">
                </el-table-column>
                <el-table-column prop="desc" :show-overflow-tooltip="true" label="简介">
                </el-table-column>
                <el-table-column prop="createDate" label="日期" width="240" :formatter="formatDate">
                </el-table-column>
                <el-table-column label="操作" width="120">
                    <template slot-scope="scope">
                        <el-button type="success" @click="downloadFile(scope.row)">下载</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="page-container">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="currentPage"
                        :page-sizes="[10, 20, 30, 40]"
                        :page-size="10"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="totalNum">
                </el-pagination>
            </div>
        </section>
    </div>
</template>

<script>
    import tools from '@/js/tools.js'
    import consts from '@/js/constant/consts.js'

    const DOWNLOAD_INTERVAL = 15000

    export default {
        name: "BookList",
        watch: {
            filterText(val) {
                this.$refs.tree.filter(val);
            }
        },
        data() {
            return {
                filterText: '',
                defaultProps: {
                    label: 'label'
                },
                lastDownloadFileName: '',
                lastDownloadFileTime: 0,
                classify: null,
                pagingQueryParam: {
                    pageSize: 10,
                    currentPage: 1
                },
                totalNum: 0,
                currentPage: 1,
                classifies: [],
                tableData: []
            };
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
            /**
             * 将选中节点的文件添加到表格列表
             */
            showFileNodeInTable(key) {
                this.tableData = []
                this.pagingQueryParam.pageSize = 10
                this.pagingQueryParam.currentPage = 1
                this.classify = key
                this.queryBookList()
            },
            handleSizeChange(value) {
                this.pagingQueryParam.pageSize = value
                this.queryBookList()
            },
            handleCurrentChange(value) {
                this.pagingQueryParam.currentPage = value
                this.queryBookList()
            },
            /**
             * 获取文件列表列表
             */
            queryBookList() {
                let that = this
                this.axios({
                    url: '/file/getPagingBook?classify=' + this.classify
                        + '&pageSize=' + this.pagingQueryParam.pageSize
                        + '&currentPage=' + this.pagingQueryParam.currentPage,
                    method: 'get'
                }).then(res => {
                    if (res.data.code === 0) {
                        that.totalNum = res.data.totalNum
                        that.tableData = res.data.data
                        that.$store.commit('saveClassify', that.classify)
                    }
                })
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
                        for (const key of res.data.data) {
                            that.classifies.push(key)
                        }
                        const classify = that.$store.getters.getClassify
                        if(classify !== null){
                            that.classify = classify
                            that.queryBookList()
                        } else if (that.classifies.length > 0) {
                            that.classify = that.classifies[0]
                            that.queryBookList()
                        }
                    }
                })
            },
            /**
             * 文件下载，使用axios不能正确处理，需要使用a标签
             */
            downloadFile(file) {
                const timeFromLastDownload = new Date().getTime() - this.lastDownloadFileTime
                if (this.lastDownloadFileId === file.id && timeFromLastDownload < DOWNLOAD_INTERVAL) {
                    this.$notify.warningEx({
                        title: '无需重复下载，请稍候再试!'
                    })
                    return
                }
                this.lastDownloadFileId = file.id
                this.lastDownloadFileTime = new Date().getTime()
                let a = document.createElement('a')
                a.href = (process.env.NODE_ENV !== 'production' ? '/api' : '') + "/file/downloadFile?id=" + file.id
                a.download = file.path
                document.body.appendChild(a)
                a.click()
                document.body.removeChild(a)
                this.$notify.successEx({
                    title: '文件正在下载，请稍等'
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
        display flex
        flex-direction row
        height 100%
        width 100%
        .book-classify-container
            width 118px
            background-color #efefef
            border-right 1px rgba(204, 204, 204, 0.4) solid
            .classify-item
                padding 3px 10px
            .classify-item:hover
                background-color #9fa5c6
            .active-classify
                background-color #5ec695
                color white
        .book-table-container
            width calc(100% - 120px)
            .book-table
                overflow-y scroll
                height calc(100% - 32px)
        .page-container
            display flex
            flex-direction row-reverse
</style>

