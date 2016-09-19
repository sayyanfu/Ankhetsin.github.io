package com.fangbian365.kuaidi.base.utils;

import java.util.HashMap;
import java.util.Map;

public class UrlUtil {

	/** HOST地址 */
	private static final String HOST_URL = "http://app.chb365.cn/";
	private static final String SYNC_DATA = "SynBaseData/index?type=";
	private static final String OPERTAI = "OperTai/index?type=";
	private static final String YY = "YY/index?type=";
	private static final String FINANCIAL = "cw/index?type=";
	private static final String OTHER = "other/index?type=";
	/** 1.用户登录接口 */
	public static String URL_LOGIN = HOST_URL + SYNC_DATA + "login";
	/** 2.厅、楼层同步接口 */
	public static String URL_HALL_SYNC = HOST_URL + SYNC_DATA + "ting";
	/** 3.台位同步接口 */
	public static String URL_TABLE_SYNC = HOST_URL + SYNC_DATA + "tai";
	/** 4.菜品分类同步接口 */
	public static String URL_DISHES_CATEGORY = HOST_URL + SYNC_DATA
			+ "producttype";
	/** 5.菜品同步接口 */
	public static String URL_DISHES = HOST_URL + SYNC_DATA + "product";
	/** 套餐明细同步接口 */
	/** 6.口味同步接口 */
	public static String URL_TASTE_SYNC = HOST_URL + SYNC_DATA + "taste";
	/** 7.退菜说明同步接口 */
	public static String URL_FOODBACK_SYNC = HOST_URL + SYNC_DATA + "backreson";
	/** 8.赠送原因同步接口 */
	public static String URL_GIVING_REASON = HOST_URL + SYNC_DATA + "zsreson";
	/** 9.部门同步接口 */
	public static String URL_DEPARTMENT = HOST_URL + SYNC_DATA + "dept";
	/** 10.用户接口 */
	public static String URL_USER = HOST_URL + SYNC_DATA + "user";
	/** 11.开台接口 */
	public static String URL_OPEN_TABLE = HOST_URL + OPERTAI + "kt";
	/** 12.换台接口 */
	public static String URL_CHANGE_TABLE = HOST_URL + OPERTAI + "ht";
	/** 13.关台 */
	public static String URL_CLOSE_TABLE = HOST_URL + OPERTAI + "closetai";
	/** 14.启用 、禁用、清台接口 */
	public static String URL_CHANGE_STATE_TABLE = HOST_URL + OPERTAI
			+ "taistatechange";
	/** 15.点菜接口 */
	public static String URL_ORDER_DISHES = HOST_URL + YY + "dc";
	/** 16.退菜接口 */
	public static String URL_RETURN_DISHES = HOST_URL + YY + "tc";
	/** 17.赠菜接口 */
	public static String URL_GIVING_DISHES = HOST_URL + YY + "zenc";
	/** 18.取消赠菜接口 */
	public static String URL_CANCEL_GIVING_DISHES = HOST_URL + YY + "undozenc";
	/** 19.更改菜品数量接口 */
	public static String URL_CHANGE_DISHES_NUMBER = HOST_URL + YY + "changecnt";
	/** 20.更改菜品价格接口 */
	public static String URL_CHANGE_DISHES_PRICE = HOST_URL + YY
			+ "changeprice";
	/** 21.获取菜品明细接口 */
	public static String URL_DISHES_DETAIL = HOST_URL + YY + "productall";
	/** 22.会员查询接口 */
	public static String URL_SEARCH_MEMBER = HOST_URL + FINANCIAL + "gethyinfo";
	/** 23.会员折扣查询接口 */
	public static String URL_SEARCH_MEMBER_DISCOUNT = HOST_URL + FINANCIAL
			+ "gethydis";
	/** 24.网络己付款查询接口 */
	public static String URL_SEARCH_ALREADY_PAY = HOST_URL + FINANCIAL
			+ "getnetpay";
	/** 25.台位结账接口 */
	public static String URL_PAY_TABLE = HOST_URL + FINANCIAL + "onejz";
	/** 26.反结账接口 */
	public static String URL_ANTI_PAY = HOST_URL + FINANCIAL + "fjz";
	/** 27.交班接口 */
	public static String URL_SHIFT_EXCHANGE = HOST_URL + FINANCIAL + "jb";
	/** 28.当班已结账查询接口 */
	public static String URL_CURRENT_SEARCH_ALREADY_PAY = HOST_URL + FINANCIAL
			+ "jzcx";
	/** 29.交班报表数据获取接口 */
	public static String URL_SHIFT_EXCHANGE_REPORTING = HOST_URL + FINANCIAL
			+ "jbdata";
	/** 30.更改服务员 */
	public static String URL_CHANGE_WAITER = HOST_URL + OTHER
			+ "changewaiter";
	/** 31.更改营销经理 */
	public static String URL_CHANGE_MANAGER = HOST_URL + OTHER
			+ "changeyxjl";
	/** 32.更改就餐人数 */
	public static String URL_CHANGE_EATER_NUMBER = HOST_URL + OTHER
			+ "changepersoncnt";
	/** 33.获取指定流水号上的菜品信息 */
	public static String URL_GET_SERIAL_DISHES = HOST_URL + YY
			+ "getproduct";
	/** 34.APP升级	 */
	public static String URL_APP_UPDATE = HOST_URL + OTHER
			+ "addedition";
	/** 35.添加临时菜品  */
	public static String URL_ADD_TEMP_DISH = HOST_URL + SYNC_DATA
			+ "addtempproduct";
	/** 36.转单接口*/
	public static String URL_TURN_ORDER = HOST_URL + YY + "zhuanc";
	/** 37.更改菜品*/
	public static String URL_SET_PRODUCT = HOST_URL + SYNC_DATA + "setproduct";
	/** 38.更改制作状态信息*/
	public static String URL_CHANGE_MAKE_STATUS = HOST_URL + YY + "upmarkstatus";
	/** 39.更改起菜状态信息*/
	public static String URL_CHANGE_WAIT_STATUS = HOST_URL + YY + "upwaitstatus";
	/** 40.市别(餐段) Meal period*/
	public static String URL_MEAL_PERIOD = HOST_URL + SYNC_DATA + "mealstime";
	/** 41.版本 */
	public static String URL_EDITION = HOST_URL + SYNC_DATA + "edition";
	/** 42.添加会员同步*/
	//public static String URL_ADD_MEMBER = HOST_URL + OTHER + "addhy";
	/** 43.会员充值*/
	/** 44./获取会员类型*/
	//public static String URL_ADD_MEMBER = HOST_URL + OTHER + "addhy";
	/** 45.宴会点菜*/
	public static String URL_YH_ORDER_DISHES = HOST_URL + YY + "yhdc";
	/** 46. 获取员工折扣率*/
	public static String URL_WORKER_DISCOUNT = HOST_URL + SYNC_DATA + "getworkerdis";
	/** 47. 批量获取菜品明细 （并单结账）*/
	public static String URL_BATCH_DISHES_DETAIL= HOST_URL + YY + "plproductall";
	/** 48. 获取员工最大抹零*/
	public static String URL_WORKER_MOLING = HOST_URL + SYNC_DATA + "getworkerml";
	/** 49. 结账*/
	public static String URL_FINAL_BILL = HOST_URL + FINANCIAL + "onejz";
	/** 50. 催菜*/
	public static String URL_REMIND_DISH = HOST_URL + YY + "reminder";
	/** 51. 商家验证接口*/
	public static String URL_BUSINESS_VERIFI = HOST_URL + SYNC_DATA + "shopverification";
	/** 52. 商家详细信息接口*/
	public static String URL_BUSINESS_DETAIL = HOST_URL + SYNC_DATA + "shopinfo";
	/** 53. 开台并点菜*/
	public static String URL_WM_KTDC = HOST_URL + YY + "ktdc";
	/** 54. 反结账账单查询*/
	public static String URL_UN_BILL_LIST = HOST_URL + FINANCIAL + "jzcx";
	/** 55. 反结账*/
	public static String URL_UN_BILL = HOST_URL + FINANCIAL + "fjz";
	/** 56. 现金券核销*/
	public static String URL_BILL_XJ_HX = HOST_URL + YY + "salebill";
	/** 49. 并单结账*/
	public static String URL_FINAL_PL_BILL = HOST_URL + FINANCIAL + "bdjz";
	/** 50. 反结账获取菜品详细详细 */
	public static String URL_FJZ_DISHES_DETAIL = HOST_URL + YY + "jzproductall";
	/** 51. 消息列表 */
	public static String URL_MSG_LIST = HOST_URL + YY + "padlsliushui";
	/** 52. 更改订单（营销经理、服务员、就餐人数）*/
	public static String URL_CHANGE_ORDER = HOST_URL + OTHER + "changeorder";
	/** 53. 获取PAD 临时点餐 菜品明细接口 */
	public static String URL_LS_PRO_ALL = HOST_URL  + "v2/" + YY + "lsproductall";
	/** 54. 删除PAD临时点餐已处理数据 接口 */
	public static String URL_LS_DELETEL = HOST_URL + YY + "padlsdelete";
	/** 55. 通知 POS 打印预结单  接口 */
	public static String URL_PRINT_YJD = HOST_URL + YY + "printyjd";
	public static Map<String, String> getCommRqstParams(Map<String, String> map) {
		if (map == null)
			map = new HashMap<String, String>();
		// TODO 添加公共参数
		if(!map.containsKey("shopid")) {
			map.put("shopid", PrefsConfig.ShopId);	
		}
		map.put("Key", PrefsConfig.key);

		return map;
	}
}
