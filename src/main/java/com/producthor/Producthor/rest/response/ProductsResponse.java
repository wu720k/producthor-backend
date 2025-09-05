package com.producthor.Producthor.rest.response;

import com.producthor.Producthor.domain.dao.ProductDao;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductsResponse {
    private List<ProductDao> products;
    private BaseResponse baseResponse;
}