package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.*;
import com.i_dont_love_null.allergy_safe.model.*;
import com.i_dont_love_null.allergy_safe.repository.*;
import com.i_dont_love_null.allergy_safe.utils.EntityListToStringList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class GuessService {
    private final GuessResponse guessResponse;
    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;
    private final IngestedFoodRepository ingestedFoodRepository;
    private final FoodRepository foodRepository;
    private final EntityListToStringList entityListToStringList;
    private final OccuredSymptomRepository occuredSymptomRepository;
    private final AllergyRepository allergyRepository;
    private final MaterialRepository materialRepository;
    private final TakenMedicineRepository takenMedicineRepository;
    private final MedicineRepository medicineRepository;
    private final IngredientRepository ingredientRepository;

    public GuessResponse guessResponse() {
        return guessResponse;
    }

    public GuessResponse guessFood(Long profileId, LocalDate startDate, LocalDate endDate) {
        Profile profile;
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);

        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");

        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작 날짜가 끝 날짜보다 앞서야합니다.");
        }
        if (!diaryRepository.existsByProfileId(profileId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 프로필에 등록된 다이어리가 없습니다.");
        }

        //프로필 아이디, 날짜 범위와 대응되는 다이어리id를 모두 담는 배열 설정
        List<Long> diaryIdList1 = new ArrayList<>();
        for (LocalDate tempDate = startDate; tempDate.isBefore(endDate) || tempDate.isEqual(endDate); tempDate = tempDate.plusDays(1)) {
            Diary foundDiary = diaryRepository.findDiaryByProfileIdAndDate(profileId, tempDate);
            if (Objects.nonNull(foundDiary)) diaryIdList1.add(foundDiary.getId());
        }
        //다이어리id와 대응되는 먹은 음식의 foodId를 모두 담는 배열 설정
        List<Long> foodIdList1 = new ArrayList<>();
        for (Long diaryId : diaryIdList1) {
            for (IngestedFood ingestedFood : ingestedFoodRepository.findAllByDiaryId(diaryId)) {
                foodIdList1.add(ingestedFood.getFood().getId());
            }
        }
        //foodId와 대응되는 material List와 allergy List의 각 name을 모두 담는 배열 설정
        List<String> materialNameList1 = new ArrayList<>();
        List<String> allergyNameList1 = new ArrayList<>();
        for (Long foodId : foodIdList1) {
            Optional<Food> optionalFood = foodRepository.findById(foodId);
            if (optionalFood.isPresent()) {
                materialNameList1.addAll(entityListToStringList.convertWithName(optionalFood.get().getMaterials()));
                allergyNameList1.addAll(entityListToStringList.convertWithName(optionalFood.get().getAllergies()));
            }
        }


        //프로필 아이디, 날짜 범위와 대응되며, 겪은 증상이 있는 다이어리 id를 모두 담은 배열 설정
        List<Long> diaryIdList2 = new ArrayList<>();
        for (Long tempId : diaryIdList1) {
            if (occuredSymptomRepository.existsByDiaryId(tempId)) {
                diaryIdList2.add(tempId);
            }
        }
        //다이어리id와 대응되는 먹은 음식의 foodId를 모두 담는 배열 설정
        List<Long> foodIdList2 = new ArrayList<>();
        for (Long diaryId : diaryIdList2) {
            for (IngestedFood ingestedFood : ingestedFoodRepository.findAllByDiaryId(diaryId)) {
                foodIdList2.add(ingestedFood.getFood().getId());
            }
        }
        //foodId와 대응되는 material List와 allergy List의 각 name을 모두 담는 배열 설정
        List<String> materialNameList2 = new ArrayList<>();
        List<String> allergyNameList2 = new ArrayList<>();
        for (Long foodId : foodIdList2) {
            Optional<Food> optionalFood = foodRepository.findById(foodId);
            if (optionalFood.isPresent()) {
                materialNameList2.addAll(entityListToStringList.convertWithName(optionalFood.get().getMaterials()));
                allergyNameList2.addAll(entityListToStringList.convertWithName(optionalFood.get().getMaterials()));
            }
        }

        //97 ~ 125행 : material 대상으로 확률 계산
        List<String> keysMaterial = new ArrayList<>(new HashSet<>(materialNameList1));
        Map<String, Integer> totalCountMapMaterial = new HashMap<String, Integer>();
        Map<String, Integer> occurCountMapMaterial = new HashMap<String, Integer>();
        Map<String, Integer> percentCountMapMaterial = new HashMap<String, Integer>();

        for (String key : keysMaterial) {
            for (String materialName1 : materialNameList1) {
                if (materialName1.equals(key)) {
                    if (!totalCountMapMaterial.containsKey(key)) totalCountMapMaterial.put(key, 0);
                    totalCountMapMaterial.put(key, totalCountMapMaterial.get(key) + 1);
                }
            }
            for (String materialName2 : materialNameList2) {
                if (materialName2.equals(key)) {
                    if (!occurCountMapMaterial.containsKey(key)) occurCountMapMaterial.put(key, 0);
                    occurCountMapMaterial.put(key, occurCountMapMaterial.get(key) + 1);
                }
            }
        }

        for (String key2 : keysMaterial) {
            if (totalCountMapMaterial.get(key2) == 1) totalCountMapMaterial.remove(key2);
            if (occurCountMapMaterial.containsKey(key2) && occurCountMapMaterial.get(key2) == 1)
                occurCountMapMaterial.remove(key2);
        }

        for (String key3 : occurCountMapMaterial.keySet()) {
            if (!percentCountMapMaterial.containsKey(key3)) percentCountMapMaterial.put(key3, 0);
            percentCountMapMaterial.put(key3, (int) Math.round(100.0 * ((double) occurCountMapMaterial.get(key3) / totalCountMapMaterial.get(key3))));
        }

        //127 ~ 156행 : allergy 대상으로 확률 계산
        List<String> keysAllergy = new ArrayList<>(new HashSet<>(allergyNameList1));
        Map<String, Integer> totalCountMapAllergy = new HashMap<String, Integer>();
        Map<String, Integer> occurCountMapAllergy = new HashMap<String, Integer>();
        Map<String, Integer> percentCountMapAllergy = new HashMap<String, Integer>();

        for (String key : keysAllergy) {
            for (String allergyName1 : allergyNameList1) {
                if (allergyName1.equals(key)) {
                    if (!totalCountMapAllergy.containsKey(key)) totalCountMapAllergy.put(key, 0);
                    totalCountMapAllergy.put(key, totalCountMapAllergy.get(key) + 1);
                }
            }
            for (String allergyName2 : allergyNameList2) {
                if (allergyName2.equals(key)) {
                    if (!occurCountMapAllergy.containsKey(key)) occurCountMapAllergy.put(key, 0);
                    occurCountMapAllergy.put(key, occurCountMapAllergy.get(key) + 1);
                }
            }
        }

        for (String key2 : keysAllergy) {
            if (totalCountMapAllergy.get(key2) == 1) totalCountMapAllergy.remove(key2);
            if (occurCountMapAllergy.containsKey(key2) && occurCountMapAllergy.get(key2) == 1)
                occurCountMapAllergy.remove(key2);
        }

        for (String key3 : occurCountMapAllergy.keySet()) {
            if (!percentCountMapAllergy.containsKey(key3)) percentCountMapAllergy.put(key3, 0);
            percentCountMapAllergy.put(key3, (int) Math.round(100.0 * ((double) occurCountMapAllergy.get(key3) / totalCountMapAllergy.get(key3))));
        }

        // 각 percentMap을 병합
        Map<String, Integer> percentCountMap = new HashMap<>();
        percentCountMap.putAll(percentCountMapMaterial);
        percentCountMap.putAll(percentCountMapAllergy);

        // percentCountMap의 키 목록을 value 값 크기 순으로 내림차순 정렬
        List<String> percentCountMapKeys = new ArrayList<>(percentCountMap.keySet());
        Collections.sort(percentCountMapKeys, (v1, v2) -> (percentCountMap.get(v2).compareTo(percentCountMap.get(v1))));

        List<GuessedData> guessedDataList = new ArrayList<>();
        // guessedData 구성
        for (int i = 0; i < percentCountMapKeys.size(); i++) {
            if (guessedDataList.size() == 4) break;
            String name = percentCountMapKeys.get(i);
            int percent = percentCountMap.get(name);
            Allergy allergy = allergyRepository.findByName(name);
            Material material = null;
            String type = "allergy";
            String imageUrl = null;
            int totalCount = 0;
            int occuredCount = 0;
            Long elementId;
            List<Long> foodIdList = new ArrayList<>();

            if (Objects.isNull(allergy)) {
                material = materialRepository.findByName(name);
                type = "material";
                if (Objects.nonNull(material)) imageUrl = material.getImageUrl();
            } else {
                imageUrl = allergy.getImageUrl();
            }
            if (type.equals("allergy")) {
                totalCount = totalCountMapAllergy.get(name);
                occuredCount = occurCountMapAllergy.get(name);
                elementId = allergy.getId();

                for (Long diaryId : diaryIdList1) {
                    Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
                    if (diaryOptional.isPresent()) {
                        List<IngestedFood> ingestedFoods = diaryOptional.get().getIngestedFoods();
                        for (IngestedFood ingestedFood : ingestedFoods) {
                            List<Allergy> allergies = ingestedFood.getFood().getAllergies();
                            for (Allergy allergy1 : allergies) {
                                if (allergy1.getId().equals(elementId)) foodIdList.add(ingestedFood.getFood().getId());
                            }
                        }
                    }
                }

            } else {
                totalCount = totalCountMapMaterial.get(name);
                occuredCount = occurCountMapMaterial.get(name);
                elementId = material.getId();

                for (Long diaryId : diaryIdList1) {
                    Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
                    if (diaryOptional.isPresent()) {
                        List<IngestedFood> ingestedFoods = diaryOptional.get().getIngestedFoods();
                        for (IngestedFood ingestedFood : ingestedFoods) {
                            List<Material> materials = ingestedFood.getFood().getMaterials();
                            for (Material material1 : materials) {
                                if (material1.getId().equals(elementId)) foodIdList.add(ingestedFood.getFood().getId());
                            }
                        }
                    }
                }
            }
            List<Card> cards = new ArrayList<>();
            List<Diary> diaries = diaryRepository.findAllByProfileIdAndDateBetweenOrderByDateDesc(profileId, startDate, endDate);
            for (Diary diary : diaries) {
                if (cards.size() == 3) break;
                List<IngestedFood> ingestedFoods = diary.getIngestedFoods();
                for (IngestedFood ingestedFood : ingestedFoods) {
                    if (foodIdList.contains(ingestedFood.getFood().getId())) {
                        Card card = new Card();
                        card.setType(CardElementType.FOOD);
                        card.setElementId(ingestedFood.getFood().getId());
                        Optional<Food> optionalFood = foodRepository.findById(ingestedFood.getFood().getId());
                        if (optionalFood.isPresent()) card.setName(optionalFood.get().getName());
                        card.setDateTime(ingestedFood.getDatetime());
                        List<Symptom> symptoms = new ArrayList<>();
                        Diary tomorrowDiary = diaryRepository.findDiaryByProfileIdAndDate(profileId, diary.getDate().plusDays(1));
                        if (Objects.nonNull(tomorrowDiary)) {
                            for (OccuredSymptom occuredSymptom : tomorrowDiary.getOccuredSymptoms()) {
                                symptoms.add(occuredSymptom.getSymptom());
                            }
                        }
                        for (OccuredSymptom occuredSymptom : diary.getOccuredSymptoms()) {
                            symptoms.add(occuredSymptom.getSymptom());
                        }
                        card.setSymptoms(symptoms);
                        cards.add(card);
                    }
                }
            }
            GuessedData guessedData = new GuessedData();
            guessedData.setType(GuessedType.from(type));
            guessedData.setElementId(elementId);
            guessedData.setImageUrl(imageUrl);
            guessedData.setName(name);
            guessedData.setTotalCount(totalCount);
            guessedData.setTotalSymptomOccuredCount(occuredCount);
            guessedData.setPercentage(percent);
            guessedData.setCards(cards);
            guessedDataList.add(guessedData);
        }
        guessResponse.setProfileId(profileId);
        guessResponse.setName(profile.getName());
        guessResponse.setStartDate(startDate);
        guessResponse.setEndDate(endDate);
        guessResponse.setGuessedData(guessedDataList);
        return guessResponse;
    }

    public GuessResponse guessMedicine(Long profileId, LocalDate startDate, LocalDate endDate) {
        Profile profile;
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);

        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");

        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작 날짜가 끝 날짜보다 앞서야합니다.");
        }
        if (!diaryRepository.existsByProfileId(profileId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 프로필에 등록된 다이어리가 없습니다.");
        }

        //프로필 아이디, 날짜 범위와 대응되는 다이어리id를 모두 담는 배열 설정
        List<Long> diaryIdList1 = new ArrayList<>();
        for (LocalDate tempDate = startDate; tempDate.isBefore(endDate) || tempDate.isEqual(endDate); tempDate = tempDate.plusDays(1)) {
            Diary foundDiary = diaryRepository.findDiaryByProfileIdAndDate(profileId, tempDate);
            if (Objects.nonNull(foundDiary)) diaryIdList1.add(foundDiary.getId());
        }
        //다이어리id와 대응되는 복용한 약의 medicineId를 모두 담는 배열 설정
        List<Long> medicineIdList1 = new ArrayList<>();
        for (Long diaryId : diaryIdList1) {
            for (TakenMedicine takenMedicine : takenMedicineRepository.findAllByDiaryId(diaryId)) {
                medicineIdList1.add(takenMedicine.getMedicine().getId());
            }
        }
        //medicineId와 대응되는 ingredient List의 name을 모두 담는 배열 설정
        List<String> ingredientNameList1 = new ArrayList<>();
        for (Long medicineId : medicineIdList1) {
            Optional<Medicine> optionalMedicine = medicineRepository.findById(medicineId);
            if (optionalMedicine.isPresent()) {
                ingredientNameList1.addAll(entityListToStringList.convertWithName(optionalMedicine.get().getIngredients()));
            }
        }


        //프로필 아이디, 날짜 범위와 대응되며, 겪은 증상이 있는 다이어리 id를 모두 담은 배열 설정
        List<Long> diaryIdList2 = new ArrayList<>();
        for (Long tempId : diaryIdList1) {
            if (occuredSymptomRepository.existsByDiaryId(tempId)) {
                diaryIdList2.add(tempId);
            }
        }
        //다이어리id와 대응되는 복용한 약의 medicineId를 모두 담는 배열 설정
        List<Long> medicineIdList2 = new ArrayList<>();
        for (Long diaryId : diaryIdList2) {
            for (TakenMedicine takenMedicine : takenMedicineRepository.findAllByDiaryId(diaryId)) {
                medicineIdList2.add(takenMedicine.getMedicine().getId());
            }
        }
        //medicineId와 대응되는 ingredient List의 name을 모두 담는 배열 설정
        List<String> ingredientNameList2 = new ArrayList<>();
        for (Long medicineId : medicineIdList2) {
            Optional<Medicine> optionalMedicine = medicineRepository.findById(medicineId);
            if (optionalMedicine.isPresent()) {
                ingredientNameList2.addAll(entityListToStringList.convertWithName(optionalMedicine.get().getIngredients()));
            }
        }

        // medicine 대상으로 확률 계산
        List<String> keysIngredient = new ArrayList<>(new HashSet<>(ingredientNameList1));
        Map<String, Integer> totalCountMapIngredient = new HashMap<String, Integer>();
        Map<String, Integer> occurCountMapIngredient = new HashMap<String, Integer>();
        Map<String, Integer> percentCountMap = new HashMap<String, Integer>();

        for (String key : keysIngredient) {
            for (String ingredientName1 : ingredientNameList1) {
                if (ingredientName1.equals(key)) {
                    if (!totalCountMapIngredient.containsKey(key)) totalCountMapIngredient.put(key, 0);
                    totalCountMapIngredient.put(key, totalCountMapIngredient.get(key) + 1);
                }
            }
            for (String ingredientName2 : ingredientNameList2) {
                if (ingredientName2.equals(key)) {
                    if (!occurCountMapIngredient.containsKey(key)) occurCountMapIngredient.put(key, 0);
                    occurCountMapIngredient.put(key, occurCountMapIngredient.get(key) + 1);
                }
            }
        }

        for (String key2 : keysIngredient) {
            if (totalCountMapIngredient.get(key2) == 1) totalCountMapIngredient.remove(key2);
            if (occurCountMapIngredient.containsKey(key2) && occurCountMapIngredient.get(key2) == 1)
                occurCountMapIngredient.remove(key2);
        }

        for (String key3 : occurCountMapIngredient.keySet()) {
            if (!percentCountMap.containsKey(key3)) percentCountMap.put(key3, 0);
            percentCountMap.put(key3, (int) Math.round(100.0 * ((double) occurCountMapIngredient.get(key3) / totalCountMapIngredient.get(key3))));
        }

        // percentCountMapIngredient의 키 목록을 value 값 크기 순으로 내림차순 정렬
        List<String> percentCountMapKeys = new ArrayList<>(percentCountMap.keySet());
        Collections.sort(percentCountMapKeys, (v1, v2) -> (percentCountMap.get(v2).compareTo(percentCountMap.get(v1))));

        // guessedData 구성
        List<GuessedData> guessedDataList = new ArrayList<>();
        for (int i = 0; i < percentCountMapKeys.size(); i++) {
            if (guessedDataList.size() == 4) break;
            String name = percentCountMapKeys.get(i);
            int percent = percentCountMap.get(name);
            Ingredient ingredient = ingredientRepository.findByName(name);
            String type = "ingredient";
            int totalCount = 0;
            int occuredCount = 0;
            Long elementId = null;
            String imageUrl = null;
            List<Long> medicineIdList = new ArrayList<>();

            totalCount = totalCountMapIngredient.get(name);
            occuredCount = occurCountMapIngredient.get(name);
            if (Objects.nonNull(ingredient)) {
                elementId = ingredient.getId();
                imageUrl = ingredient.getImageUrl();
            }


            for (Long diaryId : diaryIdList1) {
                Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
                if (diaryOptional.isPresent()) {
                    List<TakenMedicine> takenMedicines = diaryOptional.get().getTakenMedicines();
                    for (TakenMedicine takenMedicine : takenMedicines) {
                        List<Ingredient> ingredients = takenMedicine.getMedicine().getIngredients();
                        for (Ingredient ingredient1 : ingredients) {
                            if (ingredient1.getId().equals(elementId))
                                medicineIdList.add(takenMedicine.getMedicine().getId());
                        }
                    }
                }
            }
            List<Card> cards = new ArrayList<>();
            List<Diary> diaries = diaryRepository.findAllByProfileIdAndDateBetweenOrderByDateDesc(profileId, startDate, endDate);
            for (Diary diary : diaries) {
                if (cards.size() == 3) break;
                List<TakenMedicine> takenMedicines = diary.getTakenMedicines();
                for (TakenMedicine takenMedicine : takenMedicines) {
                    if (medicineIdList.contains(takenMedicine.getMedicine().getId())) {
                        Card card = new Card();
                        card.setType(CardElementType.MEDICINE);
                        card.setElementId(takenMedicine.getMedicine().getId());
                        Optional<Medicine> optionalMedicine = medicineRepository.findById(takenMedicine.getMedicine().getId());
                        if (optionalMedicine.isPresent()) card.setName(optionalMedicine.get().getName());
                        card.setDateTime(takenMedicine.getDatetime());
                        List<Symptom> symptoms = new ArrayList<>();
                        Diary tomorrowDiary = diaryRepository.findDiaryByProfileIdAndDate(profileId, diary.getDate().plusDays(1));
                        if (Objects.nonNull(tomorrowDiary)) {
                            for (OccuredSymptom occuredSymptom : tomorrowDiary.getOccuredSymptoms()) {
                                symptoms.add(occuredSymptom.getSymptom());
                            }
                        }
                        for (OccuredSymptom occuredSymptom : diary.getOccuredSymptoms()) {
                            symptoms.add(occuredSymptom.getSymptom());
                        }
                        card.setSymptoms(symptoms);
                        cards.add(card);
                    }
                }
            }
            GuessedData guessedData = new GuessedData();
            guessedData.setType(GuessedType.from(type));
            guessedData.setElementId(elementId);
            guessedData.setImageUrl(imageUrl);
            guessedData.setName(name);
            guessedData.setTotalCount(totalCount);
            guessedData.setTotalSymptomOccuredCount(occuredCount);
            guessedData.setPercentage(percent);
            guessedData.setCards(cards);
            guessedDataList.add(guessedData);
        }
        guessResponse.setProfileId(profileId);
        guessResponse.setName(profile.getName());
        guessResponse.setStartDate(startDate);
        guessResponse.setEndDate(endDate);
        guessResponse.setGuessedData(guessedDataList);
        return guessResponse;
    }

}

