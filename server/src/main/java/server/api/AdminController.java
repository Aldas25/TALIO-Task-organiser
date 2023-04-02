package server.api;


import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final BoardRepository boardRepository;

    private String adminPassword;

    public AdminController(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
        generatePassword();
    }

    /**
     * Returns all boards if password matches
     * @param enteredPassword the password chosen by user
     * @return all of the boards or a fail
     */
    @GetMapping("/boards")
    public ResponseEntity<List<Board>> getAllBoards(@RequestBody String enteredPassword){
        if(!adminPassword.equals(enteredPassword)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(boardRepository.findAll());

    }

    /**
     * Function that checks password
     * @param enteredPassword the password chosen by user
     * @return true if password == currentPassword
     */
    @PostMapping("/checkPassword")
    @ResponseBody
    public boolean checkAdminPassword(@RequestBody String enteredPassword){
        return adminPassword.equals(enteredPassword);
    }

    /**
     * Function to generate random 10 characters passsword that prints to server
     */
    private void generatePassword(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < 10; i++){
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }
        String randomString = sb.toString();

        this.adminPassword = randomString;
        System.out.println("\nThe generated password is " + adminPassword + "\n");
    }

}
