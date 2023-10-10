package com.example.common.messages;

import com.example.common.bean.Car;
import com.example.common.bean.Source;
import com.example.common.bean.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarStateMessage extends Message {

    private Car car;

    public CarStateMessage() {
        this.source = Source.CAR;
        this.type = Type.STATE;
    }

    public CarStateMessage(Car car) {
        this();
        this.car = car;
    }
}
