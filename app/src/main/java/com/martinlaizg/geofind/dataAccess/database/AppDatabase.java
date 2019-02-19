package com.martinlaizg.geofind.dataAccess.database;

import android.content.Context;

import com.martinlaizg.geofind.dataAccess.database.converter.DateTypeConverter;
import com.martinlaizg.geofind.dataAccess.database.converter.PlayLevelTypeConverter;
import com.martinlaizg.geofind.dataAccess.database.converter.UserTypeTypeConverter;
import com.martinlaizg.geofind.dataAccess.database.dao.UserDAO;
import com.martinlaizg.geofind.dataAccess.database.entity.Location;
import com.martinlaizg.geofind.dataAccess.database.entity.Maps;
import com.martinlaizg.geofind.dataAccess.database.entity.User;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {User.class, Maps.class, Location.class}, version = 1, exportSchema = false)
@TypeConverters({DateTypeConverter.class, PlayLevelTypeConverter.class, UserTypeTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract UserDAO userDAO();

}
