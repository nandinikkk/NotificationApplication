package com.location.reminder.dbscan;

import java.util.*;

import com.location.reminder.model.Locationhistory;

public class Dbscan {

	public int e;
	public int minpt;

	public Vector<Locationhistory> pointList;

	public Dbscan(int tdistance, int minpoints, Vector<Locationhistory> pointList) {
		this.e = tdistance;
		this.minpt = minpoints;
		this.pointList = pointList;
	}

	public Vector<List<Locationhistory>> resultList = new Vector<List<Locationhistory>>();

	Utility utility = new Utility();

	public Vector<Locationhistory> neighbours;

	public Vector<List<Locationhistory>> applyDbscan() {
		resultList.clear();
		// pointList.clear();
		utility.visitList.clear();

		int index2 = 0;

		while (pointList.size() > index2) {
			Locationhistory p = pointList.get(index2);
			if (!utility.isVisited(p)) {

				utility.visited(p);

				neighbours = utility.getNeighbours(p, pointList, e);

				if (neighbours.size() >= minpt) {

					int ind = 0;
					while (neighbours.size() > ind) {

						Locationhistory r = neighbours.get(ind);
						if (!utility.isVisited(r)) {
							utility.visited(r);
							Vector<Locationhistory> neighbours2 = utility.getNeighbours(r, pointList, e);
							if (neighbours2.size() >= minpt) {
								neighbours = utility.Merge(neighbours, neighbours2);
							}
						}
						ind++;
					}

					resultList.add(neighbours);
					if(resultList.size()>0){
						return resultList;
					}
				}

			}
			index2++;
		}
		return resultList;
	}

}
