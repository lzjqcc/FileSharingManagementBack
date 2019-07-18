package com.loser.sharefile.controller;

import com.loser.sharefile.controller.result.FileTemplateResult;
import com.loser.sharefile.controller.result.GroupResult;
import com.loser.sharefile.controller.result.ImageResult;
import com.loser.sharefile.controller.result.TagResult;
import com.loser.sharefile.dao.domain.FileTemplate;
import com.loser.sharefile.dao.domain.Group;
import com.loser.sharefile.dao.domain.Image;
import com.loser.sharefile.dao.domain.Tag;
import com.loser.sharefile.dao.repository.FileTemplateRepository;
import com.loser.sharefile.dao.repository.GroupRepository;
import com.loser.sharefile.dao.repository.ImageRepository;
import com.loser.sharefile.dao.repository.TagRepository;
import com.loser.sharefile.error.SystemError;
import com.loser.sharefile.utils.ModelUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/template")
public class FileTemplateController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private FileTemplateRepository fileTemplateRepository;
    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/parentGroups")
    public List<GroupResult> getParentGroups() {
        List<Group> groups = groupRepository.findByParentId(0);
        return ModelUtils.toTargets(groups, GroupResult.class);
    }
    @GetMapping("/childGroups/{groupId}")
    public List<GroupResult> getChildGroups(@PathVariable("groupId") Integer groupId) {
        return ModelUtils.toTargets(groupRepository.findByParentId(groupId), GroupResult.class);
    }
    @GetMapping("/tag/{groupId}")
    public List<TagResult> getTagByGroupId(@PathVariable("groupId") Integer groupId) {
        List<Tag> tags = tagRepository.findTagsByGroupId(groupId);
        return ModelUtils.toTargets(tags, TagResult.class);
    }

    @GetMapping("/fileTemplateDetails/{templateNum}")
    public FileTemplateResult getFileDetails(@PathVariable("templateNum") String templateNum) {
        FileTemplate fileTemplate = fileTemplateRepository.findByFileNum(templateNum);
        FileTemplateResult fileTemplateResult = ModelUtils.toTarget(fileTemplate, FileTemplateResult.class);
        List<Image> images = imageRepository.findByFileTemplateNum(templateNum);
        fileTemplateResult.setImages(ModelUtils.toTargets(images, ImageResult.class));
        return fileTemplateResult;
    }

    @GetMapping("/fileTemplatesByPage/{groupId}")
    public Page<FileTemplateResult> getFileTemplateResults(Pageable pageable, @PathVariable("groupId") Integer groupId) {
        PageImpl<FileTemplate> page = fileTemplateRepository.findByGroupIdOrderByInsertTime(groupId, pageable);
        return toFileTemplateResult(page);
    }
    /**
     * ?page=&size=&sort=firstname&sort=lastname,asc.
     * @param pageable
     * @param groupId
     * @param tagId
     * @return
     */
    @GetMapping("/fileTemplatesByPage/{groupId}/{tagId}")
    public Page<FileTemplateResult> getFileTemplateResults(Pageable pageable, @PathVariable("groupId") Integer groupId, @PathVariable("tagId") Integer tagId) {
        PageImpl<FileTemplate> fileTemplates = fileTemplateRepository.findByGroupIdAndTagIdOrderByInsertTime(groupId, tagId, pageable);
        return toFileTemplateResult(fileTemplates);
    }

    private PageImpl<FileTemplateResult> toFileTemplateResult(PageImpl<FileTemplate> fileTemplates) {
        List<FileTemplateResult> templateResults = ModelUtils.toTargets(fileTemplates.getContent(), FileTemplateResult.class);
        for (FileTemplateResult fileTemplateResult : templateResults) {
            if (!StringUtils.isEmpty(fileTemplateResult.getFileTemplateNum())) {
                Image image = imageRepository.findImagesByFileTemplateNumLimit(fileTemplateResult.getFileTemplateNum());
                ImageResult imageResult = ModelUtils.toTarget(image, ImageResult.class);
                fileTemplateResult.setImages(CollectionUtils.arrayToList(imageResult));
            }

        }
        PageImpl<FileTemplateResult> pageImpl = new PageImpl<FileTemplateResult>(templateResults, fileTemplates.getPageable(), fileTemplates.getTotalElements());
        return pageImpl;
    }

}
