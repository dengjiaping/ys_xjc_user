package com.hemaapp.hm_FrameWork.chat;


public interface ChatSendListener {
	public void noStart(ChatMessage cMessage);
	
	public void noConnect(ChatMessage cMessage);

	public void sucess(ChatMessage cMessage);

	public void failed(ChatMessage cMessage);
}
