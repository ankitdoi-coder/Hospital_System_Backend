package com.ankit.HealthCare_Backend.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AppointmentStatusEnum {
    PENDING,
    SCHEDULED,
    COMPLETED,
    CANCELED;

     @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static AppointmentStatusEnum fromValue(String value) {
        // Custom deserializer logic here
        // Map the JSON value to the corresponding enum constant
        return AppointmentStatusEnum.valueOf(value.toUpperCase());
    }
}
