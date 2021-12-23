package amin.code.orders.api;

import amin.code.orders.Repository.ShippedOrderRepository;
import amin.code.orders.email.EmailServiceImpl;
import amin.code.orders.entity.OrdersDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.SendFailedException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:8081")
@RequestMapping(path = "api/v1/orders")
@Slf4j
public class ApiController {
    @Autowired
    EmailServiceImpl emailService;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    ShippedOrderRepository shippedOrderRepository;

    @Autowired
    @Qualifier("emailSenderJob")
    private Job emailSenderJob;

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
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        List<OrdersDTO> emailNotSentOrders = shippedOrderRepository.findByEmailSentAndStatus(false, true);

        if (emailNotSentOrders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Nothing to send"));
        }

        try {
            final JobExecution jobExecution = jobLauncher.run(emailSenderJob, jobParameters);
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException | JobParametersInvalidException | JobRestartException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success"));
    }
}
