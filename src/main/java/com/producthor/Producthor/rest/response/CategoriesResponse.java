package com.producthor.Producthor.rest.response;

import com.producthor.Producthor.domain.dao.CategoryDao;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoriesResponse {
    private List<CategoryDao> categories;
    private BaseResponse baseResponse;
}