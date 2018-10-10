package de.holarse.web.articles;

import de.holarse.auth.web.HolarsePrincipal;
import de.holarse.backend.db.Article;
import de.holarse.backend.db.Attachment;
import de.holarse.backend.db.AttachmentType;
import de.holarse.backend.db.ContentType;
import de.holarse.backend.db.NodeType;
import de.holarse.backend.db.Tag;
import de.holarse.backend.db.AttachmentDataType;
import de.holarse.backend.db.Node;
import de.holarse.backend.db.User;
import de.holarse.backend.db.repositories.ArticleRepository;
import de.holarse.backend.db.repositories.AttachmentRepository;
import de.holarse.backend.db.repositories.RevisionRepository;
import de.holarse.backend.db.repositories.TagRepository;
import de.holarse.exceptions.ErrorMode;
import de.holarse.exceptions.FlashMessage;
import de.holarse.exceptions.HolarseException;
import de.holarse.exceptions.NodeLockException;
import de.holarse.exceptions.NodeNotFoundException;
import de.holarse.exceptions.RedirectException;
import de.holarse.renderer.Renderer;
import de.holarse.search.SearchEngine;
import de.holarse.services.AttachmentRenderService;
import de.holarse.services.NodeService;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path = {"wiki", "articles"})
public class ArticleController {

    Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    RevisionRepository revisionRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    SearchEngine searchEngine;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    NodeService nodeService;

    @Qualifier("wikiRenderer")
    @Autowired
    Renderer renderer;

    @Autowired
    AttachmentRenderService attachmentRenderService;

    // INDEX
    @GetMapping("/")
    public String index(final Model map) {
        map.addAttribute("nodes", articleRepository.findAll());

        return "articles/index";
    }

    // NEW
    @Secured("ROLE_USER")
    @GetMapping("/new")
    public String newArticle(final Model map, final ArticleCommand command) {
        map.addAttribute("articleCommand", command);
        map.addAttribute("contentTypes", ContentType.values());
        return "articles/form";
    }

    // CREATE
    @Secured("ROLE_USER")
    @Transactional
    @PostMapping("/")
    public RedirectView create(@ModelAttribute final ArticleCommand command, final Authentication authentication) throws Exception {
        final Article article = nodeService.initCommentableNode(Article.class);
        // Artikelinhalt
        article.setTitle(command.getTitle());
        article.setAlternativeTitle1(command.getAlternativeTitle1());
        article.setAlternativeTitle2(command.getAlternativeTitle2());
        article.setAlternativeTitle3(command.getAlternativeTitle3());
        article.setContent(command.getContent());
        article.setContentType(command.getContentType());
        article.setBranch(StringUtils.isBlank(command.getBranch()) ? "master" : command.getBranch());

        // Tags anlegen
        Set<Tag> tags = commandToTags(command.getTags());
        tagRepository.saveAll(tags);
        article.getTags().addAll(tags);

        // Artikel-Metadaten
        article.setAuthor(((HolarsePrincipal) authentication.getPrincipal()).getUser());
        article.setCreated(OffsetDateTime.now());
        article.setRevision(revisionRepository.nextRevision());

        // Slug setzen
        article.setSlug(nodeService.findNextSlug(article.getTitle(), NodeType.ARTICLE));

        articleRepository.save(article);

        searchEngine.update(article);

        return new RedirectView("/wiki/" + URLEncoder.encode(article.getSlug(), "UTF-8"), true, false, false);
    }

    // SHOW by Slug
    @Transactional
    @GetMapping("/{slug}")
    public ModelAndView showBySlug(@PathVariable final String slug, final Model map) {
        try {
            final Article article = nodeService.findArticle(slug).get();

            Hibernate.initialize(article.getTags());
            Hibernate.initialize(article.getAttachments());

            // TODO Attachment-Renderer
            final List<String> renderedAttachments = new ArrayList<>();
            for (final Attachment att : article.getAttachments()) {
                try {
                    renderedAttachments.add(attachmentRenderService.render(att));
                } catch (Exception e) {
                    logger.warn("Fehler während des Renders. Wird ignoriert", e);
                }
            }

            map.addAttribute("node", article);
            map.addAttribute("renderedAttachments", renderedAttachments);
            map.addAttribute("rendererContent", renderer.render(article.getContent()));
            return new ModelAndView("articles/show", map.asMap());
        } catch (RedirectException re) {
            return new ModelAndView(re.getRedirect());
        }
    }

    // EDIT
    @Secured("ROLE_USER")
    @Transactional
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable final Long id, final Model map, final ArticleCommand command, final Authentication authentication, final RedirectAttributes redirectAttributes) {
        final User currentUser = ((HolarsePrincipal) authentication.getPrincipal()).getUser();

        final Article article = articleRepository.findById(id).get();

        if (!nodeService.isEditable(article)) {
            throw new HolarseException("Artikel kann derzeit nicht bearbeitet werden.");
        }
        
        Hibernate.initialize(article.getTags());
        Hibernate.initialize(article.getAttachments());

        // Versuchen den Artikel zum Schreiben zu sperren
        try {
            nodeService.tryTolock(article, currentUser);
        } catch (NodeLockException nle) {
            final FlashMessage msg = new FlashMessage();
            msg.setThrowable(nle);
            msg.setMode(ErrorMode.DANGER);
            msg.setTitle("Artikel wird gerade berarbeitet");
            msg.setMessage("Der Artikel <em>" + article.getTitle() + "</em> wird seit " + nle.getNodeLock().getCreated() + " von " + nle.getNodeLock().getUser().getLogin() + " bearbeitet. Die Sperre gilt noch bis " + nle.getNodeLock().getLockUntil() + ".");
            msg.setSolution("Notfalls warten bis Sperrzeit vorbeit ist, oder einem Moderator Bescheid geben.");
            redirectAttributes.addFlashAttribute("flashMessage", msg);

            return "redirect:/wiki/" + article.getSlug();
        }

        map.addAttribute("node", article);

        command.setTitle(article.getTitle());
        command.setAlternativeTitle1(article.getAlternativeTitle1());
        command.setAlternativeTitle2(article.getAlternativeTitle2());
        command.setAlternativeTitle3(article.getAlternativeTitle3());
        command.setContent(article.getContent());
        command.setContentType(article.getContentType());
        command.setTags(article.getTags().stream().map(t -> t.getName()).collect(Collectors.joining(",")));
        command.setBranch(article.getBranch());

        map.addAttribute("articleCommand", command);
        map.addAttribute("contentTypes", ContentType.values());

        return "articles/form";
    }

    // ABORT EDIT
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @Transactional
    @GetMapping("/{id}/edit/abort")
    public String editAbort(@PathVariable final Long id, final Authentication authentication) {
        final Article article = articleRepository.findById(id).get();

        // Lock lösen
        nodeService.unlock(article);

        return "redirect:" + article.getUrl();
    }

    // UPDATE
    @Secured("ROLE_USER")
    @Transactional
    @PostMapping("/{id}")
    public RedirectView update(
            @PathVariable final Long id,
            final ArticleCommand command,
            final Authentication authentication) throws UnsupportedEncodingException, NodeLockException {
        final User currentUser = ((HolarsePrincipal) authentication.getPrincipal()).getUser();

        final Article article = articleRepository.findById(id).orElseThrow(() -> new NodeNotFoundException(id));

        // Artikel archivieren
        nodeService.createRevisionFromCurrent(article);

        // Slug ggf. archivieren
        if (!article.getTitle().equalsIgnoreCase(command.getTitle())) {
            nodeService.archivateSlug(article.getSlug(), article, NodeType.ARTICLE);
            article.setSlug(nodeService.findNextSlug(command.getTitle(), NodeType.ARTICLE));
        }

        // Artikel aktualisieren
        article.setTitle(command.getTitle());
        article.setAlternativeTitle1(command.getAlternativeTitle1());
        article.setAlternativeTitle2(command.getAlternativeTitle2());
        article.setAlternativeTitle3(command.getAlternativeTitle3());
        article.setContent(command.getContent());
        article.setContentType(command.getContentType());
        // Branch darf nicht gewechselt werden
        article.getTags().clear();

        // Tags anlegen
        Set<Tag> tags = commandToTags(command.getTags());
        tagRepository.saveAll(tags);
        article.getTags().addAll(tags);

        // Artikel-Metadaten aktualisieren
        article.setAuthor(currentUser);
        article.setChangelog(command.getChangelog());
        article.setUpdated(OffsetDateTime.now());
        article.setRevision(revisionRepository.nextRevision());

        articleRepository.save(article);

        Attachment att = new Attachment();
        att.setCreated(OffsetDateTime.now());
        att.setNodeId(article.getId());
        att.setDescription("Beispiel-Link");
        att.setAttachmentDataType(AttachmentDataType.URI);
        att.setAttachmentData("https://www.startpage.com");
        att.setAttachmentType(AttachmentType.LINK);
        att.setOrdering(1l);

        attachmentRepository.save(att);

        Attachment att2 = new Attachment();
        att2.setCreated(OffsetDateTime.now());
        att2.setNodeId(article.getId());
        att2.setDescription("Awesome Video");
        att2.setAttachmentDataType(AttachmentDataType.URI);
        att2.setAttachmentType(AttachmentType.YOUTUBE);
        att2.setAttachmentData("https://www.youtube-nocookie.com/embed/zpOULjyy-n8");
        att2.setOrdering(2l);

        attachmentRepository.save(att2);

        try {
            searchEngine.update(article);
        } catch (IOException e) {
            logger.warn("Aktualisieren des Suchindexes fehlgeschlagen", e);
        }

        // Lock lösen
        nodeService.unlock(article);

        return new RedirectView("/wiki/" + URLEncoder.encode(article.getSlug(), "UTF-8"), true, false, false);
    }

    protected String tagsToCommand(final Set<Tag> tags) {
        return tags.stream().map(t -> t.getName()).collect(Collectors.joining(","));
    }

    protected Set<Tag> commandToTags(final String tags) {
        if (StringUtils.isBlank(tags)) {
            return new HashSet<>();
        }
        return Arrays.asList(tags.split(",")).stream().map(createOrUpdateTag).collect(Collectors.toSet());
    }

    protected Function<String, Tag> createOrUpdateTag = s -> tagRepository.findByNameIgnoreCase(s.trim()).orElse(new Tag(s));

    // DELETE
    
    // unpublish
    @Secured({"ROLE_USER", "ROLE_MODERATOR", "ROLE_ADMIN"})
    @Transactional
    @GetMapping("/{id}/unpublish")
    public ResponseEntity unpublish(@PathVariable("id") Long id) throws IOException {
        final Article article = articleRepository.findById(id).get();
        article.setPublished(false);

        articleRepository.save(article);

        // Suchindex aktualisieren, da nicht veröffentlichte Artikel da auch nicht auftauchen.
        searchEngine.update(article);
        
        return ResponseEntity.ok(HttpStatus.OK);
    }
    
    // publish
    @Secured({"ROLE_USER", "ROLE_MODERATOR", "ROLE_ADMIN"})
    @Transactional
    @GetMapping("/{id}/publish")
    public ResponseEntity publish(@PathVariable("id") Long id) throws IOException {
        final Article article = articleRepository.findById(id).get();
        article.setPublished(true);

        articleRepository.save(article);

        // Suchindex aktualisieren, da veröffentlichte Artikel da auch auftauchen.
        searchEngine.update(article);
        
        return ResponseEntity.ok(HttpStatus.OK);
    }    
    
    // lock commenting
    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @Transactional
    @GetMapping("/{id}/lock_commenting")
    public ResponseEntity lockCommenting(@PathVariable("id") Long id) throws IOException {
        final Article article = articleRepository.findById(id).get();
        article.setCommentable(false);
        articleRepository.save(article);
        
        return ResponseEntity.ok(HttpStatus.OK);
    }        

    // unlock commenting
    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @Transactional
    @GetMapping("/{id}/unlock_commenting")
    public ResponseEntity openCommenting(@PathVariable("id") Long id) throws IOException {
        final Article article = articleRepository.findById(id).get();
        article.setCommentable(true);
        articleRepository.save(article);
        
        return ResponseEntity.ok(HttpStatus.OK);
    }     
    
    // lock
    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @Transactional
    @GetMapping("/{id}/lock")
    public ResponseEntity lock(@PathVariable("id") Long id) throws IOException {
        final Article article = articleRepository.findById(id).get();
        article.setLocked(true);
        articleRepository.save(article);
        
        return ResponseEntity.ok(HttpStatus.OK);
    }        

    // unlock
    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @Transactional
    @GetMapping("/{id}/unlock")
    public ResponseEntity unlock(@PathVariable("id") Long id) throws IOException {
        final Article article = articleRepository.findById(id).get();
        article.setLocked(false);
        articleRepository.save(article);
        
        return ResponseEntity.ok(HttpStatus.OK);
    } 
    
    // unlock
    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @Transactional
    @GetMapping("/{id}/archivate")
    public ResponseEntity archivate(@PathVariable("id") Long id) throws IOException {
        final Article article = articleRepository.findById(id).get();
        article.setArchived(Boolean.TRUE);
        articleRepository.save(article);
        
        return ResponseEntity.ok(HttpStatus.OK);
    }     
    
    // unarchivate
    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @Transactional
    @GetMapping("/{id}/unarchivate")
    public ResponseEntity unarchivate(@PathVariable("id") Long id) throws IOException {
        final Article article = articleRepository.findById(id).get();
        article.setArchived(Boolean.FALSE);
        articleRepository.save(article);
        
        return ResponseEntity.ok(HttpStatus.OK);
    }    
    
    
}
