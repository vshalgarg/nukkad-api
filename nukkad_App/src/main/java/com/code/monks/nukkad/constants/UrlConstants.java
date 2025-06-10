package com.code.monks.nukkad.constants;

public class UrlConstants {

	public static final String CONTEXT = "/nukkad";


	public static class CATEGORY {

		public static final String BASE = CONTEXT + "/api/category";

		public static final String CREATE = "/v1/create";

		public static final String GET_ALL = "/v1/get";

	}
 public static class ITEM{

		public static final String BASE = CONTEXT + "/api/item";

		public static final String CREATE = "/v1/create";

		public static final String GET_ALL = "/v1/get";

		public static final String GET_BY_ID = "/v1/get_by_id/{id}";

		public static final String UPDATE = "/v1/update/{id}";

 }

 public static class CUSTOMER{
		public static final String BASE = CONTEXT + "/api/customer";

		public static final String CREATE = "/v1/create";

		public static final String UPDATE = "/v1/update/{id}";

 }

 public static class OTP{
	 public static final String BASE = CONTEXT +  "/api/otp";
	 public static final String SENDOTP = "/v1/send";
	 public static final String VERIFYOTP ="/v1/verify";
 }


public static class STOREKEEPER{
		public static final String BASE = CONTEXT + "/api/storekeeper";
		public static final String CREATE = "/v1/profile/save";
		public static  final String UPDATE = "/v1/profile/update/{id}";
}

public static class PLACEORDER
{
	public static final String BASE = CONTEXT + "/api/placeOrder";
	public static final String CREATE = "/v1/createOrder";
	public static final String GET_ALL= "/v1/getAllOrder";
	public static final String GET_ORDER_BY_ID = "/v1/getOrderById";
	public static final String DELETE_ORDER = "/v1/deleteOrder";
}

public static class ADDRESS{
	public static  final String BASE = CONTEXT+ "/api/addresses";
	public static  final String CREATE = "/v1/create";
	public static  final String GET = "/v1/get";
	public static final String UPDATE = "/v1/address/{id}";

}


	public static class ORDER
	{
		public static final String BASE =CONTEXT +"/api/order";
		public static final String CREATE = "/v1/create";
		public static final String GET_ALL = "/v1/orders/status";
		public static final String GET_ALL_ORDER_BY_ID ="/v1/orders/orderById/{id}";

//		public static final String GET_BY_TRACKING = "/v1/fetch/order/fetch";
	}

	public static class CART_ITEM{

		public static final String BASE = CONTEXT + "/api/cartItem";
		public static final String ADD = "/v1/add";
		public static final String GET_CART_ITEM_FOR_CUSTOMER = "v1/customer/{customerId}";
		public static final String DELETE_CART_ITEM_BY_ID = "/v1/deleteitem/{id}";
		public static final String UPDATE_QUANTITY = "/v1/{id}/update";
	}

	private UrlConstants() {
	}

}
