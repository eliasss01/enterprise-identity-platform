package com.bourasenterprises.soapadapter.mapper;

import com.bourasenterprises.identity.soap.gen.GetUserResponse;
import com.bourasenterprises.soapadapter.client.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SoapUserMapper {

    GetUserResponse toSoap(UserResponse response);
}
