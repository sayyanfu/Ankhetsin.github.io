package com.fangbian365.kuaidi.mod.asynctask;

import java.util.HashMap;
import java.util.Map;

import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.PrefsConfig;
import com.fangbian365.kuaidi.base.utils.SysInfo;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.base.utils.UrlUtil;
import com.fangbian365.kuaidi.constans.Constans;

/**
 * 网络请求接口请添加在该类 所有网络请求的唯一入口
 * 
 * @author lruijun
 * 
 */

public class CHBRqstApi {
	public static final String TAG = "GeoTravelApi";

	private static CHBRqstApi _instance;

	private CHBRqstApi() {
	}

	public static synchronized CHBRqstApi getInstance() {
		if (_instance == null) {
			_instance = new CHBRqstApi();
		}
		return _instance;
	}

	/**
	 * 登录
	 * 
	 * @param shopid
	 * @param key
	 * @param worknum
	 * @param pwd
	 * @return
	 */
	public CHBHttpTask doLogin(String worknum, String pwd) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("worknum", worknum);
		params.put("pwd", pwd);
		params.put("shopid", PrefsConfig.ShopId);
		return new CHBHttpTask(UrlUtil.URL_LOGIN, params);
	}

	/**
	 * 厅、楼层同步
	 * 
	 * @param shopid
	 * @param key
	 * @return
	 */
	public CHBHttpTask syncHall() {
		return new CHBHttpTask(UrlUtil.URL_HALL_SYNC, null);
	}

	/**
	 * 台位同步
	 * 
	 * @param shopid
	 * @param key
	 * @param tingbh
	 * @param taibh
	 * @return
	 */
	public CHBHttpTask syncTable(String tingbh, String taibh) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tingbh", tingbh);
		params.put("taibh", taibh);
		return new CHBHttpTask(UrlUtil.URL_TABLE_SYNC, params);
	}

	/**
	 * 菜品分类同步
	 * 
	 * @param shopid
	 * @param key
	 * @return
	 */
	public CHBHttpTask categoryDishes() {
		return new CHBHttpTask(UrlUtil.URL_DISHES_CATEGORY, null);
	}

	/**
	 * 菜品同步
	 * 
	 * @param shopid
	 * @param key
	 * @return
	 */
	public CHBHttpTask getDishes() {
		return new CHBHttpTask(UrlUtil.URL_DISHES, null);
	}

	/**
	 * 口味同步
	 * 
	 * @param shopid
	 * @param key
	 * @return
	 */
	public CHBHttpTask syncTaste() {
		Map<String, String> params = new HashMap<String, String>();
		return new CHBHttpTask(UrlUtil.URL_TASTE_SYNC, params);
	}

	/**
	 * 退菜说明同步
	 * 
	 * @param shopid
	 * @param key
	 * @return
	 */
	public CHBHttpTask backFood() {
		Map<String, String> params = new HashMap<String, String>();
		return new CHBHttpTask(UrlUtil.URL_FOODBACK_SYNC, params);
	}

	/**
	 * 赠送原因同步
	 * 
	 * @param shopid
	 * @param key
	 * @return
	 */
	public CHBHttpTask giveReason() {
		Map<String, String> params = new HashMap<String, String>();
		return new CHBHttpTask(UrlUtil.URL_GIVING_REASON, params);
	}

	/**
	 * 部门同步
	 * 
	 * @param shopid
	 * @param key
	 * @return
	 */
	public CHBHttpTask syncDepartment() {
		Map<String, String> params = new HashMap<String, String>();
		return new CHBHttpTask(UrlUtil.URL_DEPARTMENT, params);
	}

	/**
	 * 用户
	 * 
	 * @param shopid
	 * @param key
	 * @return
	 */
	public CHBHttpTask getUserInfo() {
		Map<String, String> params = new HashMap<String, String>();
		return new CHBHttpTask(UrlUtil.URL_USER, params);
	}

	/**
	 * 开台、宴会开台
	 * 
	 * @param shopid
	 * @param key
	 * @param data
	 * @param yhno
	 * @param opercode
	 * @param opername
	 * @return
	 */
	public CHBHttpTask openTable(String data, String yhno) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", data);
		params.put("yhno", yhno);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		
		int s = SysWorkTools.randomNum();
		params.put("rnd", s+"");
		
		
		FrameLog.e("随机数----------",s + "" );
		return new CHBHttpTask(UrlUtil.URL_OPEN_TABLE, params);
	}

	/**
	 * 换台
	 * 
	 * @param shopid
	 * @param key
	 * @param data
	 * @param opercode
	 * @param opername
	 * @return
	 */
	public CHBHttpTask changeTable(String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("source", Constans.SOURCE_FROM);
		return new CHBHttpTask(UrlUtil.URL_CHANGE_TABLE, params);
	}

	/**
	 * 关台
	 * 
	 * @param shopid
	 * @param key
	 * @param data
	 * @param opercode
	 * @param opername
	 * @return
	 */
	public CHBHttpTask closeTable(String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_CLOSE_TABLE, params);
	}

	/**
	 * 启用 、禁用、清台
	 * 
	 * @param shopid
	 * @param key
	 * @param data
	 * @param opercode
	 * @param opername
	 * @return
	 */
	public CHBHttpTask changeTableState(String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_CHANGE_STATE_TABLE, params);
	}

	/**
	 * 点菜
	 * 
	 * @param shopid
	 * @param key
	 * @param tingbh
	 * @param taibh
	 * @param ktlsh
	 * @param data
	 * @param opercode
	 * @param opername
	 * @return
	 */
	public CHBHttpTask orderDishes(String tingbh, String taibh, String ktlsh,
			String taste, String data) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("tingbh", tingbh);
		params.put("taibh", taibh);
		params.put("ktlsh", ktlsh);
		params.put("taste", taste);
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("source", Constans.SOURCE_FROM);
		return new CHBHttpTask(UrlUtil.URL_ORDER_DISHES, params);
	}

	/**
	 * 宴会点菜
	 * 
	 * @param yhdata
	 * @param data
	 * @return
	 */
	public CHBHttpTask orderYHDishes(String yhdata, String data) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("yhdata", yhdata);
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_YH_ORDER_DISHES, params);
	}

	/**
	 * 退菜
	 * 
	 * @param tingbh
	 *            厅编号
	 * @param taibh
	 *            台编号
	 * @param ktlsh
	 *            开台流水号
	 * @param data
	 *            数据
	 * @param opercode
	 *            操作员编码
	 * @param opername
	 *            操作员名字
	 * @return
	 */
	public CHBHttpTask returnDishes(String tingbh, String taibh, String ktlsh,
			String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tingbh", tingbh);
		params.put("taibh", taibh);
		params.put("ktlsh", ktlsh);
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("source", Constans.SOURCE_FROM);
		return new CHBHttpTask(UrlUtil.URL_RETURN_DISHES, params);
	}

	/**
	 * 增菜
	 * 
	 * @param shopid
	 * @param key
	 * @param tingbh
	 * @param taibh
	 * @param ktlsh
	 * @param data
	 * @param opercode
	 * @param opername
	 * @return
	 */
	public CHBHttpTask giveDishes(String tingbh, String taibh, String ktlsh,
			String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tingbh", tingbh);
		params.put("taibh", taibh);
		params.put("ktlsh", ktlsh);
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_GIVING_DISHES, params);
	}

	/**
	 * 取消增菜
	 * 
	 * @param shopid
	 * @param key
	 * @param tingbh
	 * @param taibh
	 * @param ktlsh
	 * @param data
	 * @param opercode
	 * @param opername
	 * @return
	 */
	public CHBHttpTask cancelGivingDishes(String tingbh, String taibh,
			String ktlsh, String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tingbh", tingbh);
		params.put("taibh", taibh);
		params.put("ktlsh", ktlsh);
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_CANCEL_GIVING_DISHES, params);
	}

	/**
	 * 更改菜品数量
	 * 
	 * @param shopid
	 * @param key
	 * @param tingbh
	 * @param taibh
	 * @param ktlsh
	 * @param data
	 * @param opercode
	 * @param opername
	 * @return
	 */
	public CHBHttpTask changeDishesCount(String tingbh, String taibh,
			String ktlsh, String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tingbh", tingbh);
		params.put("taibh", taibh);
		params.put("ktlsh", ktlsh);
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_CHANGE_DISHES_NUMBER, params);
	}

	/**
	 * 更改菜口价格
	 * 
	 * @param shopid
	 * @param key
	 * @param tingbh
	 * @param taibh
	 * @param ktlsh
	 * @param data
	 * @param opercode
	 * @param opername
	 * @return
	 */
	public CHBHttpTask changeDishesPrice(String tingbh, String taibh,
			String ktlsh, String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tingbh", tingbh);
		params.put("taibh", taibh);
		params.put("ktlsh", ktlsh);
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_CHANGE_DISHES_PRICE, params);
	}

	/**
	 * 获取菜品明细
	 * 
	 * @param tingbh
	 *            厅编号
	 * @param taibh
	 *            台编号
	 * @param ktlsh
	 *            开台流水号
	 * @param opercode
	 *            操作员编号
	 * @param opername
	 *            操作员姓名
	 * @return
	 */
	public CHBHttpTask getDishesDetail(String tingbh, String taibh, String ktlsh) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tingbh", tingbh);
		params.put("taibh", taibh);
		params.put("ktlsh", ktlsh);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_DISHES_DETAIL, params);
	}
	
	/**
	 * 反结账菜品详细信息查询
	 * @param tingbh
	 * @param taibh
	 * @param ktlsh
	 * @return
	 */
	public CHBHttpTask getJzProductAll(String tingbh, String taibh, String ktlsh) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tingbh", tingbh);
		params.put("taibh", taibh);
		params.put("ktlsh", ktlsh);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_FJZ_DISHES_DETAIL, params);
	}

	/**
	 * 会员查询
	 * 
	 * @param shopid
	 * @param key
	 * @param data
	 * @return
	 */
	public CHBHttpTask searchMember(String type, String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", type);
		params.put("data", data);
		return new CHBHttpTask(UrlUtil.URL_SEARCH_MEMBER, params);
	}

	/**
	 * 会员折扣查询
	 * 
	 * @param shopid
	 * @param custerno
	 * @param custtype
	 * @return
	 */
	public CHBHttpTask searchMemberDiscount(String custerno, String custtype) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("custerno", custerno);
		params.put("custtype", custtype);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_SEARCH_MEMBER_DISCOUNT, params);
	}

	/**
	 * 网络已付款查询
	 * 
	 * @param shopid
	 * @param ktlsh
	 * @return
	 */
	public CHBHttpTask searchAlreadyPay(String ktlsh) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ktlsh", ktlsh);
		return new CHBHttpTask(UrlUtil.URL_SEARCH_ALREADY_PAY, params);
	}

	/**
	 * 台位结账
	 * 
	 * @param shopid
	 * @param ktlsh
	 * @param distype
	 * @param prodata
	 * @param paydata
	 * @param memberdata
	 * @param accdata
	 * @param automlmoney
	 * @param sdmlmoney
	 * @param mlperson
	 * @param dismoney
	 * @param disperson
	 * @param opercode
	 * @param opername
	 * @return
	 */
	public CHBHttpTask payTable(String ktlsh, String distype, String prodata,
			String paydata, String memberdata, String accdata,
			String automlmoney, String sdmlmoney, String mlperson,
			String dismoney, String disperson) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ktlsh", ktlsh);
		params.put("distype", distype);
		params.put("prodata", prodata);
		params.put("paydata", paydata);
		params.put("memberdata", memberdata);
		params.put("accdata", accdata);
		params.put("automlmoney", automlmoney);
		params.put("sdmlmoney", sdmlmoney);
		params.put("mlperson", mlperson);
		params.put("dismoney", dismoney);
		params.put("disperson", disperson);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_PAY_TABLE, params);
	}

	/**
	 * 反结账
	 * 
	 * @param shopid
	 * @param opercode
	 * @param opername
	 * @param lsh
	 * @return
	 */
	public CHBHttpTask antiPay(String lsh) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("lsh", lsh);
		return new CHBHttpTask(UrlUtil.URL_ANTI_PAY, params);
	}

	/**
	 * 交班接口
	 * 
	 * @param shopid
	 * @param opercode
	 * @param opername
	 * @param jbsj
	 * @return
	 */
	public CHBHttpTask shiftExchange(String jbsj) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("jbsj", jbsj);
		return new CHBHttpTask(UrlUtil.URL_SHIFT_EXCHANGE, params);
	}

	/**
	 * 当班已结账查询
	 * 
	 * @param shopid
	 * @param opercode
	 * @param opername
	 * @param jbsj
	 * @return
	 */
	public CHBHttpTask currentSearchAlreadyPay(String jbsj) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("sbsj", jbsj);
		return new CHBHttpTask(UrlUtil.URL_CURRENT_SEARCH_ALREADY_PAY, params);
	}

	/**
	 * 交班报表数据
	 * 
	 * @param shopid
	 * @param opercode
	 * @param opername
	 * @param jbsj
	 * @return
	 */
	public CHBHttpTask shiftExchangeReporting(String jbsj) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("sbsj", jbsj);
		return new CHBHttpTask(UrlUtil.URL_SHIFT_EXCHANGE_REPORTING, params);
	}

	/**
	 * 更换服务员
	 * 
	 * @param shopid
	 * @param ktslh
	 * @param opercode
	 * @param opername
	 * @param waitercode
	 * @param waitername
	 * @return
	 */
	public CHBHttpTask changeWaiter(String ktslh, String waitercode,
			String waitername) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ktslh", ktslh);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("waitercode", waitercode);
		params.put("waitername", waitername);
		return new CHBHttpTask(UrlUtil.URL_CHANGE_WAITER, params);
	}

	/**
	 * 更换经理
	 * 
	 * @param shopid
	 * @param ktslh
	 * @param opercode
	 * @param opername
	 * @param yxjlcode
	 * @param yxjlname
	 * @return
	 */
	public CHBHttpTask changeManager(String ktslh, String yxjlcode,
			String yxjlname) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ktslh", ktslh);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("yxjlcode", yxjlcode);
		params.put("yxjlname", yxjlname);
		return new CHBHttpTask(UrlUtil.URL_CHANGE_MANAGER, params);
	}

	/**
	 * 更改就餐人数
	 * 
	 * @param shopid
	 * @param opercode
	 * @param opername
	 * @param ktlsh
	 * @param oldcnt
	 * @param newcnt
	 * @return
	 */
	public CHBHttpTask changeEaterNumber(String ktlsh, String oldcnt,
			String newcnt) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("ktlsh", ktlsh);
		params.put("oldcnt", oldcnt);
		params.put("newcnt", newcnt);
		return new CHBHttpTask(UrlUtil.URL_CHANGE_EATER_NUMBER, params);
	}

	/**
	 * 获取指定流水号上的菜品信息
	 * 
	 * @param shopid
	 * @param opercode
	 * @param opername
	 * @param lsh
	 * @param key
	 * @return
	 */
	public CHBHttpTask getSerialDishes(String lsh) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("lsh", lsh);
		return new CHBHttpTask(UrlUtil.URL_GET_SERIAL_DISHES, params);
	}

	/**
	 * 更新软件
	 * 
	 * @return
	 */
	public CHBHttpTask getNewApk() {
		return new CHBHttpTask(UrlUtil.URL_APP_UPDATE, null);
	}

	/**
	 * 添加临时菜品
	 * 
	 * @param parentId
	 *            大类
	 * @param typeId
	 *            小类
	 * @param productName
	 *            菜品名称
	 * @param special
	 *            销售方式
	 * @param price
	 *            价格
	 * @param units
	 *            份量单位
	 * @param norms
	 *            规格
	 * @return
	 */
	public CHBHttpTask addTempDish(String parentId, String typeId,
			String productName, String special, String price, String units,
			String norms) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("parentId", parentId);
		params.put("typeId", typeId);
		params.put("productName", productName);
		params.put("special", special);
		params.put("price", price);
		params.put("units", units);
		params.put("norms", norms);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_ADD_TEMP_DISH, params);
	}

	/**
	 * 转单（转菜）
	 * 
	 * @param oldtingbh
	 *            厅编号
	 * @param oldtaibh
	 *            台编号
	 * @param oldlsh
	 *            转出台位流水号
	 * @param newtingbh
	 *            转入台位所属楼层编号
	 * @param newtaibh
	 *            转入台位号
	 * @param newlsh
	 *            转入台位流水号
	 * @param data
	 *            数据
	 * @param opercode
	 *            操作员编号
	 * @param opername
	 *            操作员姓名
	 * @return
	 */
	public CHBHttpTask turnOrderDish(String oldtingbh, String oldtaibh,
			String oldlsh, String newtingbh, String newtaibh, String newlsh,
			String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("oldtingbh", oldtingbh);
		params.put("oldtaibh", oldtaibh);
		params.put("oldlsh", oldlsh);
		params.put("newtingbh", newtingbh);
		params.put("newtaibh", newtaibh);
		params.put("newlsh", newlsh);
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("source", Constans.SOURCE_FROM);
		return new CHBHttpTask(UrlUtil.URL_TURN_ORDER, params);
	}

	/**
	 * 更改菜品状态
	 * 
	 * @param productbh
	 *            菜品编号
	 * @param status
	 *            菜品状态 （-2删除，-1下线，0正常,1临时菜品，2估清标志,3停用）
	 * @param opercode
	 *            操作员编号
	 * @param opername
	 *            操作员名称
	 * @return
	 */
	public CHBHttpTask setProduct(String productbh, String status) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("productbh", productbh);
		params.put("status", status);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_SET_PRODUCT, params);
	}

	/**
	 * 更改制作状态信息
	 * 
	 * @param data
	 *            数据
	 * @param opercode
	 *            操作员编号
	 * @param opername
	 *            操作员姓名
	 * @return
	 */
	public CHBHttpTask changeMarkStatus(String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_CHANGE_MAKE_STATUS, params);
	}

	/**
	 * 更改起菜状态信息
	 * 
	 * @param data
	 *            数据
	 * @param opercode
	 *            操作员编号
	 * @param opername
	 *            操作员姓名
	 * @return
	 */
	public CHBHttpTask changeWaitStatus(String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_CHANGE_WAIT_STATUS, params);
	}

	/**
	 * 市别(餐段)
	 * 
	 * @return
	 */
	public CHBHttpTask getMealPeriod() {
		Map<String, String> params = new HashMap<String, String>();
		return new CHBHttpTask(UrlUtil.URL_MEAL_PERIOD, params);
	}

	/**
	 * 批量获取菜品明细
	 * 
	 * @param jLiushui
	 * @return
	 */
	public CHBHttpTask getBatchDishesDetail(String jLiushui) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", jLiushui);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_BATCH_DISHES_DETAIL, params);
	}
	
	/**
	 * 获取员工最高折扣率
	 * @param uName
	 * @param uPwd
	 * @return
	 */
	public CHBHttpTask getNewDiscount(String uName, String uPwd){
		Map<String, String> params = new HashMap<String, String>();
		params.put("worknum", uName);
		params.put("pwdKey", uPwd);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_WORKER_DISCOUNT, params);
	}

	/**
	 * 获取员工最大抹零
	 * @param uName
	 * @param uPwd
	 * @return
	 */
	public CHBHttpTask getWorkerml(String uName, String uPwd) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("worknum", uName);
		params.put("pwdKey", uPwd);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_WORKER_MOLING, params);
	}
	
	/**
	 * 获取核销金额
	 * @param vouchersNum
	 * @return
	 */
	public CHBHttpTask getVouchersMoney(String lsh, String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ktlsh", lsh);
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_BILL_XJ_HX, params);
	}
	
	/**
	 * 版本
	 * 
	 * @param edition
	 * @return
	 */
	public CHBHttpTask getEdition(String edition) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("edition", edition);
		return new CHBHttpTask(UrlUtil.URL_MEAL_PERIOD, params);
	}
	
	/**
	 * 交班报表数据接口
	 * @param opercode
	 * @param opername
	 * @return
	 */
	public CHBHttpTask getShiftExchangeData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_SHIFT_EXCHANGE_REPORTING, params);
	}

	/**
	 * 台位结账
	 * @param lsh
	 * @param distype
	 * @param prodata
	 * @param paydata
	 * @param memberdata
	 * @param accdata
	 * @param automlmoney
	 * @param sdmlmoney
	 * @param mlperson
	 * @param dismoney
	 * @param disperson
	 * @return
	 */
	public CHBHttpTask getFinalBill(String lsh, String distype, String prodata,
			String paydata, String memberdata, String accdata,
			String automlmoney, String sdmlmoney, String mlperson,
			String dismoney, String disperson) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("ktlsh", lsh);
		params.put("distype", distype);
		params.put("prodata", prodata);
		params.put("paydata", paydata);
		params.put("memberdata", memberdata);
		params.put("accdata", accdata);
		params.put("automlmoney", automlmoney);
		params.put("sdmlmoney", sdmlmoney);
		params.put("mlperson", mlperson);
		params.put("dismoney", dismoney);
		params.put("disperson", disperson);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_FINAL_BILL, params);
	}
	
	/**
	 * 台位并单结账
	 * @param lsh
	 * @param distype
	 * @param prodata
	 * @param paydata
	 * @param memberdata
	 * @param accdata
	 * @param automlmoney
	 * @param sdmlmoney
	 * @param mlperson
	 * @param dismoney
	 * @param disperson
	 * @return
	 */
	public CHBHttpTask getBatchFinalBill(String lsh, String distype, String prodata,
			String paydata, String memberdata, String accdata,
			String automlmoney, String sdmlmoney, String mlperson,
			String dismoney, String disperson) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("ktlsh", lsh);
		params.put("distype", distype);
		params.put("prodata", prodata);
		params.put("paydata", paydata);
		params.put("memberdata", memberdata);
		params.put("accdata", accdata);
		params.put("automlmoney", automlmoney);
		params.put("sdmlmoney", sdmlmoney);
		params.put("mlperson", mlperson);
		params.put("dismoney", dismoney);
		params.put("disperson", disperson);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_FINAL_PL_BILL, params);
	}
	
	/**
	 * 升级
	 * @return
	 */
	public CHBHttpTask getUpdate(String edition,String name, String url, String mClass, String mByte, String Explain){
		Map<String, String> params = new HashMap<String, String>();
		params.put("edition", edition);
		params.put("name", name);
		params.put("url", url);
		params.put("Class", mClass);
		params.put("Byte", mByte);
		params.put("Explain", Explain);
		return new CHBHttpTask(UrlUtil.URL_APP_UPDATE, params);
		
	}
	
	public CHBHttpTask getEdition(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("editType", "1");
		params.put("edition", String.valueOf(SysInfo.VERSION_CODE));
		return new CHBHttpTask(UrlUtil.URL_EDITION, params);
		
	}
	
	/**
	 * 催菜
	 * @param ktlsh
	 * @param tingbh
	 * @param taibh
	 * @param ccType
	 * @param data
	 * @return
	 */
	public CHBHttpTask remindDishes(String ktlsh, String tingbh, String taibh, String ccType, String data){
		Map<String, String> params = new HashMap<String, String>();
		params.put("ktlsh", ktlsh);
		params.put("tingbh", tingbh);
		params.put("taibh", taibh);
		params.put("ccType", ccType);
		params.put("data", data);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("source", Constans.SOURCE_FROM);
		return new CHBHttpTask(UrlUtil.URL_REMIND_DISH, params);
	}
	
	/**
	 * 商家验证接口
	 * @param shopid
	 * @param shopName
	 * @param mobile
	 * @param activationCode
	 * @return
	 */
	public CHBHttpTask businessVerifi(String shopid, String shopName, String mobile, String activationCode){
		Map<String, String> params = new HashMap<String, String>();
		params.put("shopid", shopid);
		params.put("shopName", shopName);
		params.put("mobile", mobile);
		params.put("activationCode", activationCode);
		return new CHBHttpTask(UrlUtil.URL_BUSINESS_VERIFI, params);
	}
	
	/**
	 * 商家详情
	 * @return
	 */
	public CHBHttpTask businessDetail(){
		return new CHBHttpTask(UrlUtil.URL_BUSINESS_DETAIL, null);
	}
	
	/**
	 * 外卖开台并点菜
	 * @param tingbh
	 * @param taibh
	 * @param data
	 * @return
	 */
	public CHBHttpTask orderWMDish(String tingbh, String taibh, String lsh, String taste, String data) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tingbh", tingbh);
		params.put("taibh", taibh);
		params.put("ktlsh", lsh);
		params.put("personCnt", "1");
		params.put("data", data);
		params.put("taste", taste);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("source", Constans.SOURCE_FROM);
		return new CHBHttpTask(UrlUtil.URL_WM_KTDC, params);
	}
	
	/**
	 * 反结账账单获取
	 * @return
	 */
	public CHBHttpTask getUnBillList() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		return new CHBHttpTask(UrlUtil.URL_UN_BILL_LIST, params);
	}
	
	/**
	 * 反结账
	 * @param lsh
	 * @return
	 */
	public CHBHttpTask getUnBill(String lsh) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("lsh", lsh);
		
		return new CHBHttpTask(UrlUtil.URL_UN_BILL, params);
	}
	
	
	public CHBHttpTask getMsgList() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		
		return new CHBHttpTask(UrlUtil.URL_MSG_LIST, params);
	}
	/**
	 * 更改订单
	 * @param ktlsh
	 * @param oldcnt
	 * @param newcnt
	 * @return
	 */
	public CHBHttpTask changeOrder(String ktlsh, String oldcnt, String newcnt, String waitercode){
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("ktlsh", ktlsh); 
		params.put("oldcnt", oldcnt);
		params.put("newcnt", newcnt);
		params.put("waitercode", waitercode);
		params.put("waitername", "");
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		params.put("yxjlcode", "");
		params.put("yxjlname", "");
		return new CHBHttpTask(UrlUtil.URL_CHANGE_ORDER, params);
	}
	
	/**
	 * 
	 * @param lslsh
	 * @return
	 */
	public CHBHttpTask getLsProductAll(String lslsh) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("lslsh", lslsh);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		
		return new CHBHttpTask(UrlUtil.URL_LS_PRO_ALL, params);
	}
	
	/**
	 * 
	 * @param lslsh
	 * @return
	 */
	public CHBHttpTask deleteLSPad(String lslsh) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("lslsh", lslsh);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		
		return new CHBHttpTask(UrlUtil.URL_LS_DELETEL, params);
	}
	
	/**
	 * 
	 * @param lslsh
	 * @return
	 */
	public CHBHttpTask printYJD(String lsh) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("lsh", lsh);
		params.put("opercode", PrefsConfig.workerNum);
		params.put("opername", PrefsConfig.workerName);
		
		return new CHBHttpTask(UrlUtil.URL_PRINT_YJD, params);
	}
	
	
}
