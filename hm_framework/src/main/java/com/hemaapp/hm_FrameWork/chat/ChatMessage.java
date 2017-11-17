package com.hemaapp.hm_FrameWork.chat;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 聊天消息
 */
public class ChatMessage extends XtomObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6426320190009387816L;
	private int id = 0;// 该消息在本地数据库中的id
	private String fromjid;// 发送JID标识',
	private String tojid;// '接收JID标识',
	private String regdatedatetime;// '记录时间',

	private String stanza;// 原始数据包内容',
	private String dxpacktype;// 自定义数据包类型，赋值规则：（1：文本 2：图片 3：音频 4：视频）
	private String dxclientavatar;// 消息发送用户头像，赋值规则: http://图片绝对地址
	private String dxclientname;// 消息发送用户昵称，赋值规则: 名称字符串
	private String dxclientype;// 发送用户类型，赋值规则: 1：A型用户 2：B型用户（随业务而异，否则固定传1）
	private String dxgroupname; // 群组标题
	private String dxgroupavatar; // 群组图片
	private String dxgroupid; // 群组主键ID
								// (单聊时固定为0，群聊时即为群组主键；客户端可以根据此值是否为0来判断是单聊还是群聊)
	private String dxextend; // 业务耦合扩展属性存储区，多个以英文逗号分隔
	private String dxdetail; // 当dxpacktype=3或4时，封装相关属性串，多个以英文逗号分隔

	private String dxlocalpath; // 本地资源路径

	private String isread;// 是否已读 0未读1已读
	private String islisten;// 如果是语音，是否已听0未听1已听
	private String issend;// 是否发送成功0失败1成功2发送中

	private boolean isTimeVisiable = false;// 时间是否显示
	private String toavatar;
	private String tonickname;

	private String dxpackid; // packid

	private boolean voicePlaying;// 音频是否正在播放

	private String myextend;// 我发送的消息在首页需要显示的extend字段内容

	public ChatMessage(String fromjid, String tojid, String stanza,
			String regdatedatetime, String dxpacktype, String dxclientavatar,
			String dxclientname, String dxclientype, String dxgroupname,
			String dxgroupavatar, String dxgroupid, String dxextend,
			String dxdetail, String isread, String islisten, String issend,
			String toavatar, String tonickname, String dxpackid,
			String dxlocalpath) {
		super();
		this.fromjid = fromjid;
		this.tojid = tojid;
		this.stanza = stanza;
		this.regdatedatetime = regdatedatetime;
		this.dxpacktype = dxpacktype;
		this.dxclientavatar = dxclientavatar;
		this.dxclientname = dxclientname;
		this.dxclientype = dxclientype;
		this.dxgroupname = dxgroupname;
		this.dxgroupavatar = dxgroupavatar;

		this.dxgroupid = dxgroupid;
		this.dxextend = dxextend;
		this.dxdetail = dxdetail;

		this.isread = isread;
		this.islisten = islisten;
		this.issend = issend;
		this.toavatar = toavatar;
		this.tonickname = tonickname;

		this.dxpackid = dxpackid;
		this.dxlocalpath = dxlocalpath;
		log_i(toString());
	}

	@Override
	public String toString() {
		return "ChatMessage [id=" + id + ", fromjid=" + fromjid + ", tojid="
				+ tojid + ", regdatedatetime=" + regdatedatetime + ", stanza="
				+ stanza + ", dxpacktype=" + dxpacktype + ", dxclientavatar="
				+ dxclientavatar + ", dxclientname=" + dxclientname
				+ ", dxclientype=" + dxclientype + ", dxgroupname="
				+ dxgroupname + ", dxgroupavatar=" + dxgroupavatar
				+ ", dxgroupid=" + dxgroupid + ", dxextend=" + dxextend
				+ ", dxdetail=" + dxdetail + ", dxlocalpath=" + dxlocalpath
				+ ", isread=" + isread + ", islisten=" + islisten + ", issend="
				+ issend + ", isTimeVisiable=" + isTimeVisiable + ", toavatar="
				+ toavatar + ", tonickname=" + tonickname + ", dxpackid="
				+ dxpackid + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFromjid() {
		return fromjid;
	}

	public String getTojid() {
		return tojid;
	}

	public String getStanza() {
		return stanza;
	}

	public String getRegdatedatetime() {
		return regdatedatetime;
	}

	public String getdxpacktype() {
		return dxpacktype;
	}

	public String getdxavatar() {
		return dxclientavatar;
	}

	public String getdxnickname() {
		return dxclientname;
	}

	public String getdxdetail() {
		return dxdetail;
	}

	public String getdxextend() {
		return dxextend;
	}

	public String getdxgroupid() {
		return dxgroupid;
	}

	/**
	 * 消息发送用户类型，赋值规则: 1：A型用户 2：B型用户（随业务而异，否则固定传1）
	 * 
	 * @return
	 */
	public String getdxclienttype() {
		return dxclientype;
	}

	public boolean isTimeVisiable() {
		return isTimeVisiable;
	}

	public void setTimeVisiable(boolean isTimeVisiable) {
		this.isTimeVisiable = isTimeVisiable;
	}

	public String getIsread() {
		return isread;
	}

	public String getIslisten() {
		return islisten;
	}

	public String getIssend() {
		return issend;
	}

	public void setIssend(String issend) {
		this.issend = issend;
	}

	public String getdxgroupname() {
		return dxgroupname;
	}

	public String getdxgroupavatar() {
		return dxgroupavatar;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

	public void setIslisten(String islisten) {
		this.islisten = islisten;
	}

	public String getTonickname() {
		return tonickname;
	}

	public void setTonickname(String tonickname) {
		this.tonickname = tonickname;
	}

	public String getToavatar() {
		return toavatar;
	}

	public void setToavatar(String toavatar) {
		this.toavatar = toavatar;
	}

	public String getdxpackid() {
		return dxpackid;
	}

	public String getdxlocalpath() {
		return dxlocalpath;
	}

	/**
	 * @param fromjid
	 *            the fromjid to set
	 */
	public void setFromjid(String fromjid) {
		this.fromjid = fromjid;
	}

	/**
	 * @param tojid
	 *            the tojid to set
	 */
	public void setTojid(String tojid) {
		this.tojid = tojid;
	}

	/**
	 * @param regdatedatetime
	 *            the regdatedatetime to set
	 */
	public void setRegdatedatetime(String regdatedatetime) {
		this.regdatedatetime = regdatedatetime;
	}

	/**
	 * @param stanza
	 *            the stanza to set
	 */
	public void setStanza(String stanza) {
		this.stanza = stanza;
	}

	/**
	 * @param dxpacktype
	 *            the dxpacktype to set
	 */
	public void setDxpacktype(String dxpacktype) {
		this.dxpacktype = dxpacktype;
	}

	/**
	 * @param dxclientavatar
	 *            the dxclientavatar to set
	 */
	public void setDxclientavatar(String dxclientavatar) {
		this.dxclientavatar = dxclientavatar;
	}

	/**
	 * @param dxclientname
	 *            the dxclientname to set
	 */
	public void setDxclientname(String dxclientname) {
		this.dxclientname = dxclientname;
	}

	/**
	 * @param dxclientype
	 *            the dxclientype to set
	 */
	public void setDxclientype(String dxclientype) {
		this.dxclientype = dxclientype;
	}

	/**
	 * @param dxgroupname
	 *            the dxgroupname to set
	 */
	public void setDxgroupname(String dxgroupname) {
		this.dxgroupname = dxgroupname;
	}

	/**
	 * @param dxgroupavatar
	 *            the dxgroupavatar to set
	 */
	public void setDxgroupavatar(String dxgroupavatar) {
		this.dxgroupavatar = dxgroupavatar;
	}

	/**
	 * @param dxgroupid
	 *            the dxgroupid to set
	 */
	public void setDxgroupid(String dxgroupid) {
		this.dxgroupid = dxgroupid;
	}

	/**
	 * @param dxextend
	 *            the dxextend to set
	 */
	public void setDxextend(String dxextend) {
		this.dxextend = dxextend;
	}

	/**
	 * @param dxdetail
	 *            the dxdetail to set
	 */
	public void setDxdetail(String dxdetail) {
		this.dxdetail = dxdetail;
	}

	/**
	 * @param dxlocalpath
	 *            the dxlocalpath to set
	 */
	public void setDxlocalpath(String dxlocalpath) {
		this.dxlocalpath = dxlocalpath;
	}

	/**
	 * @param dxpackid
	 *            the dxpackid to set
	 */
	public void setDxpackid(String dxpackid) {
		this.dxpackid = dxpackid;
	}

	/**
	 * @return the voicePlaying
	 */
	public boolean isVoicePlaying() {
		return voicePlaying;
	}

	/**
	 * @param voicePlaying
	 *            the voicePlaying to set
	 */
	public void setVoicePlaying(boolean voicePlaying) {
		this.voicePlaying = voicePlaying;
	}

	/**
	 * @return the myextend
	 */
	public String getMyextend() {
		return myextend;
	}

	/**
	 * @param myextend the myextend to set
	 */
	public void setMyextend(String myextend) {
		this.myextend = myextend;
	}

}
