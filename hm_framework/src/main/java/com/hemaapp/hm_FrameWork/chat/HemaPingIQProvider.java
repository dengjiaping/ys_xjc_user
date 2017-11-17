package com.hemaapp.hm_FrameWork.chat;

import org.jivesoftware.smack.packet.IQ;  
import org.jivesoftware.smack.provider.IQProvider;  
import org.xmlpull.v1.XmlPullParser;  


/***
 * 功能描述：通过XEP-0199扩展协议实现心跳包
 * 程序作者：WHB  
 * 编写时间：[2014-4-1]-[下午1:23:39]
 */
public class HemaPingIQProvider implements IQProvider {    
    @Override  
    public IQ parseIQ(XmlPullParser parser) throws Exception {  
        // TODO Auto-generated method stub  
        HemaPingIQ iq = new HemaPingIQ();  
        return iq;  
    }    
}  