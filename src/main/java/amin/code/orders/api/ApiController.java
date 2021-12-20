package amin.code.orders.api;

import amin.code.orders.email.EmailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.SendFailedException;

@RestController
@CrossOrigin("http://localhost:8081")
@RequestMapping(path = "api/v1/orders")
@Slf4j
public class ApiController {
    @Autowired
    EmailServiceImpl emailService;

    @GetMapping("/all")
    public ResponseEntity<ResponseMessage> getAllOrders() {
        try {
            emailService.sendSimpleMessage("hello@world.com", "This is the message", "Thank you for registering with us");
        } catch (SendFailedException sendFailedException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("failed to send email"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("email sent"));
    }

    @PostMapping("/send/notification")
    public ResponseEntity<ResponseMessage> sendEmails() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success"));
    }
}