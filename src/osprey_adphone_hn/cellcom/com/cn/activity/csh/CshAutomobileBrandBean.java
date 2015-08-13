package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 车生活-车辆品牌实体
 * 
 * @author 周子健
 *
 */
@Root
public class CshAutomobileBrandBean {
	@Element(required = false)
	private String mParentId;
	@Element(required = false)
	private String mCarSeriesId;
	@Element(required = false)
	private String mUrl;
	@Element(required = false)
	private String mCarSeriesName;
	@Element(required = false)
	private String mDetailName;

	public String getmCarSeriesName() {
		return mCarSeriesName;
	}

	public void setmCarSeriesName(String mCarSeriesName) {
		this.mCarSeriesName = mCarSeriesName;
	}

	public String getmDetailName() {
		return mDetailName;
	}

	public void setmDetailName(String mDetailName) {
		this.mDetailName = mDetailName;
	}

	public String getmParentId() {
		return mParentId;
	}

	public void setmParentId(String mParentId) {
		this.mParentId = mParentId;
	}

	public String getmCarSeriesId() {
		return mCarSeriesId;
	}

	public void setmCarSeriesId(String mCarSeriesId) {
		this.mCarSeriesId = mCarSeriesId;
	}

	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}
}
