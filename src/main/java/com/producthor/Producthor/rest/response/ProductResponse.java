package com.producthor.Producthor.rest.response;

import com.producthor.Producthor.domain.dao.ProductDao;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ProductResponse {
    private ProductDao product;
    private BaseResponse baseResponse;
}
