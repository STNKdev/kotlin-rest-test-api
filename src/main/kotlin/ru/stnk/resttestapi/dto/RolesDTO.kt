package ru.stnk.resttestapi.dto

// Класс для отправки на клиент роли (Data Transfer Object)
class RolesDTO {

    var nameRole: String? = null

    constructor() {}

    constructor(nameRole: String) {
        this.nameRole = nameRole
    }
}