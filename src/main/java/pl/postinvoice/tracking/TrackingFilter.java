package pl.postinvoice.tracking;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@Order(2)
public class TrackingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            MultiReadHttpServletRequest request = new MultiReadHttpServletRequest(req);
            MDC.put("requestId", UUID.randomUUID().toString());

            log.info(prepareRequest(request));
            log.info(new String(request.getInputStream().readAllBytes()));

            long StartLog = System.currentTimeMillis();

            filterChain.doFilter(request, response);

            long EndLog = System.currentTimeMillis();
            log.info("End request: {} {}, status: {}, took {} ms", request.getMethod(), request.getRequestURI(), response.getStatus(), (StartLog - EndLog));
        } finally {
            MDC.clear();
        }
    }

    private static String prepareRequest(HttpServletRequest request) {
        StringBuilder requestLog = new StringBuilder("Start request: ");
        requestLog.append(request.getMethod())
                .append(" ")
                .append(request.getRequestURI());

        if (request.getQueryString() != null) {
            requestLog.append("?")
                    .append(request.getQueryString());
        }
        return requestLog.toString();
    }

}
