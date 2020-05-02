package projekti;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth == null)) {
            String username = auth.getName();
            Account account = accountRepository.findByUsername(username);
            List<Connection> connectionsTo = account.getConnectionsToThisAccount();
            List<Connection> connectionsFrom = account.getConnectionsFromThisAccount();
            List<Connection> unacceptedConnections = new ArrayList<>();
            List<Connection> acceptedConnectionsTo = new ArrayList<>();
            List<Connection> acceptedConnectionsFrom = new ArrayList<>();
            for (Connection connection : connectionsTo) {
                if (!connection.isAccepted()) {
                    unacceptedConnections.add(connection);
                } else {
                    acceptedConnectionsTo.add(connection);
                }
            }
            for (Connection connection : connectionsFrom) {
                if (connection.isAccepted()) {
                    acceptedConnectionsFrom.add(connection);
                } 
            }

            model.addAttribute("account", account);
            model.addAttribute("acceptedConnectionsTo", acceptedConnectionsTo);
            model.addAttribute("acceptedConnectionsFrom", acceptedConnectionsFrom);
            model.addAttribute("unacceptedConnections", unacceptedConnections);
        }
        return "index";
    }

    @GetMapping("/accounts")
    public String list(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "accounts";
    }

    @GetMapping("/accounts/{id}")
    public String getOne(Model model, @PathVariable Long id) {
        model.addAttribute("account", accountRepository.getOne(id));
        return "account";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String add(@RequestParam String username,
            @RequestParam String name,
            @RequestParam String password) {
        if (accountRepository.findByUsername(username) != null) {
            return "redirect:/register";
        }

        Account a = new Account(username, name, passwordEncoder.encode(password), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        accountRepository.save(a);
        return "redirect:/login";
    }

}
