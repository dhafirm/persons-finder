package com.dhafir.demo.types;


public class Latitude extends ValueType<Long> {
    public Latitude(long value) {
        super(value);
    }

    public static Latitude of(long latitude) {
        return new Latitude(latitude);
    }

}

