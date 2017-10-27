package com.plumdo.form.rest.model.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.form.entity.FormDefinition;
import com.plumdo.form.entity.FormModel;
import com.plumdo.form.repository.FormDefinitionRepository;
import com.plumdo.form.rest.definition.FormDefinitionResponse;

@Api(tags = "表单模型")
@RestController
public class FormModelDeployResource extends BaseFormModelResource {
	@Autowired
	protected FormDefinitionRepository formDefinitionRepository;

	@ApiOperation(value = "部署表单模型", notes = "根据表单模型的id来部署表单模型")
	@ApiImplicitParam(name = "id", value = "表单模型ID", required = true, dataType = "Long", paramType = "path")
	@RequestMapping(value = "/form-models/{id}/deploy", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public FormDefinitionResponse deploy(@PathVariable Long id) {
		FormModel formModel = getFormModelFromRequest(id);
		FormDefinition formDefinition = new FormDefinition();
		formDefinition.setKey(formModel.getKey());
		formDefinition.setCategory(formModel.getCategory());
		formDefinition.setName(formModel.getName());
		formDefinition.setTenantId(formModel.getTenantId());
		formDefinition.setEditorSourceBytes(formModel.getEditorSourceBytes());

		FormDefinition latestFormDefinition = null;
		if (StringUtils.isNotBlank(formModel.getTenantId())) {
			latestFormDefinition = formDefinitionRepository.findLatestFormDefinitionByKeyAndTenantId(formModel.getKey(), formModel.getTenantId());
		} else {
			latestFormDefinition = formDefinitionRepository.findLatestFormDefinitionByKey(formModel.getKey());
		}
		if (latestFormDefinition == null) {
			formDefinition.setVersion(1);
		} else {
			formDefinition.setVersion(latestFormDefinition.getVersion() + 1);
		}

		formDefinitionRepository.save(formDefinition);

		return responseFactory.createFormDefinitionResponse(formDefinition);
	}

}
