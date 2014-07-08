package cn.bidlink.framework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.CharacterEncodingFilter;


 /**
 * @description: 编码过滤，以后扩展
 * @since    Ver 1.0
 * @author   <a href="mailto:dejian.liu@ebnew.com">dejian.liu</a>
 * @Date	 2012	2012-8-24		下午3:19:00
 *
 */
public class BidCharacterEncodingFilter extends CharacterEncodingFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		super.doFilterInternal(request, response, filterChain);
	}
}

