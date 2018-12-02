package br.edu.utfpr.hello.controller;

import br.edu.utfpr.hello.model.Product.Product;
import br.edu.utfpr.hello.model.User.User;
import br.edu.utfpr.hello.services.ProductService;
import br.edu.utfpr.hello.services.UserService;
import br.edu.utfpr.hello.utils.CookieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.management.remote.rmi.RMIConnection;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.*;

@Controller
@Scope("session")
@RequestMapping("/produtos")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @GetMapping()
    public ModelAndView getAll(HttpSession session, HttpServletResponse response) {
        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }
        CookieHelper.setCurrentTimeCookie(response);

        ModelAndView mv = new ModelAndView("product/index.jsp");
        List<Product> products = productService.findAll();
        mv.addObject("products", products);
        mv.addObject("breadcrumb", "Produtos");
        return mv;
    }

    @GetMapping("/meus-produtos")
    public ModelAndView getUserProducts(HttpSession session, HttpServletResponse response) {

        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }
        CookieHelper.setCurrentTimeCookie(response);

        ModelAndView mv = new ModelAndView("product/user-products.jsp");
        User user = (User) session.getAttribute("user");
        List<Product> products = productService.findAllByUserId(user.getId());

        mv.addObject("products", products);
        mv.addObject("breadcrumb", "Meus Produtos");

        return mv;
    }

    @GetMapping("/criar")
    public ModelAndView getCreate(HttpSession session, HttpServletResponse response) {
        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }
        CookieHelper.setCurrentTimeCookie(response);

        ModelAndView mv = new ModelAndView("product/create.jsp");
        mv.addObject("breadcrumb", "Novo Produto");
        mv.addObject("action", "/produtos/salvar");

        return mv;
    }

    @PostMapping(value = "/salvar")
    public ModelAndView storeProduct(@Valid Product product, BindingResult bindingResult, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }

        if (bindingResult.hasErrors()) {
            session.setAttribute("errors", bindingResult.getAllErrors());
            session.setAttribute("old", product);
            return new ModelAndView("redirect:/produtos/criar");
        }
        User user = (User) session.getAttribute("user");

        product.setUser(user);

        productService.save(product);

        session.setAttribute("success", "Produto criado com sucesso");
        return new ModelAndView("redirect:/produtos/meus-produtos");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, HttpSession session, HttpServletResponse response) {
        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }
        CookieHelper.setCurrentTimeCookie(response);

        Optional<Product> product = productService.find(id);

        if (product.isPresent()) {
            ModelAndView view = new ModelAndView("product/edit.jsp");

            view.addObject("breadcrumb", "Editar produto");
            view.addObject("action", "/produtos/" + id);
            view.addObject("product", product.get());
            return view;
        }

        session.setAttribute("not_found", "Produto não encontrado");
        return new ModelAndView("redirect:/produtos/meus-produtos");
    }

    @PostMapping("/{id}")
    public ModelAndView update(@PathVariable("id") Long id, @Valid Product product, BindingResult bindingResult, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }

        if (bindingResult.hasErrors()) {
            session.setAttribute("errors", bindingResult.getAllErrors());
            return new ModelAndView("redirect:/produtos/editar/" + id);
        }

        Optional<Product> oldProduct = productService.find(id);

        if (!oldProduct.isPresent()) {
            session.setAttribute("error", "Produto não encontrado");
            return new ModelAndView("redirect:/produtos/meus-produtos");
        }
        User user = (User) session.getAttribute("user");

        product.setUser(user);
        product.setId(id);
        productService.save(product);
        session.setAttribute("success", "Produto editado com sucesso");
        return new ModelAndView("redirect:/produtos/meus-produtos");
    }

    @PostMapping("/{id}/apagar")
    public ModelAndView delete(@PathVariable("id") Long id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }

        productService.delete(id);
        session.setAttribute("success", "Produto apagado com sucesso!");
        return new ModelAndView("redirect:/produtos/meus-produtos");
    }

    @GetMapping("/{id}/interesse")
    public ModelAndView hasInterest(@PathVariable("id") Long id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }

        User user = (User) session.getAttribute("user");
        String interestAttr = user.getName() + "-interests";
        if(session.getAttribute(interestAttr) == null) {
            session.setAttribute(interestAttr, 1);
            session.setAttribute("interest", 1);
        } else {
            int interest = Integer.parseInt(session.getAttribute(interestAttr).toString());
            interest++;
            session.setAttribute(interestAttr, interest);
            session.setAttribute("interest", interest);
        }

        return new ModelAndView("redirect:/produtos");
    }
}
