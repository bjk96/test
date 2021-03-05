package spms.dao;

// �븷�끂�뀒�씠�뀡 �쟻�슜 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import spms.annotation.Component;
import spms.vo.Member;
import work.crypt.BCrypt;
import work.crypt.SHA256;

@Component("memberDao")
public class MySqlMemberDao implements MemberDao {
  DataSource ds;

  public void setDataSource(DataSource ds) {
    this.ds = ds;
  }

  public List<Member> selectList() throws Exception {
    Connection connection = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    try {
      connection = ds.getConnection();
      stmt = connection.createStatement();
      rs = stmt.executeQuery(
          "SELECT MNO,MNAME,EMAIL,CRE_DATE" + 
              " FROM MEMBERS" +
          " ORDER BY MNO ASC");

      ArrayList<Member> members = new ArrayList<Member>();

      while(rs.next()) {
        members.add(new Member()
        .setNo(rs.getInt("MNO"))
        .setName(rs.getString("MNAME"))
        .setEmail(rs.getString("EMAIL"))
        .setCreatedDate(rs.getDate("CRE_DATE"))	);
      }

      return members;

    } catch (Exception e) {
      throw e;

    } finally {
      try {if (rs != null) rs.close();} catch(Exception e) {}
      try {if (stmt != null) stmt.close();} catch(Exception e) {}
      try {if (connection != null) connection.close();} catch(Exception e) {}
    }
  }

  public int insert(Member member) throws Exception  {
    Connection connection = null;
    PreparedStatement stmt = null;

    try {
      connection = ds.getConnection();
      stmt = connection.prepareStatement(
          "INSERT INTO MEMBERS(EMAIL,PWD,MNAME,CRE_DATE,MOD_DATE)"
              + " VALUES (?,?,?,NOW(),NOW())");
      stmt.setString(1, member.getEmail());
      stmt.setString(2, member.getPassword());
      stmt.setString(3, member.getName());
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
          "DELETE FROM MEMBERS WHERE MNO=" + no);

    } catch (Exception e) {
      throw e;

    } finally {
      try {if (stmt != null) stmt.close();} catch(Exception e) {}
      try {if (connection != null) connection.close();} catch(Exception e) {}
    }
  }

  public Member selectOne(int no) throws Exception { 
    Connection connection = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      connection = ds.getConnection();
      stmt = connection.createStatement();
      rs = stmt.executeQuery(
          "SELECT MNO,EMAIL,MNAME,CRE_DATE FROM MEMBERS" + 
              " WHERE MNO=" + no);    
      if (rs.next()) {
        return new Member()
        .setNo(rs.getInt("MNO"))
        .setEmail(rs.getString("EMAIL"))
        .setName(rs.getString("MNAME"))
        .setCreatedDate(rs.getDate("CRE_DATE"));

      } else {
        throw new Exception("�빐�떦 踰덊샇�쓽 �쉶�썝�쓣 李얠쓣 �닔 �뾾�뒿�땲�떎.");
      }

    } catch (Exception e) {
      throw e;
    } finally {
      try {if (rs != null) rs.close();} catch(Exception e) {}
      try {if (stmt != null) stmt.close();} catch(Exception e) {}
      try {if (connection != null) connection.close();} catch(Exception e) {}
    }
  }

  public int update(Member member) throws Exception { 
    Connection connection = null;
    PreparedStatement stmt = null;
    try {
      connection = ds.getConnection();
      stmt = connection.prepareStatement(
          "UPDATE MEMBERS SET EMAIL=?,MNAME=?,MOD_DATE=now()"
              + " WHERE MNO=?");
      stmt.setString(1, member.getEmail());
      stmt.setString(2, member.getName());
      stmt.setInt(3, member.getNo());
      return stmt.executeUpdate();

    } catch (Exception e) {
      throw e;

    } finally {
      try {if (stmt != null) stmt.close();} catch(Exception e) {}
      try {if (connection != null) connection.close();} catch(Exception e) {}
    }
  }
  
  public Member exist(String email, String password) throws Exception {
    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      connection = ds.getConnection();
      stmt = connection.prepareStatement(
          "SELECT MNAME,EMAIL FROM MEMBERS"
              + " WHERE EMAIL=? AND PWD=?");
      stmt.setString(1, email);
      stmt.setString(2, password);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return new Member()
          .setName(rs.getString("MNAME"))
          .setEmail(rs.getString("EMAIL"));
      } else {
        return null;
      }
    } catch (Exception e) {
      throw e;

    } finally {
      try {if (rs != null) rs.close();} catch (Exception e) {}
      try {if (stmt != null) stmt.close();} catch (Exception e) {}
      try {if (connection != null) connection.close();} catch(Exception e) {}
    }
  }
  
  public Member selectOne(String email) {
	  Connection connection = null;
	  Statement stmt = null;
	  ResultSet rs = null;
	  
	  try {
		connection = ds.getConnection();
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select mno, email, mname, pwd, cre_date from members where email = '" + email + "'");
		
		if(rs.next()) {
			return new Member().setNo(rs.getInt("MNO")).setEmail(rs.getString("EMAIL"))
					.setName(rs.getString("MNAME"))
					.setPassword(rs.getString("PWD"))
					.setCreatedDate(rs.getDate("CRE_DATE"));
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		try {if (rs != null) rs.close();} catch (Exception e) {}
	      try {if (stmt != null) stmt.close();} catch (Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	}
	return null;
  }

@Override
public void updateMember() {
	Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs= null;
    
    SHA256 sha = SHA256.getInsatnce();
    
    try {
        conn = ds.getConnection();        
        pstmt = conn.prepareStatement("select email, pwd from members");
        rs = pstmt.executeQuery();
        
        while(rs.next()){
        	String id = rs.getString("email");	// email, pno
        	String orgPass = rs.getString("pwd");
        	String shaPass = sha.getSha256(orgPass.getBytes());
        	String bcPass = BCrypt.hashpw(shaPass, BCrypt.gensalt());
        	
        	pstmt = conn.prepareStatement(
                    "update members set pwd=? where email=?");
            pstmt.setString(1, bcPass);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        }
    } catch(Exception ex) {
        ex.printStackTrace();
    } finally {
    	if (rs != null) try { rs.close(); } catch(SQLException ex) {}
        if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
        if (conn != null) try { conn.close(); } catch(SQLException ex) {}
    }
	
}  
  
}
