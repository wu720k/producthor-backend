package com.producthor.Producthor.rest.response;

import com.producthor.Producthor.domain.dao.UserDao;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UsersResponse {
    private List<UserDao> users;
    private BaseResponse baseResponse;
}
