package com.example.fit4life.soap.endpoint;
import java.util.Optional;

import org.proekt2.fit4life.AddPhotoRequest;
import org.proekt2.fit4life.AddPhotoResponse;
import org.proekt2.fit4life.BanUserRequest;
import org.proekt2.fit4life.BanUserResponse;
import org.proekt2.fit4life.ChatRestrictUserRequest;
import org.proekt2.fit4life.ChatRestrictUserResponse;
import org.proekt2.fit4life.ChatUnrestrictUserRequest;
import org.proekt2.fit4life.ChatUnrestrictUserResponse;
import org.proekt2.fit4life.ResetPasswordRequest;
import org.proekt2.fit4life.ResetPasswordResponse;
import org.proekt2.fit4life.SubscribeStudioRequest;
import org.proekt2.fit4life.SubscribeStudioResponse;
import org.proekt2.fit4life.UnBanUserRequest;
import org.proekt2.fit4life.UnBanUserResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.fit4life.model.Photo;
import com.example.fit4life.model.Studio;
import com.example.fit4life.model.Subscription;
import com.example.fit4life.model.User;
import com.example.fit4life.repository.StudioRepository;
import com.example.fit4life.repository.UserRepository;
@Endpoint
public class UserEndpoint {
    private static final String NAMESPACE_URI = "http://fit4life.com/fit4life/usersoap";
    private UserRepository userRepository;
    private StudioRepository studioRepository;
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart="BanUserRequest")
    @ResponsePayload
    public BanUserResponse handleBanUser(@RequestPayload BanUserRequest request){
        BanUserResponse response = new BanUserResponse();

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if(userOpt.isPresent()){
            if (request.isBanned()){
                User user = userOpt.get();
                user.setBanned(true);
                userRepository.save(user);

                response.setStatus("SUCCESS");
                response.setMessage("User " + request.getUsername() + " has been banned.");
            }else{
                response.setStatus("NO_ACTION");
                response.setMessage("Ban flag was false - no action taken.");
            }
        }
        return response;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart="UnBanUserRequest")
    @ResponsePayload
    public UnBanUserResponse handleUnBanUser(@RequestPayload UnBanUserRequest request){
        UnBanUserResponse response = new UnBanUserResponse();

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if(userOpt.isPresent()){
            if (request.isUnban()){
                User user = userOpt.get();
                user.setBanned(false);
                userRepository.save(user);

                response.setStatus("SUCCESS");
                response.setMessage("User " + request.getUsername() + " has been unbanned.");
            }else{
                response.setStatus("NO_ACTION");
                response.setMessage("Ban flag was false - no action taken.");
            }
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart="ChatRestrictUserRequest")
    @ResponsePayload
    public ChatRestrictUserResponse handleChatRestrictUser(@RequestPayload ChatRestrictUserRequest request){
        ChatRestrictUserResponse response = new ChatRestrictUserResponse();

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if(userOpt.isPresent()){
            if(request.isChatRestrict()){
                User user = userOpt.get();
                user.setChatRestricted(true);
                userRepository.save(user);
                
                response.setStatus("SUCCESS");
                response.setMessage("User " + request.getUsername() + " has been chatrestricted.");
            }else{
                response.setStatus("NO_ACTION");
                response.setMessage("ChatRestrict flag was false - no action taken.");
            }
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart="ChatUnrestrictUserRequest")
    @ResponsePayload
    public ChatUnrestrictUserResponse handleChatUnrestrictUser(@RequestPayload ChatUnrestrictUserRequest request){
        ChatUnrestrictUserResponse response = new ChatUnrestrictUserResponse();

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if(userOpt.isPresent()){
            if(request.isChatUnrestrict()){
                User user = userOpt.get();
                user.setChatRestricted(false);
                userRepository.save(user);
                
                response.setStatus("SUCCESS");
                response.setMessage("User " + request.getUsername() + " has been unrestricted.");
            }else{
                response.setStatus("NO_ACTION");
                response.setMessage("ChatUnrestrict flag was false - no action taken.");
            }
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ResetPasswordRequest")
    @ResponsePayload
    public ResetPasswordResponse handleResetPassword(@RequestPayload ResetPasswordRequest request){
        ResetPasswordResponse response = new ResetPasswordResponse();

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setPassword(request.getNewPassword());
            userRepository.save(user);

            response.setStatus("SUCCESS");
            response.setMessage("Password successfully updated for user:"+user.getUsername());
        } else{
            response.setStatus("NO_ACTION");
            response.setMessage("User not found!");
        }
        return response;
    }

    @PayloadRoot(namespace=NAMESPACE_URI, localPart="SubscribeStudioRequest")
    @ResponsePayload
    public SubscribeStudioResponse handleSubscribeStudio(@RequestPayload SubscribeStudioRequest request) {
        SubscribeStudioResponse response = new SubscribeStudioResponse();

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        Optional<Studio> studioOpt = studioRepository.findById(request.getStudioId());

        if(userOpt.isEmpty()){
            response.setStatus("NO_ACTION");
            response.setMessage("User not found!");
        } else if (studioOpt.isEmpty()){
            response.setStatus("NO_ACTION");
            response.setMessage("Studio not found!");
        } else{
            User user = userOpt.get();
            Studio studio = studioOpt.get();
            Subscription sub = new Subscription();

            sub.setStudio(studio);
            sub.setUser(user);
            sub.setDuration(request.getDuration());

            user.getSubscriptions().add(sub);
            userRepository.save(user);

            response.setStatus("SUCCESS");
            response.setMessage("User subscribed to studio successfully!");
        }
        return response;
    }

    @PayloadRoot(namespace=NAMESPACE_URI, localPart="AddPhotoRequest")
    @ResponsePayload
    public AddPhotoResponse handleAddPhoto(@RequestPayload AddPhotoRequest request){
        AddPhotoResponse response = new AddPhotoResponse();

        Optional<Studio> studioOpt = studioRepository.findById(request.getStudioId());
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if(studioOpt.isEmpty()){
            response.setStatus("NO_ACTION");
            response.setMessage("Studio not found!");
        } else if(userOpt.isEmpty()){
            response.setStatus("NO_ACTION");
            response.setMessage("User not found!");
        } else{
            Studio studio = studioOpt.get();
            User user = userOpt.get();
            Photo photo = new Photo();

            photo.setStudio(studio);
            photo.setUploadedBy(user);

            response.setStatus("SUCCESS");
            response.setMessage("Photo added to studio "+studio.getName()+ "successfully by" +user.getUsername());
        }
        return response;
    }
}

