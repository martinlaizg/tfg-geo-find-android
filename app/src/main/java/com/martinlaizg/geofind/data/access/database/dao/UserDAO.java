package com.martinlaizg.geofind.data.access.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.martinlaizg.geofind.data.access.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(User user);

	@Update
	void update(User user);

	@Query("SELECT * FROM users WHERE id = :user_id")
	User getUser(int user_id);

}
