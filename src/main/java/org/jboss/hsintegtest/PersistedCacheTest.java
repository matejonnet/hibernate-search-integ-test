package org.jboss.hsintegtest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.infinispan.Cache;

/**
 * @author Matej Lazar
 */
@WebServlet(urlPatterns={"/persistedCacheTest"})
public class PersistedCacheTest extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Resource(mappedName="java:jboss/infinispan/cache/capedwarf/persistent")
    public void setCache(Cache<String, String> cache) {
        this.cache = cache;
    }

    Cache<String, String> cache;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        if (req.getParameter("put") != null) {
            getCache().put("1", "1-" + System.currentTimeMillis());
            getCache().put("2", "2-" + System.currentTimeMillis());
            out.println("Put done." + "<br />");
        }
        out.println(getCache().get("1") + "<br />");
        out.println(getCache().get("2") + "<br />");
        out.println("Get done." + "<br />");

        out.close();
    }


    private Cache<String, String> getCache() {
       return cache;
    }
}
