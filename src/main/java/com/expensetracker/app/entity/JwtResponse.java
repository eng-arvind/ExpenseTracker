package com.expensetracker.app.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class JwtResponse {

	public JwtResponse(String jwtToken) {
		super();
		this.jwtToken = jwtToken;
	}

	private String jwtToken;

	public String getJwtToken() {
		return jwtToken;
	}

}
