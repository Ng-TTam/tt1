package com.manage.userManagement.mapper;

import com.manage.userManagement.dto.request.UserRequest;
import com.manage.userManagement.dto.response.UserResponse;
import com.manage.userManagement.entity.User;
import com.manage.userManagement.entity.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);

    User toUser(UserDocument userDocument);

    UserResponse toUserResponse(User user);

    UserDocument toUserDocument(User user);

    void updateUser(@MappingTarget User user, UserRequest userRequest);
}
