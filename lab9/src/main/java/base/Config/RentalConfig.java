package base.Config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"base.Repository", "base.Controller", "base.Ui", "base.Validators"})
public class RentalConfig {
}
