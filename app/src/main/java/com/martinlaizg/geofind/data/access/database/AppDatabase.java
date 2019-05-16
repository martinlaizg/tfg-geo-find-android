package com.martinlaizg.geofind.data.access.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.martinlaizg.geofind.data.access.database.converter.DateTypeConverter;
import com.martinlaizg.geofind.data.access.database.converter.PlayLevelTypeConverter;
import com.martinlaizg.geofind.data.access.database.converter.UserTypeTypeConverter;
import com.martinlaizg.geofind.data.access.database.dao.PlaceDAO;
import com.martinlaizg.geofind.data.access.database.dao.PlayDAO;
import com.martinlaizg.geofind.data.access.database.dao.TourDAO;
import com.martinlaizg.geofind.data.access.database.dao.UserDAO;
import com.martinlaizg.geofind.data.access.database.dao.relations.PlacePlayDAO;
import com.martinlaizg.geofind.data.access.database.dao.relations.TourPlacesDAO;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.PlacePlay;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;

@Database(entities = {User.class, Tour.class, Place.class, Play.class, PlacePlay.class},
          version = 5, exportSchema = false)
@TypeConverters(
		{DateTypeConverter.class, PlayLevelTypeConverter.class, UserTypeTypeConverter.class})
public abstract class AppDatabase
		extends RoomDatabase {

	private static AppDatabase instance;

	public static synchronized AppDatabase getInstance(Context context) {
		if(instance == null) {
			instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
			                                "geo_find_database").
					fallbackToDestructiveMigration().
					build();
		}
		return instance;
	}

	public abstract UserDAO userDAO();

	public abstract TourDAO tourDAO();

	public abstract PlaceDAO placeDAO();

	public abstract PlayDAO playDAO();

	public abstract TourPlacesDAO tourPlacesDAO();

	public abstract PlacePlayDAO playPlaceDAO();

}
