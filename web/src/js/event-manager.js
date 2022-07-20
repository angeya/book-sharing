/**
 * @desc:
 * @author: WangAnJie
 * @date: 2021/8/23 9:54
 */
function EventManager() {
    this.eventsMap = new Map()
}
EventManager.prototype = {
    // 绑定方法，一个事件可以绑定多个方法，事件触发时每个方法都是执行
    bind: function(key, fn) {
        if (!this.eventsMap.has(key)) {
            this.eventsMap.set(key, [])
        }
        this.eventsMap.get(key).push(fn)
    },
    // 解绑事件方法，如果没有传入方法体，则全部解绑
    unbind: function(key, fn) {
        const fns = this.eventsMap.get(key)
        if (fns !== undefined) {
            return
        }
        if (fn === undefined) {
            fns.length = 0
        } else {
            fns.forEach((item, index) => {
                item === fn && fns.splice(index, 1)
            })
        }
    },
    // 触发事件，执行方法
    fire(key, ...args) {
        for (const fn of this.eventsMap.get(key)){
            fn.call(this, ...args)
        }
    }
}

export default new EventManager()
