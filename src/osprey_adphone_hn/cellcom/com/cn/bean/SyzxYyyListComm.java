package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class SyzxYyyListComm {
	@Element(required = false)
	private String returnCode;
	@Element(required = false)
	private String returnMessage;
	@Element(required = false)
	private String time;
	@Element(required = false)
	private String signInfo;
	@Element(required = false)
	private String systemNo;
	@Element(required = false)
	private String id;
	@Element(required = false)
	private String clientId;
	@ElementList(required = false, type = SyzxYyyList.class)
	private List<SyzxYyyList> body = new ArrayList<SyzxYyyList>();

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(String signInfo) {
		this.signInfo = signInfo;
	}

	public String getSystemNo() {
		return systemNo;
	}

	public void setSystemNo(String systemNo) {
		this.systemNo = systemNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public List<SyzxYyyList> getBody() {
		return body;
	}

	public void setBody(List<SyzxYyyList> body) {
		this.body = body;
	}

}
