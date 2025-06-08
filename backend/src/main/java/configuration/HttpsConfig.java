package configuration;

import jakarta.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.valves.ValveBase;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "server.ssl.enabled", havingValue = "true")
public class HttpsConfig {

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                context.getPipeline().addValve(new HttpsRedirectValve());

                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        tomcat.addAdditionalTomcatConnectors(createHttpConnector());
        return tomcat;
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }

    private static class HttpsRedirectValve extends ValveBase {
        @Override
        public void invoke(org.apache.catalina.connector.Request request,
                           org.apache.catalina.connector.Response response)
                throws IOException, ServletException {

            if (!request.isSecure()) {
                String method = request.getMethod();
                int redirectCode = ("GET".equals(method) || "HEAD".equals(method)) ? 301 : 307;

                String httpsUrl = "https://" + request.getServerName() + ":8443" +
                        request.getRequestURI();

                if (request.getQueryString() != null) {
                    httpsUrl += "?" + request.getQueryString();
                }

                response.setStatus(redirectCode);
                response.setHeader("Location", httpsUrl);
                response.getWriter().write("Redirecting to HTTPS...");
                return;
            }

            getNext().invoke(request, response);
        }
    }
}
