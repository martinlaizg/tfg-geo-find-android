package com.martinlaizg.geofind.data.access.database.dao;

import com.martinlaizg.geofind.data.access.database.entities.User;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public interface UserDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(User user);

	@Update
	void update(User user);

}
