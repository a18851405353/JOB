import com.filter.FilterClass;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServlet;
import java.util.EnumSet;


/**
 * 文件上传下载服务器运行主入口
 * @author mj
 *
 */
public class App
{
    private static Logger logger = Logger.getLogger(App.class);
    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);
        //使用自定义的http处理器
        //server.setHandler(new HttpHandler());
        //服务器容器
        logger.debug("服务器context初始化");
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //添加过滤器
        logger.debug("添加过滤器");
        context.addFilter(FilterClass.class,"/*", EnumSet.of(DispatcherType.REQUEST));
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new com.controller.HttpServlet()), "/*");
        //启动jetty服务
        server.start();
        System.out.println("文件上传下载服务器已启动,监听端口在8080");
        server.join();
    }
}



















