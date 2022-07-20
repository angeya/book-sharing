<!--
  * @description: 封装通用组件 分页列表
  * @author: angeya
  * @date: 2021/8/18 14:22
 -->
<template>
    <section class="book-table-container">
        <h2>分页列表</h2>
        <el-table :data="dataList"
                  :stripe=true
                  class="book-table">
            <el-table-column v-for="(item, key) in columns" :key=key :prop="item.prop"
                             :label="item.label" :width="item.width" :formatter="item.func">
                <!--<template slot-scope="scope">-->
                    <!--<ex-slot-->
                            <!--v-if="item.render"-->
                            <!--:render="item.render"-->
                            <!--:row="scope.row"-->
                            <!--:index="scope.$index"-->
                            <!--:column="item"-->
                    <!--/>-->
                <!--</template>-->
            </el-table-column>
            <!--<el-table-column prop="size" label="文件大小" width="120" :formatter="formatSize">-->
            <!--</el-table-column>-->
            <!--<el-table-column prop="label" :show-overflow-tooltip="true" label="标签">-->
            <!--</el-table-column>-->
            <!--<el-table-column prop="desc" :show-overflow-tooltip="true" label="简介">-->
            <!--</el-table-column>-->
            <!--<el-table-column prop="createDate" label="日期" width="240" :formatter="formatDate">-->
            <!--</el-table-column>-->
            <!--<el-table-column label="操作" width="120">-->
                <!--<template slot-scope="scope">-->
                    <!--<el-button type="success" @click="downloadFile(scope.row)">下载</el-button>-->
                <!--</template>-->
            <!--</el-table-column>-->
        </el-table>
        <!--<div class="block">-->
            <!--<el-pagination-->
                    <!--@size-change="handleSizeChange"-->
                    <!--@current-change="handleCurrentChange"-->
                    <!--:current-page="currentPage"-->
                    <!--:page-sizes="[10, 20, 30, 40]"-->
                    <!--:page-size="10"-->
                    <!--layout="total, sizes, prev, pager, next, jumper"-->
                    <!--:total="totalNum">-->
            <!--</el-pagination>-->
        <!--</div>-->
        <ex-slot></ex-slot>
    </section>
</template>

<script>
    import exSlot from '@/js/exSlot'
    export default {
        name: "TableAndPage",
        components: {exSlot},
        props: {
            columns: {
                type: Array,
                default: () => []
            },
            dataList: {
                type: Array,
                default: () => []
            },
            total: {
                type: Number,
                default: 0
            },
            height: {
                type: [Number],
                default: 600
            },
            isQuerying: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
                currentPage: 1
            }
        },

    }
</script>

<style scoped lang="stylus">

</style>
