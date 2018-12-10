package com.plumdo.form.resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plumdo.common.constant.CoreConstant;
import com.plumdo.common.jpa.Criteria;
import com.plumdo.common.jpa.Restrictions;
import com.plumdo.common.resource.BaseResource;
import com.plumdo.common.resource.PageResponse;
import com.plumdo.form.constant.ErrorConstant;
import com.plumdo.form.domain.ByteArray;
import com.plumdo.form.domain.FormField;
import com.plumdo.form.domain.FormLayout;
import com.plumdo.form.repository.ByteArrayRepository;
import com.plumdo.form.repository.FormFieldRepository;
import com.plumdo.form.repository.FormLayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 表单布局资源类
 *
 * @author wengwh
 * @date 2018年9月19日
 */
@RestController
public class FormLayoutResource extends BaseResource {
    private final FormLayoutRepository formLayoutRepository;
    private final FormFieldRepository formFieldRepository;
    private final ByteArrayRepository byteArrayRepository;

    @Autowired
    public FormLayoutResource(FormLayoutRepository formLayoutRepository, FormFieldRepository formFieldRepository, ByteArrayRepository byteArrayRepository) {
        this.formLayoutRepository = formLayoutRepository;
        this.formFieldRepository = formFieldRepository;
        this.byteArrayRepository = byteArrayRepository;
    }

    private FormLayout getFormLayoutFromRequest(Integer id) {
        FormLayout formLayout = formLayoutRepository.findOne(id);
        if (formLayout == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_LAYOUT_NOT_FOUND);
        }
        return formLayout;
    }

    @GetMapping(value = "/form-layouts")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormLayouts(@RequestParam Map<String, String> requestParams) {
        Criteria<FormLayout> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formLayoutRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/form-layouts/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormLayout getFormLayout(@PathVariable Integer id) {
        return getFormLayoutFromRequest(id);
    }

    @PostMapping("/form-layouts")
    @ResponseStatus(HttpStatus.CREATED)
    public FormLayout createFormLayout(@RequestBody FormLayout formLayoutRequest) {
        FormLayout formLayout = formLayoutRepository.findFirstByKeyAndTableId(formLayoutRequest.getKey(),
                formLayoutRequest.getTableId());
        if (formLayout != null) {
            exceptionFactory.throwConflict(ErrorConstant.FORM_LAYOUT_KEY_REPEAT);
        }
        return formLayoutRepository.save(formLayoutRequest);
    }

    @PutMapping(value = "/form-layouts/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormLayout updateFormLayout(@PathVariable Integer id, @RequestBody FormLayout formLayoutRequest) {
        FormLayout formLayout = getFormLayoutFromRequest(id);
        formLayout.setName(formLayoutRequest.getName());
        formLayout.setRemark(formLayoutRequest.getRemark());
        formLayout.setTenantId(formLayoutRequest.getTenantId());
        return formLayoutRepository.save(formLayout);
    }

    @DeleteMapping(value = "/form-layouts/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormLayout(@PathVariable Integer id) {
        FormLayout formLayout = getFormLayoutFromRequest(id);
        formLayoutRepository.delete(formLayout);
    }

    @GetMapping("/form-layouts/{id}/json")
    @ResponseStatus(HttpStatus.OK)
    public ObjectNode getFormLayoutJson(@PathVariable Integer id) throws Exception {
        FormLayout formLayout = getFormLayoutFromRequest(id);

        List<FormField> formFields = formFieldRepository.findByTableId(formLayout.getTableId());

        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.putPOJO("fields", formFields);

        ByteArray byteArray = byteArrayRepository.findOne(formLayout.getEditorSourceId());
        if (byteArray == null) {
            resultNode.putPOJO("json", objectMapper.createArrayNode().toString());
        } else {
            resultNode.putPOJO("json", new String(byteArray.getContentByte(), CoreConstant.DEFAULT_CHARSET));
        }

        return resultNode;
    }

    @PutMapping("/form-layouts/{id}/json")
    @ResponseStatus(HttpStatus.OK)
    public void saveFormLayoutJson(@PathVariable Integer id, @RequestBody String editorJson) throws Exception {
        FormLayout formLayout = getFormLayoutFromRequest(id);

        ByteArray byteArray = new ByteArray();
        byteArray.setName(formLayout.getName());
        byteArray.setContentByte(editorJson.getBytes(CoreConstant.DEFAULT_CHARSET));
        byteArrayRepository.save(byteArray);

        formLayout.setEditorSourceId(byteArray.getId());
        formLayoutRepository.save(formLayout);
    }
}
