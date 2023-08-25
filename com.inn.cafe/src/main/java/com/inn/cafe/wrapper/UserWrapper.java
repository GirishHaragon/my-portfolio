package com.inn.cafe.wrapper;

import lombok.Data;

@Data
public class UserWrapper {//We r developing this class as a dto class to do some changes in the status of roles(User/Admin).

    //UserWrapper user = new com.inn.cafe.wrapper.UserWrapper(1,"abc","abc@gmail.com","123","false");//We created this bcz we wanted to check whether this shows error/not bcz this is the thing we r doing in the POJO @NamedQuery. Since this is not showing any error we r good to go with the query in the POJO. We are writing the same thing like this in Pojo Query

    private Integer id;
    private String name;
    private String email;
    private String contactNumber;
    private String status;

    public UserWrapper(Integer id, String name, String email, String contactNumber, String status) {//Instead of using it we write @AllArgsConstructor
        this.id = id;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.status = status;
    }
}
