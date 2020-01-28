package ru.stnk.resttestapi.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "roles")
class Role: Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    var name: RoleName? = null

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    @JsonIgnore
    private var users: MutableList<User> = ArrayList()
    //var users: Set<User> = HashSet()

    constructor() {}

    constructor(name: RoleName) {
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

    /*override fun getAuthority(): String? {
        return name
    }*/
}