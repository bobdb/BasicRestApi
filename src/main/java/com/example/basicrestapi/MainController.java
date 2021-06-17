package com.example.basicrestapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.basicrestapi.StatusName.*;

@RestController
public class MainController {

    @Autowired
    UserService userService;

    /** API Requirements **/

    //List loyalty programs that user can transfer to
    @GetMapping("/user/{id}/programs")
    public List<Program> getPrograms(@PathVariable("id") long id) {
        return userService.findProgramsById(id);
    }

    //See user transfer history
    @GetMapping("/user/{id}/history")
    public List<Pointtransfer> getPointTransfer(@PathVariable("id") long id) {
        return userService.findPointTransfersById(id);
    }

    //Fund account account by adding points
    @PostMapping("/user/{id}/fund")
    public ResponseEntity<FundingResponse> fundAccount(@PathVariable("id") long id, @RequestBody FundingRequest fundingRequest) {
        FundingResponse response = userService.fundAccount(id, new Pointtransfer(id, fundingRequest));
        if (response.getStatus().equals(SUCCESS))
            return new ResponseEntity<FundingResponse>(response, HttpStatus.OK);
        else
            return ResponseEntity.badRequest().body(response);
    }

    //Transfer points to another loyalty program
    @PostMapping("/user/{id}/transfer")
    public ResponseEntity<FundingResponse> transfer(@PathVariable("id") long id, @RequestBody Pointtransfer pointTransfer) {
        FundingResponse response = userService.transferPoints(id, pointTransfer);
        if (response.getStatus().equals(SUCCESS))
            return new ResponseEntity<FundingResponse>(response, HttpStatus.OK);
        else
            return ResponseEntity.badRequest().body(response);
    }

    // Additional Endpoints
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") long id) {
        return userService.findById(id);
    }


    // "dummy" endpoint to represent a 3rd party receiving transferred points
    @GetMapping("/echo")
    public String echo() {
        return "ok";
    }

}