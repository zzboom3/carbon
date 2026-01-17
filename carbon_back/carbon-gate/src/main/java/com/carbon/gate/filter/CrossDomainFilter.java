/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.carbon.gate.filter;


import com.carbon.common.utils.HttpContextUtils;
import com.carbon.domain.common.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 设置允许跨域
 *
 */
@Slf4j
public class CrossDomainFilter implements Filter {

    private final Set<String> allowedOrigins;

    public CrossDomainFilter(String allowedOrigins) {
        this.allowedOrigins = parseAllowedOrigins(allowedOrigins);
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String origin = request.getHeader("Origin");
        if (isAllowAllOrigins()) {
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "false");
        } else if (origin != null && allowedOrigins.contains(origin)) {
            httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        }
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "*");
//        httpServletResponse.addHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,Authorization,token,Origin,Accept");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "*");
        httpServletResponse.addHeader("Access-Control-Request-Headers", "*");
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "*");

        String method = request.getMethod();
        if (RequestMethod.OPTIONS.name().equals(method)) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            HttpContextUtils.printJSON(response,ApiResult.ok());
            return;
        }
        filterChain.doFilter(servletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }

    private boolean isAllowAllOrigins() {
        return allowedOrigins.contains("*");
    }

    private Set<String> parseAllowedOrigins(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return new HashSet<>(Arrays.asList("*"));
        }
        Set<String> set = new HashSet<>();
        for (String part : raw.split(",")) {
            String v = part == null ? "" : part.trim();
            if (!v.isEmpty()) {
                set.add(v);
            }
        }
        if (set.isEmpty()) {
            set.add("*");
        }
        return set;
    }
}
