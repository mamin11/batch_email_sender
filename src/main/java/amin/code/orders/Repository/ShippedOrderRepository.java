package amin.code.orders.Repository;

import amin.code.orders.entity.OrdersDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShippedOrderRepository extends JpaRepository<OrdersDTO, Long> {
    Optional<OrdersDTO> findByStatus(boolean status);
}