package com.Lowser.sharefile.controller.controller;

import com.Lowser.common.utils.ModelUtils;
import com.Lowser.sharefile.controller.param.FileTemplateParam;
import com.Lowser.sharefile.controller.result.FileTemplateResult;
import com.Lowser.sharefile.controller.result.GroupResult;
import com.Lowser.sharefile.controller.result.ImageResult;
import com.Lowser.sharefile.controller.result.TagResult;
import com.Lowser.sharefile.dao.domain.FileTemplate;
import com.Lowser.sharefile.dao.domain.Group;
import com.Lowser.sharefile.dao.domain.Image;
import com.Lowser.sharefile.dao.domain.Tag;
import com.Lowser.sharefile.dao.repository.FileTemplateRepository;
import com.Lowser.sharefile.dao.repository.GroupRepository;
import com.Lowser.sharefile.dao.repository.ImageRepository;
import com.Lowser.sharefile.dao.repository.TagRepository;
import com.Lowser.sharefile.helper.ImageHelper;
import com.Lowser.common.utils.GenerateNum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ImageHelper imageHelper;

    @PostConstruct
    public void intit(){
        int i = 0;
    }
    @GetMapping("/parentGroups")
    public List<GroupResult> getParentGroups() {
        List<Group> groups = groupRepository.findByParentId(0);
        return ModelUtils.toTargets(groups, GroupResult.class);
    }
    @GetMapping("/childGroups/{groupId}")
    public List<GroupResult> getChildGroups(@PathVariable("groupId") Integer groupId) {
        return ModelUtils.toTargets(groupRepository.findByParentId(groupId), GroupResult.class);
    }
    @GetMapping("/fileTemplateDetails/{templateNum}")
    public FileTemplateResult getFileDetails(@PathVariable("templateNum") String templateNum) {
        FileTemplate fileTemplate = fileTemplateRepository.findByFileNum(templateNum);
        fileTemplate.setViewNums(fileTemplate.getViewNums()== null ? 1 : fileTemplate.getViewNums() + 1);
        fileTemplateRepository.save(fileTemplate);
        FileTemplateResult fileTemplateResult = ModelUtils.toTarget(fileTemplate, FileTemplateResult.class);
        List<Image> images = imageRepository.findByFileTemplateNum(templateNum);
        fileTemplateResult.setImages(ModelUtils.toTargets(images, ImageResult.class));
        List<Tag> tags = tagRepository.findTagByFileTemplateNum(templateNum);
        fileTemplateResult.setTagResults(ModelUtils.toTargets(tags, TagResult.class));
        return fileTemplateResult;
    }

    /**
     * ?page=&size=&sort=firstname&sort=lastname,asc
     * @param pageable
     * @param groupId
     * @return
     */
    @GetMapping("/fileTemplatesByPage/{groupId}")
    public Page<FileTemplateResult> getFileTemplateResults(Pageable pageable, @PathVariable("groupId") Integer groupId) {
        PageImpl<FileTemplate> page = fileTemplateRepository.findByGroupId(groupId, pageable);
        return toFileTemplateResult(page);
    }
    @GetMapping("/findAllByPage/{parentId}")
    public Page<FileTemplateResult> findALl(Pageable pageable, @PathVariable("parentId") Integer parentId) {
        List<Group> groups = groupRepository.findByParentId(parentId);
        List<Integer> groupIds = groups.stream().map(Group::getId).collect(Collectors.toList());
        PageImpl<FileTemplate> page = fileTemplateRepository.findByGroupIdIn(groupIds, pageable);
        return toFileTemplateResult(page);
    }
    @PostMapping("/fileTemplate/upload")
    public Object uploadFile(List<FileTemplateParam> fileTemplateParams) {
        List<FileTemplate> fileTemplates = new ArrayList<>();
        for (FileTemplateParam fileTemplateParam : fileTemplateParams) {
            FileTemplate fileTemplate = new FileTemplate();
            BeanUtils.copyProperties(fileTemplateParam, fileTemplate);
            fileTemplate.setFileNum(GenerateNum.getNum());
            if (StringUtils.isEmpty(fileTemplateParam.getImageUrls())) {
                saveImages(imageHelper.getImages(fileTemplateParam), fileTemplate.getFileNum());
            }else {
                saveImages(fileTemplateParam.getImageUrls(), fileTemplate.getFileNum());
            }
            saveTags(fileTemplateParam.getTagNames(), fileTemplate.getFileNum());
            fileTemplates.add(fileTemplate);
        }
        return fileTemplates;
        //fileTemplateRepository.saveAll(fileTemplates);
    }

    private void saveTags(List<String> tagNames, String fileNum) {
        if (CollectionUtils.isEmpty(tagNames)) {
            return;
        }
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = new Tag();
            tag.setName(tagName);
            tag.setFileTemplateNum(fileNum);
        }
        tagRepository.saveAll(tags);
    }
    private void saveImages(List<String> imageUrls, String fileNum) {
        if (CollectionUtils.isEmpty(imageUrls)) {
          return;
        }
        List<Image> images = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            Image image = new Image();
            image.setUrl(imageUrl);
            image.setFileTemplateNum(fileNum);
            images.add(image);
        }
        imageRepository.saveAll(images);
    }
    @GetMapping("createGroup/{parentId}/{groupName}}")
    public void newGroup(@PathVariable("parentId")Integer parentId,@PathVariable(name = "groupName") String groupName) {
        Group group = new Group();
        group.setName(groupName);
        group.setParentId(parentId);
        groupRepository.save(group);
    }


    private PageImpl<FileTemplateResult> toFileTemplateResult(PageImpl<FileTemplate> fileTemplates) {
        List<FileTemplateResult> templateResults = ModelUtils.toTargets(fileTemplates.getContent(), FileTemplateResult.class);
        for (FileTemplateResult fileTemplateResult : templateResults) {
            if (!StringUtils.isEmpty(fileTemplateResult.getFileNum())) {
                Image image = imageRepository.findTop1ByFileTemplateNum(fileTemplateResult.getFileNum());
                ImageResult imageResult = ModelUtils.toTarget(image, ImageResult.class);
                List<ImageResult> imageResults = new ArrayList<>();
                imageResults.add(imageResult);
                fileTemplateResult.setImages(imageResults);
            }

        }
        PageImpl<FileTemplateResult> pageImpl = new PageImpl<FileTemplateResult>(templateResults, fileTemplates.getPageable(), fileTemplates.getTotalElements());
        return pageImpl;
    }

}
