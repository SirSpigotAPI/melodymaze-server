package app.melodymaze.server.config;

public class SecuritySettings {

    public static final String LOGIN_PAGE_URL = "/signin";
    public static final String[] PERMITTED_URLS = {"signin", "login", "assets/**", "error"};

}
