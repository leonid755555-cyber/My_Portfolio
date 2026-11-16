package imhere_backend.API.controllers;

import imhere_backend.API.DTO.TagsIdDto;
import imhere_backend.businessLogic.services.TagsService;
import imhere_backend.dataBase.tables.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagsController {

    private final TagsService tagsService;

    @GetMapping
    public List<Tags> getAllTags() {
        return tagsService.findAll();
    }

    @PutMapping("/{user_id}")
    public void editProfileTags(@PathVariable Long user_id, @RequestBody TagsIdDto tagsDto) {
        tagsService.editProfileTags(user_id, tagsDto);
    }

    @GetMapping("/{user_id}")
    public List<Tags> getProfileTags(@PathVariable Long user_id) {
        return tagsService.getProfileTags(user_id);
    }

    @PostMapping
    public ResponseEntity<Tags> createTag(@RequestBody Tags tags) {
        return ResponseEntity.ok(tagsService.save(tags));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
