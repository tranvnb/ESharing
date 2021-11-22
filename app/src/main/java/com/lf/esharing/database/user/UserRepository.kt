package com.lf.esharing.database.user

class UserRepository(val userDao: UserDao, val userApi: UserApi = UserClient.getInstance()) {

    suspend fun login(username: String, password: String): Boolean {
        // only use rest api to login
        return userApi.login(mapOf(username to "username", password to "password")).isSuccessful
    }

    suspend fun getUsers(): List<UserEntity>? {
        return userApi.allUsers().body()
    }
}