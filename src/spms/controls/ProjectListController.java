package spms.controls;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import spms.annotation.Component;
import spms.bind.DataBinding;
import spms.controls.Controller;
import spms.dao.MySqlProjectDao;
import spms.dao.ProjectDao;
import spms.listeners.AppStorage;
import spms.vo.Project;
import spms.vo.ProjectListModel;

@Component("/project/list.do")
public class ProjectListController implements Controller, DataBinding {
  MySqlProjectDao projectDao;
  
  public ProjectListController setMemberDao(MySqlProjectDao projectDao) {
    this.projectDao = projectDao;
    return this;
  }

  @Override
  public String execute(Map<String, Object> model) throws Exception {
	  String pageNumberString = (String) model.get("p");
	  int page = 1;
	  
	  if(pageNumberString != null && pageNumberString.length() > 0)
		  page = Integer.parseInt(pageNumberString);
	  
	  ProjectListModel plm = getProjectListModel(page);
	  model.put("listModel", plm);
	  
	  if(plm.getTotalPageCount() > 0) { 
		  int pagePerBoard = AppStorage.getPagePerBoard();
		  int startPage = (plm.getRequestPage() - 1) / pagePerBoard * pagePerBoard + 1;
		  int endPage = startPage + pagePerBoard - 1;
		  
		  if(endPage > plm.getTotalPageCount())
			  endPage = plm.getTotalPageCount();
		  
		  model.put("pagePerBoard", pagePerBoard);
		  model.put("beginPage", startPage);
		  model.put("endPage", endPage);
	  }
	  
//	  model.put("projects", projectDao.selectList());
	  return "/project/ProjectList.jsp";
  }

private ProjectListModel getProjectListModel(int page) throws Exception {
	if(page < 0)
		throw new Exception("page number < 0 : " + page);
	
	ProjectListModel result = null;
	
	try {
		int totalArticleCount = projectDao.selectCount();
		if(totalArticleCount == 0)
			return new ProjectListModel();
	
		int articlePerPage = AppStorage.getArticlePerPage();
		int totalPageCount = calculateTotalPageCount(totalArticleCount, articlePerPage);
		int startRow = (page - 1) * articlePerPage + 1;
		int endRow = startRow + articlePerPage - 1;
	
		if(endRow > totalArticleCount)
			endRow = totalArticleCount;
	
		List<Project> projects = projectDao.select(startRow, endRow);
		ProjectListModel plm = new ProjectListModel((ArrayList<Project>) projects, page, totalPageCount, startRow, endRow);
	
		result = plm;
	} catch (Exception e) {
		throw new RuntimeException("asdfasdfasfasdfsdaf:"+e.getMessage(), e);
	}
	return result;
}

private int calculateTotalPageCount(int totalArticleCount, int articlePerPage) {
	if(totalArticleCount == 0)
		return 0;
	
	int pageCount = totalArticleCount / articlePerPage;
	
	if(totalArticleCount % articlePerPage > 0)
		pageCount++;
	
	return pageCount;
}

@Override
public Object[] getDataBinders() {
	return new Object[] {"p", String.class};
}
}
