package com.martinlaizg.geofind.data.access.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

@Database(entities = {User.class, Tour.class, Place.class, Play.class, PlacePlay.class}, version = 3, exportSchema = false)
@TypeConverters({DateTypeConverter.class, PlayLevelTypeConverter.class, UserTypeTypeConverter.class})
public abstract class AppDatabase
		extends RoomDatabase {

	private static AppDatabase instance;
	private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
		@Override
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
			super.onCreate(db);
			new PopulateDbAsyncTask(instance).execute();
		}
	};

	public static synchronized AppDatabase getInstance(Context context) {
		if (instance == null) {
			instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "geo_find_database").
					fallbackToDestructiveMigration().
					build();
		}
		return instance;
	}

	public abstract UserDAO userDAO();

	public abstract TourDAO tourDAO();

	public abstract PlaceDAO placeDAO();

	public abstract PlayDAO playDAO();

	public abstract TourPlacesDAO mapLocsDAO();

	public abstract PlacePlayDAO playPlaceDAO();

	private static class PopulateDbAsyncTask
			extends AsyncTask<Void, Void, Void> {
		private final UserDAO userDAO;

		private PopulateDbAsyncTask(AppDatabase db) {
			userDAO = db.userDAO();
		}

		@Override
		protected Void doInBackground(Void... voids) {
			//			Date date = new Date(Calendar.getInstance().getTime().getTime());
			//			User user = new User();
			//			user.setEmail("martinlaizg@gmail.com");
			//			user.setName("Martin");
			//			user.setPassword("martinlaizg");
			//			user.setUser_type(UserType.ADMINISTRATOR);
			//			userDAO.insert(user);
			return null;
		}
	}
}
