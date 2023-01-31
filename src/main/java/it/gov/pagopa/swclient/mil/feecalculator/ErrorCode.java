/*
 * ErrorCode.java
 *
 * 12 dec 2022
 */

package it.gov.pagopa.swclient.mil.feecalculator;

public final class ErrorCode {
	public static final String MODULE_ID 								= "007";
	
	public static final String ERROR_PAYMENT_METHOD 					= MODULE_ID + "000001";
	public static final String ERROR_NOTICES_LIST_EXCEEDED 				= MODULE_ID + "000002";
	public static final String ERROR_INVALID_AMOUNT	 					= MODULE_ID + "000003";
	public static final String ERROR_PAX_CODE_INVALID	 				= MODULE_ID + "000004";
	public static final String ERROR_TRANSFERS_LIST_EXCEEDED			= MODULE_ID + "000005";
	public static final String ERROR_RETRIEVING_FEES					= MODULE_ID + "000006";
	
		
	private ErrorCode() {
		
	}
}
