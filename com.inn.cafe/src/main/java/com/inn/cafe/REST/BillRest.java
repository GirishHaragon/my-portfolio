package com.inn.cafe.REST;

import com.inn.cafe.POJO.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/bill")
public interface BillRest {

    @PostMapping(path = "/generateReport")
    ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap);//Why Object bcz we r excepting a List of JSON array of type Object.

    @GetMapping(path = "/getBills")
    ResponseEntity<List<Bill>> getBills();

    @PostMapping(path = "/getPdf")
    ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap);

    @DeleteMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteBill(@PathVariable Integer id);
}
