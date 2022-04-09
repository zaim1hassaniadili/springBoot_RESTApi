package com.example.revamp.Service;

import com.example.revamp.Utils.meteoJackson.Meteo;
import org.springframework.http.ResponseEntity;

interface ServiceWeather {
    ResponseEntity getAllInfo(String city);
    ResponseEntity getTheTemperature(String city);
    ResponseEntity getDescription(String city);
}
