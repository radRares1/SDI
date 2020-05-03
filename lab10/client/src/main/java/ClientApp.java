import clientBase.Ui.UserInterface;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;
import webBase.dto.ClientsDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class ClientApp {


    public static void main(String[] args) throws Exception {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        "clientBase.config"
                );

        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        UserInterface ui  = new UserInterface(restTemplate);

        ui.runConsole();

    }
}
