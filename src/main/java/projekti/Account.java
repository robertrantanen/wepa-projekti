
package projekti;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {

    private String username;
    private String name;
    private String password;
    
    @OneToMany(mappedBy = "account")
    private List<Skill> skills;
    
    @OneToMany(mappedBy = "connecter")
    private List<Connection> connectionsFromThisAccount;
    
    @OneToMany(mappedBy = "receiver")
    private List<Connection> connectionsToThisAccount;

}
