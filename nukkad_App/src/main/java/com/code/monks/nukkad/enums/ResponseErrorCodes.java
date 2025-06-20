package com.code.monks.nukkad.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseErrorCodes {
	ADDRESS_NOT_FOUND(400,"Address not found."),
	DEFAULT_ADDRESS_CAN_NOT_BE_CHANGE(400,"Default address can not be change."),
	DEFAULT_ADDRESS_CAN_NOT_BE_DELETE(400,"Default address can not be delete"),
	CART_ITEM_NOT_FOUND(400,"Cart item not found with id: "),
	STOREKEEPER_NOT_FOUND(400,"Storekeeper not found with id: "),
	STOREKEEPER_NOT_FOUND_WITH_STORE_ID(400,"Storekeeper not found with store id: "),
	CUSTOMER_NOT_FOUND(400,"Customer not found with id: "),
	ITEM_NOT_FOUND(400,"Item not found with id: "),
	DUPLICATE_CATEGORY_EXCEPTION(1001, "Category already exists"),
	DUPLICATE_ITEM_EXCEPTION(1001,"Item already exits"),
	ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION(403,"Only customers can perform this action "),
	ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION(403,"Only storekeeper can perform this action "),

	UNHANDLED_EXCEPTION(500, "Unhandled exception"),
	SEARCH_NOT_FOUND(400, "Failed to perform search. Please try again later."),
	ITEM_NOT_SAVED_EXCEPTION(400,"Categories not found so you can't add item.");

	private int responseCode;

	private String message;

}
