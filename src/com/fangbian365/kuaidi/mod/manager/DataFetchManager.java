package com.fangbian365.kuaidi.mod.manager;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fangbian365.kuaidi.base.CHBApp;
import com.fangbian365.kuaidi.base.bean.BusinessDetail;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Diningtable;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Floor;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Mealstime;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Product;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Product_Type;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Taste;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Tcreson;
import com.fangbian365.kuaidi.base.bean.Canyin_Shop_Zsreson;
import com.fangbian365.kuaidi.base.bean.DownloadItem;
import com.fangbian365.kuaidi.base.bean.FenChuPrinter;
import com.fangbian365.kuaidi.base.bean.HandOverBean;
import com.fangbian365.kuaidi.base.bean.HaveProductBean;
import com.fangbian365.kuaidi.base.bean.HuanTaiResult;
import com.fangbian365.kuaidi.base.bean.MsgBean;
import com.fangbian365.kuaidi.base.bean.MsgProBean;
import com.fangbian365.kuaidi.base.bean.PaymentType;
import com.fangbian365.kuaidi.base.bean.ProductType;
import com.fangbian365.kuaidi.base.bean.ReminderBean;
import com.fangbian365.kuaidi.base.bean.TempDishBean;
import com.fangbian365.kuaidi.base.bean.TingSale;
import com.fangbian365.kuaidi.base.bean.TuiFoodResult;
import com.fangbian365.kuaidi.base.bean.UserInfo;
import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.CHBThreadPool;
import com.fangbian365.kuaidi.base.utils.SysWorkTools;
import com.fangbian365.kuaidi.base.utils.CHBThreadPool.JobType;
import com.fangbian365.kuaidi.mod.asynctask.CHBHttpTask;
import com.fangbian365.kuaidi.mod.asynctask.CHBRqstApi;
import com.fangbian365.kuaidi.mod.asynctask.http.BaseTask;
import com.fangbian365.kuaidi.mod.asynctask.http.FetchListener;
import com.fangbian365.kuaidi.mod.asynctask.http.TaskResult;
import com.fangbian365.kuaidi.mod.asynctask.http.TaskResultStatus;
import com.fangbian365.kuaidi.mod.asynctask.http.TaskSimpleListener;

public class DataFetchManager {
	private static final String TAG = "DataFetchManager";

	private static DataFetchManager _instance;
	private DbManager dbManager;
//	public CHBHttpTask mCurHttpTask;

	private DataFetchManager() {
		dbManager = CHBApp.getInstance().getDbManager();
	}

	public static DataFetchManager getInstance() {
		if (_instance == null)
			_instance = new DataFetchManager();
		return _instance;
	}

	
	/**
	 * 登录
	 * 
	 * @param shopid
	 * @param worknum
	 * @param pwd
	 */
	public void fetchLogin(String worknum,
			String pwd, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().doLogin(worknum, pwd);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {

						JSONObject jsonObj = JSONObject.parseObject(result.result);
						UserInfo userInfo = null;
						if (jsonObj.containsKey("data")) {
							JSONObject jsonobj = jsonObj.getJSONObject("data");
							userInfo = JSONObject.parseObject(jsonobj.toJSONString(), UserInfo.class);

						}
						listener.onPostFetch(FetchListener.STATUS_OK, userInfo);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (TextUtils.isEmpty(msg)) {
						msg = "登录失败";
					}
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
	}

	/**
     * 楼层信息同步
     */
	public void fetchHallInfo(final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().syncHall();
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						List<Canyin_Shop_Floor> hList = null;
						if (jsonObj.containsKey("data")) {
							JSONArray jsonArr = jsonObj.getJSONArray("data");
							hList = JSONObject.parseArray(
									jsonArr.toJSONString(),
									Canyin_Shop_Floor.class);

						}
						listener.onPostFetch(FetchListener.STATUS_OK, hList);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR,"数据解析失败");
					}
				} else {
					if (listener != null) {
						String msg = "";
						try {
							JSONObject jsonObj = JSONObject.parseObject(result.result);
							msg = jsonObj.getString("msg");
						} catch (Exception e) {
							e.printStackTrace();
							msg = "数据获取失败";
						}
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
					}
					
				}
			}
		});

		task.execute();
	}

	/**
	 * 餐桌信息表
	 */
	public void fetchTableInfo(String tingbh, String taibh,
			final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().syncTable(tingbh, taibh);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						List<Canyin_Shop_Diningtable> tList = null;
						if (jsonObj.containsKey("data")) {
							JSONArray jsonArr = jsonObj.getJSONArray("data");
							tList = JSONObject.parseArray(jsonArr.toJSONString(), Canyin_Shop_Diningtable.class);

						}
						listener.onPostFetch(FetchListener.STATUS_OK, tList);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null) {
						String msg = "";
						try {
							JSONObject jsonObj = JSONObject.parseObject(result.result);
							msg = jsonObj.getString("msg");
						} catch (Exception e) {
							e.printStackTrace();
							msg = "数据获取失败";
						}
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
					}
				}
			}
		});
		task.execute();
	}

	/**
	 * 菜单详情表
	 */
	public void fetchProductInfo(final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().getDishes();
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				if (listener != null)
					listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						JSONArray jsonArr = jsonObj.getJSONArray("data");
						final List<Canyin_Shop_Product> tList = JSONObject.parseArray(jsonArr.toJSONString(), Canyin_Shop_Product.class);
						CHBThreadPool.runThread(JobType.NORMAL, new Runnable() {
							
							@Override
							public void run() {
								try {
									// 数据库持久化
									dbManager.delete(Canyin_Shop_Product.class);
									if (tList != null) {
										// for (int i = 0; i < tList.size();
										// i++) {
										// Canyin_Shop_Product tableBean =
										// tList.get(i);
										// }
										dbManager.save(tList);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								if (listener != null)
									listener.onPostFetch(FetchListener.STATUS_OK, tList);
							}
						});
						
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR, "数据获取失败");
				}
			}
		});
		task.execute();
	}

	/**
	 * 餐品分类表
	 */
	public void fetchProductTypeInfo(final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().categoryDishes();
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				if (listener != null)
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						JSONArray jsonArr = jsonObj.getJSONArray("data");
						final List<Canyin_Shop_Product_Type> tList = JSONObject.parseArray(jsonArr.toJSONString(), Canyin_Shop_Product_Type.class);
						CHBThreadPool.runThread(JobType.NORMAL, new Runnable() {
							
							@Override
							public void run() {
								try {
									// 数据库持久化
									dbManager.delete(Canyin_Shop_Product_Type.class);
									if (tList != null) {
										// for (int i = 0; i < tList.size();
										// i++) {
										// Canyin_Shop_Product_Type pTypeBean =
										// tList.get(i);
										// }
										dbManager.save(tList);

									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (listener != null)
									listener.onPostFetch(FetchListener.STATUS_OK, tList);
							}
						});
							
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR, "数据获取失败");
				}
			}
		});
		task.execute();
	}

	/**
	 * 开台接口
	 * 
	 * @param data
	 *            Json
	 * @param yhno
	 *            宴会开台号
	 * @param opercode
	 *            操作员编号
	 * @param opername
	 *            操作员姓名
	 */
	public void fetchOpenTable(String data, String yhno,
			final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().openTable(data, yhno);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_OK,
									jsonObj);
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(
									FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null) {
						String msg = "数据获取失败";
						try {
							JSONObject jsonObj = JSONObject.parseObject(result.result);
							msg = jsonObj.getString("msg");
						} catch (Exception e) {
							e.printStackTrace();
							msg = "数据获取失败";
						}
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR,
								msg);
					}
				}
			}
		});
		task.execute();

	}

	/**
	 * 宴会开台接口
	 * 
	 * @param data
	 *            Json
	 * @param yhno
	 *            宴会开台号
	 * @param opercode
	 *            操作员编号
	 * @param opername
	 *            操作员姓名
	 */
	public void fetchPartyOpenTable(String data, String yhno,
			final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().openTable(data, yhno);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_OK,
									jsonObj);
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(
									FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR,
								result);
				}
			}
		});
		task.execute();

	}

	/**
	 * 餐台状态切换启用以及禁止
	 */
	public void fetchStopOpenTable(String data, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().changeTableState(data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_OK,
									jsonObj);
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR,
								result);
				}

			}
		});
		task.execute();
	}

	/**
	 * 换台操作
	 */
	public void fetchChangeTablePosition(String data,
			final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().changeTable(data);

		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						HuanTaiResult huantaiResult = jsonObj.getObject("data", HuanTaiResult.class);
						
						listener.onPostFetch(FetchListener.STATUS_OK, huantaiResult);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR,	"数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result.result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 软件升级
	 */
	public void fetchAppUpdate(String edition, String name, String url,
			String mClass, String mByte, String mExplain,
			final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().getUpdate(edition, name,
				url, mClass, mByte, mExplain);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						List<Canyin_Shop_Product> tList = null;
						if (jsonObj.containsKey("data")) {
							JSONArray jsonArr = jsonObj.getJSONArray("data");
							tList = JSONObject.parseArray(
									jsonArr.toJSONString(),
									Canyin_Shop_Product.class);

						}
						listener.onPostFetch(FetchListener.STATUS_OK, tList);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result.result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 软件升级
	 */
	public void fetchAppEdition(final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().getEdition();
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						List<DownloadItem> dList = null;
						if (jsonObj.containsKey("data")) {
							JSONArray jsonArr = jsonObj.getJSONArray("data");
							dList = JSONObject.parseArray(jsonArr.toJSONString(), DownloadItem.class);
						}
						listener.onPostFetch(FetchListener.STATUS_OK, dList);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result.result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 已点菜明细
	 */
	public void fetchDinnerList(String tingbh, String taibh, final String ktlsh, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().getDishesDetail(tingbh, taibh, ktlsh);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						List<HaveProductBean> hList = null;
						String remark = "";
						String waiterCode = "";
						String waiterName = "";
						if (jsonObj.containsKey("data")) {
							JSONObject data = jsonObj.getJSONObject("data");
							JSONObject waiter = data.getJSONObject("waiter");
							waiterCode = waiter.getString("waiterCode");
							waiterName = waiter.getString("waiterName");
							JSONArray jsonArr = data.getJSONArray("productAll");
							remark = data.getString("tasteRemark");
							hList = JSONObject.parseArray(jsonArr.toJSONString(), HaveProductBean.class);
							if (hList != null && hList.size() > 0) {
								for (HaveProductBean item : hList) {
									item.setOrderState(1);
								}
							}
						}
						listener.onPostFetch(FetchListener.STATUS_OK, hList, remark, ktlsh, waiterCode, waiterName);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
						msg = "数据获取失败";
					}
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
	}
	
	/**
	 * 反结账已点菜明细
	 */
	public void fetchFjzDinnerList(String tingbh, String taibh, final String ktlsh, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().getJzProductAll(tingbh, taibh, ktlsh);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				// TODO Auto-generated method stub
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						List<HaveProductBean> hList = null;
						if (jsonObj.containsKey("data")) {
							JSONArray jsonArr = jsonObj.getJSONArray("data");
							hList = JSONObject.parseArray(jsonArr.toJSONString(), HaveProductBean.class);
							if (hList != null && hList.size() > 0) {
								for (HaveProductBean item : hList) {
									item.setOrderState(1);
								}
							}
						}
						listener.onPostFetch(FetchListener.STATUS_OK, hList, ktlsh);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result.result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 点菜接口
	 */

	public void fetchOrderDishes(String tingbh, String taibh, String ktlsh,
			String taste, String data, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().orderDishes(tingbh, taibh, ktlsh, taste, data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						List<FenChuPrinter> tList = null;
						if (jsonObj.containsKey("msg")) {
							JSONObject jsonResult = jsonObj.getJSONObject("msg");

							JSONArray jsonArry = jsonResult.getJSONArray("product");
							tList = JSONObject.parseArray(jsonArry.toJSONString(), FenChuPrinter.class);
						}

						listener.onPostFetch(FetchListener.STATUS_OK, tList);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "下单失败";
					JSONObject jsonObj = JSONObject.parseObject(result.result);
					try {
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
					}
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
	}

	/**
	 * 宴会点菜
	 * 
	 * @param yhdata
	 * @param data
	 * @param listener
	 */
	public void fetchYHOrderDishes(String yhdata, String data,
			final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().orderYHDishes(yhdata, data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);

						listener.onPostFetch(FetchListener.STATUS_OK, jsonObj);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 菜品分类同步
	 */
	public void fetchDinnerMenus(final FetchListener listener) {
		// TODO Auto-generated method stub
		CHBHttpTask task = CHBRqstApi.getInstance().categoryDishes();
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				// TODO Auto-generated method stub
				if (result.status == TaskResultStatus.OK) {
					if (result.status == TaskResultStatus.OK) {
						try {
							JSONObject jsonObj = JSONObject
									.parseObject(result.result);
							List<Canyin_Shop_Product_Type> mList = null;
							if (jsonObj.containsKey("data")) {
								JSONArray jsonArr = jsonObj
										.getJSONArray("data");
								mList = JSONObject.parseArray(
										jsonArr.toJSONString(),
										Canyin_Shop_Product_Type.class);

							}
							listener.onPostFetch(FetchListener.STATUS_OK, mList);
						} catch (Exception e) {
							e.printStackTrace();
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
						}
					} else {
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR,
								result);
					}
				}
			}
		});
		task.execute();
	}

	/**
	 * 口味同步
	 * 
	 * @param listener
	 */
	public void fetchTaste(final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().syncTaste();
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				if (listener != null)
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						JSONArray jsonArr = jsonObj.getJSONArray("data");
						final List<Canyin_Shop_Taste> mList = JSONObject.parseArray(jsonArr.toJSONString(), Canyin_Shop_Taste.class);

						CHBThreadPool.runThread(JobType.NORMAL, new Runnable() {

							@Override
							public void run() {
								try {
									dbManager.delete(Canyin_Shop_Taste.class);
									if (mList != null) {
										// for (int i = 0; i < mList.size();
										// i++) {
										// Canyin_Shop_Taste tasteBean =
										// mList.get(i);
										// }
										dbManager.save(mList);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (listener != null)
									listener.onPostFetch(FetchListener.STATUS_OK, mList);
							}
						});

					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR, "数据获取失败");
				}
			}
		});
		task.execute();
	}

	/**
	 * 退菜说明
	 * 
	 * @param listener
	 */
	public void fetchBackFood(final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().backFood();
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				if (listener != null)
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						JSONArray jsonArr = jsonObj.getJSONArray("data");
						final List<Canyin_Shop_Tcreson> mList = JSONObject.parseArray(jsonArr.toJSONString(), Canyin_Shop_Tcreson.class);
						
						CHBThreadPool.runThread(JobType.NORMAL, new Runnable() {
							
							@Override
							public void run() {
								try {
									dbManager.delete(Canyin_Shop_Tcreson.class);
									if (mList != null) {
										// for (int i = 0; i < mList.size();
										// i++) {
										// Canyin_Shop_Tcreson tasteBean =
										// mList.get(i);
										// }
										dbManager.save(mList);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								if (listener != null)
									listener.onPostFetch(FetchListener.STATUS_OK, mList);
							}
						});

					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR, "数据获取失败");
				}
			}
		});
		task.execute();
	}

	/**
	 * 赠菜原因
	 * 
	 * @param listener
	 */
	public void fetchGiveFoodReason(final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().giveReason();
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				if (listener != null)
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						JSONArray jsonArr = jsonObj.getJSONArray("data");
						final List<Canyin_Shop_Zsreson> mList = JSONObject.parseArray(jsonArr.toJSONString(), Canyin_Shop_Zsreson.class);
						CHBThreadPool.runThread(JobType.NORMAL, new Runnable() {
							
							@Override
							public void run() {
								try {
									dbManager.delete(Canyin_Shop_Zsreson.class);
									if (mList != null) {
										// for (int i = 0; i < mList.size();
										// i++) {
										// Canyin_Shop_Zsreson tasteBean =
										// mList.get(i);
										// }
										dbManager.save(mList);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								if (listener != null)
									listener.onPostFetch(FetchListener.STATUS_OK, mList);
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR, "数据获取失败");
				}
			}
		});
		task.execute();
	}

	/**
	 * 启用 、禁用、清台接口
	 * 
	 * @param data
	 * @param listener
	 */
	public void fetchHandleTable(String data, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().changeTableState(data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_OK,
									jsonObj);
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(
									FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR,
								result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 添加临时菜品
	 * 
	 * @param parentId
	 * @param typeId
	 * @param productName
	 * @param special
	 * @param price
	 * @param units
	 * @param norms
	 * @param listener
	 */
	public void fetchAddTempDish(String parentId, String typeId,
			String productName, String special, String price, String units,
			String norms, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().addTempDish(parentId,
				typeId, productName, special, price, units, norms);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					FrameLog.d("CustomDish", "OK");
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						FrameLog.d("CustomDish", jsonObj.toString());
						TempDishBean tempDishBean = null;
						if (jsonObj.containsKey("data")) {
							JSONObject obj = jsonObj.getJSONObject("data");
							tempDishBean = JSONObject.parseObject(
									obj.toJSONString(), TempDishBean.class);

						}
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_OK,
									tempDishBean);
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					FrameLog.d("CustomDish", "FAIL");
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR,
								result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 增菜接口
	 * 
	 * @param tingbh
	 *            厅编号
	 * @param taibh
	 *            台编号
	 * @param ktlsh
	 *            开台流水号
	 * @param data
	 *            数据
	 * @param listener
	 */
	public void fetchGiveDish(String tingbh, String taibh, String ktlsh,
			String data, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().giveDishes(tingbh, taibh,
				ktlsh, data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						if (jsonObj.containsKey("data")) {

						}
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_OK, "");
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
						msg = "数据获取失败";
					}
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
	}

	/**
	 * 转菜接口
	 * 
	 * @param tingbh
	 *            厅编号
	 * @param taibh
	 *            台编号
	 * @param ktlsh
	 *            开台流水号
	 * @param data
	 *            数据
	 * @param listener
	 */
	public void fetchReturnDish(String tingbh, String taibh, String ktlsh,
			String data, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().returnDishes(tingbh, taibh,
				ktlsh, data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						List<Canyin_Shop_Tcreson> mList = null;
						if (jsonObj.containsKey("data")) {
							JSONArray jsonArr = jsonObj.getJSONArray("data");
							mList = JSONObject.parseArray(
									jsonArr.toJSONString(),
									Canyin_Shop_Tcreson.class);
							if (mList != null) {
								try {
									dbManager.delete(Canyin_Shop_Tcreson.class);
//									for (int i = 0; i < mList.size(); i++) {
//										Canyin_Shop_Tcreson tasteBean = mList
//												.get(i);
//									}
									dbManager.save(mList);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_OK, mList);
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR,
								result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 更改菜品数量
	 */

	public void fecthChangeDishesCount(String tingbh, String taibh,
			String ktlsh, String data, final FetchListener listener) {
		// TODO Auto-generated method stub
		CHBHttpTask task = CHBRqstApi.getInstance().changeDishesCount(tingbh,
				taibh, ktlsh, data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				// TODO Auto-generated method stub
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						// List<Canyin_Shop_Tcreson> mList = null;
						// if (jsonObj.containsKey("data")) {
						// JSONArray jsonArr = jsonObj.getJSONArray("data");
						// mList = JSONObject.parseArray(jsonArr.toJSONString(),
						// Canyin_Shop_Tcreson.class);
						// if (mList != null) {
						// try {
						// dbManager.delete(Canyin_Shop_Tcreson.class);
						// for (int i = 0; i < mList.size(); i++) {
						// Canyin_Shop_Tcreson tasteBean = mList.get(i);
						// dbManager.save(tasteBean);
						// }
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						// }
						//
						// }
						// if (listener != null)
						// listener.onPostFetch(FetchListener.STATUS_OK, mList);
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR,
								result);
				}
			}
		});

	}

	/**
	 * 退菜接口
	 */
	public void fecthReturnDishes(String tingbh, String taibh, String ktlsh,
			String data, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().returnDishes(tingbh, taibh,
				ktlsh, data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			};

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						TuiFoodResult tuiFood = null;
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						if (jsonObj.containsKey("data")) {
							tuiFood = jsonObj.getObject("data",TuiFoodResult.class);
						}
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_OK, tuiFood);
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
						msg = "数据获取失败";
					}
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();

	}

	/**
	 * 划菜接口
	 */
	public void fecthEliceDishes(String data, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().changeMarkStatus(data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			};

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						listener.onPostFetch(FetchListener.STATUS_OK, jsonObj);
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result);
				}
			}
		});
		task.execute();

	}

	/**
	 * 转菜接口
	 */
	public void fecthTurnOrderDish(String oldtingbh, String oldtaibh,
			String oldlsh, String newtingbh, String newtaibh, String newlsh,
			String data, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().turnOrderDish(oldtingbh,
				oldtaibh, oldlsh, newtingbh, newtaibh, newlsh, data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			};

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						listener.onPostFetch(FetchListener.STATUS_OK, jsonObj);
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "";
					if (listener != null)
						try {
							JSONObject jsonObj = JSONObject.parseObject(result.result);
						    msg = jsonObj.getString("msg");
						} catch (Exception e) {
							e.printStackTrace();
							msg = "数据解析失败";
						}
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();

	}

	/**
	 * 获取账单原价合计
	 * 
	 * @param jLiushui
	 *            流水号json数组
	 * @param listener
	 */
	public void getBatchDishesDetail(String jLiushui,
			final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().getBatchDishesDetail(
				jLiushui);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			};

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						List<HaveProductBean> hList = null;
						if (jsonObj.containsKey("data")) {
							JSONArray jsonArr = jsonObj.getJSONArray("data");
							hList = JSONObject.parseArray(
									jsonArr.toJSONString(),
									HaveProductBean.class);
							if (hList != null && hList.size() > 0) {
								for (HaveProductBean item : hList) {
									item.setOrderState(1);
								}
							}
						}
						listener.onPostFetch(FetchListener.STATUS_OK, hList);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 获取员工最高折扣率
	 * 
	 * @param uName
	 * @param uPwd
	 * @param listener
	 */
	public void fetchNewDiscount(String uName, String uPwd, final FetchListener listener) {

		CHBHttpTask task = CHBRqstApi.getInstance().getNewDiscount(uName, uPwd);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			};

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						float disRate = jsonObj.getFloatValue("data");
						listener.onPostFetch(FetchListener.STATUS_OK, disRate);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
						msg = "数据获取失败";
					}
					listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, msg);
				}
			}
		});
		task.execute();
	}

	/**
	 * 关台
	 * 
	 * @param data
	 * @param listener
	 */
	public void fetchCloseTable(String data, final FetchListener listener) {

		CHBHttpTask task = CHBRqstApi.getInstance().closeTable(data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			};

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						// JSONObject jsonObj =
						// JSONObject.parseObject(result.result);
						listener.onPostFetch(FetchListener.STATUS_OK, result);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR,
								"数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 更改起菜状态
	 */
	public void fetchWaitStatus(String data, final FetchListener listener) {
		// TODO Auto-generated method stub
		CHBHttpTask task = CHBRqstApi.getInstance().changeWaitStatus(data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject
								.parseObject(result.result);
						listener.onPostFetch(FetchListener.STATUS_OK, jsonObj);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR,
								"数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result);
				}
			}
		});

		task.execute();
	}

	/**
	 * 获取员工最大抹零
	 * 
	 * @param uName
	 * @param uPwd
	 * @param listener
	 */
	public void fetchWorkerml(String uName, String uPwd, final FetchListener listener) {

		CHBHttpTask task = CHBRqstApi.getInstance().getWorkerml(uName, uPwd);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			};

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						int fMolFlag = jsonObj.getIntValue("data");
						listener.onPostFetch(FetchListener.STATUS_OK, fMolFlag);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					JSONObject jsonObj = JSONObject.parseObject(result.result);
					String msg = jsonObj.getString("msg");
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
	}

	/**
	 * 获取交班数据接口
	 * 
	 * @param listener
	 */
	public void getShiftExchangeData(final FetchListener listener) {

		CHBHttpTask task = CHBRqstApi.getInstance().getShiftExchangeData();
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						List<HandOverBean> mList = new ArrayList<HandOverBean>();
						if (jsonObj.containsKey("data")) {
							JSONObject data = jsonObj.getJSONObject("data");
							String sbsj = data.getString("sbsj"); // 上报时间
							String totalPrice = data.getString("totalPrice"); // 原价合计
							String preferentialTj = data
									.getString("preferentialTj"); // 优惠合计
							String turnovert = data.getString("turnovert"); // 实收总额

							JSONArray payArr = data.getJSONArray("paymentType");
							FrameLog.v(TAG, "payArr.size()"+ payArr.size());
							if (payArr.size() > 0) {
								List<PaymentType> pList = JSONObject.parseArray(
										payArr.toJSONString(), PaymentType.class);
								List<List<PaymentType>> payList = SysWorkTools
										.subList(pList, 2);
								for (int i = 0; i < payList.size(); i++) {
									List<PaymentType> lst = payList.get(i);
									HandOverBean hBean;
									if (lst.size() < 2) {
										PaymentType payType = lst.get(0);
										hBean = new HandOverBean();
										hBean.setType(0);
										hBean.setPayName1(payType.getPayName());
										hBean.setPayMoneyTj1(payType
												.getPayMoneyTj());

										mList.add(hBean);
									} else {
										PaymentType payType0 = lst.get(0);
										hBean = new HandOverBean();
										hBean.setType(0);
										hBean.setPayName1(payType0.getPayName());
										hBean.setPayMoneyTj1(payType0
												.getPayMoneyTj());

										PaymentType payType1 = lst.get(1);
										hBean.setPayName2(payType1.getPayName());
										hBean.setPayMoneyTj2(payType1
												.getPayMoneyTj());

										mList.add(hBean);
									}
								}
							}
							JSONArray proArr = data.getJSONArray("productType");
							List<ProductType> proList = JSONObject.parseArray(
									proArr.toJSONString(), ProductType.class);
							for (int i = 0; i < proList.size(); i++) {
								ProductType productType = proList.get(i);
								HandOverBean hBean;
								if (i == 0) {
									hBean = new HandOverBean();
									hBean.setType(1);
									hBean.setSmallName("类别名称");
									hBean.setPriceTj("原价合计（元）");
									hBean.setSjPriceTj("实收合计（元）");

									mList.add(hBean);

									hBean = new HandOverBean();
									hBean.setType(1);
									hBean.setSmallName(productType
											.getSmallName());
									hBean.setPriceTj(productType.getPriceTj());
									hBean.setSjPriceTj(productType
											.getSjPriceTj());

									mList.add(hBean);
								} else {
									hBean = new HandOverBean();
									hBean.setType(1);
									hBean.setSmallName(productType
											.getSmallName());
									hBean.setPriceTj(productType.getPriceTj());
									hBean.setSjPriceTj(productType
											.getSjPriceTj());

									mList.add(hBean);
								}
							}

							JSONArray tingArr = data.getJSONArray("tingSale");
							if (tingArr.size() > 0) {
								List<TingSale> tList = JSONObject.parseArray(
										tingArr.toJSONString(), TingSale.class);
								List<List<TingSale>> tingList = SysWorkTools
										.subList(tList, 2);
								for (int i = 0; i < tingList.size(); i++) {
									List<TingSale> lst = tingList.get(i);
									HandOverBean hBean;
									if (lst.size() < 2) {
										TingSale tingSale = lst.get(0);
										hBean = new HandOverBean();
										hBean.setType(2);
										hBean.setFloorPrice1(tingSale
												.getSjPriceTj());
										hBean.setFloorName1(tingSale.getFloorName());

										mList.add(hBean);
									} else {
										TingSale tingSale0 = lst.get(0);
										hBean = new HandOverBean();
										hBean.setType(2);

										hBean.setFloorPrice1(tingSale0
												.getSjPriceTj());
										hBean.setFloorName1(tingSale0
												.getFloorName());

										TingSale tingSale1 = lst.get(1);
										hBean.setFloorPrice2(tingSale1
												.getSjPriceTj());
										hBean.setFloorName2(tingSale1
												.getFloorName());

										mList.add(hBean);
									}
								}
							}
							
							JSONObject hyObj = data.getJSONObject("hyDetails");
								HandOverBean hBean = new HandOverBean();
								hBean.setType(3);
								hBean.setConsumptionNum(hyObj
										.getString("consumptionNum"));
								hBean.setRechargeAmount(hyObj
										.getString("rechargeAmount"));
								hBean.setRefundAmount(hyObj
										.getString("refundAmount"));
								hBean.setConsumpAmount(hyObj
										.getString("consumpAmount"));
								mList.add(hBean);

							JSONObject otherObj = data
									.getJSONObject("otherInfo");
							HandOverBean hBean1 = new HandOverBean();
							hBean1.setType(4);
							hBean1.setCheckoutNum(otherObj
									.getString("checkoutNum"));
							hBean1.setTcAmount(otherObj.getString("tcAmount"));
							hBean1.setGiftAmount(otherObj
									.getString("giftAmount"));
							hBean1.setPersonCntTj(otherObj
									.getString("personCntTj"));
							hBean1.setMalingAmount(otherObj
									.getString("malingAmount"));
							hBean1.setDiscountAmount(otherObj
									.getString("discountAmount"));
							mList.add(hBean1);

							HandOverBean hBean2 = new HandOverBean();
							hBean2.setType(5);
							hBean2.setTurnovert(turnovert);
							mList.add(hBean2);

							listener.onPostFetch(FetchListener.STATUS_OK,
									mList, sbsj, totalPrice, preferentialTj);
						}

					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR,
								"数据解析失败");
					}
				} else {
					String msg = "";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
						msg = "数据获取失败";
					}
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
	}

	/**
	 * 交班接口
	 * 
	 * @param jbsj
	 *            交班时间
	 * @param listener
	 */
	public void fetchNextShift(String jbsj, final FetchListener listener) {
		// TODO Auto-generated method stub
		CHBHttpTask task = CHBRqstApi.getInstance().shiftExchange(jbsj);

		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				// TODO Auto-generated method stub
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						listener.onPostFetch(FetchListener.STATUS_OK, jsonObj);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR,	"数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 现金券核销
	 * @param lsh
	 * @param data
	 * @param listener
	 */
	public void fetchVoucherMoney(String lsh, String data, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().getVouchersMoney(lsh, data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			};

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						String hxMoney = null;
						if(jsonObj.containsKey("data")) {
							hxMoney = jsonObj.getString("data");
						}
						listener.onPostFetch(FetchListener.STATUS_OK, hxMoney);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 台位结账
	 * 
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
	 * @param listener
	 */
	public void fetchFinalBill(boolean bBatch, String lsh, String distype,
			String prodata, String paydata, String memberdata, String accdata,
			String automlmoney, String sdmlmoney, String mlperson,
			String dismoney, String disperson, final FetchListener listener) {
		CHBHttpTask task;
		if (bBatch) {// 并单结账
			task = CHBRqstApi.getInstance().getBatchFinalBill(lsh, distype,
					prodata, paydata, memberdata, accdata, automlmoney,
					sdmlmoney, mlperson, dismoney, disperson);
		} else {
			task = CHBRqstApi.getInstance().getFinalBill(lsh, distype, prodata,
					paydata, memberdata, accdata, automlmoney, sdmlmoney,
					mlperson, dismoney, disperson);
		}

		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			};

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						listener.onPostFetch(FetchListener.STATUS_OK, jsonObj);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR,
								"数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result);
				}
			}
		});
		task.execute();
	}

	/**
	 * 催菜
	 * 
	 * @param ktlsh
	 * @param tingbh
	 * @param taibh
	 * @param ccType
	 * @param data
	 * @param listener
	 */
	public void fetchRemindDish(String ktlsh, String tingbh, String taibh,
			String ccType, String data, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().remindDishes(ktlsh, tingbh,
				taibh, ccType, data);
		task.setListener(new TaskSimpleListener() {

			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {

						JSONObject jsonObj = JSONObject.parseObject(result.result);
						
						ReminderBean remindBean = jsonObj.getObject("data", ReminderBean.class);
						listener.onPostFetch(FetchListener.STATUS_OK, remindBean);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result.result);
				}
			}
		});
		task.execute();
	}
	/**
	 * 验证商家
	 * @param shopid
	 * @param shopName
	 * @param mobile
	 * @param activationCode
	 * @param listener
	 */
	public void businessVerifi(String shopid, String shopName, String mobile,String activationCode, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().businessVerifi(shopid, shopName, mobile, activationCode);
		task.setListener(new TaskSimpleListener() {
			
			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}
			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
//						if (jsonObj.containsKey("data")) {
//							JSONObject data = jsonObj.getJSONObject("data");
//
//						}
						listener.onPostFetch(FetchListener.STATUS_OK, jsonObj);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR,
								"数据解析失败");
					}
				} else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result);
				}

			}
		});
		task.execute();
	}
	
	/**
	 * 获取商家信息
	 * @param listener
	 */
	public void businessDetail(final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().businessDetail();
		task.setListener(new TaskSimpleListener() {
			
			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}
			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						BusinessDetail business = null;
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						if (jsonObj.containsKey("data")) {
							JSONObject data = jsonObj.getJSONObject("data");
							business = JSONObject.parseObject(
									data.toJSONString(), BusinessDetail.class);
						}
						listener.onPostFetch(FetchListener.STATUS_OK, business);
						} catch (Exception e) {
							e.printStackTrace();
								listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				}else {
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result);
				}
				
			}
		});
		task.execute();
	}
	
	/**
	 * 外卖开台并点菜
	 * @param tingbh
	 * @param taibh
	 * @param data
	 * @param listener
	 */
	public void fetchOrderWMDish(String tingbh, String taibh, String lsh, String taste, String data, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().orderWMDish(tingbh, taibh, lsh, taste, data);
		task.setListener(new TaskSimpleListener() {
			
			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}
			
			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						List<FenChuPrinter> tList = null;
						if (jsonObj.containsKey("msg")) {
							JSONObject jsonResult = jsonObj.getJSONObject("msg");

							JSONArray jsonArry = jsonResult.getJSONArray("product");
							tList = JSONObject.parseArray(jsonArry.toJSONString(), FenChuPrinter.class);
						}
						listener.onPostFetch(FetchListener.STATUS_OK, tList);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
						msg = "数据获取失败";
					}
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
					//listener.onPostFetch(FetchListener.STATUS_NET_ERROR, result);
				}
			}
		});
		task.execute();
	}
	
	
	/**
	 * 反结账
	 * @param lsh
	 * @param listener
	 */
	public void fetchUnBill(String lsh, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().getUnBill(lsh);
		task.setListener(new TaskSimpleListener() {
			
			@Override
			public void onPreExecute(BaseTask task) {
				listener.onPreFetch();
			}
			
			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						String msg = jsonObj.getString("msg");
						listener.onPostFetch(FetchListener.STATUS_OK, msg);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "反结账失败，请重试";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
					}
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
	}
	
	/**
	 * 同步市别基础数据
	 * @param listener
	 */
	public void fetchMealPeriod(final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().getMealPeriod();
		task.setListener(new TaskSimpleListener() {
			@Override
			public void onPreExecute(BaseTask task) {
				if (listener != null)
				listener.onPreFetch();
			}
			
			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						JSONArray jsonArr = jsonObj.getJSONArray("data");
						final List<Canyin_Shop_Mealstime> tList = JSONObject.parseArray(jsonArr.toJSONString(),Canyin_Shop_Mealstime.class);
						CHBThreadPool.runThread(JobType.NORMAL, new Runnable() {
							
							@Override
							public void run() {
								try {
									// 数据库持久化
									dbManager.delete(Canyin_Shop_Mealstime.class);
									if (tList != null) {
										// for (int i = 0; i < tList.size();
										// i++) {
										// Canyin_Shop_Mealstime pTypeBean =
										// tList.get(i);
										// }
										dbManager.save(tList);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								if (listener != null)
									listener.onPostFetch(FetchListener.STATUS_OK, tList);
							}
						});
							
					} catch (Exception e) {
						e.printStackTrace();
						if (listener != null)
							listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					if (listener != null)
						listener.onPostFetch(FetchListener.STATUS_NET_ERROR, "数据获取失败");
				}
			}
		});
		task.execute();
	}
	
	/**
	 * 消息列表
	 * @param listener
	 */
	public void fetchMsg(final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().getMsgList();
		task.setListener(new TaskSimpleListener() {
			@Override
			public void onPreExecute(BaseTask task) {
				if (listener != null)
				listener.onPreFetch();
			}
			
			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						List<MsgBean> msgList = null;
						if (jsonObj.containsKey("data")) {
							JSONArray jsonArr = jsonObj.getJSONArray("data");
							msgList = JSONObject.parseArray(jsonArr.toJSONString(), MsgBean.class);
						}
							listener.onPostFetch(FetchListener.STATUS_OK, msgList);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "数据获取失败";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
						msg = "数据获取失败";
					}
					
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
	}
	
	/**
	 * 更改订单
	 * @param listener
	 * @param ktlsh
	 * @param oldcnt
	 * @param newcnt
	 */
	public void fetchChangeOrder(final FetchListener listener, String ktlsh, String oldcnt, String newcnt, String waitercode) {
		CHBHttpTask task = CHBRqstApi.getInstance().changeOrder(ktlsh, oldcnt, newcnt, waitercode);
		task.setListener(new TaskSimpleListener() {
			
			@Override
			public void onPreExecute(BaseTask task) {
				if (listener != null)
					listener.onPreFetch();
			}

			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						String workerName = "";
						String workerNum = "";
						if (jsonObj.containsKey("data")) {
//							JSONObject jsonDataObj = jsonObj.getJSONObject("data");
//							JSONObject jsonWaiterObj = jsonDataObj.getJSONObject("waiter");
//						    workerName = jsonWaiterObj.getString("workerName");
//						    workerNum =  jsonWaiterObj.getString("workerNum");
						}
						listener.onPostFetch(FetchListener.STATUS_OK);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "数据获取失败";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
	}
	
	public void fetchLsProductAll(String lslsh, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().getLsProductAll(lslsh);
		task.setListener(new TaskSimpleListener() {
			@Override
			public void onPreExecute(BaseTask task) {
				if (listener != null)
				listener.onPreFetch();
			}
			
			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						List<MsgProBean> msgList = null;
						String source = ""; //来源（0：POS   1：PAD     2：小二筷点    3：外卖     4：快餐     5：扫码点餐    5：其它）
						if (jsonObj.containsKey("data")) {
							JSONObject dataObject = jsonObj.getJSONObject("data");
							JSONArray jsonArr = dataObject.getJSONArray("lsyy");
							msgList = JSONObject.parseArray(jsonArr.toJSONString(), MsgProBean.class);
							for (MsgProBean item : msgList) {
								try {
									 Canyin_Shop_Product product = dbManager.selector(Canyin_Shop_Product.class).where("productCode", "=", item.getCode()).findFirst();
									 item.setDisflag(product.getDisflag());
								} catch (DbException e) {
									e.printStackTrace();
								}
							}
							source = dataObject.getString("source");
						}
						listener.onPostFetch(FetchListener.STATUS_OK, msgList, source);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "数据获取失败";
					try {
						
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
						msg = "数据解析失败";
					}
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
		
	}
	
	public void fetchLsProductDel(String lslsh, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().deleteLSPad(lslsh);
		task.setListener(new TaskSimpleListener() {
			@Override
			public void onPreExecute(BaseTask task) {
				if (listener != null)
				listener.onPreFetch();
			}
			
			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						listener.onPostFetch(FetchListener.STATUS_OK, "");
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "删除数据失败";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
						msg = "删除数据失败";
					}
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
		
	}
	/**
	 * 打印预结单
	 * @param lslsh
	 * @param listener
	 */
	public void fetchprintYJD(String lsh, final FetchListener listener) {
		CHBHttpTask task = CHBRqstApi.getInstance().printYJD(lsh);
		task.setListener(new TaskSimpleListener() {
			@Override
			public void onPreExecute(BaseTask task) {
				if (listener != null)
				listener.onPreFetch();
			}
			
			@Override
			public void onTaskFinished(BaseTask task, TaskResult result) {
				if (result.status == TaskResultStatus.OK) {
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						JSONObject data = null;
						if (jsonObj.containsKey("data")) {
							data = jsonObj.getJSONObject("data");
						}
						listener.onPostFetch(FetchListener.STATUS_OK, data);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onPostFetch(FetchListener.STATUS_PARSER_ERROR, "数据解析失败");
					}
				} else {
					String msg = "数据获取失败";
					try {
						JSONObject jsonObj = JSONObject.parseObject(result.result);
						msg = jsonObj.getString("msg");
					} catch (Exception e) {
						e.printStackTrace();
						msg = "数据获取失败";
					}
					listener.onPostFetch(FetchListener.STATUS_NET_ERROR, msg);
				}
			}
		});
		task.execute();
		
	}
	
}
