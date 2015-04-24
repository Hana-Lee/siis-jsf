package kr.co.leehana.siis.helper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

/**
 * Created by Lee Hana on 2015-04-24 오후 2:33.
 *
 * @author Lee Hana
 */
public class StringHelperTest {

	@Test
	public void testConvertWhiteSpacesToUnderscores() {
		String expect = "토비의_스프링_3.1";

		String source = "토비의 스프링 3.1";
		String convertedSource = StringHelper
				.convertWhiteSpacesToUnderscores(source);
		assertThat(convertedSource, is(expect));

		source = " 토비의 스프링 3.1";
		convertedSource = StringHelper.convertWhiteSpacesToUnderscores(source);
		assertThat(convertedSource, is(expect));

		source = "토비의 스프링 3.1 ";
		convertedSource = StringHelper.convertWhiteSpacesToUnderscores(source);
		assertThat(convertedSource, is(expect));

		source = " 토비의 스프링 3.1 ";
		convertedSource = StringHelper.convertWhiteSpacesToUnderscores(source);
		assertThat(convertedSource, is(expect));

		source = " 토비의  스프링 3.1";
		convertedSource = StringHelper.convertWhiteSpacesToUnderscores(source);
		assertThat(convertedSource, is(expect));
	}
}