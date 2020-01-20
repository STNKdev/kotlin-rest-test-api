package ru.stnk.resttestapi.exception.registration

class DelayException : Exception {

    var delay: Long? = null
        private set

    constructor() {}

    constructor(delay: Long?) {
        this.delay = delay
    }

    fun setDelay(delay: Long) {
        this.delay = delay
    }
}
