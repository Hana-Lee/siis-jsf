package kr.co.leehana.siis.helper;

/**
 * Created by Lee Hana on 2015-04-17 오후 3:22.
 *
 * @author Lee Hana
 */
public enum UserAgent {
	SAFARI("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/600.5.17 (KHTML, like Gecko) Version/8.0.5 Safari/600.5.17"),
	CHROME("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36"),
	FIREFOX("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0"),
	IE8("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; GTB7.5; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; BRI/2)"),
	IE9("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)"),
	IE10("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)"),
	IE11("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko"),
	IE12("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0"),
	ANDROID("Mozilla/5.0 (Linux; Android 5.0; Nexus 5 Build/LPX13D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.102 Mobile Safari/537.36"),
	IOS("Mozilla/5.0 (iPhone; CPU iPhone OS 8_1_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12B466 Safari/600.1.4"),
	W_PHONE("Mozilla/5.0 (Windows Phone 8.1; ARM; Trident/7.0; Touch IEMobile/11.0; HTC; Windows Phone 8S by HTC) like Gecko");

	private String label;

	UserAgent(String userAgentLabel) {
		this.label = userAgentLabel;
	}

	public String getLabel() {
		return label;
	}
}
