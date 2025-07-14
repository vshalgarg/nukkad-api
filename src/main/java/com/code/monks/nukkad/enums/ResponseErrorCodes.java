package com.code.monks.nukkad.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseErrorCodes {
	DUPLICATE_EMAIL_FOUND_EXCEPTION(1024,"Email is already exists"),
	ITEM_NOT_FOUND_WITH_GIVEN_UNIT(1023,"Item not found in cart with given unit"),
	CART_NOT_FOUND(1022,"Cart not found for customer id"),
	EXTERNAL_API_CALL_FAILED(502,"External API call failed"),
	EXTERNAL_SERVICE_ERROR(502,"External service exception occur"),
	DEFAULT_QR_CODE_CAN_NOT_BE_DELETE(1003,"Cannot delete default QR code"),
	QR_CODE_LIMIT(1004,"Only 3 QR codes are allowed per storekeeper."),
	QR_CODE_NOT_FOUND(1005,"QR code not found. "),
	ADDRESS_NOT_FOUND(1006,"Address not found."),
	DEFAULT_ADDRESS_CAN_NOT_BE_CHANGE(1007,"Default address can not be change."),
	DEFAULT_ADDRESS_CAN_NOT_BE_DELETE(1008,"Default address can not be delete"),
	CART_ITEM_NOT_FOUND(1009," Item not found in cart with itemId: "),
	STOREKEEPER_NOT_FOUND(1010,"Storekeeper not found with id: "),
	CUSTOMER_NOT_FOUND(1011,"Customer not found with id: "),
	CATEGORY_NOT_FOUND(1012,"Category not found with id: "),
	ITEM_NOT_FOUND(1013,"Item not found with id: "),
	DUPLICATE_CATEGORY_EXCEPTION(1001, "Category already exists"),
	ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION(1016,"Only customers can perform this action "),
	ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION(1017,"Only storekeeper can perform this action "),
	ACCESS_DENIED_FOR_ADMIN_EXCEPTION(1018,"Only Admin can perform this action "),
	UNHANDLED_EXCEPTION(500, "Unhandled exception"),
	SEARCH_NOT_FOUND(1019, "Failed to perform search. Please try again later."),
	CATEGORY_NOT_FOUND_TO_SAVE_ITEM_EXCEPTION(1020,"Categories not found so you can't add item."),
    DUPLICATE_RESOURCE_EXCEPTION(1021,"Duplicate resource found");
	private int responseCode;

	private String message;

}
