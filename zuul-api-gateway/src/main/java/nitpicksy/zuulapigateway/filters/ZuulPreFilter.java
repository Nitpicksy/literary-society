package nitpicksy.zuulapigateway.filters;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class ZuulPreFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(ZuulPreFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if (request.getMethod().equals("OPTIONS")) {
            return false;
        }
        String url = request.getRequestURL().toString();
        return url.contains("api/orders");
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String header = request.getHeader("Auth");
        if (header == null || header.isEmpty() || !header.startsWith("Bearer ")) {
            ctx.setResponseStatusCode(401);
            ctx.setSendZuulResponse(false);
        } else {
            ctx.addZuulRequestHeader("Auth", header);
        }
        return null;
    }

}