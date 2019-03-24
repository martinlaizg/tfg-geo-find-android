package com.martinlaizg.geofind.data.access.database.dao;

import com.martinlaizg.geofind.data.access.database.entity.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(User user);

	@Update
	void update(User user);

	@Delete
	void delete(User user);

	@Query("SELECT * FROM users")
	LiveData<List<User>> getAllUsers();

	@Query("DELETE FROM users")
	void deleteAllUsers();

	@Query("SELECT * FROM users WHERE email = :email AND password = :password")
	List<User> getUser(String email, String password);
}
