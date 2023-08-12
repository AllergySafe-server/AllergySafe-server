package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.GuessFoodResponse;
import com.i_dont_love_null.allergy_safe.model.Food;
import com.i_dont_love_null.allergy_safe.model.Profile;
import com.i_dont_love_null.allergy_safe.repository.*;
import com.i_dont_love_null.allergy_safe.utils.EntityListToStringList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Ranges;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GuessService {
    private final GuessFoodResponse guessFoodResponse;
    public GuessFoodResponse guessResponse() {
        return guessFoodResponse;
    }
    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;
    private final IngestedFoodRepository ingestedFoodRepository;
    private final FoodRepository foodRepository;
    private final EntityListToStringList entityListToStringList;
    private final OccuredSymptomRepository occuredSymptomRepository;

    public GuessFoodResponse guessing(Long profileId, LocalDate startDate, LocalDate endDate) {
        Profile profile;
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        
        //추가 요망 : 다이어리에 겪은 증상이 없는 경우의 예외
        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");

        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작 날짜가 끝 날짜보다 앞서야합니다.");
        }
        if (!diaryRepository.existsDiariesByProfileId(profileId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 프로필에 등록된 다이어리가 없습니다.");
        }

        //프로필 아이디, 날짜 범위와 대응되는 다이어리id를 모두 담는 배열 설정
        List<Long> diaryIdList1 = new ArrayList<>();
        for(LocalDate tempDate = startDate; tempDate.isBefore(endDate)||tempDate.isEqual(endDate); tempDate = tempDate.plusDays(1)){
            diaryIdList1.add(diaryRepository.findDiaryByProfileIdAndDate(profileId, tempDate).getId());
        }
        //다이어리id와 대응되는 먹은 음식의 foodId를 모두 담는 배열 설정
        List<Long> foodIdList1 = new ArrayList<>();
        for (Long diaryId: diaryIdList1) {
            foodIdList1.add(ingestedFoodRepository.findIngestedFoodByDiaryId(diaryId).getFood().getId());
        }
        //foodId와 대응되는 material List의 각 name을 모두 담는 배열 설정
        List<String> materialNameList1 = new ArrayList<>();
        for (Long foodId: foodIdList1) {
            Optional<Food> optionalFood = foodRepository.findById(foodId);
            if (optionalFood.isPresent()) materialNameList1.addAll(entityListToStringList.convertWithName(optionalFood.get().getMaterials()));
        }

        //프로필 아이디, 날짜 범위와 대응되며, 겪은 증상이 있는 다이어리 id를 모두 담은 배열 설정
        List<Long> diaryIdList2 = new ArrayList<>();
        for(Long tempId : diaryIdList1){
            if (occuredSymptomRepository.existsByDiaryId(tempId)){
                diaryIdList2.add(tempId);
            }
        }
        //다이어리id와 대응되는 먹은 음식의 foodId를 모두 담는 배열 설정
        List<Long> foodIdList2 = new ArrayList<>();
        for (Long diaryId: diaryIdList2) {
            foodIdList2.add(ingestedFoodRepository.findIngestedFoodByDiaryId(diaryId).getFood().getId());
        }
        //foodId와 대응되는 material List의 각 name을 모두 담는 배열 설정
        List<String> materialNameList2 = new ArrayList<>();
        for (Long foodId: foodIdList2) {
            Optional<Food> optionalFood = foodRepository.findById(foodId);
            if (optionalFood.isPresent()) materialNameList2.addAll(entityListToStringList.convertWithName(optionalFood.get().getMaterials()));
        }

        List<String> keys = new ArrayList<>(new HashSet<>(materialNameList1));
        Map<String, Integer> totalCountMap = new HashMap<String, Integer>();
        Map<String, Integer> occurCountMap = new HashMap<String, Integer>();
        Map<String, Integer> percentCountMap = new HashMap<String, Integer>();

        for (String key: keys) {
            for (String materialName1: materialNameList1) {
                if (materialName1.equals(key)) {
                    if (!totalCountMap.containsKey(key)) totalCountMap.put(key, 0);
                    totalCountMap.put(key, totalCountMap.get(key) + 1);
                }
            }
            for (String materialName2: materialNameList2) {
                if (materialName2.equals(key)) {
                    if (!occurCountMap.containsKey(key)) occurCountMap.put(key, 0);
                    occurCountMap.put(key, occurCountMap.get(key) + 1);
                }
            }
        }

        for (String key2: keys) {
            if (totalCountMap.get(key2) == 1) totalCountMap.remove(key2);
            if (occurCountMap.containsKey(key2) && occurCountMap.get(key2) == 1) occurCountMap.remove(key2);
        }

        for (String key3: occurCountMap.keySet()) {
            if (!percentCountMap.containsKey(key3)) percentCountMap.put(key3, 0);
            percentCountMap.put(key3, (int) Math.round(100.0 * ((double) occurCountMap.get(key3) / totalCountMap.get(key3))));
        }



//        //materialNameList1에 담긴 String 중 한 번만 먹은 원재료를 제거
//        List<Map.Entry<String, Long>> twoOrMoreMaterialList1 = materialNameList2.stream()
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
//                .entrySet()
//                .stream()
//                .filter(element -> element.getValue() > 1)
//                .toList();
//
//        //materialNameList2에 담긴 String 중 한 번만 먹은 원재료를 제거
//        List<Map.Entry<String, Long>> twoOrMoreMaterialList2 = materialNameList2.stream()
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
//                .entrySet()
//                .stream()
//                .filter(element -> element.getValue() > 1)
//                .toList();
//
//        //( twoOrMoreMaterialList2 / twoOrMoreMaterialList1 )인 확률을 백분율로 계산
//        List<Map.Entry<String, Long>> percentOfAllergy = new ArrayList<>();
//        for (Map.Entry<String, Long> twoOrMoreMaterials1 : twoOrMoreMaterialList2) {
//            if (twoOrMoreMaterialList2.)
//        }

//        for (Map.Entry<String, Long> twoOrMoreMaterials2 : twoOrMoreMaterialList2) {
//            for (Map.Entry<String, Long> twoOrMoreMaterials1 : twoOrMoreMaterialList1){
//                if(twoOrMoreMaterials2.getKey().equals(twoOrMoreMaterials1.getKey())){
//                    Map.Entry<String, Long> nameAndPercent = new
//                    percentOfAllergy
//                    percentOfAllergy.add(Entry<twoOrMoreMaterials2.getKey(),(twoOrMoreMaterials2.getValue()/twoOrMoreMaterials1.getValue())*100>);
//                }
//            }
//        }
//        percentOfAllergy.add();



        return guessFoodResponse;
    }


}

