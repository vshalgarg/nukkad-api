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
 }


public static class SHOPKEEPER{
		public static final String BASE = CONTEXT + "/api/shopkeeper";
		public static final String CREATE = "/v1/profile/save";
		public static  final String UPDATE = "/v1/profile/update/{id}";
}

public static class ADDRESS{
	public static  final String BASE = CONTEXT+ "/api/addresses";
	public static  final String CREATE = "/v1/customer/{customerId}";
	public static  final String GET = "/v1/customer/{customerId}";
	public static  final String UPDATE = "/v1/customer/{customerId}/address/{addressId}";

}


	public static class ORDER
	{
		public static final String BASE =CONTEXT +"/api/order";
		public static final String CREATE = "/v1/create";
		public static final String GET_ALL = "/v1/orders/status";
		public static final String GET_BY_TRACKING = "/v1/fetch/order/fetch";
	}

	private UrlConstants() {
	}

}
