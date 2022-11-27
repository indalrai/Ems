package com.cats.ems.serviceImpl;

import com.cats.ems.dto.JwtPayload;

public interface CustomUserDetailService {

	JwtPayload getJwtPayload();

	void setJwtPayload(JwtPayload jwtPayload);

}