package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.DiaryElementRequest;
import com.i_dont_love_null.allergy_safe.dto.DiaryRequest;
import com.i_dont_love_null.allergy_safe.dto.DiaryResponse;
import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.model.*;
import com.i_dont_love_null.allergy_safe.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final FoodRepository foodRepository;

    private final MedicineRepository medicineRepository;

    private final SymptomRepository symptomRepository;

    private final IngestedFoodRepository ingestedFoodRepository;

    private final TakenMedicineRepository takenMedicineRepository;

    private final OccuredSymptomRepository occuredSymptomRepository;

    private final ProfileRepository profileRepository;

    private final IdResponse idResponse;

    public DiaryResponse createDiary(Long profileId, DiaryRequest diaryRequest) {
        Profile profile;
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);


        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");

        LocalDate diaryDate = diaryRequest.getDate();
        if (diaryDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "미래 날짜에 다이어리를 생성할 수 없습니다.");
        }

        Diary newDiary = Diary.builder()
                .date(diaryDate)
                .profiles(new ArrayList<>(List.of(profile)))
                .build();

        profile.getDiaries().add(newDiary);

        Diary createDiary = diaryRepository.save(newDiary);

        DiaryResponse diaryResponse = new DiaryResponse();
        diaryResponse.setDate(createDiary.getDate());
        return diaryResponse;
    }

    public IdResponse addDiaryElement(Long diaryId, DiaryElementRequest diaryElementRequest) {

        Long elementId = diaryElementRequest.getId();
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        Diary diary;

        if (diaryOptional.isPresent()) diary = diaryOptional.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 다이어리입니다.");

        List<IngestedFood> ingestedFoods = diary.getIngestedFoods();
        List<TakenMedicine> takenMedicines = diary.getTakenMedicines();
        List<OccuredSymptom> occuredSymptoms = diary.getOccuredSymptoms();

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime datetime = diaryElementRequest.getDateTime();

        if (datetime.isAfter(currentDateTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "미래의 날짜로는 등록할 수 없습니다.");
        }

        switch (diaryElementRequest.getDiaryElementType()) {
            case FOOD -> {
                Optional<Food> foodOptional = foodRepository.findById(elementId);
                Food food;

                if (foodOptional.isPresent()) food = foodOptional.get();
                else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 음식입니다.");

                IngestedFood ingestedFood = IngestedFood.builder()
                        .datetime(datetime)
                        .diary(diary)
                        .food(food)
                        .build();

                ingestedFoodRepository.save(ingestedFood);

                ingestedFoods.add(ingestedFood);
            }

            case MEDICINE -> {
                Optional<Medicine> medicineOptional = medicineRepository.findById(elementId);
                Medicine medicine;

                if (medicineOptional.isPresent()) medicine = medicineOptional.get();
                else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 약품입니다.");

                TakenMedicine takenMedicine = TakenMedicine.builder()
                        .datetime(datetime)
                        .diary(diary)
                        .medicine(medicine)
                        .build();

                takenMedicineRepository.save(takenMedicine);

                takenMedicines.add(takenMedicine);
            }

            case SYMPTOM -> {
                Optional<Symptom> symptomOptional = symptomRepository.findById(elementId);
                Symptom symptom;

                if (symptomOptional.isPresent()) symptom = symptomOptional.get();
                else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 증상입니다.");

                OccuredSymptom occuredSymptom = OccuredSymptom.builder()
                        .datetime(datetime)
                        .diary(diary)
                        .symptom(symptom)
                        .build();

                occuredSymptomRepository.save(occuredSymptom);

                occuredSymptoms.add(occuredSymptom);
            }
        }

        diaryRepository.save(
                diary.toBuilder()
                        .ingestedFoods(ingestedFoods)
                        .takenMedicines(takenMedicines)
                        .occuredSymptoms(occuredSymptoms)
                        .build()
        );

        idResponse.setId(diaryId);
        return idResponse;
    }

    public IdResponse deleteDiaryElement(Long diaryId, DiaryElementRequest diaryElementRequest) {

        Long elementId = diaryElementRequest.getId();
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        Diary diary;

        if (diaryOptional.isPresent()) diary = diaryOptional.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 다이어리입니다.");

        List<IngestedFood> ingestedFoods = diary.getIngestedFoods();
        List<TakenMedicine> takenMedicines = diary.getTakenMedicines();
        List<OccuredSymptom> occuredSymptoms = diary.getOccuredSymptoms();

        switch (diaryElementRequest.getDiaryElementType()) {
            case FOOD -> {
                List<IngestedFood> newIngestedFoods = new ArrayList<>();

                for (IngestedFood ingestedFood : ingestedFoods) {
                    if (!ingestedFood.getId().equals(elementId)) newIngestedFoods.add(ingestedFood);
                }

                if (ingestedFoods.size() == newIngestedFoods.size())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "다이어리에 등록되지 않은 음식입니다.");

                ingestedFoods = newIngestedFoods;
            }

            case MEDICINE -> {
                List<TakenMedicine> newTakenMedicines = new ArrayList<>();

                for (TakenMedicine takenMedicine : takenMedicines) {
                    if (!takenMedicine.getId().equals(elementId)) newTakenMedicines.add(takenMedicine);
                }

                if (takenMedicines.size() == newTakenMedicines.size())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필에 등록되지 않은 식품 원재료입니다.");

                takenMedicines = newTakenMedicines;
            }

            case SYMPTOM -> {
                List<OccuredSymptom> newOccuredSymptoms = new ArrayList<>();

                for (OccuredSymptom occuredSymptom : occuredSymptoms) {
                    if (!occuredSymptom.getId().equals(elementId)) newOccuredSymptoms.add(occuredSymptom);
                }

                if (occuredSymptoms.size() == newOccuredSymptoms.size())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필에 등록되지 않은 의약품 성분입니다.");

                occuredSymptoms = newOccuredSymptoms;
            }
        }

        diaryRepository.save(
                diary.toBuilder()
                        .ingestedFoods(ingestedFoods)
                        .takenMedicines(takenMedicines)
                        .occuredSymptoms(occuredSymptoms)
                        .build()
        );

        idResponse.setId(diaryId);
        return idResponse;
    }

    public DiaryResponse getDiaryList(Long profileId, LocalDate date) {
        Optional<Diary> optionalDiary = diaryRepository.findByProfilesIdAndDate(profileId, date);
        if (optionalDiary.isPresent()) {
            Diary diary = optionalDiary.get();
            Profile profile = diary.getProfiles().stream()
                    .filter(p -> p.getId().equals(profileId))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("다이어리에서 프로필을 찾을 수 없습니다."));

            DiaryResponse diaryResponse = new DiaryResponse();
            diaryResponse.setId(diary.getId());
            diaryResponse.setDate(diary.getDate());
            diaryResponse.setProfileId(profile.getId());
            diaryResponse.setIngestedFoods(diary.getIngestedFoods());
            diaryResponse.setTakenMedicines(diary.getTakenMedicines());
            diaryResponse.setOccuredSymptoms(diary.getOccuredSymptoms());
            return diaryResponse;
        } else {
            throw new NotFoundException("지정된 프로필 ID 및 날짜가 있는 다이어리를 찾을 수 없습니다.");
        }
    }

    public void deleteDiary(Long diaryId) {
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        Diary diary;

        if (diaryOptional.isPresent()) diary = diaryOptional.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 다이어리입니다.");

        diary.getProfiles().forEach(profile -> profile.getDiaries().remove(diary));
        diary.getProfiles().clear();

        diaryRepository.delete(diary);
    }
}


