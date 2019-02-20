package com.martinlaizg.geofind.data.access.database.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.LocationDAO;
import com.martinlaizg.geofind.data.access.database.entity.Location;

import java.util.List;

import androidx.lifecycle.LiveData;

public class LocationRepository {
    private LocationDAO locationDAO;
    private LiveData<List<Location>> allLocations;

    public LocationRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        locationDAO = database.locationDAO();
        allLocations = locationDAO.getAllLocations();
    }

    public void insert(Location location) {
        new InsertLocationAsyncTask(locationDAO).execute(location);
    }

    public void update(Location location) {
        new UpdateLocationAsyncTask(locationDAO).execute(location);

    }

    public void delete(Location location) {
        new DeleteLocationAsyncTask(locationDAO).execute(location);

    }

    public void deleteAllLocations() {
        new DeleteAllLocationAsyncTask(locationDAO).execute();

    }

    public LiveData<List<Location>> getAllLocations() {
        return allLocations;
    }

    private static class InsertLocationAsyncTask extends AsyncTask<Location, Void, Void> {
        private LocationDAO locationDAO;

        private InsertLocationAsyncTask(LocationDAO locationDAO) {
            this.locationDAO = locationDAO;
        }

        @Override
        protected Void doInBackground(Location... locations) {
            locationDAO.insert(locations[0]);
            return null;
        }
    }

    private static class UpdateLocationAsyncTask extends AsyncTask<Location, Void, Void> {
        private LocationDAO locationDAO;

        private UpdateLocationAsyncTask(LocationDAO locationDAO) {
            this.locationDAO = locationDAO;
        }

        @Override
        protected Void doInBackground(Location... locations) {
            locationDAO.update(locations[0]);
            return null;
        }
    }

    private static class DeleteLocationAsyncTask extends AsyncTask<Location, Void, Void> {
        private LocationDAO locationDAO;

        private DeleteLocationAsyncTask(LocationDAO locationDAO) {
            this.locationDAO = locationDAO;
        }

        @Override
        protected Void doInBackground(Location... locations) {
            locationDAO.delete(locations[0]);
            return null;
        }
    }

    private static class DeleteAllLocationAsyncTask extends AsyncTask<Void, Void, Void> {
        private LocationDAO locationDAO;

        private DeleteAllLocationAsyncTask(LocationDAO locationDAO) {
            this.locationDAO = locationDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            locationDAO.deleteAllLocations();
            return null;
        }
    }
}
