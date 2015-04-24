package kr.co.leehana.siis.helper;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Lee Hana on 2015-04-24 오후 2:24.
 *
 * @author Lee Hana
 */
public class StringHelper {

	public static String convertWhiteSpacesToUnderscores(String string) {
		if (StringUtils.isBlank(string)) {
			return string;
		}
		if (StringUtils.containsWhitespace(string)) {
			return string.trim().replaceAll("\\s+", "_");
		}

		return string;
	}
}
