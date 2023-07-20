package com.Flight_Reservation_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Flight_Reservation_app.entity.User;
import com.Flight_Reservation_app.repository.UserRepository;

@Controller
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping("/showLoginPage")
	public String showLoginPage() {
		return "login/login";
	}
	
	@RequestMapping("/showRegPage")
	public String showRegPage() {
		return "login/showReg";
	}
	
	@RequestMapping("/showReg")
	public String showReg() {
		return "login/showReg";//The control cannot find the login folder under which the jsp files are created. Therefore we need to mention the folder also in the directory.
	}
	
	@RequestMapping("/saveReg")
	public String saveReg(@ModelAttribute("user") User user) {//To take the data/to read the data from jsp page should get stored in the object. By User user we are creating an entity object. 
		userRepo.save(user);//After saving the data to DB, we need to redirect to the login page.
		return "/login/login";
	}
	
	@RequestMapping("/verifyLogin")
	public String verifyLogin(@RequestParam("email") String email, @RequestParam("password") String password, ModelMap modelMap) {
		User user = userRepo.findByEmail(email);
		if(user!=null) {
		if(user.getEmail().equals(email) && user.getPassword().equals(password)) {
			return "findFlights";
		}else {
			modelMap.addAttribute("error", "Invalid username/password");
			return "login/login";
		}
		}else {
			modelMap.addAttribute("err", "Null Credentials entered");
			return "login/login";
		}
	}
}
