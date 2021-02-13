package kz.kolesateam.confapp.common.domain

interface UserNameDataSource {

    fun saveUserName(userName: String)

    fun getUserName(): String

    fun isUserNameExists(): Boolean
}