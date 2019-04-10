package com.martinlaizg.geofind.data.access.database.dao;

import com.martinlaizg.geofind.data.access.database.entities.UserEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public interface UserDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(UserEntity userEntity);

	@Update
	void update(UserEntity userEntity);

}
