package com.mehisen.parking.service;


import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.model.request.UserRequest;
import com.mehisen.parking.model.resposne.UserResponse;
import com.mehisen.parking.repository.SlotRepository;
import com.mehisen.parking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;

    public ResponseEntity<?> createNewUser(UserRequest userRequest) {
        try {
            UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(userRequest, userEntity);
            UserEntity saved = userRepository.save(userEntity);
            return ResponseEntity.ok(getUerFromEntity(saved));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error when creating new user");
        }
    }

    public ResponseEntity<?> allUsers() {
        try {
            List<UserEntity> allUsers = userRepository.findAll();
            List<UserResponse> result = allUsers.stream().map(this::getUerFromEntity).toList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error when fetching all users");
        }
    }

    public ResponseEntity<?> addVipUser(UserRequest userRequest) {
        try {
            Optional<SlotEntity> slEntity = slotRepository.findFirstByUserId(null);
            Optional<UserEntity> userEntity = userRepository.findById(userRequest.getId());

            if (userEntity.isEmpty()) {
                return ResponseEntity.status(400).body("User Not Found");
            }

            UserEntity updatedUser = userEntity.get();

            if (updatedUser.getIsVip()) {
                return ResponseEntity.status(400).body("User Already VIP");
            }

            if (slEntity.isEmpty()) {
                return ResponseEntity.status(400).body("No Available Slot");
            }

            SlotEntity updatedSlot = slEntity.get();
            updatedSlot.setUser(updatedUser);

            updatedUser.setIsVip(true);
            updatedUser.setSlot(updatedSlot);

            slotRepository.save(updatedSlot);
            userRepository.save(updatedUser);

            return ResponseEntity.ok(getUerFromEntity(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error when adding user to vip");
        }
    }

    public ResponseEntity<?> removeVipUser(UserRequest userRequest) {
        try {
            Optional<UserEntity> userEntity = userRepository.findById(userRequest.getId());

            if (userEntity.isEmpty()) {
                return ResponseEntity.status(400).body("User Not Found");
            }

            UserEntity updatedUser = userEntity.get();

            if (!updatedUser.getIsVip() || updatedUser.getSlot() == null) {
                return ResponseEntity.status(400).body("User Not VIP");
            }
            Optional<SlotEntity> slEntity = slotRepository.findById(updatedUser.getSlot().getId());

            if (slEntity.isEmpty()) {
                return ResponseEntity.status(400).body("No Available Slot");
            }

            SlotEntity updatedSlot = slEntity.get();
            updatedSlot.setUser(null);

            updatedUser.setIsVip(false);
            updatedUser.setSlot(null);

            slotRepository.save(updatedSlot);
            userRepository.save(updatedUser);

            return ResponseEntity.ok(getUerFromEntity(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error when remove user from vip");
        }
    }

    private UserResponse getUerFromEntity(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), 0, userEntity.getIsVip(), userEntity.getIsComing());
        if (userEntity.getSlot() != null) {
            userResponse.setSlotId(userEntity.getSlot().getId());
        }
        return userResponse;
    }


}
