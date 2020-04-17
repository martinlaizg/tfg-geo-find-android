package com.martinlaizg.geofind.data.access.database.dao

import androidx.room.*
import com.martinlaizg.geofind.data.access.database.entities.User

@Dao
interface UserDAO {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(user: User?)

	@Update
	fun update(user: User?)

	@Query("SELECT * FROM users WHERE id = :userId")
	fun getUser(userId: Int): User?
}