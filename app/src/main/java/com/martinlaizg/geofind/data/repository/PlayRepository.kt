package com.martinlaizg.geofind.data.repository

import android.app.Application
import com.martinlaizg.geofind.data.access.api.service.PlayService
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.AppDatabase
import com.martinlaizg.geofind.data.access.database.dao.PlayDAO
import com.martinlaizg.geofind.data.access.database.dao.relations.PlacePlayDAO
import com.martinlaizg.geofind.data.access.database.entities.PlacePlay
import com.martinlaizg.geofind.data.access.database.entities.Play

class PlayRepository private constructor(application: Application) {
	private val playDAO: PlayDAO?
	private val playService: PlayService?
	private val tourRepo: TourRepository?
	private val userRepo: UserRepository?
	private val placePlayDAO: PlacePlayDAO?

	/**
	 * Get the play between user and tour
	 *
	 * @param userId
	 * the id of the user
	 * @param tourId
	 * the id of the tour
	 * @return the play if exists, otherwise null
	 * @throws APIException
	 * exception from API
	 */
	@Throws(APIException::class)
	fun getPlay(userId: Int, tourId: Int): Play {
		// Get from database
		var p = playDAO!!.getPlay(userId, tourId)
		if (p != null) {
			// Check out of date
			if (p.isOutOfDate) {
				playDAO.delete(p)
			} else {                    // If not retrieve data
				p.tour = tourRepo!!.getTour(tourId)
				p.user = userRepo!!.getUser(userId)
				p.places = playDAO.getPlaces(p.id)
				return p
			}
		}
		// Get from server
		try {
			p = playService!!.getUserPlay(userId, tourId)
			p.user_id = p.user.id
			p.tour_id = p.tour.id
			insert(p)
		} catch (e: APIException) {
			p = null
		}
		if (p == null) {
			p = Play(tourId, userId)
			p.tour = tourRepo!!.getTour(tourId)
		}
		return p
	}

	/**
	 * Insert the play into the local database
	 * Usually used for Play retrieved from server
	 *
	 * @param play
	 * Play to be inserted
	 */
	private fun insert(play: Play?) {
		if (play != null) {
			userRepo!!.insert(play.user)
			tourRepo!!.insert(play.tour)
			val p = playDAO!!.getPlay(play.id)
			if (p == null) {
				playDAO.insert(play)
			} else {
				playDAO.update(play)
			}
			for (place in play.places) {
				val pp = PlacePlay(place.id, play.id)
				placePlayDAO!!.insert(pp)
			}
		}
	}

	/**
	 * Complete a place of a play, creating the relation between both
	 *
	 * @param playId
	 * play to complete
	 * @param placeId
	 * place to complete
	 * @return the new play
	 * @throws APIException
	 * exception from API
	 */
	@Throws(APIException::class)
	fun completePlace(playId: Int, placeId: Int): Play? {
		val p = playService!!.createPlacePlay(playId, placeId)
		val pp = PlacePlay(placeId, playId)
		placePlayDAO!!.insert(pp)
		return p
	}

	/**
	 * Create the play and insert into the database
	 *
	 * @param userId
	 * the id of the user who plays
	 * @param tourId
	 * the id of the tour to play
	 * @return the play between user and tour
	 * @throws APIException
	 * exception from API
	 */
	@Throws(APIException::class)
	fun createPlay(userId: Int, tourId: Int): Play? {
		val p = playService!!.createUserPlay(userId, tourId)
		insert(p)
		return p
	}

	/**
	 * Get the list of plays of users
	 *
	 * @param userId
	 * the id of the user
	 * @return the list of plays
	 * @throws APIException
	 * the exception from the database
	 */
	@Throws(APIException::class)
	fun getUserPlays(userId: Int): List<Play?>? {
		val plays = playDAO!!.getUserPlays(userId)
		// Remove out of date
		var i = 0
		while (i < plays!!.size) {
			if (plays[i]!!.isOutOfDate) {
				playDAO.delete(plays.removeAt(i))
				i--
			}
			i++
		}
		if (plays.isEmpty()) {
			plays.addAll(playService!!.getUserPlays(userId)!!)
			for (p in plays) {
				userRepo!!.insert(p.user)
				tourRepo!!.insert(p.tour)
				userRepo.insert(p.tour.creator)
				insert(p)
			}
		} else {
			for (i in plays.indices) {
				plays[i].tour = tourRepo!!.getTour(plays[i].tour_id)
				plays[i].places = placePlayDAO!!.getPlayPlace(plays[i].id)
			}
		}
		return plays
	}

	companion object {
		private val TAG = PlayRepository::class.java.simpleName
		private var instance: PlayRepository? = null
		fun getInstance(application: Application): PlayRepository? {
			if (instance == null) instance = PlayRepository(application)
			return instance
		}
	}

	init {
		val database: AppDatabase = AppDatabase.Companion.getDatabase(application)
		playDAO = database.playDAO()
		placePlayDAO = database.playPlaceDAO()
		playService = PlayService.Companion.getInstance(application)
		tourRepo = TourRepository.Companion.getInstance(application)
		userRepo = UserRepository.Companion.getInstance(application)
	}
}