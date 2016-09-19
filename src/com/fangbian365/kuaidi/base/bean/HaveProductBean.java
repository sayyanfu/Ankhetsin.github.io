package com.fangbian365.kuaidi.base.bean;

import java.io.Serializable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 加菜对象
 */
@Table(name = "haveproductbean")
public class HaveProductBean implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "id", isId = true)
	private int id;
	@Column(name = "pId")
	private String pId;
	@Column(name = "pym")
	private String pym;
	@Column(name = "dis")
	private String dis;
	@Column(name = "markStatus")
	private String markStatus = "";// 划菜
	@Column(name = "smallName")
	private String smallName;
	@Column(name = "startTime")
	private String startTime;
	@Column(name = "addPersonName")
	private String addPersonName;// 服务员名字
	@Column(name = "addPersonBh")
	private String addPersonBh;
	@Column(name = "shopId")
	private String shopId;
	@Column(name = "taiBh")
	private String taiBh;
	@Column(name = "tingBh")
	private String tingBh;
	@Column(name = "lsh")
	private String lsh;

	@Column(name = "code")
	private String code;
	@Column(name = "codeName")
	private String codeName;// 菜名名
	@Column(name = "orderState")
	private int orderState;// 下单状态(0未下单，1已下单)
	@Column(name = "cnt")
	private float cnt;// 菜品数量
	@Column(name = "units")
	private String units;// 菜品单位
	@Column(name = "price")
	private Double price;// 菜品单价
	@Column(name = "taste")
	private String taste;
	@Column(name = "disflag")
	private int disflag;// 是否参与折扣0-不参与 1参与
	@Column(name = "memberPrice")
	private Double memberPrice;
	@Column(name = "actPrice")
	private Double actPrice;
	@Column(name = "waitStatus")
	private String waitStatus;// 起叫状态
	@Column(name = "priceSrc")
	private Double priceSrc;
	@Column(name = "dishTotalPrice")
	private String dishTotalPrice;// 菜品小计
	@Column(name = "waiter")
	private String waiter;// 服务员
	@Column(name = "markTaste")
	private String markTaste;// 备注
	@Column(name = "status")
	private int status;// 1正常 2赠菜 3退菜
	@Column(name = "givedish")
	private String givedish;// 增菜
	@Column(name = "returnStatus")
	private int returnStatus;// 退菜
	@Column(name = "parentId")
	private String parentId;// 大类ID
	@Column(name = "typeId")
	private String typeId;// 小类ID
	@Column(name = "sub_type")
	private String sub_type;
	@Column(name = "special")
	private String special;
	@Column(name = "isPackage")
	private String isPackage;
	@Column(name = "mcode")   
	private String mcode;
	@Column(name = "discound")
	private String discound;// 折扣
	@Column(name = "viewType")
	private Integer viewType = 0;// 0菜品 1title
	@Column(name = "pinyin")
	private String  pinyin ;
	
	public HaveProductBean() {
	}

	public HaveProductBean(Canyin_Shop_Product product,
			Canyin_Shop_Diningtable table) {
		if (table != null) {
			this.tingBh = table.getFloorcode();
			this.taiBh = table.getCode();
			this.lsh = table.getLsh();
		}
		this.code = product.getProductcode();
		this.codeName = product.getProductname();
		this.orderState = 0;
		this.cnt = 1.0f;
		this.units = product.getUnits();
		this.price = product.getOriginalprice();
		this.dishTotalPrice = String.valueOf(product.getOriginalprice());
		this.waiter = "";
		this.markTaste = "";
		this.status = 1;
		this.returnStatus = 0;
		this.disflag = product.getDisflag();
		this.memberPrice = product.getMemberprice();
		this.actPrice = product.getCurrentprice();
		this.waitStatus = "即起";
		this.priceSrc = product.getOriginalprice();
		this.returnStatus = 0;
		this.discound = String.valueOf(product.getDiscound());
		this.pinyin = product.getPinyin();
		this.markStatus = "";
	}
	

	public HaveProductBean(TempDishBean dish,Canyin_Shop_Diningtable table) {
		this.tingBh = table.getFloorcode();
		this.taiBh = table.getCode();
		this.lsh = table.getLsh();
		
		this.code = dish.getProductCode();
		this.pym = dish.getPinyin();
		this.codeName = dish.getProductName();
		this.orderState = 0;
		this.cnt = 1.0f;
		this.units = dish.getUnits();
		this.price = Double.parseDouble(dish.getOriginalPrice());
		this.dishTotalPrice = dish.getOriginalPrice();
		this.waiter = "";
		this.markTaste = "";
		this.status = Integer.parseInt(dish.getStatus());
		this.returnStatus = 0;
		this.disflag = Integer.parseInt(dish.getDisFlag());
		this.memberPrice = Double.parseDouble(dish.getMemberPrice());
		this.actPrice = Double.parseDouble(dish.getCurrentPrice());
		this.waitStatus = "即起";
		this.priceSrc = Double.parseDouble(dish.getOriginalPrice());
		this.returnStatus = 0;
		this.parentId = dish.getParentId();
		this.typeId = dish.getTypeId();
		this.sub_type = dish.getSub_type();
		this.special = dish.getSpecial();
		this.isPackage = dish.getIsPackage();
		this.mcode = dish.getMcode();
		this.discound = String.valueOf(dish.getDiscound());
		this.markStatus = "";
	}
   
	public String getGivedish() {
		return givedish;
	}

	public void setGivedish(String givedish) {
		this.givedish = givedish;
	}

	public String getPym() {
		return pym;
	}

	public void setPym(String pym) {
		this.pym = pym;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getDis() {
		return dis;
	}

	public void setDis(String dis) {
		this.dis = dis;
	}

	public String getMarkStatus() {
		return markStatus;
	}

	public void setMarkStatus(String markStatus) {
		this.markStatus = markStatus;
	}

	public String getSmallName() {
		return smallName;
	}

	public void setSmallName(String smallName) {
		this.smallName = smallName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getAddPersonName() {
		return addPersonName;
	}

	public void setAddPersonName(String addPersonName) {
		this.addPersonName = addPersonName;
	}

	public String getAddPersonBh() {
		return addPersonBh;
	}

	public void setAddPersonBh(String addPersonBh) {
		this.addPersonBh = addPersonBh;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getTaiBh() {
		return taiBh;
	}

	public void setTaiBh(String taiBh) {
		this.taiBh = taiBh;
	}

	public String getTingBh() {
		return tingBh;
	}

	public void setTingBh(String tingBh) {
		this.tingBh = tingBh;
	}

	public String getLsh() {
		return lsh;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public float getCnt() {
		return cnt;
	}

	public void setCnt(float cnt) {
		this.cnt = cnt;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getTaste() {
		return taste;
	}

	public void setTaste(String taste) {
		this.taste = taste;
	}

	public int getDisflag() {
		return disflag;
	}

	public void setDisflag(int disflag) {
		this.disflag = disflag;
	}

	public Double getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(Double memberPrice) {
		this.memberPrice = memberPrice;
	}

	public Double getActPrice() {
		return actPrice;
	}

	public void setActPrice(Double actPrice) {
		this.actPrice = actPrice;
	}

	public String getWaitStatus() {
		return waitStatus;
	}

	public void setWaitStatus(String waitStatus) {
		this.waitStatus = waitStatus;
	}

	public Double getPriceSrc() {
		return priceSrc;
	}

	public void setPriceSrc(Double priceSrc) {
		this.priceSrc = priceSrc;
	}

	public String getDishTotalPrice() {
		return dishTotalPrice;
	}

	public void setDishTotalPrice(String dishTotalPrice) {
		this.dishTotalPrice = dishTotalPrice;
	}

	public String getWaiter() {
		return waiter;
	}

	public void setWaiter(String waiter) {
		this.waiter = waiter;
	}

	public String getMarkTaste() {
		return markTaste;
	}

	public void setMarkTaste(String markTaste) {
		this.markTaste = markTaste;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(int returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getSub_type() {
		return sub_type;
	}

	public void setSub_type(String sub_type) {
		this.sub_type = sub_type;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getIsPackage() {
		return isPackage;
	}

	public void setIsPackage(String isPackage) {
		this.isPackage = isPackage;
	}

	public String getMcode() {
		return mcode;
	}

	public void setMcode(String mcode) {
		this.mcode = mcode;
	}

	public String getDiscound() {
		return discound;
	}

	public void setDiscound(String discound) {
		this.discound = discound;
	}

	public Integer getViewType() {
		return viewType;
	}

	public void setViewType(Integer viewType) {
		this.viewType = viewType;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	@Override
	public Object clone() {
		HaveProductBean bean = null;
		try {
			bean = (HaveProductBean) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HaveProductBean) {
			HaveProductBean t = (HaveProductBean) obj;
			if (this.code.equals(t.getCode())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "HaveProductBean [id=" + id + ", pId=" + pId + ", pym=" + pym
				+ ", dis=" + dis + ", markStatus=" + markStatus
				+ ", smallName=" + smallName + ", startTime=" + startTime
				+ ", addPersonName=" + addPersonName + ", addPersonBh="
				+ addPersonBh + ", shopId=" + shopId + ", taiBh=" + taiBh
				+ ", tingBh=" + tingBh + ", lsh=" + lsh + ", code=" + code
				+ ", codeName=" + codeName + ", orderState=" + orderState
				+ ", cnt=" + cnt + ", units=" + units + ", price=" + price
				+ ", taste=" + taste + ", disflag=" + disflag
				+ ", memberPrice=" + memberPrice + ", actPrice=" + actPrice
				+ ", waitStatus=" + waitStatus + ", priceSrc=" + priceSrc
				+ ", dishTotalPrice=" + dishTotalPrice + ", waiter=" + waiter
				+ ", markTaste=" + markTaste + ", status=" + status
				+ ", givedish=" + givedish + ", returnStatus=" + returnStatus
				+ ", parentId=" + parentId + ", typeId=" + typeId
				+ ", sub_type=" + sub_type + ", special=" + special
				+ ", isPackage=" + isPackage + ", mcode=" + mcode
				+ ", discound=" + discound + ", viewType=" + viewType + "]";
	}

}