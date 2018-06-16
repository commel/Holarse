package de.holarse.web.comments;

import de.holarse.auth.HolarsePrincipal;
import de.holarse.backend.db.Comment;
import de.holarse.backend.db.ContentType;
import de.holarse.backend.db.NodeType;
import de.holarse.backend.db.Role;
import de.holarse.backend.db.User;
import de.holarse.backend.db.repositories.CommentRepository;
import de.holarse.backend.db.repositories.RoleRepository;
import de.holarse.services.SecurityService;
import java.time.OffsetDateTime;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {

    Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired CommentRepository commentRepository;
    
    @Autowired RoleRepository roleRepository;
    
    @Autowired SecurityService securityService;

    @GetMapping(value = "/node/{nodeId}/comments", produces = "application/json")
    public ResponseEntity<Collection<Comment>> comments(@PathVariable("nodeId") final Long nodeId) {
        return new ResponseEntity<>(commentRepository.findByNodeIdOrderByCreated(nodeId), HttpStatus.OK);
    }
    
    @PostMapping("/node/{nodeId}/comments/")
    public ResponseEntity<String> newComment(
            @PathVariable("nodeId") final Long nodeId,
            @ModelAttribute final CommentCommand command,
            final Authentication authentication) {

        final Comment comment = new Comment();
        comment.setNodeId(nodeId);
        comment.setContent(command.getContent());
        comment.setDeleted(Boolean.FALSE);
        comment.setCreated(OffsetDateTime.now());
        comment.setDraft(Boolean.FALSE);
        comment.setLocked(Boolean.FALSE);
        comment.setPublished(Boolean.TRUE);
        comment.setArchived(Boolean.FALSE);
        comment.setContentType(ContentType.PLAIN); // TODO
        comment.setAuthor(((HolarsePrincipal) authentication.getPrincipal()).getUser());

        commentRepository.save(comment);
        
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @PostMapping("/node/{nodeId}/comments/{commentId}/delete")    
    public ResponseEntity<String> deleteComment(@PathVariable("nodeType") final NodeType nodeType,
                                @PathVariable("nodeId") final Long nodeId,
                                @PathVariable("commentId") final Long commentId,
                                final Authentication authentication) {

        final Comment comment = commentRepository.findById(commentId).get();        
        final User user = ((HolarsePrincipal) authentication.getPrincipal()).getUser();

        final Role requiredRole = roleRepository.findByCodeIgnoreCase("MODERATOR").orElseThrow(IllegalStateException::new);
        
        // Der User selbst darf löschen
        if (comment.getAuthor().equals(user) || securityService.hasUserHigherClearance(user, requiredRole.getClearanceLevel())) {
            comment.setDeleted(true);
            comment.setContent("");
            commentRepository.save(comment);
        }

        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }
}
