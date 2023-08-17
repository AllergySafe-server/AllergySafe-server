package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.*;
import com.i_dont_love_null.allergy_safe.model.*;
import com.i_dont_love_null.allergy_safe.properties.AppProperties;
import com.i_dont_love_null.allergy_safe.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private final DiaryPeriodResponse diaryPeriodResponse;

    private final ProfileService profileService;

    private final AppProperties appProperties;

    private final ImageValidationService imageValidationService;

    public IdResponse createDiary(User user, Long profileId, DiaryRequest diaryRequest) {
        profileService.checkIfFamily(user, profileId);

        Profile profile;
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);

        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");

        LocalDate diaryDate = diaryRequest.getDate();

        boolean isLocal = appProperties.getAppDomain().contains("localhost");
        LocalDate today = LocalDate.now();

        if (!isLocal) {

            LocalDateTime todayDateTime = LocalDateTime.now().plusHours(9);
            today = today.withYear(todayDateTime.getYear()).withMonth(todayDateTime.getMonthValue()).withDayOfMonth(todayDateTime.getDayOfMonth());
        }

        if (diaryDate.isAfter(today)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "미래 날짜에 다이어리를 생성할 수 없습니다.");
        }

        if (diaryRepository.existsByProfileIdAndDate(profileId, diaryDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 해당 날짜에 다이어리가 존재합니다.");
        }

        Diary newDiary = Diary.builder()
                .date(diaryDate)
                .profile(profile)
                .build();

        profile.getDiaries().add(newDiary);

        Diary createdDiary = diaryRepository.save(newDiary);
        idResponse.setId(createdDiary.getId());

        return idResponse;
    }

    public void checkIfDiaryExists(Long diaryId) {
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        if (diaryOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 다이어리입니다.");
    }

    public void checkIfFamilyByDiaryId(User user, Long diaryId) {
        checkIfDiaryExists(diaryId);
        Diary diary = diaryRepository.findById(diaryId).get();
        Profile profile = diary.getProfile();
        profileService.checkIfFamily(user, profile.getId());

    }

    public IdResponse addDiaryElement(User user, Long diaryId, DiaryElementCreateRequest diaryElementCreateRequest) {
        checkIfFamilyByDiaryId(user, diaryId);

        Long elementId = diaryElementCreateRequest.getId();
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        Diary diary;

        if (diaryOptional.isPresent()) diary = diaryOptional.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 다이어리입니다.");

        List<IngestedFood> ingestedFoods = diary.getIngestedFoods();
        List<TakenMedicine> takenMedicines = diary.getTakenMedicines();
        List<OccuredSymptom> occuredSymptoms = diary.getOccuredSymptoms();

        LocalDate diaryDate = diary.getDate();

        LocalDateTime currentDateTime = LocalDateTime.now();
        boolean isLocal = appProperties.getAppDomain().contains("localhost");
        if (!isLocal) {
            currentDateTime = currentDateTime.plusHours(9);
        }
        LocalDateTime datetime = diaryElementCreateRequest.getDateTime();

        LocalDateTime minimumDateTime = diaryDate.atStartOfDay();
        LocalDateTime maximumDateTime = diaryDate.plusDays(1).atStartOfDay();

        if (datetime.isBefore(minimumDateTime) || !datetime.isBefore(maximumDateTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "선택한 일자의 정보만 등록할 수 있습니다.");
        }

        if (datetime.isAfter(currentDateTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "미래의 날짜로는 등록할 수 없습니다.");
        }

        switch (diaryElementCreateRequest.getDiaryElementType()) {
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

                String base64String = diaryElementCreateRequest.getBase64String();

                if (Objects.nonNull(base64String) && !imageValidationService.validateImage(base64String)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PNG 또는 JPEG 파일만 업로드 가능합니다.");
                }

                OccuredSymptom occuredSymptom = OccuredSymptom.builder()
                        .datetime(datetime)
                        .diary(diary)
                        .symptom(symptom)
                        .imageUrl(diaryElementCreateRequest.getBase64String())
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

    @Transactional
    public IdResponse deleteDiaryElement(User user, Long diaryId, DiaryElementDeleteRequest diaryElementDeleteRequest) {
        checkIfFamilyByDiaryId(user, diaryId);

        Long elementId = diaryElementDeleteRequest.getId();
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        Diary diary;

        if (diaryOptional.isPresent()) diary = diaryOptional.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 다이어리입니다.");

        List<IngestedFood> ingestedFoods = diary.getIngestedFoods();
        List<TakenMedicine> takenMedicines = diary.getTakenMedicines();
        List<OccuredSymptom> occuredSymptoms = diary.getOccuredSymptoms();

        switch (diaryElementDeleteRequest.getDiaryElementType()) {
            case FOOD -> {
                List<IngestedFood> newIngestedFoods = new ArrayList<>();

                for (IngestedFood ingestedFood : ingestedFoods) {
                    if (!ingestedFood.getFood().getId().equals(elementId)) newIngestedFoods.add(ingestedFood);
                }

                if (ingestedFoods.size() == newIngestedFoods.size())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "다이어리에 등록되지 않은 음식입니다.");

                ingestedFoodRepository.deleteByFoodId(elementId);
                ingestedFoods = newIngestedFoods;
            }

            case MEDICINE -> {
                List<TakenMedicine> newTakenMedicines = new ArrayList<>();

                for (TakenMedicine takenMedicine : takenMedicines) {
                    if (!takenMedicine.getMedicine().getId().equals(elementId)) newTakenMedicines.add(takenMedicine);
                }

                if (takenMedicines.size() == newTakenMedicines.size())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "다이어리에 등록되지 않은 약품입니다.");

                takenMedicineRepository.deleteByMedicineId(elementId);
                takenMedicines = newTakenMedicines;
            }

            case SYMPTOM -> {
                List<OccuredSymptom> newOccuredSymptoms = new ArrayList<>();

                for (OccuredSymptom occuredSymptom : occuredSymptoms) {
                    if (!occuredSymptom.getSymptom().getId().equals(elementId)) newOccuredSymptoms.add(occuredSymptom);
                }

                if (occuredSymptoms.size() == newOccuredSymptoms.size())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "다이어리에 등록되지 않은 증상입니다.");

                occuredSymptomRepository.deleteBySymptomId(elementId);
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

    public DiaryResponse getDiaryList(User user, Long profileId, LocalDate date) {
        profileService.checkIfFamily(user, profileId);

        Optional<Diary> optionalDiary = diaryRepository.findByProfileIdAndDate(profileId, date);

        if (optionalDiary.isPresent()) {
            Diary diary = optionalDiary.get();
            Profile profile = diary.getProfile();

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

    public IdResponse deleteDiary(User user, Long diaryId) {
        checkIfFamilyByDiaryId(user, diaryId);
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        Diary diary;

        if (diaryOptional.isPresent()) diary = diaryOptional.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 다이어리입니다.");

        Profile profile = diary.getProfile();
        profile.getDiaries().remove(diary);

        diaryRepository.delete(diary);
        idResponse.setId(diaryId);

        return idResponse;
    }

    public DiaryPeriodResponse getDiaryPeriod(User user, Long profileId, LocalDate startDate, LocalDate endDate) {
        profileService.checkIfFamily(user, profileId);

        Optional<Profile> profileOptional = profileRepository.findById(profileId);
        Profile profile;

        if (profileOptional.isPresent()) profile = profileOptional.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");

        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "시작 날짜가 종료 날짜보다 이후일 수 없습니다.");
        }

        diaryPeriodResponse.setDiaryList(diaryRepository.findAllByProfileIdAndDateBetweenOrderByDateDesc(profileId,
                startDate, endDate));

        return diaryPeriodResponse;
    }
}


