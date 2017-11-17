package com.hemaapp.hm_FrameWork.chat;

import xtom.frame.XtomObject;

/**
 *
 */
public class FirPagCount extends XtomObject {
	private String content;// 内容
	private String time;// 时间
	private String count;// 计数个数
	private String dxclientid;// 发送者id

	private String dxpacktype;// （1：文本 2：图片 3：音频 4：视频）
	private String dxclientavatar;// 消息发送用户头像，赋值规则: http://图片绝对地址
	private String dxclientname; // 消息发送用户昵称，赋值规则: 名称字符串
	private String dxclientype;// 发送用户类型
	private String dxgroupname; // 群组标题
	private String dxgroupavatar; // 群组图片
	private String dxgroupid; // 群组主键ID
	private String dxextend; // 业务耦合扩展属性存储区，多个以英文逗号分隔
	private String dxdetail; // 当dxpacktype=3或4时，封装相关属性串，多个以英文逗号分隔

	/**
	 * @param content
	 *            聊天内容
	 * @param time
	 *            时间
	 * @param count
	 *            未读数目
	 * @param dxclientid
	 *            发送者id
	 * @param dxpacktype
	 *            （1：文本 2：图片 3：音频 4：视频）
	 * @param dxclientavatar
	 *            消息发送用户头像，赋值规则: http://图片绝对地址
	 * @param dxclientname
	 *            发送用户类型
	 * @param dxgroupname
	 *            群组标题
	 * @param dxgroupavatar
	 *            群组图片
	 * @param dxgroupid
	 *            群组主键ID
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 * @param dxdetail
	 *            当dxpacktype=3或4时，封装相关属性串，多个以英文逗号分隔
	 */
	public FirPagCount(String content, String time, String count,
			String dxclientid, String dxpacktype, String dxclientavatar,
			String dxclientname, String dxclientype, String dxgroupname,
			String dxgroupavatar, String dxgroupid, String dxextend,
			String dxdetail) {
		super();
		this.content = content;
		this.time = time;
		this.count = count;
		this.dxclientid = dxclientid;
		this.dxpacktype = dxpacktype;
		this.dxclientavatar = dxclientavatar;
		this.dxclientname = dxclientname;
		this.dxclientype = dxclientype;
		this.dxgroupname = dxgroupname;
		this.dxgroupavatar = dxgroupavatar;
		this.dxgroupid = dxgroupid;
		this.dxextend = dxextend;
		this.dxdetail = dxdetail;
		log_i(toString());
	}

	@Override
	public String toString() {
		return "FirPagCount [content=" + content + ", time=" + time
				+ ", count=" + count + ",dxclientid=" + dxclientid
				+ ", dxpacktype=" + dxpacktype + ", dxclientavatar="
				+ dxclientavatar + ", dxclientname=" + dxclientname
				+ ", dxclientype=" + dxclientype + ", dxgroupname="
				+ dxgroupname + ", dxgroupavatar=" + dxgroupavatar
				+ ", dxgroupid=" + dxgroupid + ", dxextend=" + dxextend + "]";
	}

	public String getcontent() {
		return content;
	}

	public String gettime() {
		return time;
	}

	public String getcount() {
		return count;
	}

	public String getdxclientid() {
		return dxclientid;
	}

	public String getdxpacktype() {
		return dxpacktype;
	}

	public String getdxclientavatar() {
		return dxclientavatar;
	}

	public String getdxclientname() {
		return dxclientname;
	}

	public String getdxclientype() {
		return dxclientype;
	}

	public String getdxgroupname() {
		return dxgroupname;
	}

	public String getdxgroupavatar() {
		return dxgroupavatar;
	}

	public String getdxgroupid() {
		return dxgroupid;
	}

	public String getdxextend() {
		return dxextend;
	}

	public String getdxdetail() {
		return dxdetail;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setdxclientname(String dxclientname) {
		this.dxclientname = dxclientname;
	}

	/**
	 * @return the dxpacktype
	 */
	public String getDxpacktype() {
		return dxpacktype;
	}

	/**
	 * @param dxpacktype
	 *            the dxpacktype to set
	 */
	public void setDxpacktype(String dxpacktype) {
		this.dxpacktype = dxpacktype;
	}

	public void setDxclientavatar(String dxclientavatar) {
		this.dxclientavatar = dxclientavatar;
	}
	
	

}
