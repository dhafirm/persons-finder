package com.dhafir.demo.types;

public class Longitude extends ValueType<Long>{
    public Longitude(long value) {
        super(value);
    }
    public static Longitude of(long longitude) {
        return new Longitude(longitude);
    }
}
