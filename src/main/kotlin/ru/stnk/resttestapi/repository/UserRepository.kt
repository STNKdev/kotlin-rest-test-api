package ru.stnk.resttestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.stnk.resttestapi.entity.User
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

    /*@Query(value = "SELECT * FROM USERS WHERE EMAIL = ?1", nativeQuery = true)
    User findByUserEmail (String email);*/
}
