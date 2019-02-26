package com.martinlaizg.geofind.data.access.database;

import android.content.Context;
import android.os.AsyncTask;

import com.martinlaizg.geofind.data.access.database.converter.DateTypeConverter;
import com.martinlaizg.geofind.data.access.database.converter.PlayLevelTypeConverter;
import com.martinlaizg.geofind.data.access.database.converter.UserTypeTypeConverter;
import com.martinlaizg.geofind.data.access.database.dao.LocationDAO;
import com.martinlaizg.geofind.data.access.database.dao.MapDAO;
import com.martinlaizg.geofind.data.access.database.dao.UserDAO;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.data.access.database.entity.enums.UserType;

import java.sql.Date;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, Map.class, Location.class}, version = 2, exportSchema = false)
@TypeConverters({DateTypeConverter.class, PlayLevelTypeConverter.class, UserTypeTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

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
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "geo_find_database").
                    fallbackToDestructiveMigration().
                    build();
        }
        return instance;
    }

    public abstract UserDAO userDAO();

    public abstract MapDAO mapDAO();

    public abstract LocationDAO locationDAO();

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDAO userDAO;

        private PopulateDbAsyncTask(AppDatabase db) {
            userDAO = db.userDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Date date = new Date(Calendar.getInstance().getTime().getTime());
            User user = new User();
            user.setEmail("martinlaizg@gmail.com");
            user.setName("Martin");
            user.setPassword("martinlaizg");
            user.setUser_type(UserType.ADMINISTRATOR);
            userDAO.insert(user);
            return null;
        }
    }
}
