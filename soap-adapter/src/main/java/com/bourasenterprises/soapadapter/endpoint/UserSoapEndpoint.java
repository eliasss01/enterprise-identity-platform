package com.bourasenterprises.soapadapter.endpoint;

import com.bourasenterprises.identity.soap.gen.GetUserRequest;
import com.bourasenterprises.identity.soap.gen.GetUserResponse;
import com.bourasenterprises.soapadapter.client.CoreServiceClient;
import com.bourasenterprises.soapadapter.client.dto.UserResponse;
import com.bourasenterprises.soapadapter.mapper.SoapUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@Slf4j
public class UserSoapEndpoint {

    private static final String NAMESPACE = "http://example.com/users";

    private final CoreServiceClient client;
    private final SoapUserMapper mapper;

    public UserSoapEndpoint(CoreServiceClient client, SoapUserMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "GetUserRequest")
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUserRequest request) {
        log.info("GetUserRequest SOAP per l'utente con ID: {}", request.getId());
        UserResponse user = client.getUser(request.getId());
        log.info("Dati recuperati corretamente dal core-service per utente con ID: {}", request.getId());
        return mapper.toSoap(user);
    }

}
