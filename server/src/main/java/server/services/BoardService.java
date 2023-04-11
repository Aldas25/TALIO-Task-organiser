package server.services;

import commons.Board;
import commons.CardList;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.CardListRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository repo;
    private final CardListRepository cardListRepo;

    public BoardService(BoardRepository repo, CardListRepository cardListRepo){
        this.repo = repo;
        this.cardListRepo = cardListRepo;
    }

    public List<Board> getAll() {
        return repo.findAll();
    }

    public Board getById(long id) throws Exception {
        checkId(id);
        return repo.findById(id).orElseThrow(Exception::new);
    }

    public Board add(Board board) throws Exception {
        if (isNullOrEmpty(board.title)) {
            throw new Exception();
        }
        return repo.save(board);
    }

    public Board updateBoardTitle(long id, String newTitle) throws Exception {
        Board boardFromRepo = getById(id);
        boardFromRepo.title = newTitle;
        return repo.save(boardFromRepo);
    }

    public CardList addCardList(long id, CardList cardList) throws Exception {
        if(isNullOrEmpty(cardList.title)){
            throw new Exception();
        }

        CardList saved = cardListRepo.save(cardList);

        Board board = getById(id);
        board.lists = removeDuplicateLists(board.lists);
        board.lists.add(saved);
        repo.save(board);

        return saved;
    }

    public List<CardList> getCardListsById(long id) throws Exception {
        Board board = getById(id);
        return removeDuplicateLists(board.lists);
    }

    public void deleteBoard(long id) throws Exception {
        checkId(id);
        repo.deleteById(id);
    }

    public Board getBoardByInviteKey(String enteredKey) throws Exception {
        if(enteredKey.length() != 4){
            throw new Exception();
        }

        for(Board board : getAll()){
            if(enteredKey.equals(board.inviteKey)){
                return board;
            }
        }

        throw new Exception();
    }

    private void checkId(long id) throws Exception {
        if (id < 0 || !repo.existsById(id))
            throw new Exception();
    }

    private List<CardList> removeDuplicateLists(List<CardList> lists) {
        return lists
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
