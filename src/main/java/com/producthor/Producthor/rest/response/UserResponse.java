package com.producthor.Producthor.rest.response;

import com.producthor.Producthor.domain.dao.UserDao;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private UserDao user;
    private BaseResponse baseResponse;
}
