package com.code.monks.nukkad.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseErrorCodes {
	UNHANDLED_EXCEPTION(1000, "Unhandled exception"),
	EXTERNAL_API_CALL_FAILED(1001,"External API call failed"),
	EXTERNAL_SERVICE_ERROR(1002,"External service exception occur"),
	DEFAULT_QR_CODE_CAN_NOT_BE_DELETE(1003,"Cannot delete default QR code"),
	QR_CODE_LIMIT(1004,"Only 3 QR codes are allowed per storekeeper."),
	QR_CODE_NOT_FOUND(1005,"QR code not found. "),
	ADDRESS_NOT_FOUND(1006,"Address not found."),
	DEFAULT_ADDRESS_CAN_NOT_BE_DELETE(1007,"Default address can not be delete"),
	STOREKEEPER_NOT_FOUND(1008,"Storekeeper not found with id: "),
	CUSTOMER_NOT_FOUND(1009,"Customer not found with id: "),
	CATEGORY_NOT_FOUND(1010,"Category not found with id: "),
	ITEM_NOT_FOUND(1011,"Item not found with id: "),
	DUPLICATE_CATEGORY_EXCEPTION(1012, "Category already exists"),
	ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION(1013,"Only customers can perform this action "),
	ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION(1014,"Only storekeeper can perform this action "),
	ACCESS_DENIED_FOR_ADMIN_EXCEPTION(1015,"Only Admin can perform this action "),
	SEARCH_NOT_FOUND(1016, "Failed to perform search. Please try again later."),
	CATEGORY_NOT_FOUND_TO_SAVE_ITEM_EXCEPTION(1017,"Categories not found so you can't add item."),
    DUPLICATE_RESOURCE_EXCEPTION(1018,"Duplicate resource found"),
	DUPLICATE_EMAIL_FOUND_EXCEPTION(1019,"Email is already exists"),
	DUPLICATE_CUSTOMER_PROFILE_FOUND_EXCEPTION(1020,"Customer profile is already exists"),
	DUPLICATE_STOREKEEPER_PROFILE_FOUND_EXCEPTION(1021,"Storekeeper profile is already exists"),
	SUBMIT_RATING_EXCEPTION(1022,"Unable to submit rating at this time. Please try again later."),
	CART_EMPTY(1023,"Cart is empty.So , you can not place order"),
	STOREKEEPER_CUSTOMER_MISMATCH(1024,"Storekeeper is not associate with customer"),
	ADDRESS_CUSTOMER_MISMATCH(1025,"Address is not match with customer"),
	ORDER_NOT_FOUND(1026,"Order not found"),
	UNABLE_TO_SET_NOTIFICATION_ENABLE_STATUS(1027,"Unable to set notification status at this time. Please try again later."),
	ENABLE_TO_SAVE_NOTIFICATION(1028,"Could not save notification. Please try again later."),
	ENABLE_TO_FETCH_NOTIFICATIONS(1029,"Could not fetch notifications. Please try again later."),
	UNABLE_TO_ALL_NOTIFICATIONS(1030,"Unable to delete notifications"),
	ERROR_TO_CONVERT_JAVA_OBJECT_TO_STRING(1031,"Error serializing AddressSnapshotDTO to JSON"),
	ERROR_TO_CONVERT_STRING_TO_JAVA_OBJECT(1032,"Error deserializing JSON to AddressSnapshotDTO"),
	FAILED_TO_UPLOAD_FILE_TO_FIREBASE(1033,"Failed to upload file to Firebase"),
	FAILED_TO_DELETE_FILE_FROM_FIREBASE(1034,"FAILED_TO_DELETE_FILE_FROM_FIREBASE"),
	STORE_NOT_FOUND(1035,"Store not found with "),
	STORE_NOT_LINKED_WITH_CUSTOMER(1036,"Storekeeper is not linked with this customer"),
	DEFAULT_STORE_NOT_SET(1037,"No default store set for this customer"),
	CAN_NOT_DELETE_DEFAULT_STORE(1038,"Cannot delete default store. Please set another store as default before deleting.");

	private int responseCode;
	private String message;

}
