package com.hemaapp.hm_FrameWork.chat;
import org.jivesoftware.smack.packet.IQ;  

/***
 * 功能描述：通过XEP-0199扩展协议实现心跳包
 * 程序作者：WHB  
 * 编写时间：[2014-4-1]-[下午1:23:39]
 */
public class HemaPingIQ extends IQ {  
    public static final String ELEMENT = "ping";  
    public static final String NAMESPACE = "urn:xmpp:ping";    
    @Override  
    public String getChildElementXML() {  
        StringBuffer sb = new StringBuffer();  
        sb.append("<").append(ELEMENT).append(" xmlns=\"").append(NAMESPACE)  
                .append("\">");    
        sb.append("</").append(ELEMENT).append(">");  
        return sb.toString();    
    }    
    
}  