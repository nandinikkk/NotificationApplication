package com.location.reminder.util;


import com.google.android.gms.location.places.Place;

import java.lang.reflect.Field;
import java.util.List;

public class PlaceCommons {

    public static String getPlaceTypesCSV(Place myPlace) {


        List<Integer> placeTypes = myPlace.getPlaceTypes();
        String placetypes = "";
        for (Integer placetype : placeTypes) {
            try {
                placetypes += "," + getPlaceTypeForValue(placetype);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return placetypes;

    }

    private static String getPlaceTypeForValue(int value) throws Exception {
        Field[] fields = Place.class.getDeclaredFields();
        String name;
        for (Field field : fields) {
            name = field.getName().toLowerCase();
            if (name.startsWith("type_") && field.getInt(null) == value) {
                return name.replace("type_", "");
            }
        }
        throw new IllegalArgumentException("place value " + value + " not found.");
    }

}
