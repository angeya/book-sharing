/**
 * @desc:
 * @author: WangAnJie
 * @date: 2021/8/10 15:29
 */
import notifyTypeEnum from '@/js/constant/notify-type-enum'
export default {
    register(Vue) {
        Vue.prototype.$notify.successEx = function(option) {
            Object.assign(option, {
                type: notifyTypeEnum.SUCCESS,
            })
            open(Vue, option)
        }
        Vue.prototype.$notify.warningEx = function(option) {
            Object.assign(option, {
                type: notifyTypeEnum.WARNING,
            })
            open(Vue, option)
        }
        Vue.prototype.$notify.infoEx = function(option) {
            Object.assign(option, {
                type: notifyTypeEnum.INFO,
            })
            open(Vue, option)
        }
        Vue.prototype.$notify.errorEx = function(option) {
            Object.assign(option, {
                type: notifyTypeEnum.ERROR,
            })
            open(Vue, option)
        }
    }
}
function open(Vue, option) {
    Vue.prototype.$notify({
        type: option.type,
        title: option.title,
        message: option.content,
        position: "bottom-right"
    })
}
