package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.TourService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.TourDAO;
import com.martinlaizg.geofind.data.access.database.entities.Tour;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AppDatabase.class, TourService.class, PlaceRepository.class, UserRepository.class,
		                TourRepository.class})
public class TourRepositoryTest {

	@Mock
	private Application application;
	@Mock
	private AppDatabase appDatabase;
	@Mock
	private TourDAO tourDAO;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockStatic(AppDatabase.class);
		mockStatic(TourService.class);
		mockStatic(PlaceRepository.class);
		mockStatic(UserRepository.class);

	}

	@Test
	public void getAllToursTest() {
		when(tourDAO.getAll()).thenReturn(new ArrayList<>());
		when(appDatabase.tourDAO()).thenReturn(tourDAO);
		when(AppDatabase.getInstance(any())).thenReturn(appDatabase);

		TourRepository tourRepository = spy(TourRepository.getInstance(application));
		try {
			doReturn(new ArrayList<Tour>()).when(tourRepository, "refreshTours");
		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		try {
			List<Tour> allTours = tourRepository.getAllTours();
			Assert.assertEquals(0, allTours.size());
		} catch(APIException e) {
			Assert.fail("Exception do not expected");
		}
	}

	@Test
	public void updateTest() {
		Assert.assertTrue(true);
	}

	@Test
	public void createTest() {
		Assert.assertTrue(true);
	}

	@Test
	public void getTourTest() {
		Assert.assertTrue(true);
	}
}