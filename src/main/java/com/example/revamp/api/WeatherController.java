package com.example.revamp.api;


import com.example.revamp.Service.ServiceGmailAPIImplementation;
import com.example.revamp.Service.ServiceWeatherImplementation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {
    ServiceWeatherImplementation serviceWeatherImplementation = new ServiceWeatherImplementation();


    @GetMapping("/weatherinfo")
    ResponseEntity getWeatherInfo(@RequestParam(defaultValue = "Greenwich")String city){
        return serviceWeatherImplementation.getAllInfo(city);
    }
    @GetMapping("/weatherdescription")
    ResponseEntity getWeahterDescription(@RequestParam(defaultValue = "Greenwich")String city){
        return  serviceWeatherImplementation.getDescription(city);
    }
    @GetMapping("/weathertemperature")
    ResponseEntity getTemperature(@RequestParam(defaultValue = "Greenwich")String city){
        return  serviceWeatherImplementation.getTheTemperature(city);
    }

}
