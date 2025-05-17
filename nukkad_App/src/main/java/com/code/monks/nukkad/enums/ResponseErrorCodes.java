package com.code.monks.nukkad.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseErrorCodes {

	DUPLICATE_CATEGORY_EXCEPTION(1001, "Category already exists"),

	UNHANDLED_EXCEPTION(500, "Unhandled exception"),;

	private int responseCode;

	private String message;

}
