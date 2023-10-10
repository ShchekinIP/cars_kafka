package com.example.office.service;

import com.example.common.bean.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class WaitingRoutesService {
    private final Lock lock = new ReentrantLock(true);
    private final List<Route> routes = new ArrayList<>();

    public List<Route> routes() {
        return routes;
    }

    public void add(Route route) {
        try {
            lock.lock();
            routes.add(route);
        } finally {
            lock.unlock();
        }
    }

    public void remove(Route route) {
        try {
            lock.lock();
            routes.removeIf(route::equals);
        } finally {
            lock.unlock();
        }
    }
}
