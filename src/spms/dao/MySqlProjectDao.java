package spms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import spms.annotation.Component;
import spms.vo.Project;

@Component("projectDao")
public class MySqlProjectDao implements ProjectDao {
  DataSource ds;

  public void setDataSource(DataSource ds) {
    this.ds = ds;
  }

  public List<Project> selectList() throws Exception {
    Connection connection = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    try {
      connection = ds.getConnection();
      stmt = connection.createStatement();
      rs = stmt.executeQuery(
          "SELECT PNO,PNAME,STA_DATE,END_DATE,STATE" + 
          " FROM PROJECTS" +
          " ORDER BY PNO DESC");

      ArrayList<Project> projects = new ArrayList<Project>();

      while(rs.next()) {
        projects.add(new Project()
          .setNo(rs.getInt("PNO"))
          .setTitle(rs.getString("PNAME"))
          .setStartDate(rs.getDate("STA_DATE"))
          .setEndDate(rs.getDate("END_DATE"))
          .setState(rs.getInt("STATE"))	);
      }

      return projects;

    } catch (Exception e) {
      throw e;

    } finally {
      try {if (rs != null) rs.close();} catch(Exception e) {}
      try {if (stmt != null) stmt.close();} catch(Exception e) {}
      try {if (connection != null) connection.close();} catch(Exception e) {}
    }
  }

  public int insert(Project project) throws Exception  {
    Connection connection = null;
    PreparedStatement stmt = null;

    try {
      connection = ds.getConnection();
      stmt = connection.prepareStatement(
        "INSERT INTO PROJECTS"
        + "(PNAME,CONTENT,STA_DATE,END_DATE,STATE,CRE_DATE,TAGS)"
        + " VALUES (?,?,?,?,0,NOW(),?)");
      stmt.setString(1, project.getTitle());
      stmt.setString(2, project.getContent());
      stmt.setDate(3, new java.sql.Date(project.getStartDate().getTime()));
      stmt.setDate(4, new java.sql.Date(project.getEndDate().getTime()));
      stmt.setString(5, project.getTags());
      
      return stmt.executeUpdate();

    } catch (Exception e) {
      throw e;

    } finally {
      try {if (stmt != null) stmt.close();} catch(Exception e) {}
      try {if (connection != null) connection.close();} catch(Exception e) {}
    }
  }
  
  public Project selectOne(int no) throws Exception { 
    Connection connection = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      connection = ds.getConnection();
      stmt = connection.createStatement();
      rs = stmt.executeQuery(
        "SELECT PNO,PNAME,CONTENT,STA_DATE,END_DATE,STATE,CRE_DATE,TAGS"
        + " FROM PROJECTS WHERE PNO=" + no);    
      if (rs.next()) {
        return new Project()
          .setNo(rs.getInt("PNO"))
          .setTitle(rs.getString("PNAME"))
          .setContent(rs.getString("CONTENT"))
          .setStartDate(rs.getDate("STA_DATE"))
          .setEndDate(rs.getDate("END_DATE"))
          .setState(rs.getInt("STATE")) 
          .setCreatedDate(rs.getDate("CRE_DATE"))
          .setTags(rs.getString("TAGS"));

      } else {
        throw new Exception("�빐�떦 踰덊샇�쓽 �봽濡쒖젥�듃瑜� 李얠쓣 �닔 �뾾�뒿�땲�떎.");
      }

    } catch (Exception e) {
      throw e;
    } finally {
      try {if (rs != null) rs.close();} catch(Exception e) {}
      try {if (stmt != null) stmt.close();} catch(Exception e) {}
      try {if (connection != null) connection.close();} catch(Exception e) {}
    }
  }

  public int update(Project project) throws Exception { 
    Connection connection = null;
    PreparedStatement stmt = null;
    try {
      connection = ds.getConnection();
      stmt = connection.prepareStatement(
        "UPDATE PROJECTS SET "
        + " PNAME=?,"
        + " CONTENT=?,"
        + " STA_DATE=?,"
        + " END_DATE=?,"
        + " STATE=?,"
        + " TAGS=?"
        + " WHERE PNO=?");
      stmt.setString(1, project.getTitle());
      stmt.setString(2, project.getContent());
      stmt.setDate(3, new java.sql.Date(project.getStartDate().getTime()));
      stmt.setDate(4, new java.sql.Date(project.getEndDate().getTime()));
      stmt.setInt(5, project.getState());
      stmt.setString(6, project.getTags());
      stmt.setInt(7, project.getNo());
      
      return stmt.executeUpdate();

    } catch (Exception e) {
      throw e;

    } finally {
      try {if (stmt != null) stmt.close();} catch(Exception e) {}
      try {if (connection != null) connection.close();} catch(Exception e) {}
    }
  }  

  public int delete(int no) throws Exception {  
    Connection connection = null;
    Statement stmt = null;

    try {
      connection = ds.getConnection();
      stmt = connection.createStatement();
      return stmt.executeUpdate(
          "DELETE FROM PROJECTS WHERE PNO=" + no);

    } catch (Exception e) {
      throw e;

    } finally {
      try {if (stmt != null) stmt.close();} catch(Exception e) {}
      try {if (connection != null) connection.close();} catch(Exception e) {}
    }
  }
  
  public int selectCount() throws SQLException {
	  Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			connection = ds.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT count(*) FROM PROJECTS");
			rs.next();
			result = rs.getInt(1);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
			}
		}
		return result;
  }
  
  public List<Project> select(int startRow, int endRow) {
	  Connection connection = null;
	  PreparedStatement stmt = null;
	  ResultSet rs = null;
	  
	  try {
		connection = ds.getConnection();
		stmt = connection.prepareStatement("select pno, pname, sta_date, end_date, state from projects order by pno desc limit ?, ?");
		
		stmt.setInt(1, startRow);
		stmt.setInt(2, endRow - startRow + 1);
		
		rs = stmt.executeQuery();
		
		ArrayList<Project> projects = new ArrayList<Project>();
		
		while(rs.next()) {
			Project proj = new Project();
			
			proj.setNo(rs.getInt("PNO"));
			proj.setTitle(rs.getString("PNAME"));
			proj.setStartDate(rs.getDate("STA_DATE"));
			proj.setEndDate(rs.getDate("END_DATE"));
			proj.setState(rs.getInt("STATE"));
			
			projects.add(proj);
		}
		
		return projects;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		if(rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(stmt != null)
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	return null;
  }
}
