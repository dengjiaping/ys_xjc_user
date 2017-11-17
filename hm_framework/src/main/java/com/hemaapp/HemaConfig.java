package com.hemaapp;

/**
 * 该项目配置信息
 */
public class HemaConfig {
	/**
	 * 网络请求连接超时时限(单位:毫秒)
	 */
	public static final int TIMEOUT_HTTP = 30000;
	/**
	 * 网络请求尝试次数
	 */
	public static final int TRYTIMES_HTTP = 1;
	/**
	 * 登录请求id
	 */
	public static final int ID_LOGIN = -1;
	/**
	 * 第三方登录请求id
	 */
	public static final int ID_THIRDSAVE = -2;
	/**
	 * 是否启用友盟统计
	 */
	public static boolean UMENG_ENABLE = false;
	/**
	 * 是否显示网络不给力异常Toast提示
	 */
	public static boolean TOAST_NET_ENABLE = true;
	/**
	 * 群聊红包类型  默认101
	 */
	public static final String DXPACKTYPE = "101";
	/**
	 * 群红包接收id分割符号stanza 第二个字符 为接收人id
	 */
	public static final String CHAT_SEPARTOR = "###";
	
}
