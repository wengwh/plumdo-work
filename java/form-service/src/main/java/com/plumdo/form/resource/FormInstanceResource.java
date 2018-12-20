package com.plumdo.form.resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plumdo.common.client.jdbc.JdbcClient;
import com.plumdo.common.jpa.Criteria;
import com.plumdo.common.jpa.Restrictions;
import com.plumdo.common.resource.BaseResource;
import com.plumdo.common.resource.PageResponse;
import com.plumdo.form.constant.ErrorConstant;
import com.plumdo.form.constant.TableConstant;
import com.plumdo.form.domain.FormDefinition;
import com.plumdo.form.domain.FormInstance;
import com.plumdo.form.repository.FormDefinitionRepository;
import com.plumdo.form.repository.FormInstanceRepository;
import com.plumdo.form.request.FormInstanceCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表单定义资源类
 *
 * @author wengwh
 * @date 2018/12/10
 */
@RestController
public class FormInstanceResource extends BaseResource {
    private final FormInstanceRepository formInstanceRepository;
    private final FormDefinitionRepository formDefinitionRepository;
    private final JdbcClient jdbcClient;

    @Autowired
    public FormInstanceResource(FormInstanceRepository formInstanceRepository, FormDefinitionRepository formDefinitionRepository, JdbcClient jdbcClient) {
        this.formInstanceRepository = formInstanceRepository;
        this.formDefinitionRepository = formDefinitionRepository;
        this.jdbcClient = jdbcClient;
    }

    private FormInstance getFormInstanceFromRequest(Integer id) {
        FormInstance formInstance = formInstanceRepository.findOne(id);
        if (formInstance == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_DEFINITION_NOT_FOUND);
        }
        return formInstance;
    }

    @GetMapping(value = "/form-instances")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormInstances(@RequestParam Map<String, String> requestParams) {
        Criteria<FormInstance> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("relationTable", requestParams.get("relationTable")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formInstanceRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/form-instances/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormInstance getFormInstance(@PathVariable Integer id) {
        return getFormInstanceFromRequest(id);
    }

    @PostMapping(value = "/form-instances")
    @ResponseStatus(value = HttpStatus.CREATED)
    public FormInstance createFormInstance(@RequestBody FormInstanceCreateRequest createRequest) {
        if (createRequest.getFormDefinitionId() == null && createRequest.getFormDefinitionKey() == null) {
            exceptionFactory.throwIllegalArgument(ErrorConstant.FORM_KEY_ID_NOT_FOUND);
        }

        FormDefinition formDefinition;
        if (createRequest.getFormDefinitionId() != null) {
            formDefinition = formDefinitionRepository.findOne(createRequest.getFormDefinitionId());
        } else if (createRequest.getTenantId() != null) {
            formDefinition = formDefinitionRepository.findLatestFormDefinitionByKeyAndTenantId(createRequest.getFormDefinitionKey(), createRequest.getTenantId());
        } else {
            formDefinition = formDefinitionRepository.findLatestFormDefinitionByKey(createRequest.getFormDefinitionKey());
        }

        if (formDefinition == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_DEFINITION_NOT_FOUND);
        }

        int tableRelationId = createFormData(formDefinition.getRelationTable(), createRequest.getFormData());
        FormInstance formInstance = new FormInstance();
        formInstance.setFormDefinitionId(formDefinition.getId());
        formInstance.setTableRelationId(tableRelationId);
        formInstance.setRelationTable(formDefinition.getRelationTable());
        formInstance.setTenantId(formDefinition.getTenantId());

        return formInstanceRepository.save(formInstance);
    }


    @GetMapping("/form-instances/{id}/data")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getFormInstanceData(@PathVariable Integer id) {
        FormInstance formInstance = getFormInstanceFromRequest(id);

        return getFormData(formInstance.getRelationTable(), formInstance.getTableRelationId());
    }

    @PutMapping("/form-instances/{id}/data")
    @ResponseStatus(HttpStatus.OK)
    public int getFormInstanceData(@PathVariable Integer id, @RequestBody Map<String, String> formData) {
        FormInstance formInstance = getFormInstanceFromRequest(id);

        return updateFormData(formInstance.getRelationTable(), formInstance.getTableRelationId(), formData);
    }

    private Map<String, Object> getFormData(String tableName, int id) {
        String sql = "SELECT * FROM " + TableConstant.DATABASE_NAME + "`" + tableName + "` WHERE ID_=" + id;

        return jdbcClient.queryForMap(sql);
    }

    private int createFormData(String tableName, Map<String, String> formData) {
        String keySql = formData.keySet().stream().map(e -> "`" + e + "`").collect(Collectors.joining(","));
        String valueSql = formData.values().stream().map(e -> "'" + (e == null ? "" : e) + "'").collect(Collectors.joining(","));

        String sql = "INSERT INTO " + TableConstant.DATABASE_NAME + "`" + tableName + "`(" + keySql + ") values(" + valueSql + ")";

        return jdbcClient.insert(sql, "id_");
    }

    private int updateFormData(String tableName, int id, Map<String, String> formData) {
        String updateSql = formData.entrySet().stream().map(e -> "`" + e.getKey() + "`='" + e.getValue() + "' ").collect(Collectors.joining(","));

        String sql = "UPDATE " + TableConstant.DATABASE_NAME + "`" + tableName + "` SET " + updateSql + " WHERE ID_=" + id;

        return jdbcClient.update(sql);
    }

}
