package app;

public interface Account {
	String get_account_name();

	String get_account_pwd();

	boolean is_logged_in();

	boolean is_locked();

	void set_account_name(String name);

	void set_account_pwd(String pwd);

	void log_in();

	void log_out();

	void lock();

	void unlock();
}