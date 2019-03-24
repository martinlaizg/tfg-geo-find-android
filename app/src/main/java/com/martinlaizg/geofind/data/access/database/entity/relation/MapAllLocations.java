package com.martinlaizg.geofind.data.access.database.entity.relation;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class MapAllLocations {
	@Embedded
	public Map map;
	@Relation(parentColumn = "id", entityColumn = "map_id", entity = Location.class)
	public List<Location> locations;
}
