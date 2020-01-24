package ru.stnk.resttestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.stnk.resttestapi.entity.Role
import ru.stnk.resttestapi.entity.RoleName

@Repository
interface RolesRepository : JpaRepository<Role, Long> {

    fun findByName(roleName: RoleName): Role

}
