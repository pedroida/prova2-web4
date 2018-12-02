package br.edu.utfpr.hello.controller;

import br.edu.utfpr.hello.model.User.User;
import br.edu.utfpr.hello.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@Scope("session")
@RequestMapping("/")
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping()
    public ModelAndView home() {
        return new ModelAndView("redirect:/produtos");

    }

    @GetMapping("login")
    public ModelAndView getLoginForm(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return new ModelAndView("redirect:/produtos");
        }

        return new ModelAndView("login.jsp");
    }

    @PostMapping("login")
    public ModelAndView login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
        if (session.getAttribute("user") != null) {
            return new ModelAndView("redirect:/produtos");
        }

        Optional<User> user = loginService.login(email, password);

        if (!user.isPresent()) {
            session.setAttribute("error", "Email e/ou senha inválidos");
            return new ModelAndView("redirect:/login");
        }

        String interestAttr = user.get().getName() + "-interests";
        if (session.getAttribute(interestAttr) == null) {
            session.setAttribute(interestAttr, 0);
            session.setAttribute("interest", 0);
        }

        session.setAttribute("user", user.get());
        session.setAttribute("success", "login realizado com sucesso!");
        return new ModelAndView("redirect:/produtos");
    }

    @GetMapping("registrar")
    public ModelAndView getRegisterForm(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return new ModelAndView("redirect:/produtos");
        }

        return new ModelAndView("registrar.jsp");
    }

    @PostMapping("registrar")
    public ModelAndView register(
            @Valid @ModelAttribute User user,
            @RequestParam("confirm_password") String confirm,
            BindingResult bindingResult,
            HttpSession session) {

        if (session.getAttribute("user") != null) {
            return new ModelAndView("redirect:/produtos");
        }

        if (loginService.findByEmail(user.getEmail()).isPresent()) {
            bindingResult.addError(new ObjectError("duplicated_email", "Email já cadastrado"));
        }

        if (bindingResult.hasErrors()) {

            session.setAttribute("errors", bindingResult.getAllErrors());
            session.setAttribute("old", user);

            return new ModelAndView("redirect:/registrar");
        }

        if (!user.getPassword().trim().equals(confirm.trim())) {
            bindingResult.addError(new ObjectError("password_confirm", "A confirmação de senha não confere"));
            session.setAttribute("old", user);
            session.setAttribute("errors", bindingResult.getAllErrors());

            return new ModelAndView("redirect:/registrar");
        }

        user.setPassword(loginService.hashWith256(user.getPassword()));

        loginService.save(user);

        String interestAttr = user.getName() + "-interests";
        session.setAttribute(interestAttr, 0);
        session.setAttribute("interest", 0);

        session.setAttribute("user", user);
        session.setAttribute("success", "Cadastro realizado com sucesso");
        return new ModelAndView("redirect:/produtos");
    }

    @GetMapping("logout")
    public ModelAndView logout(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }

        session.removeAttribute("user");
        return new ModelAndView("redirect:login");
    }
}
