package com.jee.spring_labs.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class ServletRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String> headerMap;

    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    public ServletRequestWrapper(HttpServletRequest request) {
        super(request);
        headerMap = new HashMap<>();
    }

    public Enumeration<String> getHeaderNames() {
        HttpServletRequest request = (HttpServletRequest) getRequest();
        List<String> list = new ArrayList<>();
        for (Enumeration e = request.getHeaderNames(); e.hasMoreElements(); ) {
            list.add(e.nextElement().toString());
        }
        list.addAll(headerMap.keySet());
        return Collections.enumeration(list);
    }

    public String getHeader(String name) {
        Object value;
        if ((value = headerMap.get("" + name)) != null)
            return value.toString();
        else
            return ((HttpServletRequest) getRequest()).getHeader(name);
    }
}
