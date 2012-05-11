package org.jboss.hsintegtest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.infinispan.Cache;
import org.infinispan.query.CacheQuery;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;

/**
 * @author Matej Lazar
 */
@WebServlet(urlPatterns={"/cacheTest"})
public class CacheTest extends HttpServlet {

//    @Resource(mappedName="java:jboss/infinispan/container/capedwarf")
//    public void setCacheContainer(EmbeddedCacheManager cacheContainer) {
//        this.cacheManager = cacheContainer;
//    }
//    EmbeddedCacheManager cacheManager;

    @Resource(mappedName="java:jboss/infinispan/cache/capedwarf/default")
    public void setCache(Cache<Long, SimpleEmail> cache) {
        this.cache = cache;
    }
    Cache<Long, SimpleEmail> cache;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        storeEmail("message");
        sleep();
        storeEmail("message");
        sleep();
        storeEmail("message");

        TermQuery termQuery = new TermQuery( new Term( "to", "complaints-office@world.com" ) );
        CacheQuery q = getCacheQuery( termQuery, SimpleEmail.class );

        out.println("<br />");
        out.println("size: " + q.getResultSize() + "<br />");

        list(out, q);

        if (req.getParameter("del") != null) {
            clear();
            out.println("List afret delete.<br />");
            list(out, q);
        }

        out.println("done.");

        out.close();
    }

    private void list(PrintWriter out, CacheQuery q) {
        List<Object> list = q.list();
        for (Object email : list) {
            out.println(email + "<br />");
        }
    }

    private void clear() {
        getCache().clear();
    }

    private CacheQuery getCacheQuery(Query query, Class<?> classes) {
        SearchManager sm = Search.getSearchManager(getCache());
        //return sm.getQuery(query, classes);
        return sm.getQuery(query);

    }

    private void storeEmail(String message) {
        SimpleEmail email = new SimpleEmail();
        email.to = "complaints-office@world.com";
        email.setMessage(message);
        getCache().put(System.currentTimeMillis(), email);
    }

    private void sleep() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Cache getCache() {
       //return cacheManager.getCache();
       return cache;
    }
}
