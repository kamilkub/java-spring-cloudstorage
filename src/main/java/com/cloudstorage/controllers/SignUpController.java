package com.cloudstorage.controllers;


import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.model.StorageUser;
import com.cloudstorage.service.SignUpService;
import com.cloudstorage.service.StatisticsService;
import com.cloudstorage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Collectors;

@Controller
@Validated
@RequiredArgsConstructor
public class SignUpController {
    private final SignUpService signUpService;
    private final StorageService storageService;
    private final StatisticsService statisticsService;

    public static void main(String[] args) {
        class Todo {
            private int userId;
            private int id;
            private String title;
            private boolean completed;

            public Todo () {

            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isCompleted() {
                return completed;
            }

            public void setCompleted(boolean completed) {
                this.completed = completed;
            }
        }

        try {
            URL url = new URI("https://jsonplaceholder.typicode.com/todos/1").toURL();

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder s = new StringBuilder(reader.lines().collect(Collectors.joining()));

            new JSONConverter<Todo>().writeStringToObject(s.toString(), Todo.class);
            reader.close();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/sign-in")
    public String showLoginPage(
            UserAuthenticationFilter userAuthenticationFilter
    ) {
        if (userAuthenticationFilter.isAuthenticated())
            return "redirect:/user";
        else
            return "auth_templates/sign-in";
    }

    @GetMapping("/sign-up")
    public String signUpPage(
            UserAuthenticationFilter userAuthenticationFilter,
            Model model
    ) {
        if (userAuthenticationFilter.isAuthenticated())
            return "redirect:/user";
        else
            model.addAttribute("user", new StorageUser());
        return "auth_templates/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpUser(
            @Valid @ModelAttribute("user") StorageUser user,
            BindingResult result,
            Model model
    ) {
        result.getAllErrors().stream().findFirst();


        if (result.hasErrors()) {
            model.addAttribute("listOfErrors", result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList()));
        } else {
            boolean isEmailTaken = signUpService.isEmailTaken(user.getEmail());
            boolean isUsernameTaken = signUpService.isUsernameTaken(user.getUsername());

            if (!isEmailTaken && !isUsernameTaken) {
                String pinFromService = signUpService.signUpUser(user);
                storageService.init(user.getUsername());
                statisticsService.initLogFile(user.getUsername());

                model.addAttribute("pin", "http://localhost:8080/activate?pin=" + pinFromService);
                model.addAttribute("userSaved", true);
                model.addAttribute("qrcode", true);
            }
            model.addAttribute("exists", isEmailTaken || isUsernameTaken);
        }

        return "auth_templates/sign-up";
    }

    @GetMapping("/activate")
    public String activateUser(
            @RequestParam("pin") String pin,
            Model model
    ) {
        String attribute = signUpService.activateUser(pin) ? "activationSuccess" : "wrongPing";
        model.addAttribute(attribute, true);
        return "activatePage";
    }
}