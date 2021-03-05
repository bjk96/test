package spms.listeners;

public class AppStorage {
	private static int pagePerBoard;
	private static int articlePerPage;
	
	public static int getPagePerBoard() {
		return pagePerBoard;
	}
	public static void setPagePerBoard(int pagePerBoard) {
		AppStorage.pagePerBoard = pagePerBoard;
	}
	public static int getArticlePerPage() {
		return articlePerPage;
	}
	public static void setArticlePerPage(int articlePerPage) {
		AppStorage.articlePerPage = articlePerPage;
	}
	
	
}
