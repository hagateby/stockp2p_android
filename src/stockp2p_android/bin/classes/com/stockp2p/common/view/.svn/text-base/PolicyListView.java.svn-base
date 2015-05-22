package com.pactera.nci.common.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.http.ResponseInfo;
import com.pactera.nci.R;
import com.pactera.nci.common.cache.PolicyList;
import com.pactera.nci.common.cache.PolicyList.ErrorPolicyCallBack;
import com.pactera.nci.common.cache.SertificatonInfor;
import com.pactera.nci.common.cache.SertificatonInfor.ErrorCallBack;
import com.pactera.nci.common.cache.UserInfoManager;
import com.pactera.nci.common.data.Constants;
import com.pactera.nci.common.serviceengin.Des3;
import com.pactera.nci.common.serviceengin.EnginCallback;
import com.pactera.nci.common.serviceengin.ServiceEngin;
import com.pactera.nci.common.util.ExitApplication;
import com.pactera.nci.common.util.NetUtil;
import com.pactera.nci.common.util.TipUitls;
import com.pactera.nci.components.accessory_risk_low_coverage_renewal.AccessoryRiskLowCoverageRenewal;
import com.pactera.nci.components.accessory_risk_not_renewal.AccessoryRiskNotRenewal;
import com.pactera.nci.components.accumulatedinterestreceive.AccumulatedInterestReceive;
import com.pactera.nci.components.age_change.AgeChange;
import com.pactera.nci.components.annuityreceive.AnnuityReceive;
import com.pactera.nci.components.applicantinforchange.ApplicantInfoChangeDetail;
import com.pactera.nci.components.applicationtelchange.TelchangeInfor;
import com.pactera.nci.components.date_change.DateChange;
import com.pactera.nci.components.insurerinforchange.InsuredInfoChangeDetail;
import com.pactera.nci.components.main_risks_renewal.MainRisksRenewal;
import com.pactera.nci.components.policy_transfer.PolicyTransfer;
import com.pactera.nci.components.policyinforquery.PolicyInforQuery;
import com.pactera.nci.components.policyloancredit.PolicyLoanCredit;
import com.pactera.nci.components.policyloanrepayment.PolicyLoanRepaymentBaseInformation;
import com.pactera.nci.components.policyloss.PolicyDetails;
import com.pactera.nci.components.policypledgeloans.PolicyPledgeLoans;
import com.pactera.nci.components.policyreinstatement.PolicyReinstatement;
import com.pactera.nci.components.policyrele.PolicyReleActivity;
import com.pactera.nci.components.policysurrender.PolicySurrender;
import com.pactera.nci.components.receivechange.InsuredGetStyleChangeDetail;
import com.pactera.nci.components.receiveinforquery.RecQueFragment;
import com.pactera.nci.components.renewalpayment.ReneInfor;
import com.pactera.nci.components.renewalpaymentinforchange.ChangeInfor;
import com.pactera.nci.components.universalinsurancereceive.UniversalInsuranceReceive;
import com.pactera.nci.components.way_change.WayChange;
import com.pactera.nci.framework.BaseFragment;
import com.pactera.nci.framework.LoginActicity;

/**
 * 我的保险 - 所有类的保单列表
 * 
 * @author haix
 */
@SuppressLint("ValidFragment")
public class PolicyListView extends BaseFragment {

	private static String TAG = "PolicyListView";
	private ListView policyLv;
	/**
	 * 获取保单列表的单例
	 */
	private List<PolicyList> polList = null;
	/**
	 * 获取我的账户信息单例
	 */
	private SertificatonInfor sertificatonInfor = null;
	private WebSettings webseting;
	private String appCacheDir;
	private View thisView;
	private ProgressDialog pd;
	private CommonDialog commonDialog;
	private boolean THREAD = false;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				pd.dismiss();
				setContent();
				break;
			case 1:
				pd.dismiss();
				commonDialog = new CommonDialog(context, 1, "确定", null,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								commonDialog.dismiss();
								context.finish();
							}
						}, null, "提示", msg.obj.toString());
				commonDialog.show();
				break;
			case 10:
				pd.dismiss();
				commonDialog = new CommonDialog(context, 1, "确定", "取消",
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								commonDialog.dismiss();
								context.finish();
								context.startActivity(new Intent(context,
										PolicyReleActivity.class));
							}
						}, null, "提示", "您还没有进行保单关联,立即跳转到保单关联");
				commonDialog.show();
				break;
			case 8:
				pd.dismiss();
				if (msg.obj.toString().equals("查无数据")) {
					commonDialog = new CommonDialog(context, 1, "确定", "取消",
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									commonDialog.dismiss();
									context.finish();
									context.startActivity(new Intent(context,
											PolicyReleActivity.class));
								}
							}, null, "提示", context.getResources().getString(
									R.string.policyrele));
					commonDialog.show();
				} else {
					new CommonDialog(context, 1, "确定", "",
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									context.finish();
								}
							}, null, "提示", msg.obj.toString()).show();
				}
				break;
			case 99:
				pd.dismiss();
				commonDialog = new CommonDialog(context, 1, "确定", null,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								commonDialog.dismiss();
								UserInfoManager.getInstance().exitLogin();
								ExitApplication.getInstance().exitLogin(context);
								Intent intent = new Intent(context,
										LoginActicity.class);
								intent.putExtra("isExit", "true");
								startActivity(intent);
							}
						}, null, "提示", msg.obj.toString());
				commonDialog.show();
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 功能名称
	 */
	private String functionName;
	/**
	 * 点击了哪一项的全局变量
	 */
	private int pos;

	public PolicyListView(String functionName) {
		this.functionName = functionName;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.common_policylist, null);
		init(thisView, functionName);
		return thisView;
	}

	/**
	 * 请求数据
	 */
	@Override
	protected void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);
		if (NetUtil.hasNetWork(context)) {
			pd = new ProgressDialog(context);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("正在请求服务器...");
			pd.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					polList = PolicyList.getInstance(context,
							new ErrorPolicyCallBack() {

								@Override
								public void onFailure(int i, String resultMsg) {
									// TODO Auto-generated method stub
									THREAD = true;
									Message message = Message.obtain();
									message.what = i;
									message.obj = resultMsg;
									handler.sendMessage(message);

								}
							});
					if (THREAD == false) {
						sertificatonInfor = SertificatonInfor.getInstance(
								context, new ErrorCallBack() {

									@Override
									public void onFailure(int i,
											String resultMsg) {
										// TODO Auto-generated method stub
										THREAD = true;
										Message message = Message.obtain();
										message.what = i;
										message.obj = resultMsg;
										handler.sendMessage(message);

									}
								});

					}
					if (THREAD == false) {
						handler.sendEmptyMessage(0);
					}

				}
			}).start();

		} else {
			commonDialog = new CommonDialog(context, 2, "确定", "取消",
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							commonDialog.dismiss();
							Intent intent = null;
							// 判断手机系统的版本 即API大于10 就是3.0或以上版本
							if (android.os.Build.VERSION.SDK_INT > 10) {
								intent = new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS);
							} else {
								intent = new Intent();
								ComponentName component = new ComponentName(
										"com.android.settings",
										"com.android.settings.WirelessSettings");
								intent.setComponent(component);
								intent.setAction("android.intent.action.VIEW");
							}
							context.startActivity(intent);

						}
					}, new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							commonDialog.dismiss();
							if (context.getSupportFragmentManager()
									.getBackStackEntryCount() <= 1) {
								context.finish();
							} else {
								context.getSupportFragmentManager()
										.popBackStackImmediate();
							}
						}
					}, "网络设置提示", "网络连接不可用,是否进行设置?");
			commonDialog.show();

		}

	}

	/**
	 * 设置ListView,根据跳转过来的类名进行下一步操作
	 */
	private void setContent() {
		thisView.findViewById(R.id.pol_policylist_item)
				.setVisibility(View.GONE);
		policyLv = (ListView) thisView.findViewById(R.id.policylv);
		adapterHeader();
		policyLv.setAdapter(new MyAdapter(context));
		policyLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				String errMsg = "";
				// 投保人电话变更
				if (Constants.ApplicationTelChange.equals(functionName)) {
					if (position != 0) {
						position -= 1;
						errMsg = "您不是保单" + polList.get(position).getContNo()
								+ "的被保险人，无法对该保单进行投保人电话变更操作";
						if (isPolicyholder(polList.get(position).getRelation(),
								errMsg)) {
							thisManager
									.beginTransaction()
									.replace(
											R.id.tab_container,
											new TelchangeInfor(polList.get(
													position).getContNo(),
													polList.get(position)
															.getRiskName()))
									.addToBackStack(TAG).commit();
						}
					}

				}
				// 累计生息领取
				else if (Constants.AccumulatedInterestReceive
						.equals(functionName)) {
					if (position != 0) {
						position -= 1;
						errMsg = "您不是保单" + polList.get(position).getContNo()
								+ "的被保险人，无法对该保单进行累计生息部分领取操作";
						if (isInsured(polList.get(position).getRelation(),
								errMsg)) {
							thisManager
									.beginTransaction()
									.replace(
											R.id.tab_container,
											new AccumulatedInterestReceive(
													polList.get(position)
															.getContNo(),
													polList.get(position)
															.getRiskName()))
									.addToBackStack(
											Constants.AccumulatedInterestReceive)
									.commit();
						}
					}
				}
				// 年金满期金
				else if (Constants.AnnuityReceive.equals(functionName)) {
					if (position != 0) {
						position -= 1;
						errMsg = "您不是保单" + polList.get(position).getContNo()
								+ "的被保险人，无法对该保单进行年金、满期金领取操作";
						if (isInsured(polList.get(position).getRelation(),
								errMsg)) {
							final String ContNo = polList.get(position)
									.getContNo();
							final String riskName = polList.get(position)
									.getRiskName();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("cid", UserInfoManager.getInstance()
									.getCid());
							map.put("loginId", UserInfoManager.getInstance()
									.getUserName());
							map.put("sessionId", UserInfoManager.getInstance()
									.getSessionId());
							map.put("contNo", polList.get(position).getContNo());
							String para = JSON.toJSONString(map);
							/**
							 * 通过保单号获取该保单领取信息
							 */
							ServiceEngin.Request(context, "02_00_03_10_01_I01",
									"getAnnuityQuery", para, new EnginCallback(
											context) {
										public void onSuccess(ResponseInfo arg0) {
											super.onSuccess(arg0);
											canclDialog();// 取消对话框
											try {
												AnnuityReceiveParseJson(Des3
														.decode(arg0.result
																.toString()),
														ContNo, riskName);
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										};
									});
						}
					}
				}
				// 投保人资料变更
				else if (Constants.ApplicantInforChange.equals(functionName)) {
					errMsg = "您不是保单" + polList.get(position).getContNo()
							+ "的投保人，无法对该保单进行投保人资料变更操作";				
					if (OnlyisPolicyholder(polList.get(position).getRelation())) {
						VerifyEffectDate(position);
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new ApplicantInfoChangeDetail(polList
												.get(position).getContNo(),
												polList.get(position)
														.getRiskName()))
								.addToBackStack("ApplicantInfoChangeDetail")
								.commit();
					}
				}
				// 被保险人资料变更
				else if (Constants.InsurerInforChange.equals(functionName)) {
					errMsg = "您不是保单" + polList.get(position).getContNo()
							+ "的被保险人，无法对该保单进行被保险人资料变更操作";
					if (OnlyisInsured(polList.get(position).getRelation(),
							errMsg)) {
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new InsuredInfoChangeDetail(polList
												.get(position).getContNo(),
												polList.get(position)
														.getRiskName()))
								.addToBackStack("InsuredInfoChangeDetail")
								.commit();
					}
				}
				// // 交费信息查询
				// else if (Constants.PayInforQuery.equals(functionName)) {
				// startActivity(new Intent(context, PayQueActivity.class)
				// .putExtra("policyNo", polList.get(position)
				// .getContNo()));
				// }
				// 保单信息查询,(我的保单)
				else if (Constants.PolicyInforQuery.equals(functionName)) {
					thisManager
							.beginTransaction()
							.replace(
									R.id.tab_container,
									new PolicyInforQuery(polList.get(position)
											.getContNo(), polList.get(position)
											.getRiskName(), polList.get(
											position).getRelation()))
							.addToBackStack("PolicyInforList").commit();
				}
				// 贷款续贷
				else if (Constants.PolicyLoanCredit.equals(functionName)) {
					if (position != 0) {
						position -= 1;
						errMsg = "目前仅支持投被保险人为同一人的保单贷款续贷，如有需要请至我公司柜面申请";
						if (isHolder(polList.get(position).getRelation(),
								errMsg)) {
							thisManager
									.beginTransaction()
									.replace(
											R.id.tab_container,
											new PolicyLoanCredit(polList.get(
													position).getContNo(),
													polList.get(position)
															.getRiskName()))
									.addToBackStack("PolicyLoanCredit")
									.commit();

						}
					}
				}
				// 贷款清偿
				else if (Constants.PolicyLoanRepayment.equals(functionName)) {
					if (position != 0) {
						position -= 1;
						errMsg = "您不是保单" + polList.get(position).getContNo()
								+ "的被保险人，无法对该保单进行贷款清偿操作";
						if (isPolicyholder(polList.get(position).getRelation(),
								errMsg)) {
							thisManager
									.beginTransaction()
									.replace(
											R.id.tab_container,
											new PolicyLoanRepaymentBaseInformation(
													polList.get(position)
															.getContNo(),
													polList.get(position)
															.getRiskName()))
									.addToBackStack(
											"PolicyLoanRepaymentBaseInformation")
									.commit();

						}
					}

				}
				// 保单质押贷款
				else if (Constants.PolicyPledgeLoans.equals(functionName)) {
					if (position != 0) {
						position -= 1;
						errMsg = "目前仅支持投被保险人为同一人的保单质押贷款，如有需要请至我公司柜面申请";
						if (isHolder(polList.get(position).getRelation(),
								errMsg)) {
							thisManager
									.beginTransaction()
									.replace(
											R.id.tab_container,
											new PolicyPledgeLoans(polList.get(
													position).getContNo(),
													polList.get(position)
															.getRiskName()))
									.addToBackStack("PolicyPledgeLoans")
									.commit();

						}
					}

				}
				// 保单复效
				else if (Constants.PolicyReinstatement.equals(functionName)) {
					if (position != 0) {
						position -= 1;
						errMsg = "目前仅支持投被保险人为同一人的保单复效业务，如有需要请至我公司柜面申请";
						if (isHolder(polList.get(position).getRelation(),
								errMsg)) {
							thisManager
									.beginTransaction()
									.replace(
											R.id.tab_container,
											new PolicyReinstatement(polList
													.get(position).getContNo(),
													polList.get(position)
															.getRiskName()))
									.addToBackStack("PolicyReinstatement")
									.commit();

						}
					}

				}
				// 整单退保
				else if (Constants.PolicySurrender.equals(functionName)) {
					if (position != 0) {
						position -= 1;
						pos = position;
						errMsg = "您不是保单" + polList.get(position).getContNo()
								+ "的被保险人，无法对该保单进行整单退保操作";
						if (isPolicyholder(polList.get(position).getRelation(),
								errMsg)) {
							/**
							 * 通过保单号查询保单退保信息
							 */
							final String ContNo = polList.get(position)
									.getContNo();
							final String riskName = polList.get(position)
									.getRiskName();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("cid", UserInfoManager.getInstance()
									.getCid());
							map.put("loginId", UserInfoManager.getInstance()
									.getUserName());
							map.put("sessionId", UserInfoManager.getInstance()
									.getSessionId());
							map.put("contNo", polList.get(position).getContNo());
							String para = JSON.toJSONString(map);
							String bizId = "02_00_03_13_01_I01";
							String requstName = "allPolicySurrenderQuery";
							ServiceEngin.Request(context, bizId, requstName,
									para, new EnginCallback(context) {

										@Override
										public void onSuccess(ResponseInfo arg0) {
											super.onSuccess(arg0);
											canclDialog();
											try {
												Log.e("请求成功", Des3
														.decode(arg0.result
																.toString()));
												// 对返回的json字符串进行json解析
												policySurrenderParseJson(Des3
														.decode(arg0.result
																.toString()),
														ContNo, riskName);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									});
						}

					}
				}
				// 领取形式变更
				else if (Constants.Receivechange.equals(functionName)) {
					errMsg = "您不是保单" + polList.get(position).getContNo()
							+ "的被保险人，无法对该保单进行领取形式变更操作";
					if (OnlyisInsured(polList.get(position).getRelation(),
							errMsg)) {
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new InsuredGetStyleChangeDetail(polList
												.get(position).getContNo(),
												polList.get(position)
														.getRiskName()))
								.addToBackStack("InsuredGetStyleChangeDetail")
								.commit();
					}
				}
				// 生存/满期领取查询
				else if (Constants.ReceiveInforQuery.equals(functionName)) {
					Intent intent = new Intent(context, RecQueFragment.class);
					intent.putExtra("policyNo", polList.get(position)
							.getContNo());
					startActivity(intent);
				}
				// 续期缴费
				else if (Constants.Renewalpayment.equals(functionName)) {
					errMsg = "您不是保单" + polList.get(position).getContNo()
							+ "的投保人，无法对该保单进行续期缴费操作";
					if (isPolicyholder(polList.get(position).getRelation(),
							errMsg)) {

						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new ReneInfor(polList.get(position)
												.getContNo(), polList.get(
												position).getRiskName()))
								.addToBackStack("ReneInfor").commit();
					}

				}
				// 续期缴费信息变更
				else if (Constants.RenewalpaymentInforChange
						.equals(functionName)) {
					errMsg = "您不是保单" + polList.get(position).getContNo()
							+ "的投保人，无法对该保单进行缴费信息变更操作";
					if (isPolicyholder(polList.get(position).getRelation(),
							errMsg)) {
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new ChangeInfor(polList.get(position)
												.getContNo(), polList.get(
												position).getRiskName()))
								.addToBackStack("ChangeInfor").commit();
					}

				}
				// 万能险领取
				else if (Constants.UniversalInsuranceReceive
						.equals(functionName)) {
					if (position != 0) {
						position -= 1;
						errMsg = "您不是保单" + polList.get(position).getContNo()
								+ "的投保人，无法对该保单进行万能险部分领取操作";
						if (isPolicyholder(polList.get(position).getRelation(),
								errMsg)) {
							thisManager
									.beginTransaction()
									.replace(
											R.id.tab_container,
											new UniversalInsuranceReceive(
													polList.get(position)
															.getContNo(),
													polList.get(position)
															.getRiskName()))
									.addToBackStack(
											"UniversalInsuranceReceiveList")
									.commit();
						}
					}
				}

				// 保单挂失
				else if (Constants.PolicyLossActivity.equals(functionName)) {
					// 要求投保人
					if (polList.get(position).getRelation()
							.equals(Constants.Policyholder)
							|| polList.get(position).getRelation()
									.equals(Constants.PolicyholderWithInsured)) {
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new PolicyDetails(polList.get(position)
												.getContNo()))
								.addToBackStack(TAG).commit();
					} else {
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", "您不是保单"
										+ polList.get(position).getContNo()
										+ "的投保人，无法对该单进行保单挂失的操作！").show();
					}
				}
				// 附加险满期不续保
				else if (Constants.AccessoryRiskNotRenewalActivity
						.equals(functionName)) {
					// 要求投保人
					if (polList.get(position).getRelation()
							.equals(Constants.Policyholder)
							|| polList.get(position).getRelation()
									.equals(Constants.PolicyholderWithInsured)) {
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new AccessoryRiskNotRenewal(polList
												.get(position).getContNo()))
								.addToBackStack(TAG).commit();
					} else {
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", "您不是保单"
										+ polList.get(position).getContNo()
										+ "的投保人，无法对该单进行附加险满期不续保的操作！").show();
					}
				}
				// 附加满期低保额
				else if (Constants.AccessoryRiskLowCoverageRenewalActivity
						.equals(functionName)) {
					// 要求投保人
					if (polList.get(position).getRelation()
							.equals(Constants.Policyholder)
							|| polList.get(position).getRelation()
									.equals(Constants.PolicyholderWithInsured)) {
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new AccessoryRiskLowCoverageRenewal(
												polList.get(position)
														.getContNo()))
								.addToBackStack(TAG).commit();
					} else {
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", "您不是保单"
										+ polList.get(position).getContNo()
										+ "的投保人，无法对该单进行附加险满期降低保额续保的操作！").show();
					}
				}
				// 领取年龄变更
				else if (Constants.AgeChangeActivity.equals(functionName)) {
					// 要求投保人
					if (polList.get(position).getRelation()
							.equals(Constants.Policyholder)
							|| polList.get(position).getRelation()
									.equals(Constants.PolicyholderWithInsured)) {
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new AgeChange(polList.get(position)
												.getContNo()))
								.addToBackStack(TAG).commit();
					} else {
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", "您不是保单"
										+ polList.get(position).getContNo()
										+ "的投保人，无法对该单进行领取年龄变更的操作！").show();
					}
				}
				// 领取日期变更
				else if (Constants.DateChangeActivity.equals(functionName)) {
					// 要求投保人
					if (polList.get(position).getRelation()
							.equals(Constants.Insured)
							|| polList.get(position).getRelation()
									.equals(Constants.PolicyholderWithInsured)) {
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new DateChange(polList.get(position)
												.getContNo()))
								.addToBackStack(TAG).commit();
					} else {
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", "您不是保单"
										+ polList.get(position).getContNo()
										+ "的被保险人，无法对该单进行领取日期变更的操作！").show();
					}
				}
				// 领取方式变更
				else if (Constants.WayChangeActivity.equals(functionName)) {
					// 要求投保人
					if (polList.get(position).getRelation()
							.equals(Constants.Policyholder)
							|| polList.get(position).getRelation()
									.equals(Constants.PolicyholderWithInsured)) {
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new WayChange(polList.get(position)
												.getContNo()))
								.addToBackStack(TAG).commit();
					} else {
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", "您不是保单"
										+ polList.get(position).getContNo()
										+ "的投保人，无法对该单进行领取方式变更的操作！").show();
					}
				}
				// 保单迁移
				else if (Constants.PolicyTransferActivity.equals(functionName)) {
					// 要求投保人
					if (!polList.get(position).getRelation()
							.equals(Constants.Policyholder)
							|| polList.get(position).getRelation()
									.equals(Constants.PolicyholderWithInsured)) {
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new PolicyTransfer(polList
												.get(position).getContNo()))
								.addToBackStack(TAG).commit();
					} else {
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", "您不是保单"
										+ polList.get(position).getContNo()
										+ "的投保人，无法对该单进行保单迁移的操作！").show();
					}
				}
				// 主险续保
				else if (Constants.MainRisksRenewalActivity
						.equals(functionName)) {
					// 要求投保人
					if (polList.get(position).getRelation()
							.equals(Constants.Insured)
							|| polList.get(position).getRelation()
									.equals(Constants.PolicyholderWithInsured)) {
						thisManager
								.beginTransaction()
								.replace(
										R.id.tab_container,
										new MainRisksRenewal(polList.get(
												position).getContNo()))
								.addToBackStack(TAG).commit();
					} else {
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", "您不是保单"
										+ polList.get(position).getContNo()
										+ "的被保险人，无法对该单进行主险续保的操作！").show();
					}
				}

			}
		});

	}

	/**
	 * 对返回的字符串进行json解析
	 * 
	 * @param json
	 */
	private void parseJson(String json) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObjs = JSON.parseObject(json);
			String ResultCode = jsonObjs.getString("ResultCode");
			String ResultMsg = jsonObjs.getString("ResultMsg");
			if (ResultCode.equals("0") && ResultCode != null) {
				JSONObject jsonObjs1 = jsonObjs
						.getJSONObject("PolicySurrender");
				JSONArray jsonObjs2 = jsonObjs.getJSONArray("riskMessages");
				String terminateState = jsonObjs1.getString("terminateState");
				if (!terminateState.equals("未终止")) {
					Toast.makeText(context, "此保单已经终止,无法申请退保业务!",
							Toast.LENGTH_LONG).show();
				} else {
					String appName = jsonObjs1.getString("appName");
					String contNo = jsonObjs1.getString("contNo");
					String loanState = jsonObjs1.getString("loanState");
					String insName = jsonObjs1.getString("insName");

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("appName", appName);
					map.put("contNo", contNo);
					map.put("loanState", loanState);
					map.put("insName", insName);
					list.add(map);
					for (int i = 0; i < jsonObjs2.size(); i++) {
						JSONObject jsonObj = jsonObjs2.getJSONObject(i);
						String riskCode = jsonObj.getString("riskCode");
						String riskName = jsonObj.getString("riskName");
						String standPrem = jsonObj.getString("standPrem");
						map = new HashMap<String, String>();
						map.put("riskCode", riskCode);
						map.put("riskName", riskName);
						map.put("standPrem", standPrem);
						list.add(map);
					}
					Intent intent = new Intent(context, PolicySurrender.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("information", list);
					intent.putExtras(bundle);
					intent.putExtra("policyNo", polList.get(pos).getContNo());
					intent.putExtra("policyName", polList.get(pos)
							.getRiskName());
					startActivity(intent);
				}
			} else if (jsonObjs.get("ResultCode") != null
					&& !jsonObjs.get("ResultCode").toString().equals("99")) {
				new AlertDialog.Builder(context).setTitle("提示")
						.setMessage(ResultMsg).setPositiveButton("确定", null)
						.create().show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 设置列表头html 内容
	 */
	private void adapterHeader() {

		View view = LayoutInflater.from(context).inflate(
				R.layout.common_policylist_head, null);
		WebView view1 = (WebView) view.findViewById(R.id.pol_policylist_ll);
		webseting = view1.getSettings();
		webseting.setDomStorageEnabled(true);
		webseting.setAppCacheMaxSize(1024 * 1024 * 8);
		appCacheDir = context.getDir("cache", Context.MODE_PRIVATE).getPath();
		webseting.setAppCachePath(appCacheDir);
		webseting.setAllowFileAccess(true);
		webseting.setAppCacheEnabled(true);
		webseting.setCacheMode(WebSettings.LOAD_DEFAULT);
		view1.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReachedMaxAppCacheSize(long spaceNeeded,
					long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(spaceNeeded * 2);
			}
		});
		if (Constants.ApplicationTelChange.equals(functionName)) {
			view1.loadUrl(Constants.URLHTML5+"/html5/tips/ApplicationTelchangeTip.html");
			policyLv.addHeaderView(view);
		} else if (Constants.AccumulatedInterestReceive.equals(functionName)) {
			view1.loadUrl(Constants.URLHTML5+"/html5/tips/AccumulatedInterestReceiveAgreementTip.html");
			policyLv.addHeaderView(view);
		} else if (Constants.AnnuityReceive.equals(functionName)) {
			view1.loadUrl(Constants.URLHTML5+"/html5/tips/AnnuityReceiveAgreementTip.html");
			policyLv.addHeaderView(view);
		} else if (Constants.PolicyLoanCredit.equals(functionName)) {
			view1.loadUrl(Constants.URLHTML5+"/html5/tips/PolicyLoanCreditTip.html");
			policyLv.addHeaderView(view);
		} else if (Constants.PolicyLoanRepayment.equals(functionName)) {
			view1.loadUrl(Constants.URLHTML5+"/html5/tips/PolicyLoanRepaymentTip.html");
			policyLv.addHeaderView(view);
		} else if (Constants.PolicyPledgeLoans.equals(functionName)) {
			view1.loadUrl(Constants.URLHTML5+"/html5/tips/PolicyPledgeLoansTip.html");
			policyLv.addHeaderView(view);
		} else if (Constants.PolicyReinstatement.equals(functionName)) {
			view1.loadUrl(Constants.URLHTML5+"/html5/tips/PolicyReinstatementTip.html");
			policyLv.addHeaderView(view);
		} else if (Constants.PolicySurrender.equals(functionName)) {
			view1.loadUrl(Constants.URLHTML5+"/html5/tips/PolicySurrenderTip.html");
			policyLv.addHeaderView(view);
		} else if (Constants.UniversalInsuranceReceive.equals(functionName)) {
			view1.loadUrl(Constants.URLHTML5+"/html5/tips/UniversalInsuranceReceiveAgreementTip.html");
			policyLv.addHeaderView(view);
		}

	}

	/**
	 * 仅投保人资料变更有此限制
	 * 
	 * @param position
	 * @return
	 */
	private boolean VerifyEffectDate(final int position) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String oneMonthBeforeDate = sdf.format(calendar.getTime());
		// 若保单生效未满一个月
		if (polList.get(position).getEffectDate().compareTo(oneMonthBeforeDate) > 0) {
			PreserveValidateAndWarn
					.getInstance()
					.showPolListDialog(
							context,
							"您选择的保单"
									+ polList.get(position).getContNo()
									+ "由于生效期不满一个月，为了确保您的信息安全性，暂不能通过网站修改联系电话。如您仍需办理，请携带有效证件到公司客服中心亲自办理。",
							null, 0, true);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return 是否投保人/同时判断了是否进行过实名认证
	 */
	private boolean isPolicyholder(String relation, String errMsg) {
		// 认证状态判断
		TipUitls.Log(TAG, "relation1----->" + relation);
		if (!sertificatonInfor.sertificationStatus()) {
			commonDialog = new CommonDialog(context, 1, "确定", null,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							commonDialog.dismiss();
						}
					}, null, "提示", getResources().getString(
							R.string.policylist_sertificationStatus));
			commonDialog.show();
			return false;
		} else if (!relation.equals(Constants.Policyholder)
				&& !relation.equals(Constants.PolicyholderWithInsured)) {
			commonDialog = new CommonDialog(context, 1, "确定", null,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							commonDialog.dismiss();
						}
					}, null, "提示", errMsg);
			commonDialog.show();

			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return 仅判断是否投保人/不判断是否进行过实名认证
	 */
	private boolean OnlyisPolicyholder(String relation) {
		// 认证状态判断
		TipUitls.Log(TAG, "relation1----->" + relation);
		if (!relation.equals(Constants.Policyholder)
				&& !relation.equals(Constants.PolicyholderWithInsured)) {
			commonDialog = new CommonDialog(context, 1, "确定", null,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							commonDialog.dismiss();
						}
					}, null, "提示", "您无权申请此变更业务");
			commonDialog.show();

			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return 是否被保险人/同时判断是否实名认证过
	 */
	private boolean isInsured(String relation, String errMsg) {
		TipUitls.Log(TAG, "relation2----->" + relation);
		if (!sertificatonInfor.sertificationStatus()) {
			commonDialog = new CommonDialog(context, 1, "确定", null,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							commonDialog.dismiss();
						}
					}, null, "提示", getResources().getString(
							R.string.policylist_sertificationStatus));
			commonDialog.show();
			return false;
		} else if (!relation.equals(Constants.Insured)
				&& !relation.equals(Constants.PolicyholderWithInsured)) {
			commonDialog = new CommonDialog(context, 1, "确定", null,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							commonDialog.dismiss();
						}
					}, null, "提示", errMsg);
			commonDialog.show();
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @return 仅判断是否被保险人/不判断是否实名认证过
	 */
	private boolean OnlyisInsured(String relation, String errMsg) {
		TipUitls.Log(TAG, "relation2----->" + relation);
		if (!relation.equals(Constants.Insured)
				&& !relation.equals(Constants.PolicyholderWithInsured)) {
			commonDialog = new CommonDialog(context, 1, "确定", null,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							commonDialog.dismiss();
						}
					}, null, "提示", errMsg);
			commonDialog.show();
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @return 是否 即是投保人又是被保险人
	 */
	private boolean isHolder(String relation, String errMsg) {
		TipUitls.Log(TAG, "relation3----->" + relation);
		if (!sertificatonInfor.sertificationStatus()) {
			commonDialog = new CommonDialog(context, 1, "确定", null,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							commonDialog.dismiss();
						}
					}, null, "提示", getResources().getString(
							R.string.policylist_sertificationStatus));
			commonDialog.show();
			return false;
		} else if (!relation.equals(Constants.PolicyholderWithInsured)) {
			commonDialog = new CommonDialog(context, 1, "确定", null,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							commonDialog.dismiss();
						}
					}, null, "提示", errMsg);
			commonDialog.show();

			return false;
		}
		return true;
	}

	/**
	 * adpater
	 * 
	 * @author haix
	 * 
	 */
	public class MyAdapter extends BaseAdapter {
		private LayoutInflater layoutInflater;

		public MyAdapter(Context context) {
			this.layoutInflater = LayoutInflater.from(context);

		}

		public int getCount() {
			return polList == null ? 0 : polList.size();
		}

		public PolicyList getItem(int position) {
			return polList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(
						R.layout.common_policylist_item, null);
				viewHolder.policyNO = (TextView) convertView
						.findViewById(R.id.pol_policylist_item_tv_policyno);
				viewHolder.firstRight = (TextView) convertView
						.findViewById(R.id.pol_policylist_item_tv_firstright);
				viewHolder.secondRight = (TextView) convertView
						.findViewById(R.id.pol_policylist_item_tv_secondright);
				viewHolder.thirdRight = (TextView) convertView
						.findViewById(R.id.pol_policylist_item_tv_thirdright);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			PolicyList policy = polList.get(position);
			viewHolder.policyNO.setText(policy.getContNo());
			viewHolder.firstRight.setText(policy.getRiskName());
			viewHolder.secondRight.setText(policy.getEffectDate());
			viewHolder.thirdRight.setText(policy.getAdministration());
			return convertView;
		}
	}

	private class ViewHolder {
		private TextView policyNO;
		private TextView firstRight;
		private TextView secondRight;
		private TextView thirdRight;
	}

	/**
	 * 整单退保的解析
	 */
	private void policySurrenderParseJson(String result, String policyNo,
			String riskName) {
		try {
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map;
			CommonDialog dialog;
			JSONObject jsonObjs = JSONObject.parseObject(result);
			String ResultCode = jsonObjs.getString("ResultCode");
			String ResultMsg = jsonObjs.getString("ResultMsg");
			if (ResultCode.equals("0") && ResultCode != null) {
				JSONObject jsonObjs1 = jsonObjs
						.getJSONObject("PolicySurrender");
				JSONArray jsonObjs2 = jsonObjs.getJSONArray("riskMessages");
				String terminateState = jsonObjs1.getString("terminateState");
				if (!terminateState.equals("未终止")) {
					Toast.makeText(context, "该保单已终止，您无需再次操作退保业务!",
							Toast.LENGTH_LONG).show();
				} else {
					String appName = jsonObjs1.getString("appName");
					String contNo = jsonObjs1.getString("contNo");
					String loanState = jsonObjs1.getString("loanState");
					String insName = jsonObjs1.getString("insName");

					map = new HashMap<String, String>();
					map.put("appName", appName);
					map.put("contNo", contNo);
					map.put("loanState", loanState);
					map.put("insName", insName);
					list.add(map);
					for (int i = 0; i < jsonObjs2.size(); i++) {
						JSONObject jsonObj = jsonObjs2.getJSONObject(i);
						String riskCode = jsonObj.getString("riskCode");
						String riskname = jsonObj.getString("riskName");
						String standPrem = jsonObj.getString("standPrem");
						map = new HashMap<String, String>();
						map.put("riskCode", riskCode);
						map.put("riskName", riskname);
						map.put("standPrem", standPrem);
						list.add(map);
					}
					thisManager
							.beginTransaction()
							.replace(
									R.id.tab_container,
									new PolicySurrender(list, polList.get(pos)
											.getContNo(), polList.get(pos)
											.getRiskName()))
							.addToBackStack("PolicySurrender").commit();

				}
			} else if (jsonObjs.get("ResultCode") != null
					&& !jsonObjs.get("ResultCode").toString().equals("99")) {
				commonDialog = new CommonDialog(context, 1, "确定", null,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								commonDialog.dismiss();
								thisManager.popBackStackImmediate();
							}
						}, null, "提示	", ResultMsg);
				commonDialog.show();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 年金满期金需要先查询该保单的unicustomid来进行判断是否第一被保险人
	 */
	private void AnnuityReceiveParseJson(String result, String policyNo,
			String riskName) {
		JSONObject obj = JSON.parseObject(result);
		if (obj.get("ResultCode") != null
				&& Integer.parseInt(obj.get("ResultCode").toString()) == 0) {// 请求成功
			JSONArray myArray = obj.getJSONArray("RiskList");
			ArrayList<Object> list = new ArrayList<Object>();
			for (int i = 0; i < myArray.size(); i++) {
				ArrayList<Object> riskList = new ArrayList<Object>();
				JSONObject o = myArray.getJSONObject(i);
				JSONArray myArray1 = o.getJSONArray("getAnnuityList");
				ArrayList<Map<String, String>> annuityList = new ArrayList<Map<String, String>>();
				for (int j = 0; j < myArray1.size(); j++) {
					JSONObject oo = myArray1.getJSONObject(j);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("itemName", oo.getString("itemName"));
					map.put("dutyCode", oo.getString("dutyCode"));
					map.put("getDate", oo.getString("getDate"));
					map.put("payMoney", oo.getString("payMoney"));
					map.put("payType", oo.getString("payType"));
					annuityList.add(map);
				}
				HashMap<String, String> map1 = new HashMap<String, String>();
				map1.put("riskCode", o.getString("riskCode"));
				map1.put("riskName", o.getString("riskName"));
				map1.put("getMoney", o.getString("getMoney"));
				riskList.add(annuityList);
				riskList.add(map1);
				list.add(riskList);
			}

			String coreCid = obj.getString("CoreCid");
			// 校验是否第一被保险人
			if (sertificatonInfor.getUniCustmorId().equals(coreCid)) {
				// if (true) {//忽略此上边的判断
				thisManager
						.beginTransaction()
						.replace(
								R.id.tab_container,
								new AnnuityReceive(coreCid, policyNo, riskName,
										list))
						.addToBackStack(Constants.AnnuityReceive).commit();
			} else {
				new CommonDialog(context, 1, "确定", null, null, null, "提示",
						"您无权申请此变更业务").show();
			}
		} else if (obj.get("ResultCode") != null
				&& !obj.get("ResultCode").toString().equals("99")) {// 请求不成功
			commonDialog = new CommonDialog(context, 1, "确定", null,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							commonDialog.dismiss();
							// thisManager.popBackStackImmediate();
						}
					}, null, "提示", obj.get("ResultMsg").toString());
			commonDialog.show();
		}
	}

}
