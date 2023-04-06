package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String inviteKey;

    @OneToMany (fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CardList> lists;

    public String title;

    public Board(String title, List<CardList> lists){
        this.title = title;
        this.lists = lists;

        generateInviteKey();
    }

    @SuppressWarnings("unused")
    public Board() {
        // for object mapper
    }

    private void generateInviteKey(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < 4; i++){
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }
        String randomString = sb.toString();

        this.inviteKey = randomString;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == this.getClass()
                && ((Board) obj).id == this.id;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
