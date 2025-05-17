package com.code.monks.nukkad.constants;

public class UrlConstants {

	public static final String CONTEXT = "nukkad";

	public static class CATEGORY {

		public static final String BASE = CONTEXT + "/api/category";

		public static final String CREATE = "/v1/create";

		public static final String GET_ALL = "/v1/get";

	}

	public static final String UPDATE_CATEGORY_BY_ID = "v1/updateCategory/{id}";

	public static final String ITEM = "/item";

	public static final String SAVE_ITEM = "/v1/save";

	public static final String GET_ALL_ITEMS = "/v1/getAllItems";

	public static final String GET_ITEM_BY_ID = "/v1/getItemById/{id}";

	public static final String UPDATE_ITEM_BY_ID = "/v1/updateItemById/{id}";

	public static final String OTP = "/api/otp";

	public static final String SEND_OTP = "/send";

	public static final String CUSTOMER = "/customer";

	public static final String CREATE_CUSTOMER = "/v1/customer/profile/create";

	public static final String UPDATE_CUSTOMER = "/v1/customer/profile/update/{id}";

	public static final String SHOPKEEPER = "/shopkeeper";

	public static final String SAVE_SHOPKEEPER_DETAILS = "/v1/shopkeeper/profile/save";

	public static final String GET_SHOPKEEPER_DETAILS_BY_ID = "/v1/shopkeeper/profile/getShopkeeperDetailsById/{id}";

	public static final String UPDATE_SHOPKEEPER_DETAILS = "/v1/shopkeeper/profile/update/{id}";

	private UrlConstants() {
	}

}
