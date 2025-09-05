package com.producthor.Producthor.rest.response;
import com.producthor.Producthor.domain.dao.OrderDao;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class OrderResponse {
    private OrderDao order;
    private BaseResponse baseResponse;
}
