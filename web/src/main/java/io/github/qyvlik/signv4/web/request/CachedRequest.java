package io.github.qyvlik.signv4.web.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * https://www.baeldung.com/spring-reading-httpservletrequest-multiple-times
 */
public class CachedRequest extends HttpServletRequestWrapper {

    private final byte[] cachedBody;
    private final MultiValueMap<String, String> form;

    public CachedRequest(HttpServletRequest request,
                         final byte[] cachedBody,
                         MultiValueMap<String, String> form) throws IOException {
        super(request);
        this.cachedBody = cachedBody;
        this.form = form;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedInputStream(this.cachedBody);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        // Create a reader from cachedContent
        // and return it
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    @Override
    public String getParameter(String name) {
        String queryStringValue = super.getParameter(name);
        String formValue = this.form.getFirst(name);
        return (queryStringValue != null ? queryStringValue : formValue);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> result = new LinkedHashMap<>();
        Enumeration<String> names = getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            result.put(name, getParameterValues(name));
        }
        return result;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Set<String> names = new LinkedHashSet<>();
        names.addAll(Collections.list(super.getParameterNames()));
        names.addAll(this.form.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        List<String> formParam = this.form.get(name);
        if (formParam == null) {
            return parameterValues;
        }
        if (parameterValues == null || getQueryString() == null) {
            return StringUtils.toStringArray(formParam);
        } else {
            List<String> result = new ArrayList<>(parameterValues.length + formParam.size());
            result.addAll(Arrays.asList(parameterValues));
            result.addAll(formParam);
            return StringUtils.toStringArray(result);
        }
    }
}