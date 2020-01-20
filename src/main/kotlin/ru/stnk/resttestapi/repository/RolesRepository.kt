package ru.stnk.resttestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.stnk.resttestapi.entity.Roles

@Repository
interface RolesRepository : JpaRepository<Roles, Long> {

    fun findByName(name: String): Roles

}
