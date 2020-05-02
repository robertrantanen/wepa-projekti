
package projekti;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill extends AbstractPersistable<Long> {
    
    private String skill;
    private int likes = 0;
    private String likers;
    
    @ManyToOne
    private Account account;
    
}
