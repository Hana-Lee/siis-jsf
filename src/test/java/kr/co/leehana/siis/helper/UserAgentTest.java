package kr.co.leehana.siis.helper;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

/**
 * Created by Lee Hana on 2015-04-24 오후 2:48.
 *
 * @author Lee Hana
 */
public class UserAgentTest {

	@Test
	public void testSafariUserAgent() {
		assertThat(UserAgent.valueOf("SAFARI"), is(not(nullValue())));
		assertThat(UserAgent.valueOf("SAFARI"), is(UserAgent.SAFARI));
		assertThat(UserAgent.valueOf("SAFARI").getLabel(), is("Mozilla/5.0 "
				+ "(Macintosh; Intel Mac OS X 10_10_3) "
				+ "AppleWebKit/600.5.17 "
				+ "(KHTML, like Gecko) Version/8.0.5 Safari/600.5.17"));
	}

	@Test
	public void testChromeUserAgent() {
		assertThat(UserAgent.valueOf("CHROME"), is(not(nullValue())));
		assertThat(UserAgent.valueOf("CHROME"), is(UserAgent.CHROME));
		assertThat(UserAgent.valueOf("CHROME").getLabel(), is("Mozilla/5.0 "
				+ "(Macintosh; Intel Mac OS X 10_10_3) "
				+ "AppleWebKit/537.36 (KHTML, like Gecko) "
				+ "Chrome/42.0.2311.90 Safari/537.36"));
	}

	@Test
	public void testFirefoxUserAgent() {
		assertThat(UserAgent.valueOf("FIREFOX"), is(not(nullValue())));
		assertThat(UserAgent.valueOf("FIREFOX"), is(UserAgent.FIREFOX));
		assertThat(UserAgent.valueOf("FIREFOX").getLabel(), is("Mozilla/5.0 "
				+ "(Macintosh; Intel Mac OS X 10.10; rv:37.0) "
				+ "Gecko/20100101 Firefox/37.0"));
	}

	@Test
	public void testIE8UserAgent() {
		assertThat(UserAgent.valueOf("IE8"), is(not(nullValue())));
		assertThat(UserAgent.valueOf("IE8"), is(UserAgent.IE8));
		assertThat(UserAgent.valueOf("IE8").getLabel(), is("Mozilla/4.0 "
				+ "(compatible; MSIE 8.0; Windows NT 5.1; "
				+ "Trident/4.0; GTB7.5; "
				+ ".NET4.0C; .NET4.0E; .NET CLR 2.0.50727; "
				+ ".NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; BRI/2)"));
	}

	@Test
	public void testIE9UserAgent() {
		assertThat(UserAgent.valueOf("IE9"), is(not(nullValue())));
		assertThat(UserAgent.valueOf("IE9"), is(UserAgent.IE9));
		assertThat(UserAgent.valueOf("IE9").getLabel(), is("Mozilla/5.0 "
				+ "(compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)"));
	}

	@Test
	public void testIE10UserAgent() {
		assertThat(UserAgent.valueOf("IE10"), is(not(nullValue())));
		assertThat(UserAgent.valueOf("IE10"), is(UserAgent.IE10));
		assertThat(UserAgent.valueOf("IE10").getLabel(), is("Mozilla/5.0 "
				+ "(compatible; MSIE 10.0; "
				+ "Windows NT 6.1; WOW64; Trident/6.0)"));
	}

	@Test
	public void testIE11UserAgent() {
		assertThat(UserAgent.valueOf("IE11"), is(not(nullValue())));
		assertThat(UserAgent.valueOf("IE11"), is(UserAgent.IE11));
		assertThat(UserAgent.valueOf("IE11").getLabel(), is("Mozilla/5.0 "
				+ "(Windows NT 6.1; WOW64; "
				+ "Trident/7.0; rv:11.0) like Gecko"));
	}

	@Test
	public void testIE12UserAgent() {
		assertThat(UserAgent.valueOf("IE12"), is(not(nullValue())));
		assertThat(UserAgent.valueOf("IE12"), is(UserAgent.IE12));
		assertThat(UserAgent.valueOf("IE12").getLabel(), is("Mozilla/5.0 "
				+ "(Windows NT 10.0; WOW64) "
				+ "AppleWebKit/537.36 (KHTML, like Gecko) "
				+ "Chrome/39.0.2171.71 Safari/537.36 Edge/12.0"));
	}

	@Test
	public void testAndroidUserAgent() {
		assertThat(UserAgent.valueOf("ANDROID"), is(not(nullValue())));
		assertThat(UserAgent.valueOf("ANDROID"), is(UserAgent.ANDROID));
		assertThat(UserAgent.valueOf("ANDROID").getLabel(), is("Mozilla/5.0 "
				+ "(Linux; Android 5.0; Nexus 5 Build/LPX13D) "
				+ "AppleWebKit/537.36 (KHTML, like Gecko) "
				+ "Chrome/38.0.2125.102 Mobile Safari/537.36"));
	}

	@Test
	public void testAppleUserAgent() {
		assertThat(UserAgent.valueOf("IOS"), is(not(nullValue())));
		assertThat(UserAgent.valueOf("IOS"), is(UserAgent.IOS));
		assertThat(UserAgent.valueOf("IOS").getLabel(), is("Mozilla/5.0 "
				+ "(iPhone; CPU iPhone OS 8_1_3 like Mac OS X) "
				+ "AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 "
				+ "Mobile/12B466 Safari/600.1.4"));
	}

	@Test
	public void testWindowsPhoneUserAgent() {
		assertThat(UserAgent.valueOf("W_PHONE"), is(not(nullValue())));
		assertThat(UserAgent.valueOf("W_PHONE"), is(UserAgent.W_PHONE));
		assertThat(UserAgent.valueOf("W_PHONE").getLabel(), is("Mozilla/5.0 "
				+ "(Windows Phone 8.1; ARM; Trident/7.0; "
				+ "Touch IEMobile/11.0; HTC; "
				+ "Windows Phone 8S by HTC) like Gecko"));
	}
}
