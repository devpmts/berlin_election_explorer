package com.devpmts.kolporit.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;

public class Bezirk {

    public String name = "none";

    @Id
    public Integer bezirkNummer = -1;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
