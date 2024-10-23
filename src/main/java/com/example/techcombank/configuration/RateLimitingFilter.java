package com.example.techcombank.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitingFilter implements Filter {
    // Map to store request timestamps per IP address
    private final Map<String, Deque<Long>> requestTimestampsPerIpAddress = new ConcurrentHashMap<>();

    // Maximum requests allowed per second
    private static final int MAX_REQUESTS_PER_SECOND = 25;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String clientIpAddress = httpServletRequest.getRemoteAddr();
        long currentTimeMillis = System.currentTimeMillis();

        // Initialize the timestamp deque for the client IP if it doesn't exist
        requestTimestampsPerIpAddress.putIfAbsent(clientIpAddress, new LinkedList<>());
        Deque<Long> requestTimestamps = requestTimestampsPerIpAddress.get(clientIpAddress);

        synchronized (requestTimestamps) {
            // Remove timestamps older than 1 second (1000 milliseconds)
            while (!requestTimestamps.isEmpty() && (currentTimeMillis - requestTimestamps.peekFirst() > 1000)) {
                requestTimestamps.pollFirst();
            }

            // Check if the number of requests in the last second exceeds the limit
            if (requestTimestamps.size() >= MAX_REQUESTS_PER_SECOND) {
                httpServletResponse.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
                httpServletResponse.getWriter().write("Too many requests. Please try again later.");
                return;
            }

            // Add the current timestamp
            requestTimestamps.addLast(currentTimeMillis);
        }

        // Allow the request to proceed
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Optional: Initialization logic, if needed
    }

    @Override
    public void destroy() {
        // Optional: Cleanup resources, if needed
    }
}
