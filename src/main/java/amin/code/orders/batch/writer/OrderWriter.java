package amin.code.orders.batch.writer;

import amin.code.orders.Repository.ShippedOrderRepository;
import amin.code.orders.entity.OrdersDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class OrderWriter implements ItemWriter<OrdersDTO> {
    @Autowired
    ShippedOrderRepository shippedOrderRepository;

    @Override
    public void write(List<? extends OrdersDTO> list) throws Exception {
      log.debug("item writer: {}", list.get(0));
      shippedOrderRepository.saveAllAndFlush(list);
    }
}
