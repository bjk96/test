package spms.controls;

import java.util.Map;

import javax.servlet.http.HttpSession;

import spms.annotation.Component;
import spms.bind.DataBinding;
import spms.dao.MemberDao;
import spms.vo.Member;
import work.crypt.BCrypt;
import work.crypt.SHA256;

// Annotation �쟻�슜
@Component("/auth/login.do")
public class LogInController implements Controller, DataBinding {
  MemberDao memberDao;
  
  public LogInController setMemberDao(MemberDao memberDao) {
    this.memberDao = memberDao;
    return this;
  }
  
  public Object[] getDataBinders() {
    return new Object[]{
        "loginInfo", spms.vo.Member.class
    };
  }
  
  @Override
  public String execute(Map<String, Object> model) throws Exception {
    Member loginInfo = (Member)model.get("loginInfo");
    
    if (loginInfo.getEmail() == null) { // �엯�젰�뤌�쓣 �슂泥��븷 �븣
      return "/auth/LogInForm.jsp";
      
    } else { // �쉶�썝 �벑濡앹쓣 �슂泥��븷 �븣
//      Member member = memberDao.exist(
//          loginInfo.getEmail(), 
//          loginInfo.getPassword());
//      
//      if (member != null) {
//        HttpSession session = (HttpSession)model.get("session");
//        session.setAttribute("member", member);
//        return "redirect:../member/list.do";
//      } else {
//        return "/auth/LogInFail.jsp";
//      }
    	
    	SHA256 sha = SHA256.getInsatnce();
    	String orgPass = loginInfo.getPassword();
    	String shaPass = sha.getSha256(orgPass.getBytes());
    	Member member = memberDao.selectOne(loginInfo.getEmail());
    	String dbpwd = member.getPassword();
    	
    	if(BCrypt.checkpw(shaPass, dbpwd)) {
    		HttpSession session = (HttpSession)model.get("session");
    		session.setAttribute("member", member);
    		return "redirect:../member/list.do";
    	} else {
    		return "/auth/LogInFail.jsp";
    	}
    }
  }
}
