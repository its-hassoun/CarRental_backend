package com.projetmultimedia.main.DTO__requests;
import com.projetmultimedia.main.DTO__requests.LoginRequest ;

public class RegisterRequest extends LoginRequest {
    private String name;
    private int age;
    private String governorate;
    // Getters and Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGovernorate() {
		return governorate;
	}
	public void setGovernorate(String governorate) {
		this.governorate = governorate;
	}
}