package com.martinlaizg.geofind.data.access.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.martinlaizg.geofind.data.access.database.converter.DateTypeConverter
import com.martinlaizg.geofind.data.access.database.converter.PlayLevelTypeConverter
import com.martinlaizg.geofind.data.access.database.converter.UserTypeTypeConverter
import com.martinlaizg.geofind.data.access.database.dao.PlaceDAO
import com.martinlaizg.geofind.data.access.database.dao.PlayDAO
import com.martinlaizg.geofind.data.access.database.dao.TourDAO
import com.martinlaizg.geofind.data.access.database.dao.UserDAO
import com.martinlaizg.geofind.data.access.database.dao.relations.PlacePlayDAO
import com.martinlaizg.geofind.data.access.database.entities.*

@Database(entities = [User::class, Tour::class, Place::class, Play::class, PlacePlay::class],
		version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class, PlayLevelTypeConverter::class, UserTypeTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun userDAO(): UserDAO
	abstract fun tourDAO(): TourDAO
	abstract fun placeDAO(): PlaceDAO
	abstract fun playDAO(): PlayDAO
	abstract fun playPlaceDAO(): PlacePlayDAO

	companion object {

		@Volatile
		private var INSTANCE: AppDatabase? = null

		@kotlin.jvm.JvmStatic
		@Synchronized
		fun getDatabase(context: Context): AppDatabase {
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "geo_find_database")
						.fallbackToDestructiveMigration()
						.build()
				INSTANCE = instance
				instance
			}
		}
	}
}