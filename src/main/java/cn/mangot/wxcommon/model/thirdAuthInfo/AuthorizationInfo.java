package cn.mangot.wxcommon.model.thirdAuthInfo;
import java.util.List;
/**
 * Auto-generated: 2018-11-20 19:1:11
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class AuthorizationInfo {

    private String authorizer_appid;
    private String authorizer_access_token;
    private int expiresIn;
    private String authorizer_refresh_token;
    private List<FuncInfo> func_info;
   
	public String getAuthorizer_appid() {
		return authorizer_appid;
	}
	public void setAuthorizer_appid(String authorizer_appid) {
		this.authorizer_appid = authorizer_appid;
	}
	public String getAuthorizer_access_token() {
		return authorizer_access_token;
	}
	public void setAuthorizer_access_token(String authorizer_access_token) {
		this.authorizer_access_token = authorizer_access_token;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getAuthorizer_refresh_token() {
		return authorizer_refresh_token;
	}
	public void setAuthorizer_refresh_token(String authorizer_refresh_token) {
		this.authorizer_refresh_token = authorizer_refresh_token;
	}
	public List<FuncInfo> getFunc_info() {
		return func_info;
	}
	public void setFunc_info(List<FuncInfo> func_info) {
		this.func_info = func_info;
	}

}