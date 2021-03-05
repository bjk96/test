package spms.vo;

import java.util.ArrayList;
import java.util.List;

public class ProjectListModel {
	private List<Project> projectList;
	private int requestPage;
	private int totalPageCount;
	private int startRow;
	private int endRow;
	
	public ProjectListModel() {
		this(new ArrayList<Project>(), 0, 0, 0, 0);
	}
	
	public ProjectListModel(ArrayList<Project> arrayList,  int pageNumber, int totalPageCount, int firstRow, int endRow) {
		this.projectList = arrayList;
		this.requestPage = pageNumber;
		this.totalPageCount = totalPageCount;
		this.startRow = firstRow;
		this.endRow = endRow;
	}

	public boolean isHasProject() {
		return !projectList.isEmpty();
	}
	
	public List<Project> getProjectList() {
		return projectList;
	}
	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}
	public int getRequestPage() {
		return requestPage;
	}
	public void setRequestPage(int requestPage) {
		this.requestPage = requestPage;
	}
	public int getTotalPageCount() {
		return totalPageCount;
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
}
