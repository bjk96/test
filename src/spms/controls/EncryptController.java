package spms.controls;

import java.util.Map;

import spms.annotation.Component;
import spms.dao.MySqlMemberDao;

@Component("/member/encrypt.do")
public class EncryptController implements Controller {
	MySqlMemberDao dao;
	
	public EncryptController setMemberDao(MySqlMemberDao dao) {
		this.dao = dao;
		return this;
	}

	@Override
	public String execute(Map<String, Object> model) throws Exception {
		dao.updateMember();
		return "redirect:list.do";
	}

}
