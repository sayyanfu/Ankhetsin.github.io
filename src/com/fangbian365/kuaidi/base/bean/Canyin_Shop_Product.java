/**
 * 菜品
 */

package com.fangbian365.kuaidi.base.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "canyin_shop_product")
public class Canyin_Shop_Product {
	@Column(name = "productId")
	private String productId;
	@Column(name = "parentId")
	private String parentId;
	@Column(name = "typeId")
	private String typeId;
	@Column(name = "shopId")
	private String shopId;
	@Column(name = "productCode")
	private String productCode;// 菜品编号
	@Column(name = "productName")
	private String productName;
	@Column(name = "special")
	private Integer special;
	@Column(name = "isPackage")
	private Integer isPackage;
	@Column(name = "packagePersons")
	private Integer packagePersons;
	@Column(name = "mcode")
	private String mcode;
	@Column(name = "pic")
	private String pic;
	@Column(name = "originalPrice")
	private Double originalPrice;// 原始价格
	@Column(name = "memberPrice")
	private Double memberPrice;// 会员价
	@Column(name = "units")
	private String units;
	@Column(name = "norms")
	private String norms;
	@Column(name = "disFlag")
	// 打折标识 1允许打折 0 不允许打折
	private Integer disFlag;
	@Column(name = "isSale")
	private Integer isSale;// 1堂食 2外卖
	@Column(name = "discound")
	private Double discound;
	@Column(name = "currentPrice")
	private Double currentPrice;// 活动价 现价
	@Column(name = "remark")
	private String remark;
	@Column(name = "intro")
	private String intro;
	@Column(name = "createTime")
	private Integer createTime;
	@Column(name = "pinyin")
	private String pinyin;
	@Column(name = "sold")
	private Integer sold;
	@Column(name = "praise")
	private Integer praise;
	@Column(name = "status")
	private Integer status;// '-2删除，-1下线，0正常,1临时菜品，2估清标志,3停用'
	@Column(name = "sub_type")
	private Integer sub_type;
	private int type = 1;// 0为临时菜,1为正常菜，2为添加后的临时菜

	public Canyin_Shop_Product() {
	}

	public Canyin_Shop_Product(HaveProductBean hBean) {

		this.parentId = hBean.getParentId();
		this.typeId = hBean.getTypeId();
		this.productCode = hBean.getCode();
		this.productName = hBean.getCodeName();
		this.special = Integer.parseInt(hBean.getSpecial());
		this.isPackage = Integer.parseInt(hBean.getIsPackage());
		this.mcode = hBean.getMcode();
		this.originalPrice = hBean.getPriceSrc();
		this.memberPrice = hBean.getMemberPrice();
		this.units = hBean.getUnits();
		this.disFlag = hBean.getDisflag();
		this.isSale = 1;
		this.discound = Double.parseDouble(hBean.getDiscound());
		this.currentPrice = hBean.getActPrice();
		this.remark = hBean.getMarkTaste();
		this.intro = "";
		this.createTime = 0;
		this.pinyin = hBean.getPym();
		this.sold = 0;
		this.praise = 0;
		this.status = 0;
		this.sub_type = 0;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	// productId
	public void setProductid(String productid) {
		this.productId = productid;
	}

	public String getProductid() {
		return productId;
	}

	// parentId
	public void setParentid(String parentid) {
		this.parentId = parentid;
	}

	public String getParentid() {
		return parentId;
	}

	// typeId
	public void setTypeid(String typeid) {
		this.typeId = typeid;
	}

	public String getTypeid() {
		return typeId;
	}

	// shopId
	public void setShopid(String shopid) {
		this.shopId = shopid;
	}

	public String getShopid() {
		return shopId;
	}

	// productCode
	public void setProductcode(String productcode) {
		this.productCode = productcode;
	}

	public String getProductcode() {
		return productCode;
	}

	// productName
	public void setProductname(String productname) {
		this.productName = productname;
	}

	public String getProductname() {
		return productName;
	}

	// special
	public void setSpecial(Integer special) {
		this.special = special;
	}

	public Integer getSpecial() {
		return special;
	}

	// isPackage
	public void setIspackage(Integer ispackage) {
		this.isPackage = ispackage;
	}

	public Integer getIspackage() {
		return isPackage;
	}

	// packagePersons
	public void setPackagepersons(Integer packagepersons) {
		this.packagePersons = packagepersons;
	}

	public Integer getPackagepersons() {
		return packagePersons;
	}

	// mcode
	public void setMcode(String mcode) {
		this.mcode = mcode;
	}

	public String getMcode() {
		return mcode;
	}

	// pic
	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPic() {
		return pic;
	}

	// originalPrice
	public void setOriginalprice(Double originalprice) {
		this.originalPrice = originalprice;
	}

	public Double getOriginalprice() {
		return originalPrice;
	}

	// memberPrice
	public void setMemberprice(Double memberprice) {
		this.memberPrice = memberprice;
	}

	public Double getMemberprice() {
		return memberPrice;
	}

	// units
	public void setUnits(String units) {
		this.units = units;
	}

	public String getUnits() {
		return units;
	}

	// norms
	public void setNorms(String norms) {
		this.norms = norms;
	}

	public String getNorms() {
		return norms;
	}

	// disFlag
	public void setDisflag(Integer disflag) {
		this.disFlag = disflag;
	}

	public Integer getDisflag() {
		return disFlag;
	}

	// isSale
	public void setIssale(Integer issale) {
		this.isSale = issale;
	}

	public Integer getIssale() {
		return isSale;
	}

	// discound
	public void setDiscound(Double discound) {
		this.discound = discound;
	}

	public Double getDiscound() {
		return discound;
	}

	// currentPrice
	public void setCurrentprice(Double currentprice) {
		this.currentPrice = currentprice;
	}

	public Double getCurrentprice() {
		return currentPrice;
	}

	// remark
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	// intro
	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getIntro() {
		return intro;
	}

	// createTime
	public void setCreatetime(Integer createtime) {
		this.createTime = createtime;
	}

	public Integer getCreatetime() {
		return createTime;
	}

	// pinyin
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getPinyin() {
		return pinyin;
	}

	// sold
	public void setSold(Integer sold) {
		this.sold = sold;
	}

	public Integer getSold() {
		return sold;
	}

	// praise
	public void setPraise(Integer praise) {
		this.praise = praise;
	}

	public Integer getPraise() {
		return praise;
	}

	// status
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	// sub_type
	public void setSub_Type(Integer sub_type) {
		this.sub_type = sub_type;
	}

	public Integer getSub_Type() {
		return sub_type;
	}

	@Override
	public String toString() {
		return "Canyin_Shop_Product [productId=" + productId + ", parentId="
				+ parentId + ", typeId=" + typeId + ", shopId=" + shopId
				+ ", productCode=" + productCode + ", productName="
				+ productName + ", special=" + special + ", isPackage="
				+ isPackage + ", packagePersons=" + packagePersons + ", mcode="
				+ mcode + ", pic=" + pic + ", originalPrice=" + originalPrice
				+ ", memberPrice=" + memberPrice + ", units=" + units
				+ ", norms=" + norms + ", disFlag=" + disFlag + ", isSale="
				+ isSale + ", discound=" + discound + ", currentPrice="
				+ currentPrice + ", remark=" + remark + ", intro=" + intro
				+ ", createTime=" + createTime + ", pinyin=" + pinyin
				+ ", sold=" + sold + ", praise=" + praise + ", status="
				+ status + ", sub_type=" + sub_type + "]";
	}

}