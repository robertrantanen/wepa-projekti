/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SkillController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SkillRepository skillRepository;

    @PostMapping("/accounts/{id}/skills/{skillId}")
    public String likeSkill(@PathVariable Long id, @PathVariable Long skillId) {
        Skill skill = skillRepository.getOne(skillId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (!skill.getLikers().contains(username)) {
            skill.setLikers(skill.getLikers() + username + " ");
            skill.setLikes(skill.getLikes() + 1);
        }
        skillRepository.save(skill);
        return "redirect:/accounts/{id}";
    }

    @PostMapping("/skills")
    public String addSkill(@RequestParam String name
    ) {
        Skill skill = new Skill();
        skill.setSkill(name);
        skill.setLikes(0);
        skill.setLikers("");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account account = accountRepository.findByUsername(username);

        skill.setAccount(account);

        skillRepository.save(skill);
        return "redirect:/";
    }

}
