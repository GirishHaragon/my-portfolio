package com.blog.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MainClass {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();//We have created an object of 'BCryptPasswordEncoder()' which we get it from security package, the Jar file that we added 'ModelMapper', in our first lecture of SpringSecurity, that has this object.
        System.out.println(encoder.encode("password"));//Let's see how this password is converted to Encoded value.
    }//So in the output we get Encoded text for password 'password' is "$2a$10$rrwvIvWc1mlh9IcInTWGzenxJyMro1GmREtuRCnuU8o95TZt60odu" which cannot be decoded anyway. So this is what spring security does, that's all the advantage we have with it.. //So now this is the way the password will be saved in the DB. So if Decoding technique was there then anybody could decode it & get the passwords. Decoding is what Hackers will be looking at,,
    //Now If any client/customer of our real project says that they r facing issues in banking application in the real production environment, then we have to open up the DB, & if passwords of customers r directly kept open, that will be a big problem, & if passwords r encoded, even the developer looking at the eal business environment, he will not have the access to real data. And without Authentication he cannot use anything in there. Now this is what we need to apply in such cases.
}
