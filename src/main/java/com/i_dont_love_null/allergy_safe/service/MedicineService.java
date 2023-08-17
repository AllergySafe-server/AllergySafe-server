package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.MedicineResponse;
import com.i_dont_love_null.allergy_safe.model.Medicine;
import com.i_dont_love_null.allergy_safe.repository.MedicineRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;

    public List<MedicineResponse> getAllMedicines() {
        List<Medicine> medicines = medicineRepository.findAll();
        List<MedicineResponse> result = new ArrayList<>();

        for (Medicine medicine : medicines) {
            result.add(medicine.toMedicineResponse());
        }
        return result;
    }

    public MedicineResponse getMedicineResponseById(Long medicineId) {
        Optional<Medicine> medicine = medicineRepository.findById(medicineId);

        if (medicine.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 의약품입니다.");
        }

        return medicine.get().toMedicineResponse();
    }

    public Medicine getMedicineById(Long medicineId) {
        Optional<Medicine> medicine = medicineRepository.findById(medicineId);

        if (medicine.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 의약품입니다.");
        }

        return medicine.get();
    }

}
