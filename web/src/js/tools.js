/**
 * @desc:
 * @author: WangAnJie
 * @date: 2021/8/17 10:58
 */
import consts from './constant/consts'

export default {
    formatDate (date, formatString) {
        const o = {
            'M+': date.getMonth() + 1, // 月份
            'd+': date.getDate(), // 日
            'h+': date.getHours(), // 小时
            'H+': date.getHours(), // 小时
            'm+': date.getMinutes(), // 分
            's+': date.getSeconds(), // 秒
            'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
            fff: date.getMilliseconds() // 毫秒
        }
        let _fmt = formatString
        if (/(y+)/.test(_fmt)) _fmt = _fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
        for (const k in o) {
            if (new RegExp('(' + k + ')').test(_fmt)) {
                _fmt = _fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
            }
        }
        return _fmt
    },
    byteToMFormat(value) {
        return (value / 1024 / 1024).toFixed(3) + 'M'
    },
    kToMFormat(value) {
        return (value / 1024).toFixed(3) + 'M'
    },
    kToM(value) {
        return value / 1024
    },
    splitLabels(labels) {
        return labels.replace(/#\*#/g, consts.LABELS_SHOW_SEPARATOR)
    }
}
