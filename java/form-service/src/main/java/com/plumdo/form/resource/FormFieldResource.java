package com.plumdo.form.resource;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.common.jpa.Criteria;
import com.plumdo.common.jpa.Restrictions;
import com.plumdo.common.resource.BaseResource;
import com.plumdo.common.resource.PageResponse;
import com.plumdo.form.constant.ErrorConstant;
import com.plumdo.form.domain.FormField;
import com.plumdo.form.repository.FormFieldRepository;

/**
 * 表单字段资源类
 *
 * @author wengwh
 * @date 2018年9月19日
 */
@RestController
public class FormFieldResource extends BaseResource {
	@Autowired
	private FormFieldRepository formFieldRepository;
	
	private FormField getFormFieldFromRequest(Integer id) {
		FormField formField = formFieldRepository.findOne(id);
		if (formField == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_FIELD_NOT_FOUND);
		}
		return formField;
	}

	@GetMapping(value = "/form-fields")
	@ResponseStatus(value = HttpStatus.OK)
	public PageResponse getFormFields(@RequestParam Map<String, String> requestParams) {
		Criteria<FormField> criteria = new Criteria<FormField>();
		criteria.add(Restrictions.eq("id", requestParams.get("id")));
		criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
		criteria.add(Restrictions.like("key", requestParams.get("key")));
		criteria.add(Restrictions.like("name", requestParams.get("name")));
		criteria.add(Restrictions.like("remark", requestParams.get("remark")));
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
		return createPageResponse(formFieldRepository.findAll(criteria, getPageable(requestParams)));
	}

	@GetMapping(value = "/form-fields/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public FormField getFormField(@PathVariable Integer id) {
		return getFormFieldFromRequest(id);
	}

	@PostMapping("/form-fields")
	@ResponseStatus(HttpStatus.CREATED)
	public FormField createFormField(@RequestBody FormField formFieldRequest) {
		return formFieldRepository.save(formFieldRequest);
	}

	@PutMapping(value = "/form-fields/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public FormField updateFormField(@PathVariable Integer id, @RequestBody FormField formFieldRequest) {
		FormField formField = getFormFieldFromRequest(id);
		formField.setName(formFieldRequest.getName());
		formField.setRemark(formFieldRequest.getRemark());
		formField.setTenantId(formFieldRequest.getTenantId());
		return formFieldRepository.save(formField);
	}

	@DeleteMapping(value = "/form-fields/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteFormField(@PathVariable Integer id) {
		FormField formField = getFormFieldFromRequest(id);
		formFieldRepository.delete(formField);
	}
}
