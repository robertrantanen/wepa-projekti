
package projekti;

import java.time.LocalDateTime;
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
public class Message extends AbstractPersistable<Long> {
 
    private LocalDateTime date = LocalDateTime.now();
    private String content;
    private String writer;
    private int likes = 0;
    private String likers;
    
    @OneToMany(mappedBy = "message")
    private List<Comment> comments;
 
}
