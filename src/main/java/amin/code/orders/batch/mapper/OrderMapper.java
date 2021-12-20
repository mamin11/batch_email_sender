package amin.code.orders.batch.mapper;

import amin.code.orders.entity.OrdersDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements RowMapper<OrdersDTO> {
    @Override
    public OrdersDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return OrdersDTO
                .builder()
                .orderId(rs.getLong("order_id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .cost(rs.getString("cost"))
                .itemId(rs.getString("item_id"))
                .itemName(rs.getString("item_name"))
                .shipDate(rs.getDate("ship_date"))
                .status(rs.getBoolean("status"))
                .emailSent(rs.getBoolean("email_sent"))
                .build();
    }
}
