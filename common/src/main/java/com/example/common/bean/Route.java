package com.example.common.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    private String carName;
    private List<RoutePath> paths = new ArrayList<>();

    public boolean isNotAssigned() {
        return Objects.isNull(carName);
    }
}
