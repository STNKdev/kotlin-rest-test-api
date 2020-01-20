package ru.stnk.resttestapi.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "roles")
class Roles : GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    var id: Long? = null

    @Column(name = "name")
    var name: String = ""

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    @JsonIgnore
    private var users: MutableList<User> = ArrayList()

    constructor() {}

    constructor(name: String) {
        this.name = name
    }

    /*fun getUsers(): List<User>? {
        return users
    }

    fun setUser(user: MutableList<User>) {
        this.users = user
    }

    fun addUser(user: User) {
        users!!.add(user)
    }*/

    override fun getAuthority(): String? {
        return name
    }
}