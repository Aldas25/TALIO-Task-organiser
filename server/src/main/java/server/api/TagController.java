package server.api;


import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TagRepository;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository repo;

    public TagController(TagRepository repo){
        this.repo = repo;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Tag> addTag(@PathVariable("id") long id, @RequestBody Tag tag){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Tag saved = repo.save(tag);
        repo.save(tag);

        return ResponseEntity.ok(tag);
    }

    @GetMapping(path = { "", "/" })
    public List<Tag> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }


    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTag(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        //removeTagFromItsList(id);
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(
            @PathVariable("id") long id,
            @RequestBody Tag tag
    ){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Tag tagFromRepo = repo.findById(id).get();

        tagFromRepo.title = tag.title;
        Tag saved = repo.save(tagFromRepo);

        return ResponseEntity.ok(saved);
    }

}
