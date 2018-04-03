package com.plumdo.form.rest.definition.resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.plumdo.form.entity.FormDefinition;
import com.plumdo.form.exception.ObjectNotFoundException;
import com.plumdo.form.repository.FormDefinitionRepository;
import com.plumdo.form.rest.BaseResource;

public class BaseFormDefinitionResource extends BaseResource{

	@Autowired
	protected FormDefinitionRepository formDefinitionRepository;
	
	protected FormDefinition getFormDefinitionFromRequest(Long id){
		FormDefinition formDefinition = formDefinitionRepository.findOne(id);
		if(formDefinition == null){
			throw new ObjectNotFoundException("Could not find a FormDefinition with id '" + id + "'.",FormDefinition.class);
		}
		return formDefinition;
	}
	
}
