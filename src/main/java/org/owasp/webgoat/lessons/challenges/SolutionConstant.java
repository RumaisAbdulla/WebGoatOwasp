import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "solutionconstant")
public class SolutionConstant {
    private String adminpass;
}
