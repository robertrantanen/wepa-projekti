package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/messages")
    public String list(Model model) {
        Pageable pageable = PageRequest.of(0, 25, Sort.by("date").descending());
        model.addAttribute("messages", messageRepository.findAll(pageable));
        return "messages";
    }

    @PostMapping("/messages")
    public String create(@RequestParam String message) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account account = accountRepository.findByUsername(username);
        Message msg = new Message();
        msg.setContent(message);
        msg.setLikes(0);
        msg.setWriter(account.getName());
        messageRepository.save(msg);

        return "redirect:/messages";
    }

    @PostMapping("/messages/{id}")
    public String likeMessages(@PathVariable Long id) {
        Message msg = messageRepository.getOne(id);
        msg.setLikes(msg.getLikes() + 1);
        messageRepository.save(msg);
        return "redirect:/messages";
    }
}
