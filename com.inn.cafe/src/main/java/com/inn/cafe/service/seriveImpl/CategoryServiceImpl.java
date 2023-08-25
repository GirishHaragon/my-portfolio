package com.inn.cafe.service.seriveImpl;

import com.google.common.base.Strings;
import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.Category;
import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.CategoryDao;
import com.inn.cafe.service.CategoryService;
import com.inn.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j//With the help of this we can print some logs into the console.
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
//          Before letting somebody to create category we need to check the user whether he is an admin or not.
            if(jwtFilter.isAdmin()){
                if(validateCategoryMap(requestMap, false)){
                    categoryDao.save(getCategoryFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Category added Successfully!..", HttpStatus.OK);
                }
            }else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd){
        Category category = new Category();
        if(isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                log.info("Inside if");//For testing whether this if block has run or not we have used this logger command. If the if block is executed then Inside If will be printed in console. And if we enter the url without filterValue then this if block will not be executed.
                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                //We check if the particular Admin id exists in the DB or not
                //But before that we have to validate the request
                if (validateCategoryMap(requestMap, true)){
                    Optional<Category> optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty()){
                        categoryDao.save(getCategoryFromMap(requestMap, true));
                        return CafeUtils.getResponseEntity("Category Updated Successfully!..", HttpStatus.OK);
                    }else {
                        return CafeUtils.getResponseEntity("Category id doesn't exist", HttpStatus.OK);
                    }
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
