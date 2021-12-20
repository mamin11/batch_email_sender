package amin.code.orders.batch.processor;

import amin.code.orders.email.EmailServiceImpl;
import amin.code.orders.entity.OrdersDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.SendFailedException;

@Slf4j
public class OrderItemProcessor implements ItemProcessor<OrdersDTO, OrdersDTO> {

    @Autowired
    EmailServiceImpl emailService;

    @Override
    public OrdersDTO process(OrdersDTO ordersDTO) throws Exception {
        log.debug("processor: {}", ordersDTO);
        try {
            emailService.sendSimpleMessage(ordersDTO.getEmail(), "Your Order has been shipped!", "Thank you for shopping with us");
            ordersDTO.setEmailSent(true);
        } catch (SendFailedException sendFailedException) {
            log.debug("error: {}", sendFailedException.getMessage());
        }
        return ordersDTO;
    }
}
