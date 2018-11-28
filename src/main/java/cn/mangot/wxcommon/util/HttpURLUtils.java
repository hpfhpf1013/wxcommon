package cn.mangot.wxcommon.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HttpURLUtils {
	private static Logger LOGGER = LoggerFactory.getLogger(HttpURLUtils.class);

	public static String doPost(String reqUrl, Map<String, String> parameters) {
		HttpURLConnection urlConn = null;
		try {
			urlConn = sendPost(reqUrl, parameters);
			String responseContent = getContent(urlConn);
			return responseContent.trim();
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
				urlConn = null;
			}
		}
	}

	private static String getContent(HttpURLConnection urlConn) {
		try {
			String responseContent = null;
			InputStream in = urlConn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
			return responseContent;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static HttpURLConnection sendPost(String reqUrl, Map<String, String> parameters) {
		HttpURLConnection urlConn = null;
		try {
			String params = generatorParamString(parameters);
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(reqUrl + "?" + parameters);
			}
			URL url = new URL(reqUrl);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setConnectTimeout(50000);// （单位：毫秒）jdk
			urlConn.setReadTimeout(50000);// （单位：毫秒）jdk 1.5换成这个,读操作超时
			urlConn.setDoOutput(true);
			byte[] b = params.getBytes();
			urlConn.getOutputStream().write(b, 0, b.length);
			urlConn.getOutputStream().flush();
			urlConn.getOutputStream().close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return urlConn;
	}

	/**
	 * 将parameters中数据转换成用"&"链接的http请求参数形式
	 * 
	 * @param parameters
	 * @return
	 */
	public static String generatorParamString(Map<String, String> parameters) {
		StringBuffer params = new StringBuffer();
		if (parameters != null) {
			for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				String value = parameters.get(name);
				params.append(name + "=");
				try {
					params.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (Exception e) {
					String message = String.format("'%s'='%s'", name, value);
					throw new RuntimeException(message, e);
				}
				if (iter.hasNext())
					params.append("&");
			}
		}
		return params.toString();
	}

	/**
	 * 将parameters中数据转换成用"&"链接的http请求参数形式
	 * 
	 * @param parameters
	 * @return
	 */
	public static String generatorParamStringRenRen(Map<String,String[]> parameters) {
		StringBuffer params = new StringBuffer();
		if (parameters != null) {
			for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				String[] values= parameters.get(name); 
				String value = "";
                for(int i=0;i<values.length;i++){   
                	value+=values[i];
                }  
				params.append(name + "=");
				try {
					params.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (Exception e) {
					String message = String.format("'%s'='%s'", name, value);
					throw new RuntimeException(message, e);
				}
				if (iter.hasNext())
					params.append("&");
			}
		}
		return params.toString();
	}
	/**
	 * 
	 * @param link
	 * @param charset
	 * @return
	 */
	public static String doGet(String link, String charset) {
		try {
			URL url = new URL(link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for (int i = 0; (i = in.read(buf)) > 0;) {
				out.write(buf, 0, i);
			}
			out.flush();
			String s = new String(out.toByteArray(), charset);
			return s;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * UTF-8编码
	 * 
	 * @param link
	 * @return
	 */
	public static String doGet(String link) {
		return doGet(link, "UTF-8");
	}

	public static int getIntResponse(String link) {
		String str = doGet(link);
		return Integer.parseInt(str.trim());
	}
	
	public static String getRequestData(HttpServletRequest request){
        try {
        	BufferedReader reader = request.getReader();
            char[] buf = new char[512];
            int len = 0;
            StringBuffer contentBuffer = new StringBuffer();
            while ((len = reader.read(buf)) != -1) {
                contentBuffer.append(buf, 0, len);
            }
            String content = contentBuffer.toString();
            if(content == null){
                content = "";
            }
            return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
       
    }
	
	/*
	 * post 的方式请求数据
	 */
	public static String post(String url,String content) {
        String line     = "";
        String message    = "";
        String returnData  = "";
        Boolean postState   = false;
        BufferedReader bufferedReader = null;
        try {
            URL urlObject = new URL(url);
            HttpURLConnection urlConn = (HttpURLConnection) urlObject.openConnection();
            urlConn.setDoOutput(true);
            /*设定禁用缓存*/
            urlConn.setRequestProperty("Cache-Control", "no-cache");
            /*维持长连接*/
            urlConn.setRequestProperty("Connection", "Keep-Alive");
            /*设置字符集*/
            urlConn.setRequestProperty("Charset", "UTF-8");
            /*设定输出格式为json*/
            urlConn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            /*设置使用POST的方式发送*/
            urlConn.setRequestMethod("POST");
            /*设置不使用缓存*/
            urlConn.setUseCaches(false);
            /*设置容许输出*/
            urlConn.setDoOutput(true);
            /*设置容许输入*/
            urlConn.setDoInput(true);
            urlConn.connect();
            OutputStreamWriter outStreamWriter = new OutputStreamWriter(urlConn.getOutputStream(),"UTF-8");
            outStreamWriter.write(content);
            outStreamWriter.flush();
            outStreamWriter.close();
            /*若post失败*/
            if((urlConn.getResponseCode() != 200)){
                returnData = "";
                message = "发送POST失败！"+ "code="+urlConn.getResponseCode() + "," + "失败消息："+ urlConn.getResponseMessage();
                // 定义BufferedReader输入流来读取URL的响应 
                InputStream errorStream = urlConn.getErrorStream();
                if(errorStream != null) 
                        {
                    InputStreamReader inputStreamReader = new InputStreamReader(errorStream,"utf-8");
                    bufferedReader = new BufferedReader(inputStreamReader);
                    while ((line = bufferedReader.readLine()) != null) {
                        message += line;
                    }
                    inputStreamReader.close();
                }
                errorStream.close();
                System.out.println("发送失败！错误信息为："+message);
            } else{
                /*发送成功返回发送成功状态*/
                postState = true;
                // 定义BufferedReader输入流来读取URL的响应 
                InputStream inputStream = urlConn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                bufferedReader = new BufferedReader(inputStreamReader);
                while ((line = bufferedReader.readLine()) != null) {
                    message += line;
                }
                returnData = message;
                inputStream.close();
                inputStreamReader.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
            catch (final IOException ex) {
                ex.printStackTrace();
            }
            return returnData;
        }
    }
}
