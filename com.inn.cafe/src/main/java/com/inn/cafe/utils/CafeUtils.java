package com.inn.cafe.utils;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CafeUtils {//Here is going to be the generic methods which can be used in any classes.

    private CafeUtils() {//We are developing 1 private constructor here so that we cannot create object of this class.

    }//We r going to define every method in this class will be static so that we can directly access it with the clas name dot method name we want to access.

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
    }

    public static String getUUID() {
        Date date = new Date();
        long time = date.getTime();
        return "Bil-" + time;//This will give us a Unique UUID that consist of Bill-time of generation concatinated.
    }

    public static JSONArray getJsonArrayFromString(String data) throws JSONException{
        JSONArray jsonArray = new JSONArray(data);
        return jsonArray;
    }

    public static Map<String, Object> getMapFromJson(String data){
        if (!Strings.isNullOrEmpty(data))
            return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
            }.getType());
        return new HashMap<>();//Whatever JSON is there it will convert it into Map<String, Object> form.
    }

    public static Boolean isFileExist(String path){
        log.info("Inside isFileExist {}", path);
        try {
            File file = new File(path);
            return (file != null && file.exists()) ? Boolean.TRUE : Boolean.FALSE;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}