package artxew.framework.environment.valid;
import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Artxe2
 */
public class SafeHtmlValidator implements ConstraintValidator<SafeHtml, String> {
	private static final Pattern startTagRegex = Pattern.compile("<");
	private static final Pattern tagNameRegex = Pattern.compile("^/?[a-z][a-z-]*");
	private static final Pattern scriptRegex = Pattern.compile("^script");
	private static final Pattern attrNameRegex = Pattern.compile("^ [a-z][a-z-]*");
	private static final Pattern attrValueRegex = Pattern.compile("^\"[^\"]*(?<!\\\\)(?:\\\\\\\\)*\"");
	private static final Pattern javascriptRegex = Pattern.compile("^\s*javascript|&#");

	/**
	 * @author Artxe2
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		char[] html = value.toCharArray();
		int length = html.length;
		int index = 0;
		Matcher match = startTagRegex.matcher(CharBuffer.wrap(html, index, length));
		// eat element tag
		while (match.find()) {
			index += match.end();
			match = tagNameRegex.matcher(CharBuffer.wrap(html, index, length - index));
			// no tag name
			if (!match.find()) {
				return false;
			}
			// script tag
			if (scriptRegex.matcher(CharBuffer.wrap(html, index, match.end())).find()) {
				return false;
			}
			index += match.end();
			match = attrNameRegex.matcher(CharBuffer.wrap(html, index, length - index));
			// eat attribute
			while (match.find()) {
				// event handler
				if (html[index + 1] == 'o' && html[index + 2] == 'n') {
					return false;
				}
				index += match.end();
				// no attribute assignment
				if (index == length || html[index++] != '=') {
					return false;
				}
				match = attrValueRegex.matcher(CharBuffer.wrap(html, index, length - index));
				// no attribute value
				if (!match.find()) {
					return false;
				}
				// attribute value include "javascript" or "&#"
				if (javascriptRegex.matcher(CharBuffer.wrap(html, index + 1, length - index - 1)).find()) {
				return false;
				}
				index += match.end();
				match = attrNameRegex.matcher(CharBuffer.wrap(html, index, length - index));
			}
			// syntax error
			if (index == length || html[index++] != '>') {
				return false;
			}
			match = startTagRegex.matcher(CharBuffer.wrap(html, index, length - index));
		}
		return true;
	}
}