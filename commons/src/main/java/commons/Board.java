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

    /**
     * The constructor of Board
     * @param title The title of the board
     * @param lists The list of CardList in board
     */
    public Board(String title, List<CardList> lists){
        this.title = title;
        this.lists = lists;

        generateInviteKey();
    }

    @SuppressWarnings("unused")
    public Board() {
        // for object mapper
    }

    /**
     * Generates invite key to join board
     */
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

    /**
     * Equals method
     * @param obj the obj to check if equals
     * @return true if equals, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == this.getClass()
                && ((Board) obj).id == this.id;
    }

    /**
     * Hash code function
     * @return The hash value
     */
    @Override
    public int hashCode() {
        return (int)id;
    }

    /**
     * @return The string version of this objet
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
