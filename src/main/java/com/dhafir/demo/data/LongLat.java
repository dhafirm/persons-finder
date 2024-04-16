package com.dhafir.demo.data;

import com.dhafir.demo.types.Latitude;
import com.dhafir.demo.types.Longitude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LongLat {
    private double latitude;
    private double longitude;



    public static LongLat of(Location location) {
        return new LongLat(location.getLatitude(), location.getLongitude());
    }
}
