package hcmute.edu.vn.HeThongQuanLyRapPhim.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MomoConfig {

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secret}")
    private String secretKey;

    @Value("${momo.url}")
    private String endpoint;

    @Value("${momo.redirectUrl}")
    private String redirectUrl;

    @Value("${momo.ipnUrl}")
    private String ipnUrl;

    public String getPartnerCode() { return partnerCode; }
    public String getAccessKey() { return accessKey; }
    public String getSecretKey() { return secretKey; }
    public String getEndpoint() { return endpoint; }
    public String getRedirectUrl() { return redirectUrl; }
    public String getIpnUrl() { return ipnUrl; }
}
