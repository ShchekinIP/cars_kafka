package com.example.office.controller;

import com.example.common.bean.Route;
import com.example.office.service.PathService;
import com.example.office.service.WaitingRoutesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/routes")
public class RouteRest {
    private final WaitingRoutesService waitingRoutesService;
    private final PathService pathService;

    @PostMapping(path = "route")
    public void addRoute(@RequestBody List<String> stationList) {
        Route route = pathService.convertToRoute(stationList);
        waitingRoutesService.add(route);
    }
}
