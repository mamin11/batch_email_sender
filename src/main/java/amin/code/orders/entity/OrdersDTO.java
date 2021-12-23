package amin.code.orders.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Builder
@Data
@Entity(name = "shipped_order")
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long orderId;
    private String firstName;
    private String lastName;
    private String email;
    private String cost;
    private String itemId;
    private String itemName;
    private Date shipDate;
    private boolean status;
    private boolean emailSent;
}
