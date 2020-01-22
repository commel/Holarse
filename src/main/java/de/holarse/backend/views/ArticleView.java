package de.holarse.backend.views;

import de.holarse.backend.db.Attachment;
import de.holarse.backend.db.types.AttachmentGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Viewmodel für Artikel
 * @author comrad
 */
public class ArticleView implements PageTitleView {
    
    private Long nodeId;
    private String mainTitle;
    private String alternativeTitle1;
    private String alternativeTitle2;
    private String alternativeTitle3;
    
    private String content = "";
    private String formattedContent = "";
    private String plainContent = "";
    
    private String slug;
    
    private String tagLine; // Representation der Eingabezeile für Tags
    private List<String> tags = new ArrayList<>(20);
    private final Map<AttachmentGroup, List<Attachment>> attachments = new HashMap<>();
   
    @Override
    public String getUrl() {
        return String.format("/wiki/%s", slug);
    }
    
    @Override
    public String getEditUrl() {
        return String.format("/wiki/%s/edit", nodeId);
    }

    public Long getNodeId() {
        return nodeId;
    }
    
    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }
    
    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getAlternativeTitle1() {
        return alternativeTitle1;
    }

    public void setAlternativeTitle1(String alternativeTitle1) {
        this.alternativeTitle1 = alternativeTitle1;
    }

    public String getAlternativeTitle2() {
        return alternativeTitle2;
    }

    public void setAlternativeTitle2(String alternativeTitle2) {
        this.alternativeTitle2 = alternativeTitle2;
    }

    public String getAlternativeTitle3() {
        return alternativeTitle3;
    }

    public void setAlternativeTitle3(String alternativeTitle3) {
        this.alternativeTitle3 = alternativeTitle3;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getFormattedContent() {
        return formattedContent;
    }

    public void setFormattedContent(String formattedContent) {
        this.formattedContent = formattedContent;
    }

    @Override
    public String getPlainContent() {
        return plainContent;
    }

    public void setPlainContent(String plainContent) {
        this.plainContent = plainContent;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
    
    public List<String> getTags() {
        return tags;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }
        
    public Map<AttachmentGroup, List<Attachment>> getAttachments() {
        return attachments;
    }
    
    public List<Attachment> getVideos() {
        return getAttachments().get(AttachmentGroup.VIDEO);
    }
    
    public List<Attachment> getScreenshots() {
        return getAttachments().get(AttachmentGroup.IMAGE);
    }    
    
    public List<Attachment> getWebsites() {
        return getAttachments().get(AttachmentGroup.WEBSITE);
    }


    @Override
    public String getPageTitle() {
        return String.format("%s", mainTitle);
    }      
    
}
