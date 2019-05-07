package com.martinlaizg.geofind.data.repository;

import com.martinlaizg.geofind.data.access.database.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TourRepositoryTest {

	AppDatabase database;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getAllTours() {
		assertEquals(1, 1);
	}

	@Test
	public void getTour() {
		assertEquals(1, 1);
	}
}