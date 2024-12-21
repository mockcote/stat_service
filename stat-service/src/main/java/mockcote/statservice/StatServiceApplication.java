package mockcote.statservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class StatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatServiceApplication.class, args);
    }

}
