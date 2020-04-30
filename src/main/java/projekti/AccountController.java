package projekti;

import java.util.ArrayList;
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
    SkillRepository skillRepository;

    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth == null)) {
            String username = auth.getName();
            Account account = accountRepository.findByUsername(username);
            model.addAttribute("account", account);
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

    @PostMapping("/accounts/{id}/skills/{skillId}")
    public String likeSkill(@PathVariable Long id, @PathVariable Long skillId) {
        Skill skill = skillRepository.getOne(skillId);
        skill.setLikes(skill.getLikes() + 1);
        skillRepository.save(skill);
        return "redirect:/accounts/{id}";
    }

    @PostMapping("/accounts/{id}")
    public String addConnection(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account connecter = accountRepository.findByUsername(username);
        Account receiver = accountRepository.getOne(id);
        Connection connection = new Connection(connecter, receiver, false);
        connectionRepository.save(connection);
        return "redirect:/accounts";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String add(@RequestParam String username,
            @RequestParam String name,
            @RequestParam String password
    ) {
        if (accountRepository.findByUsername(username) != null) {
            return "redirect:/register";
        }

        Account a = new Account(username, name, passwordEncoder.encode(password), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        accountRepository.save(a);
        return "redirect:/login";
    }

    @PostMapping("/")
    public String addSkill(@RequestParam String name
    ) {
        Skill skill = new Skill();
        skill.setSkill(name);
        skill.setLikes(0);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account account = accountRepository.findByUsername(username);

        skill.setAccount(account);

        skillRepository.save(skill);
        return "redirect:/";
    }

}
