package com.mehisen.parking.service;

import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.payload.resposne.SlotResponse;
import com.mehisen.parking.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;

    public SlotEntity findById(Long id) {
        return slotRepository.findById(id).get();
    }

    public void addVipUser(SlotEntity slotEntity, UserEntity userEntity) {
        slotEntity.setUser(userEntity);
        slotRepository.save(slotEntity);
    }

    public void removeVipUser(SlotEntity slotEntity) {
        slotEntity.setUser(null);
        slotRepository.save(slotEntity);
    }

    public SlotEntity createNewSlot() {
        return slotRepository.save(new SlotEntity());
    }

    public List<SlotEntity> allSlots() {
        return slotRepository.findAll();
    }

    public SlotEntity findFirstByUserId(Long id) {
        return slotRepository.findFirstByUserId(id).get();
    }

}
