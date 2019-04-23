package com.martinlaizg.geofind.data.access.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.martinlaizg.geofind.data.access.database.entities.User;

@Dao
public interface UserDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(User user);

	@Update
	void update(User user);

	@Query("SELECT * FROM users WHERE user_id = :user_id")
	User getUser(int user_id);
}
