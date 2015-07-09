package com.rekmock.web;

import com.rekmock.CWDDataSetStore;
import com.rekmock.MockConfig;
import com.rekmock.RekMockProvider;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by Faleh Omar on 02/04/2015.
 */
public class RekMockServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String testCaseNameParam = servletRequest.getParameter("testcasename");
        String recordParam = servletRequest.getParameter("record");

        if (testCaseNameParam != null) {

            MockConfig.Record record = recordParam == null ? MockConfig.Record.None : MockConfig.Record.valueOf(recordParam);
            MockConfig mockConfig = new MockConfig();
            mockConfig.setTestCaseName(testCaseNameParam);

            mockConfig.setRecord(record);
            mockConfig.setStore(CWDDataSetStore.class);

            RekMockProvider.getInstance().newTestCaseContext(mockConfig);


        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
