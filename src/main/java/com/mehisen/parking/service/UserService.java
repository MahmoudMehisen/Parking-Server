package com.mehisen.parking.service;


import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.payload.request.UserRequest;
import com.mehisen.parking.payload.resposne.UserResponse;
import com.mehisen.parking.repository.SlotRepository;
import com.mehisen.parking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;


    public UserEntity userInfo(Long id) {
        return userRepository.findById(id).get();
    }

    public List<UserEntity> allUsers() {
        return userRepository.findAll();
    }

    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserEntity addVipUser(UserEntity userEntity, SlotEntity slotEntity) {
        slotEntity.setUser(userEntity);

        userEntity.setIsVip(true);
        userEntity.setSlot(slotEntity);

        slotRepository.save(slotEntity);
        userRepository.save(userEntity);

        return userEntity;
    }

    public UserEntity removeVipUser(UserEntity userEntity,SlotEntity slotEntity) {
        slotEntity.setUser(null);

        userEntity.setIsVip(false);
        userEntity.setSlot(null);

        slotRepository.save(slotEntity);
        userRepository.save(userEntity);

        return userEntity;
    }


    private UserResponse getUerFromEntity(UserEntity userEntity) {
        List<String> roles = userEntity.getRoles().stream().map(roleEntity -> roleEntity.getName().toString()).toList();
        UserResponse userResponse = new UserResponse(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), 0, userEntity.getIsVip(), userEntity.getIsComing(), roles);
        if (userEntity.getSlot() != null) {
            userResponse.setSlotId(userEntity.getSlot().getId());
        }
        return userResponse;
    }


}
