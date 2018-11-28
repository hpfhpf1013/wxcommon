package cn.mangot.wxcommon.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/** 
 * @description: 从request里面获得IP 
 * @author pengfei.he
 * @date 2011-7-1
 */
public class IpUtils {

	public static String getIp(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return IpUtils.getIpAddress(request);
	}
	public static String getIpAddress(HttpServletRequest request)
	{
		String ip=request.getHeader("X-Forwarded-For");
		if(ip == null || ip.length() == 0) {
			ip=request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0) {
			ip=request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0) {
			ip=request.getRemoteAddr();
		}
		if(ip!=null) {
			String[] temp = ip.split(",");
			if(temp.length>1)
				ip = temp[0];
		}
		return ip.trim();
	}
	
	/**
	 * 
	 * @Description 返回所有网络接口
	 * 
	 * @author chen.su
	 * @date 2013-6-4 下午05:58:53 
	 * @return
	 * @throws SocketException NetworkInterface[]
	 */
	public static NetworkInterface[] getAllNetworkInterface() throws SocketException {
		Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
		List<NetworkInterface> list = new ArrayList<NetworkInterface>();
		
		for( ;enumeration.hasMoreElements(); ) {
			list.add(enumeration.nextElement());
		}
		
		return list.toArray(new NetworkInterface[list.size()]);
	}
	
	/**
	 * 
	 * @Description 返回物理地址, 每一字节用冒号分割.
	 * @author chen.su
	 * @date 2013-6-4 下午05:46:54 
	 * @return String
	 * @throws SocketException 
	 */
	public static String getPhysicalAddress( NetworkInterface networkInterface ) throws SocketException {
		byte[] bytearray = networkInterface.getHardwareAddress();
		
		return bytesToHexString( bytearray );
	}
	
	/**
	 * 
	 * @Description 
	 * @author chen.su
	 * @date 2013-6-4 下午05:54:35 
	 * @param src
	 * @return String
	 */
	public static String bytesToHexString(byte[] src) {
		if( src == null || src.length == 0 ) {
			return null;
		}
		
	    StringBuilder stringBuilder = new StringBuilder();
	    
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }
	    
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;
	        
	        String hv = Integer.toHexString(v);
	        
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }
	        
	        stringBuilder.append(hv).append(":");  
	    }
	    
	    return stringBuilder.substring(0, stringBuilder.toString().length() - 1);
	}
	
	public static String getFirstPhysicalAddress() throws SocketException {
		
		NetworkInterface[] networkIntefaceArray = getAllNetworkInterface();
		NetworkInterface networkInterface = null;
		String result = null;
			
		for( int i = 0; i < networkIntefaceArray.length; i++ ) {
			networkInterface = networkIntefaceArray[i];
				
			result = getPhysicalAddress(networkInterface);
				
			if( result != null ) {
				return result;
			}
		}
		
		return null;
	}
	
	public static void main(String[] args ) throws SocketException {
		System.out.println(	getFirstPhysicalAddress() );
	}
}
