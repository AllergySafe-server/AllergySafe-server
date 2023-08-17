package com.i_dont_love_null.allergy_safe.service;


import com.i_dont_love_null.allergy_safe.dto.FoodFromApiResponse;
import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.dto.MedicineFromApiResponse;
import com.i_dont_love_null.allergy_safe.model.*;
import com.i_dont_love_null.allergy_safe.properties.AppProperties;
import com.i_dont_love_null.allergy_safe.repository.*;
import com.i_dont_love_null.allergy_safe.utils.SimpleHttp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class PublicAPIService {

    private final AppProperties appProperties;
    private final FoodRepository foodRepository;
    private final AllergyRepository allergyRepository;
    private final MaterialRepository materialRepository;
    private final MedicineRepository medicineRepository;
    private final IngredientRepository ingredientRepository;

    private final IdResponse idResponse;
    private final MedicineFromApiResponse medicineFromApiResponse;

    public IdResponse getFoodIdFromApiByBarcodeNo(String barcode) {
        Food food = foodRepository.findByBarcode(barcode);
        if (!Objects.isNull(food)) {
            idResponse.setId(food.getId());
            return idResponse;
        }

        FoodFromApiResponse foodFromApiResponse = getFoodFromApiByProductListReportNo(getProductListReportNoByBarcodeNO(barcode));

        List<Allergy> foundAllergies = new ArrayList<>();
        for (String allergyName : foodFromApiResponse.getAllergies()) {
            Allergy allergy = Optional.ofNullable(allergyRepository.findByName(allergyName)).orElseGet(() -> {
                Allergy newAllergy = new Allergy(null, allergyName, null);
                return allergyRepository.save(newAllergy);
            });
            foundAllergies.add(allergy);
        }

        List<Material> foundMaterials = new ArrayList<>();
        for (String materialName : foodFromApiResponse.getMaterials()) {
            Material material = Optional.ofNullable(materialRepository.findByName(materialName)).orElseGet(() -> {
                Material newMaterial = new Material(null, materialName, null);
                return materialRepository.save(newMaterial);
            });
            foundMaterials.add(material);
        }

        Food newFood = Food.builder()
                .name(foodFromApiResponse.getName())
                .barcode(barcode)
                .allergies(foundAllergies)
                .materials(foundMaterials)
                .build();
        newFood = foodRepository.save(newFood);

        idResponse.setId(newFood.getId());

        return idResponse;
    }

    public IdResponse getMedicineIdFromApiByBarcodeNo(String barcode) {
        Medicine medicine = medicineRepository.findByBarcode(barcode);
        if (!Objects.isNull(medicine)) {
            idResponse.setId(medicine.getId());
            return idResponse;
        }

        MedicineFromApiResponse medicineFromApiResponse = getMedicineFromApiByBarcodeNo(barcode);

        List<Ingredient> foundIngredients = new ArrayList<>();
        for (String ingredientName : medicineFromApiResponse.getIngredients()) {
            Ingredient ingredient = Optional.ofNullable(ingredientRepository.findByName(ingredientName)).orElseGet(() -> {
                Ingredient newIngredient = new Ingredient(null, ingredientName, null);
                return ingredientRepository.save(newIngredient);
            });
            foundIngredients.add(ingredient);
        }

        Medicine newMedicine = Medicine.builder()
                .name(medicineFromApiResponse.getName())
                .barcode(barcode)
                .ingredients(foundIngredients)
                .build();
        newMedicine = medicineRepository.save(newMedicine);

        idResponse.setId(newMedicine.getId());

        return idResponse;
    }

    public String getProductListReportNoByBarcodeNO(String barcode) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.foodsafetykorea.go.kr")
                .path("/api/" + appProperties.getFoodSafetyApiKey() + "/C005/json/1/1/BAR_CD=" + barcode)
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        if (result.getStatusCode() != HttpStatus.OK)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "바코드 정보를 불러올 수 없습니다. 수동으로 항목을 추가해 주세요.");
        String json = result.getBody();

        JSONObject jsonData;
        JSONObject c005Data;
        JSONArray rowsData;

        try {
            jsonData = new JSONObject(json);
            c005Data = jsonData.getJSONObject("C005");
            rowsData = c005Data.getJSONArray("row");
            if (Objects.isNull(rowsData))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "바코드 정보를 불러올 수 없습니다. 수동으로 항목을 추가해 주세요.");

            for (int i = 0; i < rowsData.length(); i++) {
                JSONObject rowData = rowsData.getJSONObject(i);
                if (rowData.has("PRDLST_REPORT_NO")) {
                    if (rowData.get("PRDLST_REPORT_NO") instanceof String)
                        return (String) rowData.get("PRDLST_REPORT_NO");
                }
            }
        } catch (JSONException jsonException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "바코드 정보를 불러올 수 없습니다. 수동으로 항목을 추가해 주세요. (JSONException)");
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "바코드 정보를 불러올 수 없습니다. 수동으로 항목을 추가해 주세요.");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "바코드 정보를 불러올 수 없습니다. 수동으로 항목을 추가해 주세요.");
    }

    public FoodFromApiResponse getFoodFromApiByProductListReportNo(String productListReportNo) {
        String uri = "https://apis.data.go.kr/B553748/CertImgListServiceV2/getCertImgListServiceV2" +
                "?" +
                "serviceKey=" +
                appProperties.getKoreaPublicApiKey() +
                "&returnType=" +
                "json" +
                "&prdlstReportNo=" +
                productListReportNo;

        String json;
        try {
            json = SimpleHttp.get(uri);
        } catch (IOException ignored) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "공공 데이터 API 서버가 응답하지 않습니다. 잠시 후 다시 시도해 주세요. (IOException)");
        } catch (Exception ignored) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "공공 데이터 API 서버가 응답하지 않습니다. 잠시 후 다시 시도해 주세요.");
        }

        JSONObject jsonObject;
        JSONObject bodyData;
        JSONArray itemsData;
        JSONObject itemData;
        String name = "";
        String materialsString = "";
        String allergiesString = "";
        List<String> materials;
        List<String> allergies;

        try {
            jsonObject = new JSONObject(json);
            bodyData = jsonObject.getJSONObject("body");
            itemsData = bodyData.getJSONArray("items");
            itemData = itemsData.getJSONObject(0).getJSONObject("item");

            if (itemData.has("prdlstNm")) {
                if (itemData.get("prdlstNm") instanceof String) name = (String) itemData.get("prdlstNm");
            }
            if (itemData.has("rawmtrl")) {
                if (itemData.get("rawmtrl") instanceof String) materialsString = (String) itemData.get("rawmtrl");
            }
            if (itemData.has("allergy")) {
                if (itemData.get("allergy") instanceof String) allergiesString = (String) itemData.get("allergy");
            }
        } catch (JSONException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 불러올 수 없습니다. 수동으로 항목을 추가해 주세요. (JSON Parse 오류)");
        } catch (Exception ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 불러올 수 없습니다. 수동으로 항목을 추가해 주세요.");
        }
        FoodFromApiResponse foodFromApiResponse = new FoodFromApiResponse();
        materialsString = materialsString.replaceAll("\\([^)]*\\)", "");
        materialsString = materialsString.replaceAll("\\d+(\\.\\d+)?%", "");
        materialsString = materialsString.replace("※특정성분:", ",");
        materialsString = materialsString.replace("[", ",");
        materialsString = materialsString.replace("함유", "");
        materialsString = materialsString.replaceAll(".:", "");
        materialsString = materialsString.replaceAll("[^가-힣a-zA-Z0-9,]", "");
        materialsString = materialsString.replace(" ", "");
        String[] array = materialsString.split(",");
        materials = new ArrayList<>(new HashSet<>(Arrays.asList(array)));

        allergiesString = allergiesString.replace("함유", "");
        allergiesString = allergiesString.replace("포함", "");
        allergiesString = allergiesString.replace("알수없음", "");
        allergiesString = allergiesString.replace(" ", "");
        List<String> array2 = new ArrayList<>(List.of(allergiesString.split(",")));
        if (array2.size() == 1 && array2.get(0).equals("")) array2.clear();
        List<String> array3 = new ArrayList<>();

        List<Allergy> knownAllergies = allergyRepository.findAll();
        for (String foundAllergy : array2) {
            boolean isSimilar = false;
            for (Allergy knownAllergy : knownAllergies) {
                if (foundAllergy.contains(knownAllergy.getName())) {
                    isSimilar = true;
                    break;
                }
            }
            if (!isSimilar) array3.add(foundAllergy);
        }

        for (String material : materials) {
            for (Allergy allergy : knownAllergies) {
                if (material.contains(allergy.getName())) {
                    array3.add(allergy.getName());
                }
            }
        }
        allergies = new ArrayList<>(new HashSet<>(array3));

        foodFromApiResponse.setName(name);
        foodFromApiResponse.setMaterials(materials);
        foodFromApiResponse.setAllergies(allergies);

        return foodFromApiResponse;

    }


    public MedicineFromApiResponse getMedicineFromApiByBarcodeNo(String barcode) {
        String uri = "https://apis.data.go.kr/1471000/DrugPrdtPrmsnInfoService04/getDrugPrdtPrmsnDtlInq03" +
                "?" +
                "serviceKey=" +
                appProperties.getKoreaPublicApiKey() +
                "&type=" +
                "json" +
                "&bar_code=" +
                barcode;

        String json;
        try {
            json = SimpleHttp.get(uri);
        } catch (IOException ignored) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "공공 데이터 API 서버가 응답하지 않습니다. 잠시 후 다시 시도해 주세요. (IOException)");
        } catch (Exception ignored) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "공공 데이터 API 서버가 응답하지 않습니다. 잠시 후 다시 시도해 주세요.");
        }


        JSONObject jsonObject;
        JSONObject body;
        JSONArray items;
        JSONObject item;
        String name = "";
        String mainIngredientsString = "";
        String ingredientsString = "";

        try {
            jsonObject = new JSONObject(json);
            body = jsonObject.getJSONObject("body");
            items = body.getJSONArray("items");
            item = items.getJSONObject(0);

            if (item.has("ITEM_NAME")) {
                if (item.get("ITEM_NAME") instanceof String) name = (String) item.get("ITEM_NAME");
            }
            if (item.has("MAIN_ITEM_INGR")) {
                if (item.get("MAIN_ITEM_INGR") instanceof String)
                    mainIngredientsString = (String) item.get("MAIN_ITEM_INGR");
            }
            if (item.has("INGR_NAME")) {
                if (item.get("INGR_NAME") instanceof String) ingredientsString = (String) item.get("INGR_NAME");
            }

            mainIngredientsString = mainIngredientsString.replaceAll("\\[.*?\\]", "");
            mainIngredientsString = mainIngredientsString.replace('·', '|');
            String[] parts = mainIngredientsString.split("\\s+");
            if (parts.length == 3) {
                mainIngredientsString = String.join(" ", List.of(parts).subList(0, parts.length - 2));
            }
            mainIngredientsString = mainIngredientsString.replace(" ", "");
            ArrayList<String> list = new ArrayList<>(List.of(mainIngredientsString.split("\\|")));

            ingredientsString = ingredientsString.replaceAll("\\[.*?\\]", "");
            ingredientsString = ingredientsString.replace('·', '|');
            String[] parts2 = ingredientsString.split("\\s+");
            if (parts2.length == 3) {
                ingredientsString = String.join(" ", List.of(parts2).subList(0, parts2.length - 2));
            }
            ingredientsString = ingredientsString.replace(" ", "");
            ArrayList<String> list2 = new ArrayList<>(List.of(ingredientsString.split("\\|")));

            Set<String> set = new HashSet<>(list);
            set.addAll(list2);

            List<String> ingredients = new ArrayList<>(set);

            medicineFromApiResponse.setName(name);
            medicineFromApiResponse.setIngredients(ingredients);

            return medicineFromApiResponse;
        } catch (JSONException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "의약품 정보를 불러올 수 없습니다. (JSONException)");
        } catch (Exception ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "의약품 정보를 불러올 수 없습니다.");
        }
    }
}
