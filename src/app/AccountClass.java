package app;

public class AccountClass implements Account {
	private String name;
	private String pwd;
	private boolean logged_in;
	private boolean locked;

	public AccountClass(String name, String pwd) {
		this.name = name;
		this.pwd = pwd;
		this.logged_in = false;
		this.locked = false;
	}

	public String get_account_name() {
		return name;
	}

	public String get_account_pwd() {
		return pwd;
	}

	public boolean is_logged_in() {
		return logged_in;
	}

	public boolean is_locked() {
		return locked;
	}

	public void set_account_name(String name) {
		this.name = name;
	}

	public void set_account_pwd(String pwd) {
		this.pwd = pwd;
	}

	public void log_in() {
		this.logged_in = true;
	}

	public void log_out() {
		this.logged_in = false;
	}

	public void lock() {
		this.locked = true;
	}

	public void unlock() {
		this.locked = false;
	}
}