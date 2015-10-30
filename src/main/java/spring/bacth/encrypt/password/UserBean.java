package spring.bacth.encrypt.password;

/**
 * @author JCZOBEIDE
 *
 */
public class UserBean {


	public UserBean(String login, String pwd) {
		super();
		this.login = login;
		this.pwd = pwd;
	}
	
	private String login;
	private String pwd;


	public String getLogin() {
		return login;
	}

	public String getPwd() {
		return pwd;
	}
	
	@Override
	public String toString() {
		return "PasswordBean [accesId=" + login + ", pwd=" + pwd + "]";
	}

}
