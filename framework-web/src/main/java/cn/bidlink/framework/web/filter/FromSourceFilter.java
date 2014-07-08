package cn.bidlink.framework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FromSourceFilter implements Filter{

    public static String  FROM_SOURCE_COOKIE_NAME ="_cookie_from_source";
    public static String  FROM_SOURCE_URL_PARAMETER_NAME ="_source";

    public  static String FROM_SOURCE_ATTRIBUTE_NAME="_from_source";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
          // 请求的uri   
        HttpServletRequest httpServletRequest=(HttpServletRequest)request;
        HttpServletResponse httpServletResponse=(HttpServletResponse)response;
        Cookie [] cookies = httpServletRequest.getCookies();
        String fromSourceCookieValue = getCookieValue(FROM_SOURCE_COOKIE_NAME,cookies);
        String source = httpServletRequest.getParameter(FROM_SOURCE_URL_PARAMETER_NAME);
         if(source!=null&&!source.equals("")){
            httpServletRequest.setAttribute(FROM_SOURCE_ATTRIBUTE_NAME,source);  //设置attribute，设置cookie
            httpServletRequest.getSession().setAttribute(FROM_SOURCE_ATTRIBUTE_NAME,source);
            setCookie(httpServletResponse,FROM_SOURCE_COOKIE_NAME,source);
        }else if (fromSourceCookieValue!=null){
             httpServletRequest.setAttribute(FROM_SOURCE_ATTRIBUTE_NAME,fromSourceCookieValue);  //设置attribute
             httpServletRequest.getSession().setAttribute(FROM_SOURCE_ATTRIBUTE_NAME,fromSourceCookieValue);
        }
        System.out.println("---fromsource-----"+httpServletRequest.getAttribute(FROM_SOURCE_ATTRIBUTE_NAME));
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }


    private  String getCookieValue (String  cookieName, Cookie[] cookies){

        if (cookies == null || cookieName == null || cookieName.length() == 0) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (cookieName.equals(cookies[i].getName())
//                    && request.getServerName().equals(cookies[i].getDomain())
                    ) {
                return cookies[i].getValue();
            }
        }
        return null;
    }




    private  void setCookie(
                                 HttpServletResponse response, String name, String value ) {
        Cookie cookie = new Cookie(name, value == null ? "" : value);
//        cookie.setMaxAge(maxAge);
//        cookie.setPath(getPath(request));
        response.addCookie(cookie);
    }






    
}
