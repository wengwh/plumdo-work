package com.plumdo.form.resource;

import com.plumdo.common.jpa.Criteria;
import com.plumdo.common.jpa.Restrictions;
import com.plumdo.common.resource.BaseResource;
import com.plumdo.common.resource.PageResponse;
import com.plumdo.form.constant.ErrorConstant;
import com.plumdo.form.domain.FormTable;
import com.plumdo.form.repository.FormFieldRepository;
import com.plumdo.form.repository.FormLayoutRepository;
import com.plumdo.form.repository.FormTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 数据表资源类
 *
 * @author wengwh
 * @date 2018年8月29日
 */
@RestController
public class FormTableResource extends BaseResource {
    private final FormTableRepository formTableRepository;
    private final FormFieldRepository formFieldRepository;
    private final FormLayoutRepository formLayoutRepository;

    @Autowired
    public FormTableResource(FormTableRepository formTableRepository, FormFieldRepository formFieldRepository, FormLayoutRepository formLayoutRepository) {
        this.formTableRepository = formTableRepository;
        this.formFieldRepository = formFieldRepository;
        this.formLayoutRepository = formLayoutRepository;
    }

    private FormTable getFormTableFromRequest(Integer id) {
        FormTable formTable = formTableRepository.findOne(id);
        if (formTable == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formTable;
    }

    @GetMapping(value = "/form-tables")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormTables(@RequestParam Map<String, String> requestParams) {
        Criteria<FormTable> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.like("key", requestParams.get("key")));
        criteria.add(Restrictions.like("category", requestParams.get("category")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formTableRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/form-tables/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormTable getFormTable(@PathVariable Integer id) {
        return getFormTableFromRequest(id);
    }

    @PostMapping("/form-tables")
    @ResponseStatus(HttpStatus.CREATED)
    public FormTable createFormTable(@RequestBody FormTable formTableRequest) {
        FormTable formTable = formTableRepository.findByKey(formTableRequest.getKey());
        if (formTable != null) {
            exceptionFactory.throwConflict(ErrorConstant.FORM_TABLE_KEY_REPEAT);
        }
        return formTableRepository.save(formTableRequest);
    }

    @PutMapping(value = "/form-tables/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormTable updateFormTable(@PathVariable Integer id, @RequestBody FormTable formTableRequest) {
        FormTable formTable = getFormTableFromRequest(id);
        formTable.setName(formTableRequest.getName());
        formTable.setCategory(formTableRequest.getCategory());
        formTable.setRemark(formTableRequest.getRemark());
        formTable.setTenantId(formTableRequest.getTenantId());
        return formTableRepository.save(formTable);
    }

    @DeleteMapping(value = "/form-tables/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteFormTable(@PathVariable Integer id) {
        FormTable formTable = getFormTableFromRequest(id);

        formTableRepository.delete(formTable);

        formFieldRepository.deleteByTableId(id);

        formLayoutRepository.deleteByTableId(id);
    }

}
