package com.example.revamp.api;

import com.example.revamp.Service.ServiceGmailAPIImplementation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/gmail")
@RequiredArgsConstructor
public class GmailController{
   ServiceGmailAPIImplementation serviceGmailAPIImplementation;
    {
        try {
            serviceGmailAPIImplementation = new ServiceGmailAPIImplementation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/user")
     void gmailUser(@RequestParam(defaultValue ="10")int nbr){
         try{
             System.out.println(serviceGmailAPIImplementation.getEmail(nbr));
         }catch(Exception ex){
             ex.getMessage();
         }

    }

    @GetMapping("/messagestotal")
    void gmailTotal(){
        try{
            System.out.println(serviceGmailAPIImplementation.getNumberOfEmail());
        }catch(Exception ex){
            ex.getMessage();
        }

    }
    @GetMapping("/delete")
    void gmailDelete(@RequestParam(defaultValue ="10")int nbr){
        try{
            serviceGmailAPIImplementation.deleteEmail(nbr);
        }catch(Exception ex){
            ex.getMessage();
        }

    }

    @GetMapping("/deleteall")
    void gmailDeleteAll(){
        try{
            serviceGmailAPIImplementation.deleteAllEmail();
        }catch (Exception ex){
            ex.getMessage();
        }
    }
}
