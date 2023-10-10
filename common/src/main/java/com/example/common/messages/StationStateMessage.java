package com.example.common.messages;

import com.example.common.bean.Source;
import com.example.common.bean.Station;
import com.example.common.bean.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StationStateMessage extends Message {

    private Station station;

    public StationStateMessage() {
        this.source = Source.STATION;
        this.type = Type.STATE;
    }

    public StationStateMessage(Station station) {
        this();
        this.station = station;
    }
}
