package ru.stnk.resttestapi.results

import com.fasterxml.jackson.annotation.JsonInclude

// Класс для единого шаблона ответа от сервера
@JsonInclude(JsonInclude.Include.NON_NULL)
class RestResponse {

    var data: Any? = null
    var error: Int = 0
    var description: String? = null

    constructor() {
        this.data = null
        this.error = 0
        this.description = ""
    }

    constructor(error: Int, description: String) {
        this.data = null
        this.error = error
        this.description = description
    }

    constructor(data: Any, error: Int, description: String) {
        this.data = data
        this.error = error
        this.description = description
    }

    constructor(error: Int) {
        this.data = null
        this.error = error
        this.description = ""
    }

    constructor(error: Int, ex: Throwable) {
        this.data = null
        this.error = error
        this.description = ex.message
    }

}
