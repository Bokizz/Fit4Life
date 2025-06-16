package com.example.fit4life.soap.endpoint;
import org.proekt2.fit4life.BanUserRequest;
import org.proekt2.fit4life.BanUserResponse;
import org.proekt2.fit4life.ChatRestrictUserRequest;
import org.proekt2.fit4life.ChatRestrictUserResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
@Endpoint
public class UserEndpoint {
    private static final String NAMESPACE_URI = "http://fit4life.com/fit4life/usersoap";
    @PayloadRoot(namespace = NAMESPACE_URI, localPart="BanUserRequest")
    @ResponsePayload
    public BanUserResponse handleBanUser(@RequestPayload BanUserRequest request){
        BanUserResponse response = new BanUserResponse();

        if (request.isBanned()){
            response.setStatus("SUCCESS");
            response.setMessage("User " + request.getUsername() + " has been banned.");
        }else{
            response.setStatus("NO_ACTION");
            response.setMessage("Ban flag was false - no action taken.");
        }
        
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart="ChatRestrictUserRequest")
    @ResponsePayload
    public ChatRestrictUserResponse handleChatRestrictUser(@RequestPayload ChatRestrictUserRequest request){
        ChatRestrictUserResponse response = new ChatRestrictUserResponse();

        if(request.isChatRestricted()){
            response.setStatus("SUCCESS");
            response.setMessage("User " + request.getUsername() + " has been chatrestricted.");
        }else{
            response.setStatus("NO_ACTION");
            response.setMessage("ChatRestrict flag was false - no action taken.");
        }
        return response;
    }
}

