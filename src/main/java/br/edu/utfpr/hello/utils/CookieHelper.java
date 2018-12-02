package br.edu.utfpr.hello.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CookieHelper {
    public static void setCurrentTimeCookie(HttpServletResponse response) {

        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        String time = fmt.format(new Date());
        Cookie cookie = new Cookie("currentTime", time);
        response.addCookie(cookie);
    }
}
