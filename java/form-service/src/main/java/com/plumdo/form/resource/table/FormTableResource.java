package com.plumdo.form.resource.table;

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
import com.plumdo.form.domain.FormTable;
import com.plumdo.form.repository.FormTableRepository;

/**
 * 数据表资源类
 *
 * @author wengwh
 * @date 2018年8月29日
 */
@RestController
public class FormTableResource extends BaseResource {
	@Autowired
	private FormTableRepository formTableRepository;
	
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
		Criteria<FormTable> criteria = new Criteria<FormTable>();
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
		return formTableRepository.save(formTableRequest);
	}

	@PutMapping(value = "/form-tables/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public FormTable updateFormTable(@PathVariable Integer id, @RequestBody FormTable formTableRequest) {
		FormTable formTable = getFormTableFromRequest(id);
		formTable.setName(formTableRequest.getName());
		formTable.setRemark(formTableRequest.getRemark());
		formTable.setTenantId(formTableRequest.getTenantId());
		return formTableRepository.save(formTable);
	}

	@DeleteMapping(value = "/form-tables/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteFormTable(@PathVariable Integer id) {
		FormTable formTable = getFormTableFromRequest(id);
		formTableRepository.delete(formTable);
	}
}
