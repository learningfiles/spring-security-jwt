package com.example.demo.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class Tester {
    @Test
    public void object_to_json_and_reverse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Car car = new Car("color-1", "car-1");
        String json = mapper.writeValueAsString(car);
        System.out.println(json);

        Car carMapped = mapper.readValue(json, Car.class);
        System.out.println(carMapped);
    }
}
