package com.devpmts.kolporit.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;

public class Wahlbezirk {

    @Id
    String id;

    public Bezirk bezirk = new Bezirk();

    public Integer nummer = -1;

    public Integer plz = -1;

    public String anschrift = "-";

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
