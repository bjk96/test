package spms.listeners;

// �봽濡쒗띁�떚 �뙆�씪 �쟻�슜 : ApplicationContext �궗�슜
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import spms.context.ApplicationContext;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
  static ApplicationContext applicationContext;
  
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }
  
  @Override
  public void contextInitialized(ServletContextEvent event) {
    try {
      ServletContext sc = event.getServletContext();
      
      String propertiesPath = sc.getRealPath(sc.getInitParameter("contextConfigLocation"));
      applicationContext = new ApplicationContext(propertiesPath);
      
      AppStorage.setArticlePerPage(Integer.parseInt(sc.getInitParameter("articlePerPage")));
      AppStorage.setPagePerBoard(Integer.parseInt(sc.getInitParameter("pagePerBoard")));
      
    } catch(Throwable e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent event) {}
}
