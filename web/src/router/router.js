/**
 * @desc:
 * @author: WangAnJie
 * @date: 2021/8/9 17:12
 */
import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/views/login/Login'
import Main from '@/views/main/Main'
import BookList from '@/views/book-list/BookList'
import BookManage from '@/views/book-manage/BookManage'
import BookUpload from '@/views/upload/BookUpload'
Vue.use(Router)

const router = new Router({
    routes: [
        {
            name: 'index',
            path: '/',
            redirect: '/login'
        },
        {
            name: 'login',
            path: '/login',
            component: Login
        },
        {
            name: 'main',
            path: '/main',
            component: Main,
            redirect: {
                name: 'bookList'
            },
            children: [
                {
                    name: 'bookList',
                    path: '/book-list',
                    component: BookList
                },{
                    name: 'bookManagement',
                    path: '/book-manage',
                    component: BookManage
                },
                {
                    name: 'bookUpload',
                    path: '/book-upload',
                    component: BookUpload
                }
            ]
        },
    ]
})
export  default router;
