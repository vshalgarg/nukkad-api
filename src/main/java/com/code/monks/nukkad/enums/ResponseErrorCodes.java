package com.code.monks.nukkad.enums;

import com.code.monks.nukkad.exception.InvalidOrderStatusException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseErrorCodes {
//	BULK_CATEGORY_CREATION_FAILED(1004,"Bulk category creation failed"),
	EXTERNAL_API_CALL_FAILED(502,"External API call failed"),
	EXTERNAL_SERVICE_ERROR(502,"External service exception occur"),
	DEFAULT_QR_CODE_CAN_NOT_BE_DELETE(1003,"Cannot delete default QR code"),
	QR_CODE_LIMIT(1004,"Only 3 QR codes are allowed per storekeeper."),
	QR_CODE_NOT_FOUND(1005,"QR code not found. "),
	ADDRESS_NOT_FOUND(1006,"Address not found."),
	DEFAULT_ADDRESS_CAN_NOT_BE_CHANGE(1007,"Default address can not be change."),
	DEFAULT_ADDRESS_CAN_NOT_BE_DELETE(1008,"Default address can not be delete"),
	CART_ITEM_NOT_FOUND(1009,"Cart item not found with id: "),
	STOREKEEPER_NOT_FOUND(1010,"Storekeeper not found with id: "),
	STOREKEEPER_NOT_FOUND_WITH_STORE_ID(400,"Storekeeper not found with store id: "),
	CUSTOMER_NOT_FOUND(1011,"Customer not found with id: "),
	CATEGORY_NOT_FOUND(1012,"Category not found with id: "),
	ITEM_NOT_FOUND(1013,"Item not found with id: "),
	DUPLICATE_CATEGORY_EXCEPTION(1001, "Category already exists"),
	DUPLICATE_ITEM_EXCEPTION(1015,"Item already exits"),
	ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION(1016,"Only customers can perform this action "),
	ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION(1017,"Only storekeeper can perform this action "),
	ACCESS_DENIED_FOR_ADMIN_EXCEPTION(1018,"Only Admin can perform this action "),
	UNHANDLED_EXCEPTION(500, "Unhandled exception"),
	SEARCH_NOT_FOUND(400, "Failed to perform search. Please try again later."),
	ITEM_NOT_SAVED_EXCEPTION(400,"Categories not found so you can't add item."),
    DUPLICATE_RESOURCE_EXCEPTION(400,""),

	INVALID_ORDER_STATUS_EXCEPTION(10409, "Order status cannot be null"),
	ORDER_STATUS_UPDATE_EXCEPTION(10400,"Oops! Order are cancelled. Status cannot be update "),
	UNAUTHORIZED_DISPATCH_EXCEPTION(10401,"You are not authorized to dispatch this order."),
	HANDLE_INVALID_ITEM_LIST(10402,"Item list must not be null or empty"),
	ORDER_ALREADY_CANCELLED_EXCEPTION(10403,"Order already cancelled so its cannot be update");

	private int responseCode;

	private String message;

}
