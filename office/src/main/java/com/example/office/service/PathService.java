package com.example.office.service;

import com.example.common.bean.Route;
import com.example.common.bean.RoutePath;
import com.example.common.bean.RoutePoint;
import com.example.office.provider.StationsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {
    private final StationsProvider stationsProvider;

    public RoutePath makePath(String from, String to) {
        return new RoutePath(stationsProvider.getRoutePoint(from), stationsProvider.getRoutePoint(to), 0);
    }

    public Route convertToRoute(List<String> locations) {
        Route route = new Route();
        List<RoutePath> paths = new ArrayList<>();
        List<RoutePoint> points = new ArrayList<>();

        locations.forEach(location -> {
            stationsProvider.getStations().stream().filter(station -> station.getName().equals(location)).findFirst().ifPresent(station -> {
                RoutePoint point = new RoutePoint(station);
                points.add(point);
            });
        });

        for (int i = 0; i < points.size() - 1; i++) {
            paths.add(new RoutePath());
        }

        paths.forEach(row -> {
            int index = paths.indexOf(row);
            if (row.getFrom() == null) {
                row.setFrom(points.get(index));
                int i = index + 1;
                if (points.size() > i) {
                    row.setTo(points.get(i));
                } else {
                    row.setTo(points.get(index));
                }
            }
        });

        route.setPaths(paths);

        return route;
    }
}
