package com.code.monks.nukkad.constants;

public class UrlConstants {

	public static final String CONTEXT = "/nukkad";


	public static class CATEGORY {

		public static final String BASE = CONTEXT + "/api/category";

		public static final String CREATE = "/v1/create";
		public static final String UPDATE = "/v1/update";
		public static final String GET_ALL = "/v1/get";
		public static final String GET_BY_ID = "/v1/get/{id}";
	}
	
	public static class ITEM{

		public static final String BASE = CONTEXT + "/api/item";
		public static final String CREATE = "/v1/create";
		public static final String GET_ALL = "/v1/get";
		public static final String GET_BY_ID = "/v1/get_by_id/{id}";
		public static final String UPDATE = "/v1/update/{id}";
		public static final String GET_BY_CATEGORY = "/v1/get_by_category/{categoryId}";


	}

 public static class CUSTOMER{

		public static final String BASE = CONTEXT + "/api/customer";
		public static final String CREATE = "/v1/create";
		public static final String UPDATE = "/v1/update";
		public static final String ADD_STORE_TO_CUSTOMER = "/v1/add/store";
		public  static final String GET_MY_STORES = "/v1/myStores";
		public static final String DELETE_STORE = "/v1/delete/store";

 }

 public static class OTP{

	 public static final String BASE = CONTEXT +  "/api/otp";
	 public static final String SEND_OTP = "/v1/send";
	 public static final String VERIFY_OTP ="/v1/verify";
 }


public static class STOREKEEPER{

		public static final String BASE = CONTEXT + "/api/storekeeper";
		public static final String CREATE = "/v1/profile/save";
		public static  final String UPDATE = "/v1/profile/update";
}

public static class ADDRESS{
	public static  final String BASE = CONTEXT+ "/api/addresses";
	public static  final String CREATE = "/v1/create";
	public static  final String GET = "/v1/get";
	public static final String UPDATE = "/v1/address/{id}";
	public static final String DELETE = "/v1/address/{id}/delete";
	public static final String MARK_AS_DEFAULT = "/v1/address/{id}/mark-default";
}


	public static class ORDER
	{
		public static final String BASE =CONTEXT +"/api/orders";
		public static final String PLACE_ORDER = "/v1/placeOrder";
		public static final String GET_ORDER_BY_STOREKEEPER ="/v1/orderByStoreKeeper";
//		public static final String CANCELLED_ORDER_BY_STOREKEEPER ="/v1/order/cancel/{id}";
		public static final String UPDATE_STATUS = "/v1/updateByStatus/{id}";
		public static final String ORDER_HISTORY ="/v1/history";
		public static final String REPEAT_ORDER ="/v1/repeatOrder/{id}";
		public static final String DISPATCH_ORDER ="/v1/dispatchOrder";
	}

	public static class CART_ITEM{

		public static final String BASE = CONTEXT + "/api/cartItem";
		public static final String ADD = "/v1/add";
		public static final String GET_CART_ITEM_FOR_CUSTOMER = "/v1/get";
		public static final String DELETE_CART_ITEM_BY_ID = "/v1/deleteItem/{id}";
		public static final String UPDATE_QUANTITY = "/v1/{id}/update";
	}

	public static class SEARCH{

		public static final String BASE = CONTEXT + "/api/search";
		public static final String GET = "v1/get";
	}

	public static class RATING
	{
		public static final String BASE = CONTEXT + "/api/rating";
		public static final String CREATE ="/v1/create";
	}

	public static class STOREKEEPER_QR_CODE{
		public static final String BASE = CONTEXT + "/api/qr";
		public static final String UPLOAD = "/v1/upload";
		public static final String GET = "/v1/getAll/qrCodes";
		public static final String UPDATE = "/v1/update/{id}";
		public static final String MARK_AS_DEFAULT = "v1/{id}/default";
		public static final String DELETE = "/v1/delete/{id}";
	}

	private UrlConstants() {
	}

}
