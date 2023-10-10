package com.example.common.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoutePoint {
    private String name;
    private double x;
    private double y;

    public RoutePoint(Station station) {
        this.name = station.getName();
        this.x = station.getX();
        this.y = station.getY();
    }
}
