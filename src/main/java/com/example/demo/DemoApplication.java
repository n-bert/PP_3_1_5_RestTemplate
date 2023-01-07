package com.example.demo;

import com.example.demo.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        User user = new User(3L, "James", "Brown", (byte) 20);
        final String url = "http://94.198.50.185:7081/api/users";
        String answer = "Answer: ";

        RestTemplate rt = new RestTemplate();
        ResponseEntity<Object[]> getResponse = rt.getForEntity(url, Object[].class);

        String cookie = Objects.requireNonNull(getResponse.getHeaders().get("Set-Cookie")).toString().replace("[", "");

        HttpHeaders headers = new HttpHeaders();
        headers.set("cookie", cookie);

        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> postResponse = rt.postForEntity(url, entity, String.class);
        answer += postResponse.getBody();

        user.setName("Thomas");
        user.setLastName("Shelby");

        ResponseEntity<String> updateResponse = rt.exchange(url, HttpMethod.PUT, entity, String.class);
        answer += updateResponse.getBody();

        ResponseEntity<String> deleteResponse = rt.exchange(url + "/3", HttpMethod.DELETE, entity, String.class);
        System.out.println(answer + deleteResponse.getBody());

        System.exit(0);
    }
}
