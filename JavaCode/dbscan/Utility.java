package com.location.reminder.dbscan;

import java.util.Iterator;
import java.util.Vector;

import com.location.reminder.model.Locationhistory;

public class Utility {

	public Vector<Locationhistory> visitList = new Vector<Locationhistory>();

	public double getDistance(Locationhistory p, Locationhistory q) {

		double theta = p.getLongitude() - q.getLongitude();
		double dist = Math.sin(deg2rad(p.getLatitude())) * Math.sin(deg2rad(q.getLatitude()))
				+ Math.cos(deg2rad(p.getLatitude())) * Math.cos(deg2rad(q.getLatitude())) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;

		dist = dist * 1000;

		return dist;

	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public Vector<Locationhistory> getNeighbours(Locationhistory p, Vector<Locationhistory> pointList, int e) {
		Vector<Locationhistory> neigh = new Vector<Locationhistory>();
		
		Iterator<Locationhistory> points = pointList.iterator();
		while (points.hasNext()) {
			Locationhistory q = points.next();
			double distance = getDistance(p, q);
			if (distance <= e) {
				neigh.add(q);
			}
		}
		return neigh;
	}

	public void visited(Locationhistory d) {
		visitList.add(d);

	}

	public boolean isVisited(Locationhistory c) {
		if (visitList.contains(c)) {
			return true;
		} else {
			return false;
		}
	}

	public Vector<Locationhistory> Merge(Vector<Locationhistory> a, Vector<Locationhistory> b) {

		Iterator<Locationhistory> it5 = b.iterator();
		while (it5.hasNext()) {
			Locationhistory t = it5.next();
			if (!a.contains(t)) {
				a.add(t);
			}
		}
		return a;
	}

	public Boolean equalPoints(Locationhistory m, Locationhistory n) {
		if ((m.getLatitude() == n.getLatitude()) && (m.getLongitude() == n.getLongitude()))
			return true;
		else
			return false;
	}

}
