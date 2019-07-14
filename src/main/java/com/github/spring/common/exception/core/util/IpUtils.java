package com.github.spring.common.exception.core.util;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <pre>
 *  获取ip工具类
 * </pre>
 */
@UtilityClass
public class IpUtils {

    private static final String DOT = ".";

    /**
     * 获取客户端ip. 请求体默认从全局上下文获取.
     *
     * @param otherHeaderNames 请求头
     * @return 客户端ip
     */
    public String getClientIP(String... otherHeaderNames) {
        HttpServletRequest request = WebUtils.getRequest();
        if (request == null) {
            return null;
        }

        return getClientIP(request);
    }

    /**
     * <p>ip使用int表示.</p>
     * <p>
     * ip有4段, 每段最大值为255, 即 2^8 - 1, 刚好是一个字节能表示的最大值,
     * 所以4个字节的int刚好能用来表示一个ip地址.
     * </p>
     *
     * @param ip ip地址
     * @return int的ip
     */
    public Integer ipToInt(String ip) {
        String[] ips = ip.split("\\.");
        int ipFour = 0;
        //因为每个位置最大255，刚好在2进制里表示8位
        for (String ip4 : ips) {
            int ip4a = Integer.parseInt(ip4);
            //这里应该用+也可以,但是位运算更快
            ipFour = (ipFour << 8) | ip4a;
        }
        return ipFour;
    }

    /**
     * 使用int表示的ip地址, 转换成字符串的ip
     *
     * @param ip 使用int表示的ip地址
     * @return 字符串的ip地址
     */
    public String intToIp(Integer ip) {
        //思路很简单，每8位拿一次，就是对应位的IP
        StringBuilder sb = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
            int ipa = (ip >> (8 * i)) & (0xff);
            sb.append(".").append(ipa);
        }
        sb.delete(0, 1);
        return sb.toString();
    }


    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    private String getClientIP(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}
