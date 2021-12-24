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
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
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

    @GetMapping("/test")
    public ResponseEntity<ResponseMessage> getAllOrders() {
        try {
            emailService.sendSimpleMessage("hello@world.com", "This is the message", "Thank you for registering with us");
        } catch (SendFailedException sendFailedException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("failed to send email"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("email sent"));
    }

    @GetMapping("/count")
    public ResponseEntity<OrdersResponse> getTotalOrder() {
        long ordersCount = shippedOrderRepository.count();

        OrdersResponse response = new OrdersResponse();
        response.setMessage("success");
        HashMap<String, Long> count = new HashMap<>();
        count.put("total", ordersCount);
        response.setResponse(count);

        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/email-sent")
    public ResponseEntity<OrdersResponse> getEmailsSent() {
        long ordersCount = shippedOrderRepository.countByEmailSent(true);

        OrdersResponse response = new OrdersResponse();
        response.setMessage("success");
        HashMap<String, Long> count = new HashMap<>();
        count.put("total", ordersCount);
        response.setResponse(count);

        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/send/notification")
    public ResponseEntity<ResponseMessage> sendEmails() {
        Random random = new Random();
        int randomWithNextInt = random.nextInt();

        JobParameter param = new JobParameter(String.valueOf(randomWithNextInt));
        JobParameters jobParameters = new JobParametersBuilder().addParameter("unique", param).toJobParameters();
        List<OrdersDTO> emailNotSentOrders = shippedOrderRepository.findByEmailSentAndStatus(false, true);

        if (emailNotSentOrders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Nothing to send"));
        }

        try {
            final JobExecution jobExecution = jobLauncher.run(emailSenderJob, jobParameters);
                Date create = jobExecution.getStartTime();
                Date end = jobExecution.getEndTime();
                int diff = end.getSeconds() - create.getSeconds();
                log.debug("difference = {}", diff);
                TimeUnit.SECONDS.sleep(diff);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success"));
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException | JobParametersInvalidException |InterruptedException | JobRestartException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }
}
