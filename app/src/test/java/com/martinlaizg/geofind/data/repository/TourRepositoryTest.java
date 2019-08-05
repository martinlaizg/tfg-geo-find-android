package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.TourDAO;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.enums.PlayLevel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TourRepositoryTest {

	@Mock
	private TourDAO tourDAO;
	@Mock
	private AppDatabase appDatabase;
	@Mock
	private Application application;

	private TourRepository repository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getTourTest() {
		repository = RepositoryFactory.getTourRepository(application);
		Integer tour_id = 1;
		String tour_name = "";
		String tourDescription = "";
		PlayLevel minLevel = PlayLevel.COMPASS;
		Integer creatorId = 1;
		Tour expectedTour = new Tour(tour_id, tour_name, tourDescription, minLevel, null, null,
		                             creatorId, null);
		when(tourDAO.getTour(tour_id)).thenReturn(expectedTour);
		when(appDatabase.tourDAO()).thenReturn(tourDAO);

		Tour actualTour = null;
		try {
			actualTour = repository.getTour(tour_id);
		} catch(APIException e) {
			fail("Exception throwed");
		}
		assertEquals(expectedTour, actualTour);
	}

	//	public Tour getTour(Integer id) throws APIException {
	//		Tour t = tourDAO.getTour(id);
	//		if(t == null) {
	//			t = tourService.getTour(id);
	//			insert(t);
	//		} else {
	//			t.setCreator(userRepo.getUser(t.getCreator_id()));
	//			t.setPlaces(placeRepo.getTourPlaces(id));
	//		}
	//		return t;
	//	}

}

